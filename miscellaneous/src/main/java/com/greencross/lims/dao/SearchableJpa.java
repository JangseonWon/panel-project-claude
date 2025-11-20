package com.greencross.lims.dao;

import com.greencross.lims.dto.QueryServerside;
import org.springframework.data.domain.*;

import javax.persistence.EntityManager;
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
	default long count(QueryServerside query) {
		CriteriaBuilder cb = builder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<Entity> c = q.from(entityClass());
		query.filters().stream().map(f -> map(cb, c, f)).filter(Objects::nonNull).reduce(cb::and).ifPresent(q::where);
		q.select(cb.count(c));
		return em().createQuery(q).getSingleResult();
	}
	default Stream<Entity> list(QueryServerside query) {
		int page = query.page();
		int limit = query.limit();
		int start = page*limit;
		String sort = query.sortBy();
		boolean isAsc = query.asc();
		CriteriaBuilder cb = builder();
		CriteriaQuery<Entity> q = cb.createQuery(entityClass());
		Root<Entity> c = q.from(entityClass());
		query.filters().stream().map(f -> map(cb, c, f)).filter(Objects::nonNull).reduce(cb::and).ifPresent(q::where);
		if(sort!=null && !sort.trim().isEmpty()) q.orderBy(isAsc?cb.asc(AbstractJpaDAO.parse(c, sort)):cb.desc(AbstractJpaDAO.parse(c, sort)));
		q.distinct(true).select(c);
		return em().createQuery(q)
				   .setFirstResult(start)
				   .setMaxResults(limit)
				   .getResultStream()
				   .filter(Objects::nonNull);
	}
	default Page<Entity> search(QueryServerside query) {
		assert query != null;
		Pageable pageable;
		if(query.sortBy()!=null) {
			boolean isAsc = query.asc();
			pageable = PageRequest.of(query.page(), query.limit(), Sort.by(isAsc? Sort.Direction.ASC: Sort.Direction.DESC, query.sortBy()));
		} else pageable = PageRequest.of(query.page(), query.limit());
		return new PageImpl<>(list(query).collect(Collectors.toList()), pageable, count(query));
	}
	Predicate map(CriteriaBuilder cb, Root<Entity> c, QueryServerside.Filter filter);
}
