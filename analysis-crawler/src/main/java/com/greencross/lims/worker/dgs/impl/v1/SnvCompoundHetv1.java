package com.greencross.lims.worker.dgs.impl.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcgenome.lims.test.dgs.TestInfo;
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

@Service("DGS-SNV-CompoundHet ver.1")
public class SnvCompoundHetv1 extends SnvWorkerImpl {
	private static final Pattern FILE_NAME_PATTERN = Pattern.compile("^((\\d{2}DGS\\d{1,4})[-_]*(\\d{1,4})*[-_](\\d{8}-\\d{3}-\\d{4})(?:[-_]([^.]+))*[-_](\\d+)).*.compoundHET.txt$");
	private static final UUID SHEET = UUID.fromString("d0ccf612-c7b4-4390-8a38-1f36591671cc");
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
			UUID sheet = SHEET;
			String panel = "WGS";

			int row = (m.group(3)!=null&&!m.group(3).trim().isEmpty())?Integer.parseInt(m.group(3)):Integer.parseInt(m.group(6));
			String ext = request.pk().sample()+":"+request.pk().service();
			return new Analysis(new Analysis.AnalysisPK(sheet, batch, row, ext)).serial(serial).panel(panel)
																				.sample(request.pk().sample()).service(request.pk().service());
		}

		@Override
		public List<String> tags(Map<String, String> values) {
			return List.of("het");
		}
	};
	private final List<String> services = new LinkedList<>();
	public SnvCompoundHetv1(ElasticsearchRestTemplate em, SnvCandidateDAO candidateDao, SnvConsensualClassDao consensualDao, RequestDAO requestDAO, ObjectMapper om) {
		super(em, requestDAO, candidateDao, consensualDao, om, PROCESSOR);
		for(TestInfo t: TestInfo.TESTS) services.add(t.code());
	}
	@Override
	protected String[] services() {
		return services.toArray(String[]::new);
	}
	@Override
	public String name() {
		return "DGS-SNV-CompoundHet ver.1";
	}
	@Override
	public boolean chk(File file) {
		if(!"v1".equalsIgnoreCase(file.getParentFile().getName())) return false;
		if(!"DGS".equalsIgnoreCase(file.getParentFile().getParentFile().getName())) return false;
		String fileName = file.getName();
		Matcher m = FILE_NAME_PATTERN.matcher(fileName);
		return m.find();
	}
}
