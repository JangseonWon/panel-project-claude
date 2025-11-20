package com.greencross.lims.publish;

import com.greencross.lims.dao.AbstractJpaDAO;
import com.greencross.lims.entity.SnvConsensualClass;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.stream.Stream;

@Repository
public class SnvConsensualClassDao extends AbstractJpaDAO<SnvConsensualClass> {
	public Stream<SnvConsensualClass> findBySnv(String id) {
		CriteriaBuilder cb = builder();
		CriteriaQuery<SnvConsensualClass> q = cb.createQuery(SnvConsensualClass.class);
		Root<SnvConsensualClass> c = q.from(SnvConsensualClass.class);
		q.where(cb.like(parse(c,"snv"), "%" + id))
		 .distinct(true).select(c);
		return em().createQuery(q)
				   .getResultStream()
				   .filter(Objects::nonNull);
	}
}
