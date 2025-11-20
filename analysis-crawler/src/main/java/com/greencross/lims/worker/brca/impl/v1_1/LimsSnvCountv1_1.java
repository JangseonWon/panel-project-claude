package com.greencross.lims.worker.brca.impl.v1_1;

import com.gcgenome.lims.test.genomescreen.TestInfo;
import com.greencross.lims.dao.AnalysisDAO;
import com.greencross.lims.entity.Analysis;
import com.greencross.lims.entity.Request;
import com.greencross.lims.worker.SnvCountWorker;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service("BRCA-SnvCount ver.1.1")
public class LimsSnvCountv1_1 extends SnvCountWorker {
	private static final Pattern FILE_NAME_PATTERN = Pattern.compile("(\\d{2}BRCA\\d{1,4}).variant_count_LIMS.txt$");
	// I_21BRCA021-09_21BRCA244_20210323-171-5122
	private static final Pattern SERIAL_PATTERN = Pattern.compile("^(I_(\\d{2}BRCA\\d{1,4})[-_](\\d{1,4})(([-_]\\d{2}(.*)[-_]\\d{1,4})|[-_]\\d{2}(\\D+)\\d{1,4})*.*[-_]((\\d{8}-\\d{3}-\\d{4})|(?i)(positive))([^.]*))$");
	private static final UUID SHEET = UUID.fromString("a118c948-74b9-4929-abf9-35d8a50e6f37");
	private final AnalysisDAO requestDAO;
	private final List<String> services = new LinkedList<>();
	public LimsSnvCountv1_1(AnalysisDAO dao) {
		super(dao);
		this.requestDAO =dao;
		Arrays.stream(com.gcgenome.lims.test.single.TestInfo.TESTS).map(com.gcgenome.lims.test.single.TestInfo::code).forEach(services::add);
		Arrays.stream(com.gcgenome.lims.test.geneplus.TestInfo.TESTS_BRCA).map(com.gcgenome.lims.test.geneplus.TestInfo::code).forEach(services::add);
		Arrays.stream(TestInfo.TESTS_BRCA).map(TestInfo::code).forEach(services::add);
		services.add("N001");
		services.add("ON001");
	}
	protected String[] services() {
		return services.toArray(String[]::new);
	}
	@Override
	public boolean chk(File file) {
		if(!"v1.1".equalsIgnoreCase(file.getParentFile().getName())) return false;
		if(!"BRCA".equalsIgnoreCase(file.getParentFile().getParentFile().getName())) return false;
		String fileName = file.getName();
		Matcher m = FILE_NAME_PATTERN.matcher(fileName);
		return m.find();
	}
	@Override
	public String name() {
		return "BRCA-SnvCount ver.1.1";
	}
	private static final String FIND_REQUEST_BY_SAMPLE = "SELECT * FROM request WHERE " +
			"sample IN (SELECT id FROM sample where patient=(SELECT patient FROM sample WHERE id=:sample)) " +
			"AND date_request >= (SELECT max(date_request)+'-1 week' FROM request WHERE sample=:sample) " +
			"AND date_request <= (SELECT max(date_request)+'1 week' FROM request WHERE sample=:sample)";
	@Override
	protected List<Analysis> findEntity(String fileName) {
		var sample = sample(fileName);
		if(sample != null && sample == 0L) return Collections.emptyList();
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
	@Override
	protected Long sample(String fileName) {
		Matcher m = SERIAL_PATTERN.matcher(fileName);
		if(!m.find()) return null;
		if(m.group(9) == null || m.group(9).isBlank()) return null;
		long val = Long.parseLong(m.group(9).replace("-", ""));
		if(val >= 202209010000000L) return 0L;
		return val;
	}
	@Override
	protected Analysis analysis(String fileName, @Nullable Request request) {
		Matcher m = SERIAL_PATTERN.matcher(fileName);
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
	public final String batch(File file) {
		Matcher m = FILE_NAME_PATTERN.matcher(file.getName());
		if(!m.find()) return null;
		return m.group(1);
	}
}
