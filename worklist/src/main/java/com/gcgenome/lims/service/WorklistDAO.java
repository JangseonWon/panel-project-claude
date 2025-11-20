package com.gcgenome.lims.service;

import com.gcgenome.lims.dao.AbstractJpaDAO;
import com.gcgenome.lims.dao.GroupableJpa;
import com.gcgenome.lims.dto.QueryServerside;
import com.gcgenome.lims.entity.Sample;
import com.gcgenome.lims.entity.WorklistRareDisease;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.util.Objects;
import java.util.stream.Stream;

@Repository
public class WorklistDAO extends AbstractJpaDAO<WorklistRareDisease> implements GroupableJpa<WorklistRareDisease> {
	@Override
	public Predicate map(CriteriaBuilder cb, Root<WorklistRareDisease> c, QueryServerside.Filter filter) {
		return map(cb, c, filter.key(), filter.value());
	}
	// Cockroachdb에 MV 가 구현되면, 아래와 같이 join할 필요 없이 MV에서 검색 후 PK를 얻어와서 목록을 뿌려주는 형식으로 변경할 수 있다.
	private Predicate map(CriteriaBuilder cb, Root<WorklistRareDisease> c, String key, String value) {
		if(key == null || key.trim().isEmpty()) {
			Predicate[] predicates = Stream.of(
					map(cb, c, "title", value),
					map(cb, c, "id", value),
					map(cb, c, "sample", value),
					map(cb, c, "patient", value)
			).filter(Objects::nonNull).toArray(Predicate[]::new);
			return cb.or(predicates);
		} else if("title".equalsIgnoreCase(key)) {
			return cb.like(c.get("title"), "%" + value + "%");
		} else if("id".equalsIgnoreCase(key)) {
			return cb.equal(parse(c,"recepts.serial"), value);
		} else if("sample".equalsIgnoreCase(key)) {
			if(value == null) return null;
			if(value.contains("-")) value = value.replace("-", "");
			if(value.matches("\\d+")) return cb.equal(parse(c,"recepts.sample"), em().getReference(Sample.class, Long.parseLong(value)));
			else return null;
		} else if("patient".equalsIgnoreCase(key)) {
			return cb.equal(parse(c,"recepts.sample.patient.name"), value);
		} else return null;
	}
	public Integer findLastIndex() {
		CriteriaBuilder cb = builder();
		CriteriaQuery<Integer> q = cb.createQuery(Integer.class);
		Root<WorklistRareDisease> c = q.from(WorklistRareDisease.class);
		Expression<Integer> countExp = cb.max(c.get("worklist"));
		q.select(countExp);
		return em().createQuery(q).getSingleResult();
	}
}
