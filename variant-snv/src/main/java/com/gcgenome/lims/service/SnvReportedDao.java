package com.gcgenome.lims.service;

import com.gcgenome.lims.dao.AbstractJpaDAO;
import com.gcgenome.lims.dao.SearchableJpa;
import com.gcgenome.lims.dto.QueryServerside;
import com.gcgenome.lims.entity.Snv;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Objects;
import java.util.stream.Stream;

@Repository
public class SnvReportedDao extends AbstractJpaDAO<Snv> implements SearchableJpa<Snv> {
	@Override
	public Predicate map(CriteriaBuilder cb, Root<Snv> c, QueryServerside.Filter filter) {
		return map(cb, c, filter.key(), filter.value());
	}
	public Predicate map(CriteriaBuilder cb, Root<Snv> c,  String key, String value) {
		if(key == null || key.trim().isEmpty()) {
			Predicate[] predicates = Stream.of(
					map(cb, c, "batch", value),
					map(cb, c, "snv", value),
					map(cb, c, "sample", value),
					map(cb, c, "patient", value),
					map(cb, c, "panel", value)
			).filter(Objects::nonNull).toArray(Predicate[]::new);
			return cb.or(predicates);
		} else if("snv".equalsIgnoreCase(key)) return cb.like(parse(c,"snv"), "%" + value);
		else if("reported".equalsIgnoreCase(key)) return cb.equal(parse(c, "reported"), true);
		return cb.equal(c.get(key), value);
	}
}
