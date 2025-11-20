package com.greencross.lims.kanban;

import com.greencross.lims.dao.AbstractJpaDAO;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Objects;
import java.util.stream.Stream;

@Repository
public class IssueDAO extends AbstractJpaDAO<IssueRareDisease> {
	public Stream<IssueRareDisease> findByActivated(boolean activated) {
		CriteriaBuilder cb = builder();
		CriteriaQuery<IssueRareDisease> q = cb.createQuery(entityClass());
		Root<IssueRareDisease> c = q.from(entityClass());
		q.where(cb.equal(c.get("activated"), activated))
		 .orderBy(cb.asc(c.get("sample")), cb.asc(c.get("service")))
		 .select(c);
		return em().createQuery(q)
				   .getResultStream()
				   .filter(Objects::nonNull);
	}
}
