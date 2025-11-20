package com.gcgenome.lims.service.comment;

import com.gcgenome.lims.dao.AbstractJpaDAO;
import com.gcgenome.lims.dao.SearchableJpa;
import com.gcgenome.lims.dto.QueryServerside;
import com.gcgenome.lims.entity.SnvComment;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Objects;
import java.util.stream.Stream;

@Repository
public class SnvCommentDao extends AbstractJpaDAO<SnvComment> implements SearchableJpa<SnvComment> {
	@Override
	public Predicate map(CriteriaBuilder cb, Root<SnvComment> c, QueryServerside.Filter filter) {
		return map(cb, c, filter.key(), filter.value());
	}
	public Predicate map(CriteriaBuilder cb, Root<SnvComment> c,  String key, String value) {
		if("snv".equalsIgnoreCase(key)) return cb.like(parse(c,"snv"), "%" + value);
		return cb.equal(c.get(key), value);
	}
	public SnvComment findLastBySnv(String id) {
		CriteriaBuilder cb = builder();
		CriteriaQuery<SnvComment> q = cb.createQuery(SnvComment.class);
		Root<SnvComment> c = q.from(SnvComment.class);
		q.where(cb.like(parse(c,"snv"), "%" + id)).distinct(true).orderBy(cb.desc(c.get("createTime"))).select(c);
		try {
			return em().createQuery(q).setMaxResults(1).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public Stream<SnvComment> findBySnv(String id) {
		CriteriaBuilder cb = builder();
		CriteriaQuery<SnvComment> q = cb.createQuery(SnvComment.class);
		Root<SnvComment> c = q.from(SnvComment.class);
		q.where(cb.like(parse(c,"snv"), "%" + id))
		 .distinct(true).select(c);
		return em().createQuery(q)
				   .getResultStream()
				   .filter(Objects::nonNull);
	}
}
