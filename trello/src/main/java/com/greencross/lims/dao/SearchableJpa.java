package com.greencross.lims.dao;

import com.greencross.lims.dto.Query_;
import org.springframework.data.domain.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface SearchableJpa<Entity> {
	Class<Entity> entityClass();
	CriteriaBuilder builder();
	EntityManager em();
	default long count(Query_ query) {
		CriteriaBuilder cb = builder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<Entity> c = q.from(entityClass());
		if(query.getFilters()!=null) query.getFilters().stream().map(f -> map(cb, c, f)).filter(Objects::nonNull).reduce(cb::and).ifPresent(q::where);
		q.select(cb.count(c));
		return em().createQuery(q).getSingleResult();
	}
	default Stream<Entity> list(Query_ query) {
		int page = query.getPage();
		int limit = query.getLimit();
		int start = page*limit;
		String sort = query.getSortBy();
		boolean isAsc = query.getAsc();
		CriteriaBuilder cb = builder();
		CriteriaQuery<Entity> q = cb.createQuery(entityClass());
		Root<Entity> c = q.from(entityClass());
		if(query.getFilters()!=null) query.getFilters().stream().map(f -> map(cb, c, f)).filter(Objects::nonNull).reduce(cb::and).ifPresent(q::where);
		if(sort!=null && !sort.trim().isEmpty()) q.orderBy(isAsc?cb.asc(AbstractJpaDAO.parse(c, sort)):cb.desc(AbstractJpaDAO.parse(c, sort)));
		q.distinct(true).select(c);
		TypedQuery<Entity> t = em().createQuery(q);
		if(limit > 0) t.setFirstResult(start).setMaxResults(limit);
		return t.getResultStream().filter(Objects::nonNull);
	}
	default Page<Entity> search(Query_ query) {
		assert query != null;
		Pageable pageable;
		if(query.getSortBy()!=null) {
			boolean isAsc = query.getAsc();
			pageable = PageRequest.of(query.getPage(), query.getLimit(), Sort.by(isAsc? Sort.Direction.ASC: Sort.Direction.DESC, query.getSortBy()));
		} else pageable = PageRequest.of(query.getPage(), query.getLimit());
		return new PageImpl<>(list(query).collect(Collectors.toList()), pageable, count(query));
	}
	Predicate map(CriteriaBuilder cb, Root<Entity> c, Query_.Companion.Filter filter);
}
