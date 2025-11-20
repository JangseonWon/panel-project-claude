package com.greencross.lims.worker.wes.impl.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcgenome.lims.test.geneplus.TestInfo;
import com.gcgenome.lims.test.wes.SingleGeneTest;
import com.greencross.lims.dao.RequestDAO;
import com.greencross.lims.dao.SnvCandidateDAO;
import com.greencross.lims.dao.SnvConsensualClassDao;
import com.greencross.lims.entity.Analysis;
import com.greencross.lims.entity.Request;
import com.greencross.lims.worker.SnvWorkerImpl;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("WES-SNV-MT ver.1")
public class SnvMtv1 extends SnvWorkerImpl {
	private static final Pattern FILE_NAME_PATTERN = Pattern.compile("^((\\d{2}WES\\d{1,4})[-_]*(\\d{1,4})*[-_](\\d{8}-\\d{3}-\\d{4})(?:[-_]([^.]+))*[-_](\\d+)).*_MT.annotation.txt$");
	private static final UUID SHEET = UUID.fromString("01396c17-df3e-4669-a11b-d86c56144cee");
	private static final UUID SHEET_RD = UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5");
	public static final Process PROCESSOR = new Process() {
		@Override
		public Long sample(String fileName) {
			Matcher m = FILE_NAME_PATTERN.matcher(fileName);
			if(!m.find()) return null;
			if(m.group(4) == null || m.group(4).isBlank()) return null;
			return Long.parseLong(m.group(4).replace("-", ""));
		}
		@Override
		public String batch(String fileName) {
			Matcher m = FILE_NAME_PATTERN.matcher(fileName);
			if(!m.find()) return null;
			return m.group(2);
		}
		@Override
		public Analysis map(String fileName, @Nullable Request request) {
			if(request == null) return null;
			Matcher m = FILE_NAME_PATTERN.matcher(fileName);
			if(!m.find()) return null;
			String serial = m.group(1);
			String batch = m.group(2);
			UUID sheet = m.group(5).equalsIgnoreCase("WES") ? SHEET : SHEET_RD;
			String panel = "WES";
			int row = Integer.parseInt(m.group(6));
			String ext = request.pk().sample()+":"+request.pk().service();
			return new Analysis(new Analysis.AnalysisPK(sheet, batch, row, ext)).serial(serial).panel(panel)
																				.sample(request.pk().sample()).service(request.pk().service());
		}

		@Override
		public List<String> tags(Map<String, String> values) {
			return List.of("mt");
		}
	};
	private final List<String> services = new LinkedList<>();
	public SnvMtv1(ElasticsearchRestTemplate em, SnvCandidateDAO candidateDao, SnvConsensualClassDao consensualDao, RequestDAO requestDAO, ObjectMapper om) {
		super(em, requestDAO, candidateDao, consensualDao, om, PROCESSOR);
		for(com.gcgenome.lims.test.wes.TestInfo t: com.gcgenome.lims.test.wes.TestInfo.TESTS) services.add(t.code());
		for(com.gcgenome.lims.test.single.TestInfo t: com.gcgenome.lims.test.single.TestInfo.TESTS) services.add(t.code());
		for(com.gcgenome.lims.test.panel.TestInfo t: com.gcgenome.lims.test.panel.TestInfo.TESTS) services.add(t.code());
		for(com.gcgenome.lims.test.single.TestInfo t: SingleGeneTest.TESTS) services.add(t.code());
		for(TestInfo t: TestInfo.TESTS_ETC) services.add(t.code());
	}
	@Override
	protected String[] services() {
		return services.toArray(String[]::new);
	}
	@Override
	public String name() {
		return "WES-SNV-MT ver.1";
	}
	@Override
	public boolean chk(File file) {
		if(!"v1".equalsIgnoreCase(file.getParentFile().getName())) return false;
		if(!"WES".equalsIgnoreCase(file.getParentFile().getParentFile().getName())) return false;
		String fileName = file.getName();
		Matcher m = FILE_NAME_PATTERN.matcher(fileName);
		return m.find();
	}
}
