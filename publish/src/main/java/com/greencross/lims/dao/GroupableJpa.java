package com.greencross.lims.dao;

import com.greencross.lims.dto.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;

import java.util.Objects;
import java.util.stream.Stream;

public interface GroupableJpa<Entity> extends SearchableJpa<Entity> {
	default Stream<GroupByResult> groupBy(Query query, String column) {
		CriteriaBuilder cb = builder();
		CriteriaQuery<GroupByResult> q = cb.createQuery(GroupByResult.class);
		Root<Entity> c = q.from(entityClass());
		query.filters().stream().map(f -> map(cb, c, f)).filter(Objects::nonNull).reduce(cb::and).ifPresent(q::where);
		Expression<String> groupBy = AbstractJpaDAO.parse(c, column);
		Expression<Long> countExp = cb.countDistinct(groupBy);
		q.groupBy(groupBy).multiselect(groupBy.alias("key"), countExp.alias("count"));
		return em().createQuery(q).getResultStream();
	}
}
