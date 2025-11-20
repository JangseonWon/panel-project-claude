package com.greencross.lims.worker.cancer.impl.g22;

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
import java.util.stream.Stream;

@Service("G22 Cancer-SnvCount ver.1")
public class SnvCountv1 extends SnvCountWorker {
	private static final Pattern FILE_NAME_PATTERN = Pattern.compile("^(\\d{2}G22\\d{5}-*Cancer\\d{1,4}).variant_count_LIMS.txt$");
	// 21Cancer024_GSC-06_20210324-171-5133
	private static final Pattern SERIAL_PATTERN = Pattern.compile("^(((\\d{2}G22\\d{5}-*Cancer\\d{1,4})[-_]((\\d{8}-\\d{3}-\\d{4})|(?i)(positive))([^.]*?))[-_](((.+?)-(\\d{1,4}))|(\\d{2}(\\D+)\\d{1,4}?))?[-_](\\D{1,4}))\\.annotation\\.txt$");
	private static final UUID SHEET = UUID.fromString("025394fd-39ec-4cdf-9398-f62959aacdf5");
	private static final UUID SHEET2 = UUID.fromString("a118c948-74b9-4929-abf9-35d8a50e6f37");
	private final AnalysisDAO dao;
	private final List<String> services = new LinkedList<>();
	public SnvCountv1(AnalysisDAO dao) {
		super(dao);
		this.dao = dao;
		Arrays.stream(com.gcgenome.lims.test.panel.TestInfo.TESTS).map(com.gcgenome.lims.test.panel.TestInfo::code).forEach(services::add);
		Arrays.stream(com.gcgenome.lims.test.single.TestInfo.TESTS).map(com.gcgenome.lims.test.single.TestInfo::code).forEach(services::add);
		Arrays.stream(com.gcgenome.lims.test.geneplus.TestInfo.TESTS_ETC).map(com.gcgenome.lims.test.geneplus.TestInfo::code).forEach(services::add);
		Arrays.stream(com.gcgenome.lims.test.geneplus.TestInfo.TESTS_BRCA).map(com.gcgenome.lims.test.geneplus.TestInfo::code).forEach(services::add);
		Arrays.stream(TestInfo.TESTS_CANCER).map(TestInfo::code).forEach(services::add);
		services.add("N002"); services.add("N022"); services.add("ON040");
		services.add("G2200101");services.add("G2200201");services.add("G2200102");
	}
	protected String[] services() {
		return services.toArray(String[]::new);
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

	@Override
	public String name() {
		return "G22 Cancer-SnvCount ver.1";
	}
	private static final String FIND_ANALYSIS_BY_FILENAME = "SELECT * FROM panel.analysis WHERE file=:file";
	private static final String FIND_REQUEST_BY_SAMPLE = "SELECT * FROM request WHERE " +
			"sample IN (SELECT id FROM sample where patient=(SELECT patient FROM sample WHERE id=:sample)) " +
			"AND date_request >= (SELECT max(date_request)+'-1 week' FROM request WHERE sample=:sample) " +
			"AND date_request <= (SELECT max(date_request)+'1 week' FROM request WHERE sample=:sample)";
	@Override
	protected List<Analysis> findEntity(String fileName) {
		var sample = sample(fileName);
		if(sample != null && sample == 0L) return Collections.emptyList();
		List<Analysis> current = dao.em().createNativeQuery(FIND_ANALYSIS_BY_FILENAME, Analysis.class).setParameter("file", fileName).getResultList();
		if(sample == null) {
			if(current!=null && !current.isEmpty()) return current;
			else {
				var entity = analysis(fileName, null);
				if(entity!=null) {
					entity = dao.em().merge(entity);
					return List.of(entity);
				} else return List.of();
			}
		} else {
			var hit = current.stream().map(Analysis::service).collect(Collectors.toSet());
			var targets = Arrays.stream(services()).collect(Collectors.toSet());
			List<Request> req = dao.em().createNativeQuery(FIND_REQUEST_BY_SAMPLE, Request.class).setParameter("sample", sample).getResultList();
			var ommited = req.stream()
							 .filter(r->!hit.contains(r.pk().service()))
					         .filter(r -> getPanelServices(fileName).contains(r.pk().service()))
							 .filter(r->targets.contains(r.pk().service()))
							 .map(r-> analysis(fileName, r)).map(dao.em()::merge);
			return Stream.concat(current.stream(), ommited).collect(Collectors.toList());
		}
	}
	@Override
	protected Long sample(String fileName) {
		Matcher m = SERIAL_PATTERN.matcher(fileName);
		if(!m.find()) return null;
		if(m.group(4) == null || m.group(4).isBlank()) return null;
		return Long.parseLong(m.group(4).replace("-", ""));
	}
	@Override
	protected Analysis analysis(String fileName, @Nullable Request request) {
		Matcher m = SERIAL_PATTERN.matcher(fileName);
		if(!m.find()) return null;
		String serial = m.group(1);
		String batch = m.group(3);
		UUID sheet = SHEET;
		String panel = null;
		if(m.group(10)!=null) panel = m.group(10);
		else if(m.group(13)!=null) {
			panel = m.group(13);
			sheet = SHEET2;
		}
		int row = m.group(11)!=null?Integer.parseInt(m.group(11)):0;
		if(request!=null) {
			String ext = request.pk().sample()+":"+request.pk().service();
			return new Analysis(new Analysis.AnalysisPK(sheet, batch, row, ext)).serial(serial).panel(panel)
					.sample(request.pk().sample()).service(request.pk().service());
		} else return new Analysis(new Analysis.AnalysisPK(sheet, batch, row, m.group(6))).serial(serial).panel(panel);
	}

	@Override
	public String batch(File file) {
		Matcher m = FILE_NAME_PATTERN.matcher(file.getName());
		if(!m.find()) return null;
		return m.group(1);
	}
}
