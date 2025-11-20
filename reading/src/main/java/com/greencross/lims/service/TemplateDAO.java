package com.greencross.lims.service;

import com.greencross.lims.dao.AbstractJpaDAO;
import com.greencross.lims.entity.Sheet;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
public class TemplateDAO extends AbstractJpaDAO<Sheet> {
	public Optional<Sheet> findByName(String name) {
		CriteriaBuilder cb = builder();
		CriteriaQuery<Sheet> q = cb.createQuery(Sheet.class);
		Root<Sheet> c = q.from(Sheet.class);
		q.select(c).where(cb.equal(c.get("name"), name));
		return em().createQuery(q).getResultStream().findFirst();
	}
}
