package com.greencross.lims.publish;

import com.greencross.lims.dao.AbstractJpaDAO;
import com.greencross.lims.entity.Test;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.stream.Stream;

@Repository
public class HandbookTestDao extends AbstractJpaDAO<Test> {
	public Stream<Test> findByCode(String id) {
		CriteriaBuilder cb = builder();
		CriteriaQuery<Test> q = cb.createQuery(Test.class);
		Root<Test> c = q.from(Test.class);
		q.where(cb.like(parse(c,"code"), "%-" + id)).distinct(true).select(c);
		return em().createQuery(q)
				   .getResultStream()
				   .filter(Objects::nonNull);
	}
}
