package com.greencross.lims.kanban;

import com.greencross.lims.dao.AbstractJpaDAO;
import com.greencross.lims.entity.Comment;
import com.greencross.lims.entity.Issue;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Objects;
import java.util.stream.Stream;

@Repository
public class CommentDAO extends AbstractJpaDAO<Comment> {
	public Stream<Comment> list(Issue parent) {
		CriteriaBuilder cb = builder();
		CriteriaQuery<Comment> q = cb.createQuery(entityClass());
		Root<Comment> c = q.from(entityClass());
		q.where(cb.equal(c.get("issue"), parent))
		 .orderBy(cb.asc(c.get("createTime")))
		 .select(c);
		return em().createQuery(q)
				   .getResultStream()
				   .filter(Objects::nonNull);
	}
}
