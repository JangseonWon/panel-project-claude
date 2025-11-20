package com.greencross.lims.worker.brca.impl.v1_1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcgenome.lims.test.genomescreen.TestInfo;
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

@Service("BRCA-SNV-Candidates ver.1.1")
public class SnvCandidatesv1_1 extends SnvWorkerImpl {
	private static final Pattern FILE_NAME_PATTERN = Pattern.compile("^(I_(\\d{2}BRCA\\d{1,4})[-_](\\d{1,4})(([-_]\\d{2}(.*)[-_]\\d{1,4})|[-_]\\d{2}(\\D+)\\d{1,4})*.*[-_]((\\d{8}-\\d{3}-\\d{4})|(?i)(positive))([^.]*)).annotation_filter_Candidates.txt$");
	private static final UUID SHEET = UUID.fromString("a118c948-74b9-4929-abf9-35d8a50e6f37");
	private static final Process PROCESSOR = new Process() {
		@Override
		public Long sample(String fileName) {
			Matcher m = FILE_NAME_PATTERN.matcher(fileName);
			if(!m.find()) return null;
			if(m.group(9) == null || m.group(9).isBlank()) return null;
			return Long.parseLong(m.group(9).replace("-", ""));
		}
		@Override
		public String panel(String fileName) {
			Matcher m = FILE_NAME_PATTERN.matcher(fileName);
			if(!m.find()) return null;
			String panel = m.group(6);
			if(panel == null) panel = m.group(7);
			return panel;
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
			int row = Integer.parseInt(m.group(3));
			String panel = m.group(6);
			if(panel == null) panel = m.group(7);
			String ext = request!=null?(request.pk().sample()+":"+request.pk().service()):m.group(8);
			Analysis entity = new Analysis(new Analysis.AnalysisPK(SHEET, batch, row, ext)).serial(serial).panel(panel);
			if(request!=null) return entity.sample(request.pk().sample()).service(request.pk().service());
			return entity;
		}

		@Override
		public List<String> tags(Map<String, String> values) {
			return List.of("candidate");
		}
	};
	private final List<String> services = new LinkedList<>();
	public SnvCandidatesv1_1(ElasticsearchRestTemplate em, SnvCandidateDAO candidateDao, SnvConsensualClassDao consensualDao, RequestDAO requestDAO, ObjectMapper om) {
		super(em, requestDAO, candidateDao, consensualDao, om, PROCESSOR);
		Arrays.stream(com.gcgenome.lims.test.single.TestInfo.TESTS).map(com.gcgenome.lims.test.single.TestInfo::code).forEach(services::add);
		Arrays.stream(com.gcgenome.lims.test.geneplus.TestInfo.TESTS_BRCA).map(com.gcgenome.lims.test.geneplus.TestInfo::code).forEach(services::add);
		Arrays.stream(TestInfo.TESTS_BRCA).map(TestInfo::code).forEach(services::add);
		services.add("N001");
		services.add("ON001");
	}
	@Override
	protected String[] services() {
		return services.toArray(String[]::new);
	}
	@Override
	public String name() {
		return "BRCA-SNV-Candidates ver.1.1";
	}
	@Override
	public boolean chk(File file) {
		if(!"v1.1".equalsIgnoreCase(file.getParentFile().getName())) return false;
		if(!"BRCA".equalsIgnoreCase(file.getParentFile().getParentFile().getName())) return false;
		String fileName = file.getName();
		boolean matched = FILE_NAME_PATTERN.matcher(fileName).find();
		if(matched && PROCESSOR.sample(fileName) >= 202209010000000L) return false;
		return matched;
	}
}
