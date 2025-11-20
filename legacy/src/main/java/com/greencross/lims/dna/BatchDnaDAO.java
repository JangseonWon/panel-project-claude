package com.greencross.lims.dna;

import com.greencross.lims.dao.AbstractJpaDAO;
import com.greencross.lims.dao.GroupableJpa;
import com.greencross.lims.dto.Query;
import com.greencross.lims.entity.BatchDna;
import com.greencross.lims.entity.BatchTemplate;
import com.greencross.lims.entity.Sample;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class BatchDnaDAO extends AbstractJpaDAO<BatchDna> implements GroupableJpa<BatchDna> {
	@Override
	public Predicate map(CriteriaBuilder cb, Root<BatchDna> c, Query.Filter filter) {
		return map(cb, c, filter.key(), filter.value());
	}
	private Predicate map(CriteriaBuilder cb, Root<BatchDna> c, String key, String value) {
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
			return cb.equal(parse(c,"analysis.serial"), value);
		} else if("sample".equalsIgnoreCase(key)) {
			if(value == null) return null;
			if(value.contains("-")) value = value.replace("-", "");
			if(value.matches("\\d+")) return cb.equal(parse(c,"analysis.sample"), em().getReference(Sample.class, Long.parseLong(value)));
			else return null;
		} else if("patient".equalsIgnoreCase(key)) {
			return cb.equal(parse(c,"analysis.sample.patient.name"), value);
		} else return null;
	}
	public Optional<Integer> findLastBatch() {
		CriteriaBuilder cb = builder();
		CriteriaQuery<Integer> q = cb.createQuery(Integer.class);
		Root<BatchDna> c = q.from(BatchDna.class);
		Expression<Integer> maxExp = cb.max(c.get("batch"));
		q.select(maxExp).where(cb.equal(c.get("sheet"), em().getReference(BatchTemplate.class, BatchDna._ID)));
		return Optional.ofNullable(em().createQuery(q).getSingleResult());
	}
}
