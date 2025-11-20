package com.greencross.lims.worker.cancer.impl.v3;

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

@Service("Cancer-SNV ver.3")
public class SnvAllv3 extends SnvWorkerImpl {
	private static final Pattern FILE_NAME_PATTERN = Pattern.compile("^((\\d{2}(?:OS-)*Cancer\\d{1,4})[_-]([A-Za-z0-9]+)-(\\d{1,4})[_-](\\d{8}-\\d{3}-\\d{4}))\\.annotation\\.txt$");
	private static final UUID SHEET = UUID.fromString("025394fd-39ec-4cdf-9398-f62959aacdf5");
	private static final UUID SHEET2 = UUID.fromString("a118c948-74b9-4929-abf9-35d8a50e6f37");
	public static final Process PROCESSOR = new Process() {
		@Override
		public Long sample(String fileName) {
			Matcher m = FILE_NAME_PATTERN.matcher(fileName);
			if(!m.find()) return null;
			if(m.group(5) == null || m.group(5).isBlank()) return null;
			return Long.parseLong(m.group(5).replace("-", ""));
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
			UUID sheet = SHEET;
			String panel = null;
			if(m.group(3)!=null) panel = m.group(3);
			int row = m.group(4)!=null?Integer.parseInt(m.group(4)):0;
			if(request!=null) {
				String ext = request.pk().sample()+":"+request.pk().service();
				return new Analysis(new Analysis.AnalysisPK(sheet, batch, row, ext)).serial(serial).panel(panel)
																					.sample(request.pk().sample()).service(request.pk().service());
			} else return new Analysis(new Analysis.AnalysisPK(sheet, batch, row, m.group(5))).serial(serial).panel(panel);
		}

		@Override
		public List<String> tags(Map<String, String> values) {
			return List.of("all");
		}

	};
	private final List<String> services = new LinkedList<>();
	public SnvAllv3(ElasticsearchRestTemplate em, SnvCandidateDAO candidateDao, SnvConsensualClassDao consensualDao, RequestDAO requestDAO, ObjectMapper om) {
		super(em, requestDAO, candidateDao, consensualDao, om, PROCESSOR);
		Arrays.stream(com.gcgenome.lims.test.panel.TestInfo.TESTS).map(com.gcgenome.lims.test.panel.TestInfo::code).forEach(services::add);
		Arrays.stream(com.gcgenome.lims.test.single.TestInfo.TESTS).map(com.gcgenome.lims.test.single.TestInfo::code).forEach(services::add);
		Arrays.stream(com.gcgenome.lims.test.geneplus.TestInfo.TESTS_ETC).map(com.gcgenome.lims.test.geneplus.TestInfo::code).forEach(services::add);
		Arrays.stream(com.gcgenome.lims.test.geneplus.TestInfo.TESTS_BRCA).map(com.gcgenome.lims.test.geneplus.TestInfo::code).forEach(services::add);
		Arrays.stream(TestInfo.TESTS_CANCER).map(TestInfo::code).forEach(services::add);
		services.add("N002"); services.add("N022"); services.add("ON040");
		services.add("G2200101");services.add("G2200201");services.add("G2200102");
	}
	@Override
	protected String[] services() {
		return services.toArray(String[]::new);
	}
	@Override
	public String name() {
		return "Cancer-SNV ver.3";
	}
	@Override
	public boolean chk(File file) {
		if(!"v2".equalsIgnoreCase(file.getParentFile().getName())) return false;
		if(!"Cancer".equalsIgnoreCase(file.getParentFile().getParentFile().getName()) &&
		   !"OS-Cancer".equalsIgnoreCase(file.getParentFile().getParentFile().getName())) return false;
		String fileName = file.getName();
		boolean matched = FILE_NAME_PATTERN.matcher(fileName).find();
		if(matched && PROCESSOR.sample(fileName) < 202209010000000L) return false;
		return matched;
	}
}
