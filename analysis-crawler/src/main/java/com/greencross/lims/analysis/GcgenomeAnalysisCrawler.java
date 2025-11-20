package com.greencross.lims.analysis;

import com.greencross.lims.dao.AnalysisFileDAO;
import com.greencross.lims.dao.SnvConsensualClassDao;
import com.greencross.lims.entity.AnalysisFile;
import com.greencross.lims.webhook.VariantCountMap;
import com.greencross.lims.webhook.VariantCountRequest;
import com.greencross.lims.webhook.WebhookHandler;
import com.greencross.lims.worker.SnvWorkerImpl;
import com.greencross.lims.worker.Worker;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration
public class GcgenomeAnalysisCrawler {
	private final Logger Log = LoggerFactory.getLogger(getClass());
	private final AnalysisFileDAO dao3;
	private final SnvConsensualClassDao consensualClassDao;
	private final PlatformTransactionManager tx;
	@Value("${gcgenome.tmp-dir}")
	private String tmp;
	@Value("${gcgenome.processed-dir}")
	private String processed;
	@Value("${gcgenome.undefined-dir}")
	private String error;
	private final List<Worker> workers;
	private final WebhookHandler webhookHandler;
	private final VariantCountMap countMap;

	public GcgenomeAnalysisCrawler(AnalysisFileDAO dao3, SnvConsensualClassDao consensualClassDao, PlatformTransactionManager txManager, List<Worker> workers, WebhookHandler webhookHandler, VariantCountMap countMap) {
		this.dao3 = dao3;
		this.consensualClassDao = consensualClassDao;
		this.tx = txManager;
		this.workers = workers;
		this.webhookHandler = webhookHandler;
		this.countMap = countMap;
	}

	// 1시간에 1번 크롤링
	@Scheduled(fixedDelay=1000*60*60, initialDelay = 10000)
	public void insertNewRequest() throws InterruptedException {
		while(pending){
			Thread.sleep(3000);
		}

		File dir = new File(tmp);
		File err = new File(error);
		File std = new File(processed);
		if(!dir.exists()) dir.mkdirs();
		if(!err.exists()) err.mkdirs();
		if(!std.exists()) std.mkdirs();
		if(dir.listFiles()==null) return;
		System.out.println("Crawling..");
		for(File panel: dir.listFiles()) {
			System.out.println("\t" + panel);
			if(panel!=null) for(File version: panel.listFiles()) {
				System.out.println("\t\t" + version);
				if(version!=null) for(File child: version.listFiles()) {
					UUID id = UUID.randomUUID();
					String fileName = child.getName();
					long size = child.length();
					if(size <= 1) continue;
					TransactionStatus t = tx.getTransaction(TransactionDefinition.withDefaults());
					AnalysisFile entity = new AnalysisFile().id(id).name(fileName).size(size).path(child.getAbsolutePath());
					dao3.merge(entity);

					String batch = null;
					try {
						boolean processed = false;
						for(Worker w: workers) if(w.chk(child)) {
							batch = w.batch(child);
							System.out.println("Processing file:" + panel.getName() + "/" + version.getName() + "/" + child.getName());
							w.process(entity, child);
							processed = true;
						}
						tx.commit(t);
						if(processed && batch!=null) try {
							if(!std.toPath().resolve(batch).toFile().exists()) std.toPath().resolve(batch).toFile().mkdirs();
							Files.move(child.toPath(), std.toPath().resolve(batch).resolve(child.getName()), StandardCopyOption.REPLACE_EXISTING);
						} catch (IOException ioException) {
							ioException.printStackTrace();
						}
					} catch(Exception e) {
						e.printStackTrace();
						webhookHandler.sendError(e, fileName);
						tx.rollback(t);
						if(batch!=null) try {
							if(!err.toPath().resolve(batch).toFile().exists()) err.toPath().resolve(batch).toFile().mkdirs();
							Files.move(child.toPath(), err.toPath().resolve(batch).resolve(child.getName()), StandardCopyOption.REPLACE_EXISTING);
						} catch (IOException ioException) {
							ioException.printStackTrace();
						}
					}
				}
			}
		}
		System.out.println("Complete Crawling");
		if(countMap.hasRequests()) sendProgressWithWebhook();
	}

	private boolean pending = false;

	private void sendProgressWithWebhook(){
		ElasticsearchRestTemplate restTemplate = countMap.getRestTemplate();
		countMap.consumeMap().forEach(entry -> {
			String serial = entry.getKey();
			List<VariantCountRequest> requests = new ArrayList<>(entry.getValue());
			String batchRow = requests.get(0).getBatchRow();
			var indices = new String[]{requests.get(0).getIndex()};
			long hits = restTemplate.execute(client ->
					client.count(new CountRequest(indices, QueryBuilders.wildcardQuery("analysis", "*" + batchRow)), RequestOptions.DEFAULT).getCount()
			);
			webhookHandler.sendProgress(serial, requests, hits);
		});

	}

	// 1시간에 1번 크롤링
	@Scheduled(fixedDelay=1000*60*60)
	public void updateCandidates() {
		System.out.println("Update Consensual..");
		pending = true;
		TransactionStatus t = tx.getTransaction(TransactionDefinition.withDefaults());
		SnvWorkerImpl.CONSENSUAL_CLASSES.clear();
		consensualClassDao.findLast().forEach(v->{
			String clazz = v.classification();
			clazz = mapCls(clazz);
			if(clazz!=null) SnvWorkerImpl.CONSENSUAL_CLASSES.put(v.snv(), clazz);
		});
		tx.rollback(t);
		pending = false;
		System.out.println("Update Complete");
	}
	private static String mapCls(String clazz) {
		if("Pathogenic".equalsIgnoreCase(clazz))		return "P";
		if("Likely Pathogenic".equalsIgnoreCase(clazz))	return "LP";
		if("VUS".equalsIgnoreCase(clazz))				return "VUS";
		//if("Likely Benign".equalsIgnoreCase(clazz))	return "LB";		// LB, B는 Assign 하지 않는다
		//if("Benign".equalsIgnoreCase(clazz))			return "B";
		return null;
	}
}
