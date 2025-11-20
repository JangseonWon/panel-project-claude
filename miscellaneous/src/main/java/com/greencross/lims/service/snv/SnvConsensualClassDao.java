package com.greencross.lims.service.snv;

import com.greencross.lims.dao.AbstractJpaDAO;
import com.greencross.lims.dao.SearchableJpa;
import com.greencross.lims.dto.QueryServerside;
import com.greencross.lims.entity.SnvConsensualClass;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Objects;
import java.util.stream.Stream;

@Repository
public class SnvConsensualClassDao extends AbstractJpaDAO<SnvConsensualClass> implements SearchableJpa<SnvConsensualClass> {
	@Override
	public Predicate map(CriteriaBuilder cb, Root<SnvConsensualClass> c, QueryServerside.Filter filter) {
		return map(cb, c, filter.key(), filter.value());
	}
	public Predicate map(CriteriaBuilder cb, Root<SnvConsensualClass> c,  String key, String value) {
		if("snv".equalsIgnoreCase(key)) return cb.like(parse(c,"snv"), "%" + value);
		return cb.equal(c.get(key), value);
	}
	public SnvConsensualClass findLastBySnv(String id) {
		CriteriaBuilder cb = builder();
		CriteriaQuery<SnvConsensualClass> q = cb.createQuery(SnvConsensualClass.class);
		Root<SnvConsensualClass> c = q.from(SnvConsensualClass.class);
		q.where(cb.like(parse(c,"snv"), "%" + id)).distinct(true).orderBy(cb.desc(c.get("createTime"))).select(c);
		try {
			return em().createQuery(q).setMaxResults(1).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
}
