package com.greencross.lims.worker.wes.impl.v2;

import com.gcgenome.lims.test.geneplus.TestInfo;
import com.gcgenome.lims.test.wes.SingleGeneTest;
import com.greencross.lims.dao.PanelTypeService;
import com.greencross.lims.dao.RequestDAO;
import com.greencross.lims.entity.Analysis;
import com.greencross.lims.entity.AnalysisFile;
import com.greencross.lims.entity.PanelType;
import com.greencross.lims.entity.Request;
import com.greencross.lims.worker.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service("WES-LIMS Stat ver.2")
public class LimsStatv2 implements Worker {
	private static final Pattern FILE_NAME_PATTERN = Pattern.compile("^(\\d{2}WES\\d{1,4}).LIMS_stat.txt$");
	private static final Pattern SERIAL_PATTERN = Pattern.compile("^(\\d{2}WES\\d{1,4})(-RE)*[_-]([A-Za-z0-9]+)-(\\d{1,4})[_-](\\d{8}-\\d{3}-\\d{4})(.*)$");
	private static final UUID SHEET_WES = UUID.fromString("01396c17-df3e-4669-a11b-d86c56144cee");
	private static final UUID SHEET_RD = UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5");
	private final RequestDAO requestDAO;
	private final List<String> services = new LinkedList<>();
	@Autowired
	private PanelTypeService panelTypeService;
	public LimsStatv2(RequestDAO requestDAO) {
		this.requestDAO = requestDAO;
		for(com.gcgenome.lims.test.wes.TestInfo t: com.gcgenome.lims.test.wes.TestInfo.TESTS) services.add(t.code());
		for(com.gcgenome.lims.test.single.TestInfo t: com.gcgenome.lims.test.single.TestInfo.TESTS) services.add(t.code());
		for(com.gcgenome.lims.test.single.TestInfo t: SingleGeneTest.TESTS) services.add(t.code());
		for(com.gcgenome.lims.test.panel.TestInfo t: com.gcgenome.lims.test.panel.TestInfo.TESTS) services.add(t.code());
		for(TestInfo t: TestInfo.TESTS_ETC) services.add(t.code());
	}

	private Set<String> getPanelServices(String fileName){
		String batchPanel = analysis(fileName, null).pk().batch();
		batchPanel = batchPanel.substring(2, batchPanel.length()-3);;
		return panelTypeService.getPanelType(batchPanel).stream()
				.map(PanelType::service)
				.collect(Collectors.toSet());
	}

	@Override
	public boolean chk(File file) {
		if(!"v1".equalsIgnoreCase(file.getParentFile().getName())) return false;
		if(!"WES".equalsIgnoreCase(file.getParentFile().getParentFile().getName())) return false;
		String fileName = file.getName();
		Matcher m = FILE_NAME_PATTERN.matcher(fileName);
		return m.find();
	}
	protected String[] services() {
		return services.toArray(String[]::new);
	}
	@Override
	public void process(AnalysisFile entity, File file) throws Exception {
		List<String> lines = Files.readAllLines(file.toPath());
		Map<String, Integer> headers = lines.stream().findFirst()
											.map(line->line.split("\t", -1))
											.map(h->{
												Map<String, Integer> header = new HashMap<>();
												for(int i = 0; i < h.length; ++i) header.put(h[i].toLowerCase(), i);
												return header;
											}).get();
		for(int i = 1; i < lines.size(); ++i) {
			String line = lines.get(i);
			if(line == null || line.trim().isEmpty()) continue;
			String[] split = line.split("\t", -1);
			Map<String, Object> values = new HashMap<>();
			for(Map.Entry<String, Integer> e: headers.entrySet()) values.put(e.getKey(), split[e.getValue()]);
			List<Analysis> analysis = findEntity(split[0].trim());
			for(Analysis a: analysis) {
				Map<String, Object> map = a.value();
				if(map == null) map = new HashMap<>();
				map.putAll(values);
				requestDAO.em().merge(a.value(map));
			}
		}
	}

	@Override
	public String name() {
		return "WES-LIMS Stat ver.2";
	}
	private static final String FIND_REQUEST_BY_SAMPLE = "SELECT * FROM request WHERE " +
			"sample IN (SELECT id FROM sample where patient=(SELECT patient FROM sample WHERE id=:sample)) " +
			"AND date_request >= (SELECT max(date_request)+'-1 week' FROM request WHERE sample=:sample) " +
			"AND date_request <= (SELECT max(date_request)+'1 week' FROM request WHERE sample=:sample)";
	private List<Analysis> findEntity(String fileName) {
		var sample = sample(fileName);
		if(sample == null) {
			var entity = analysis(fileName, null);
			entity = requestDAO.em().merge(entity);
			return List.of(entity);
		} else {
			var targets = Arrays.stream(services()).collect(Collectors.toSet());
			List<Request> req = requestDAO.em().createNativeQuery(FIND_REQUEST_BY_SAMPLE, Request.class).setParameter("sample", sample).getResultList();
			return req.stream()
					  .filter(r->targets.contains(r.pk().service()))
					  .filter(r -> getPanelServices(fileName).contains(r.pk().service()))
					  .map(r->analysis(fileName, r))
					  .map(a->{
					  		Analysis prev = requestDAO.em().find(Analysis.class, a.pk());
					  		if(prev!=null) return prev;
					  		else return requestDAO.em().merge(a);
					  }).collect(Collectors.toList());
		}
	}
	public Long sample(String fileName) {
		Matcher m = SERIAL_PATTERN.matcher(fileName);
		if(!m.find()) return null;
		if(m.group(5) == null || m.group(5).isBlank()) return null;
		return Long.parseLong(m.group(5).replace("-", ""));
	}
	public Analysis analysis(String fileName, @Nullable Request request) {
		Matcher m = SERIAL_PATTERN.matcher(fileName);
		if(!m.find()) return null;
		String batch = m.group(1);
		String panel = "WES";
		UUID sheet = m.group(3).equalsIgnoreCase("WES") ? SHEET_WES : SHEET_RD;
		int row = Integer.parseInt(m.group(4));
		if(request!=null && "wes".equalsIgnoreCase(panel)) {
			String ext = request.pk().sample()+":"+request.pk().service();
			return new Analysis(new Analysis.AnalysisPK(sheet, batch, row, ext)).panel(panel).serial(fileName)
																				.sample(request.pk().sample()).service(request.pk().service());
		} else return new Analysis(new Analysis.AnalysisPK(sheet, batch, row, m.group(5))).panel(panel).serial(fileName);
	}
	@Override
	public final String batch(File file) {
		Matcher m = FILE_NAME_PATTERN.matcher(file.getName());
		if(!m.find()) return null;
		return m.group(1);
	}
}
