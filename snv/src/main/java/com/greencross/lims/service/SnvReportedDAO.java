package com.greencross.lims.service;

import com.greencross.lims.dao.AbstractJpaDAO;
import com.greencross.lims.entity.Snv;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.stream.Stream;

@Repository
public class SnvReportedDAO extends AbstractJpaDAO<Snv> {
	public Stream<Snv> findSnvByRequest(long sample, String service) {
		CriteriaBuilder cb = builder();
		CriteriaQuery<Snv> cq = cb.createQuery(Snv.class);
		Root<Snv> from = cq.from(Snv.class);
		cq.where(cb.and(cb.equal(from.get("sample"), sample), cb.equal(from.get("service"), service)));
		return em().createQuery(cq).getResultStream();
	}
}
