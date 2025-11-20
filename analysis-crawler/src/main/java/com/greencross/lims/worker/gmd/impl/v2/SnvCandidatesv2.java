package com.greencross.lims.worker.gmd.impl.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcgenome.lims.test.genomescreen.TestInfo;
import com.gcgenome.lims.test.wes.TestWithSingleInfo;
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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("GMD-SNV-Candidates ver.2")
public class SnvCandidatesv2 extends SnvWorkerImpl {
	private static final Pattern FILE_NAME_PATTERN = Pattern.compile("^((\\d{2}([A-Z]+)\\d{1,4})(-RE)*[-_]((\\d{8}-\\d{3}-\\d{4})|(?i)(.+))[-_](.+)[-_](\\d{1,4})(.*))_[MI].annotation.filter_Candidates.txt$");
	private static final UUID SHEET_WES = UUID.fromString("01396c17-df3e-4669-a11b-d86c56144cee");
	private static final UUID SHEET_RD = UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5");
	public static final Process PROCESSOR = new Process() {
		@Override
		public Long sample(String fileName) {
			Matcher m = FILE_NAME_PATTERN.matcher(fileName);
			if(!m.find()) return null;
			if(m.group(6) == null || m.group(6).isBlank()) return null;
			return Long.parseLong(m.group(6).replace("-", ""));
		}
		@Override
		public String batch(String fileName) {
			Matcher m = FILE_NAME_PATTERN.matcher(fileName);
			if(!m.find()) return null;
			return m.group(2);
		}
		@Override
		public Analysis map(String fileName, @Nullable Request request) {
			Matcher m = FILE_NAME_PATTERN.matcher(fileName);
			if(!m.find()) return null;
			String serial = m.group(1);
			String batch = m.group(2);
			String panel = m.group(3);
			int row = Integer.parseInt(m.group(9));
			UUID sheet = SHEET_RD;
			if(request!=null) {
				if(Arrays.stream(TestWithSingleInfo.TESTS).anyMatch(t -> t.code().equalsIgnoreCase(request.pk().service()))) sheet = SHEET_WES;
				else if("wes".equalsIgnoreCase(panel)) sheet = SHEET_WES;
				String ext = request.pk().sample() + ":" + request.pk().service();
				return new Analysis(new Analysis.AnalysisPK(sheet, batch, row, ext)).panel(panel).serial(serial)
																					.sample(request.pk().sample()).service(request.pk().service());
			} else {
				if("wes".equalsIgnoreCase(panel)) sheet = SHEET_WES;
				return new Analysis(new Analysis.AnalysisPK(sheet, batch, row, m.group(5))).panel(panel).serial(serial);
			}
		}

		@Override
		public List<String> tags(Map<String, String> values) {
			return List.of("candidate");
		}
	};
	private final List<String> services = new LinkedList<>();
	public SnvCandidatesv2(ElasticsearchRestTemplate em, SnvCandidateDAO candidateDao, SnvConsensualClassDao consensualDao, RequestDAO requestDAO, ObjectMapper om) {
		super(em, requestDAO, candidateDao, consensualDao, om, PROCESSOR);
		for(com.gcgenome.lims.test.panel.TestInfo t: com.gcgenome.lims.test.panel.TestInfo.TESTS) services.add(t.code());
		for(com.gcgenome.lims.test.single.TestInfo t: com.gcgenome.lims.test.single.TestInfo.TESTS)
			if(t.category() == com.gcgenome.lims.test.single.TestInfo.Category.ETC) services.add(t.code());
		for(com.gcgenome.lims.test.geneplus.TestInfo t: com.gcgenome.lims.test.geneplus.TestInfo.TESTS_ETC) services.add(t.code());
		for(TestInfo t: TestInfo.TESTS_RD) services.add(t.code());
		for(TestWithSingleInfo t: TestWithSingleInfo.TESTS) services.add(t.code());
		for(com.gcgenome.lims.test.wes.TestInfo t: com.gcgenome.lims.test.wes.TestInfo.TESTS) services.add(t.code());
	}
	@Override
	protected String[] services() {
		return services.toArray(String[]::new);
	}
	@Override
	public String name() {
		return "GMD-SNV-Candidates ver.2";
	}
	@Override
	public boolean chk(File file) {
		if(!"v2".equalsIgnoreCase(file.getParentFile().getName())) return false;
		if(PANELS.stream().noneMatch(p->p.equalsIgnoreCase(file.getParentFile().getParentFile().getName()))) return false;
		String fileName = file.getName();
		Matcher m = FILE_NAME_PATTERN.matcher(fileName);
		return m.find();
	}
	private final static Set<String> PANELS = Set.of("AN",
			"AU",
			"CD",
			"CM",
			"CT",
			"DM",
			"EP",
			"EY",
			"HL",
			"IE",
			"IM",
			"KD",
			"MC",
			"MD",
			"MT",
			"ND",
			"PD",
			"PN",
			"RA",
			"RP",
			"SD",
			"SP",
			"SS",
			"ST",
			"GMD");
}
