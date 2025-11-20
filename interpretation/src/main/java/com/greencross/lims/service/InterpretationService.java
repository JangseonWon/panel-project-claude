package com.greencross.lims.service;

import com.greencross.lims.dto.interpretation.InterpretationParam;
import com.greencross.lims.entity.Interpretation;
import com.greencross.lims.service.interpretation.Interpretable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class InterpretationService {
	private final InterpretationDAO dao;
	private final List<Interpretable> interpretables;
	public InterpretationService(@Qualifier("in") InterpretationDAO dao, List<Interpretable> interpretables) {
		this.dao = dao;
		this.interpretables = interpretables;
	}
	@Transactional(readOnly = true)
	public Map<String, Object> get(long sample, String service) {
		Interpretation.InterpretationPK pk = Interpretation.InterpretationPK.builder().sample(sample).service(service).build();
		return dao.find(pk).map(Interpretation::value).orElse(null);
	}
	@Transactional
	public void create(long sample, String service, Map<String, Object> json) {
		Interpretation.InterpretationPK pk = Interpretation.InterpretationPK.builder().sample(sample).service(service).build();
		if(json!=null && !json.isEmpty()) {
			Interpretation entity = dao.find(pk).orElse(new Interpretation().pk(pk));
			dao.merge(entity.value(json));
		} else if(dao.exists(pk)) dao.remove(dao.em().getReference(Interpretation.class, pk));
	}
	@Transactional
	public void delete(long sample, String service) {
		Interpretation.InterpretationPK pk = Interpretation.InterpretationPK.builder().sample(sample).service(service).build();
		if(dao.exists(pk)) dao.remove(dao.em().getReference(Interpretation.class, pk));
	}

	public Mono<Object> auto(long sample, String service, InterpretationParam param) {
		return interpretables.stream().filter(i->i.chk(sample, service)).map(i->i.interpret(sample, service, param)).findFirst().orElseThrow();
	}

	public Object negative(long sample, String service) {
		return interpretables.stream().filter(i->i.chk(sample, service)).map(i->i.negative(sample, service)).findFirst().orElseThrow();
	}
}
