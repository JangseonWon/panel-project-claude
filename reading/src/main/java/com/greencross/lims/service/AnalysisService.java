package com.greencross.lims.service;

import com.gcgenome.lims.test.geneplus.TestInfo;
import com.greencross.lims.dto.Analysis;
import com.greencross.lims.dto.QueryServerside;
import com.greencross.lims.entity.Interpretation;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.trans.AnalysisToDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AnalysisService {
	private final AnalysisDAO dao;
	public AnalysisService(AnalysisDAO dao) {
		this.dao = dao;
	}
	public Page<Analysis> list(String type, QueryServerside query) {
		if(query.filters()==null) query.filters(List.of());
		query.filters(Stream.concat(query.filters().stream(), Stream.of(
				new QueryServerside.Filter().key("type").value(type),
				new QueryServerside.Filter().key("reception").value("true"),
				new QueryServerside.Filter().key("activityState").value("ACTIVE")
				)).collect(Collectors.toList()));
		return dao.search(query).map(AnalysisToDTO::map);
	}

	public List<Analysis> list(long sample) {
		return dao.em().createQuery("SELECT a FROM Analysis a WHERE sample=:sample", com.greencross.lims.entity.Analysis.class)
				  .setParameter("sample", dao.em().getReference(Sample.class, sample)).getResultList().stream()
				  .map(AnalysisToDTO::map)
				  .map(dto->{
					  Interpretation interpretation = dao.em().find(Interpretation.class, Interpretation.InterpretationPK.builder().sample(sample).service(dto.service()).build());
					  return dto.result(result(interpretation));
				  }).collect(Collectors.toList());
	}

	public Analysis find(long sample, String service, String batch, int row) {
		return dao.em().createQuery("SELECT a FROM Analysis a WHERE sample=:sample AND service=:service AND batch=:batch AND row=:row", com.greencross.lims.entity.Analysis.class)
				  .setParameter("sample", dao.em().getReference(Sample.class, sample))
				  .setParameter("service", dao.em().getReference(com.greencross.lims.entity.Service.class, service))
				  .setParameter("pk.batch", batch)
				  .setParameter("pk.row", row)
				  .getResultList().stream().findFirst()
				  .map(AnalysisToDTO::map)
				  .map(dto->{
					  Interpretation interpretation = dao.em().find(Interpretation.class, Interpretation.InterpretationPK.builder().sample(sample).service(service).build());
					  return dto.result(result(interpretation));
				  }).orElseThrow(()->new RuntimeException("Can't find Analysis:" + sample + ", " + service + ", " + batch + ", " +row));
	}
	private String result(Interpretation interpretation) {
		if(interpretation == null) return null;
		String service = interpretation.pk().service();
		if(Arrays.stream(com.gcgenome.lims.test.panel.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))		return (String) interpretation.value().get("result");
		if(Arrays.stream(com.gcgenome.lims.test.single.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))		return (String) interpretation.value().get("result");
		if(Arrays.stream(TestInfo.TESTS_ETC).anyMatch(t->t.code().equalsIgnoreCase(service)))	return (String) interpretation.value().get("result");
		if(Arrays.stream(TestInfo.TESTS_BRCA).anyMatch(t->t.code().equalsIgnoreCase(service)))return (String) interpretation.value().get("result");
		//if(Arrays.stream(com.greencross.lims.test.dgs.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))			return DgsCollapseElement.build(sample, service);
		//if(Arrays.stream(com.greencross.lims.test.bloodcancer.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))	return BloodCancerCollapseElement.build(sample, service);
		//if(Arrays.stream(com.greencross.lims.test.hrd.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))			return HrdCollapseElement.build(sample, service);
		//if(Arrays.stream(com.greencross.lims.test.mrd.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))			return MrdCollapseElement.build(sample, service);
		return null;
	}
}
