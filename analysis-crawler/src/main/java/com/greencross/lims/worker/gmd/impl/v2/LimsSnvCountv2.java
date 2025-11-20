package com.greencross.lims.worker.gmd.impl.v2;

import com.gcgenome.lims.test.genomescreen.TestInfo;
import com.gcgenome.lims.test.wes.TestWithSingleInfo;
import com.greencross.lims.dao.AnalysisDAO;
import com.greencross.lims.dao.RequestDAO;
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

@Service("GMD-LIMS SnvCount ver.2")
public class LimsSnvCountv2 extends SnvCountWorker {
	private static final Pattern FILE_NAME_PATTERN = Pattern.compile("^(\\d{2}[A-Z]+\\d{1,4}).variant_count_LIMS.txt$");
	// 21GMD030_20210330-171-5130_SD-4_M
	private static final Pattern SERIAL_PATTERN = Pattern.compile("^(\\d{2}([A-Z]+)\\d{1,4})(-RE)*[-_]((\\d{8}-\\d{3}-\\d{4})|(?i)(.+))[-_](.+)[-_](\\d{1,4})(.*)$");
	private static final UUID SHEET_WES = UUID.fromString("01396c17-df3e-4669-a11b-d86c56144cee");
	private static final UUID SHEET_RD = UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5");
	private final List<String> services = new LinkedList<>();
	private final RequestDAO dao;
	public LimsSnvCountv2(AnalysisDAO dao, RequestDAO dao1) {
		super(dao);
		this.dao = dao1;
		for(com.gcgenome.lims.test.panel.TestInfo t: com.gcgenome.lims.test.panel.TestInfo.TESTS) services.add(t.code());
		for(com.gcgenome.lims.test.single.TestInfo t: com.gcgenome.lims.test.single.TestInfo.TESTS)
			if(t.category() == com.gcgenome.lims.test.single.TestInfo.Category.ETC) services.add(t.code());
		for(com.gcgenome.lims.test.geneplus.TestInfo t: com.gcgenome.lims.test.geneplus.TestInfo.TESTS_ETC) services.add(t.code());
		for(TestInfo t: TestInfo.TESTS_RD) services.add(t.code());
		for(com.gcgenome.lims.test.wes.TestInfo t: com.gcgenome.lims.test.wes.TestInfo.TESTS) services.add(t.code());
	}
	@Override
	public boolean chk(File file) {
		if(!"v2".equalsIgnoreCase(file.getParentFile().getName())) return false;
		if(PANELS.stream().noneMatch(p->p.equalsIgnoreCase(file.getParentFile().getParentFile().getName()))) return false;
		String fileName = file.getName();
		Matcher m = FILE_NAME_PATTERN.matcher(fileName);
		return m.find();
	}
	protected String[] services() {
		return services.toArray(String[]::new);
	}
	@Override
	public String name() {
		return "GMD-LIMS SnvCount ver.2";
	}
	private static final String FIND_REQUEST_BY_SAMPLE = "SELECT * FROM request WHERE " +
			"sample IN (SELECT id FROM sample where patient=(SELECT patient FROM sample WHERE id=:sample)) " +
			"AND date_request >= (SELECT max(date_request)+'-1 week' FROM request WHERE sample=:sample) " +
			"AND date_request <= (SELECT max(date_request)+'1 week' FROM request WHERE sample=:sample)";
	@Override
	protected List<Analysis> findEntity(String fileName) {
		var sample = sample(fileName);
		if(sample == null) {
			var entity = analysis(fileName, null);
			entity = dao.em().merge(entity);
			return List.of(entity);
		} else {
			var targets = Arrays.stream(services()).collect(Collectors.toSet());
			List<Request> req = dao.em().createNativeQuery(FIND_REQUEST_BY_SAMPLE, Request.class).setParameter("sample", sample).getResultList();
			return req.stream()
					  .filter(r->targets.contains(r.pk().service()))
					  .filter(r -> getPanelServices(fileName).contains(r.pk().service()))
					  .map(r->analysis(fileName, r))
					  .map(a->{
					  		Analysis prev = dao.em().find(Analysis.class, a.pk());
					  		if(prev!=null) return prev;
					  		else return dao.em().merge(a);
					  }).collect(Collectors.toList());
		}
	}
	@Override
	protected Long sample(String fileName) {
		Matcher m = SERIAL_PATTERN.matcher(fileName);
		if(!m.find()) return null;
		if(m.group(5) == null || m.group(5).isBlank()) return null;
		return Long.parseLong(m.group(5).replace("-", ""));
	}
	@Override
	protected Analysis analysis(String fileName, @Nullable Request request) {
		Matcher m = SERIAL_PATTERN.matcher(fileName);
		if(!m.find()) return null;
		String batch = m.group(1);
		String panel = m.group(2);
		int row = Integer.parseInt(m.group(8));
		UUID sheet = SHEET_RD;
		if(request!=null) {
			if(Arrays.stream(TestWithSingleInfo.TESTS).anyMatch(t -> t.code().equalsIgnoreCase(request.pk().service()))) sheet = SHEET_WES;
			else if("wes".equalsIgnoreCase(panel)) sheet = SHEET_WES;
			String ext = request.pk().sample()+":"+request.pk().service();
			return new Analysis(new Analysis.AnalysisPK(sheet, batch, row, ext)).panel(panel).serial(fileName)
					.sample(request.pk().sample()).service(request.pk().service());
		} else {
			if("wes".equalsIgnoreCase(panel)) sheet = SHEET_WES;
			return new Analysis(new Analysis.AnalysisPK(sheet, batch, row, m.group(4))).panel(panel).serial(fileName);
		}
	}
	@Override
	public final String batch(File file) {
		Matcher m = FILE_NAME_PATTERN.matcher(file.getName());
		if(!m.find()) return null;
		return m.group(1);
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
