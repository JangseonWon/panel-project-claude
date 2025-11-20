package com.greencross.lims.worker.cancer.impl.vTemp;

import com.gcgenome.lims.test.genomescreen.TestInfo;
import com.greencross.lims.dao.RequestDAO;
import com.greencross.lims.entity.Analysis;
import com.greencross.lims.entity.AnalysisFile;
import com.greencross.lims.entity.Request;
import com.greencross.lims.worker.Worker;

import javax.annotation.Nullable;
import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

//@Service("CANCER-LIMS Stat ver.1.1")
public class LimsStatv1 implements Worker {
	/*
	Sample Name     Depth(X)        0X(%)   5X(%)   10X(%)  20X(%)  30X(%)  50X(%)
	21WES022_20211013-171-5087_ALS-1_M      202.38  1.0     95.7    94.9    93.0    91.3    86.9
	21WES022_20211013-171-5088_ALS-3_M      260.04  1.0     96.3    95.0    93.9    92.5    89.5
	21WES022_20211013-171-5089_ALS-2_M      175.39  2.0     95.6    94.7    92.7    90.7    84.7
	21WES022_20211013-971-2803_WES-4_M      199.04  1.0     98.0    97.4    96.4    95.3    91.9
	21WES022_20211015-171-5131_ALS-6_M      196.44  2.0     95.5    94.6    92.9    91.1    86.4
	21WES022_20211015-171-5132_ALS-5_M      264.81  1.0     96.1    95.4    93.7    92.6    90.3
	 */
	private static final Pattern FILE_NAME_PATTERN = Pattern.compile("^(\\d{2}[-A-Za-z]+\\d{1,4}).LIMS_stat.txt$");
	// 21GMD030_20210330-171-5130_SD-4_M
	private static final Pattern SERIAL_PATTERN = Pattern.compile("^((\\d{2}(?:OS-)*Cancer\\d{1,4})[-_]((\\d{2}(\\D+)\\d{1,4}.*)|([^-]*)[-_])?(\\d{1,4})*[-_]((\\d{8}-\\d{3}-\\d{4})|(?i)(positive))([^.]*))$");
	private static final UUID SHEET = UUID.fromString("025394fd-39ec-4cdf-9398-f62959aacdf5");
	private static final UUID SHEET2 = UUID.fromString("a118c948-74b9-4929-abf9-35d8a50e6f37");
	private final RequestDAO requestDAO;
	private final List<String> services = new LinkedList<>();
	public LimsStatv1(RequestDAO requestDAO) {
		this.requestDAO = requestDAO;
		Arrays.stream(com.gcgenome.lims.test.panel.TestInfo.TESTS).map(com.gcgenome.lims.test.panel.TestInfo::code).forEach(services::add);
		Arrays.stream(com.gcgenome.lims.test.single.TestInfo.TESTS).map(com.gcgenome.lims.test.single.TestInfo::code).forEach(services::add);
		Arrays.stream(com.gcgenome.lims.test.geneplus.TestInfo.TESTS_ETC).map(com.gcgenome.lims.test.geneplus.TestInfo::code).forEach(services::add);
		Arrays.stream(com.gcgenome.lims.test.geneplus.TestInfo.TESTS_BRCA).map(com.gcgenome.lims.test.geneplus.TestInfo::code).forEach(services::add);
		Arrays.stream(TestInfo.TESTS_CANCER).map(TestInfo::code).forEach(services::add);
		services.add("N002"); services.add("N022"); services.add("ON040");
	}

	@Override
	public boolean chk(File file) {
		if(!"v1.1".equalsIgnoreCase(file.getParentFile().getName())) return false;
		if(!"Cancer".equalsIgnoreCase(file.getParentFile().getParentFile().getName()) &&
				!"OS-Cancer".equalsIgnoreCase(file.getParentFile().getParentFile().getName())) return false;
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
		return "CANCER-LIMS Stat ver.1.1";
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
		if(m.group(9) == null || m.group(9).isBlank()) return null;
		return Long.parseLong(m.group(9).replace("-", ""));
	}
	public Analysis analysis(String fileName, @Nullable Request request) {
		Matcher m = SERIAL_PATTERN.matcher(fileName);
		if(!m.find()) return null;

		String serial = m.group(1);
		String batch = m.group(2);
		UUID sheet = SHEET;
		String panel = null;
		if(m.group(6)!=null) panel = m.group(6);
		else if(m.group(5)!=null) {
			panel = m.group(5);
			sheet = SHEET2;
		}
		int row = m.group(7)!=null?Integer.parseInt(m.group(7)):0;
		if(request!=null) {
			String ext = request.pk().sample()+":"+request.pk().service();
			return new Analysis(new Analysis.AnalysisPK(sheet, batch, row, ext)).serial(serial).panel(panel)
					.sample(request.pk().sample()).service(request.pk().service());
		} else return new Analysis(new Analysis.AnalysisPK(sheet, batch, row, m.group(10))).serial(serial).panel(panel);
	}
	@Override
	public final String batch(File file) {
		Matcher m = FILE_NAME_PATTERN.matcher(file.getName());
		if(!m.find()) return null;
		return m.group(1);
	}
}
