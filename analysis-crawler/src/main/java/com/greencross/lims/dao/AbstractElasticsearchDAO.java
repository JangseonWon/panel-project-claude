package com.greencross.lims.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Field;
import org.springframework.data.elasticsearch.core.query.Query;

public abstract class AbstractElasticsearchDAO {
	private ElasticsearchRestTemplate em;
	private final IndexCoordinates index;
	@SuppressWarnings("unchecked")
	public AbstractElasticsearchDAO(ElasticsearchRestTemplate em, IndexCoordinates index) {
		this.em = em;
		this.index = index;
	}
	public boolean exists(String id) {
		return em.exists(id, index);
	}
	public <T> T save(T entity) {
		return em.save(entity, index);
	}
	public String delete(String id) {
		return em.delete(id, index);
	}
	public ElasticsearchOperations em() {
		return em;
	}
	public CriteriaBuilderAbstract criteria() {
		return new CriteriaBuilderAbstract();
	}
	public CriteriaBuilderRequireParameter find(Field field) {
		return new CriteriaBuilderRequireParameter(new Criteria(field));
	}

	public static class CriteriaBuilder {
		private Criteria criteria;
		private CriteriaBuilder(Criteria criteria) {
			this.criteria = criteria;
		}
		Criteria criteria() {
			return criteria;
		}
		void criteria(Criteria criteria) {
			this.criteria = criteria;
		}
		@Override
		public String toString() {
			return criteria.toString();
		}
	}
	public final class CriteriaBuilderAbstract extends CriteriaBuilder {
		private CriteriaBuilderAbstract() {
			super(null);
		};
		private CriteriaBuilderAbstract(CriteriaBuilder builder) {
			super(builder.criteria);
		}
		public CriteriaBuilder and(CriteriaBuilder builder) {
			Criteria c1 = criteria();
			Criteria c2 = builder.criteria();
			if(c2 == null) return this;
			else if(c1==null) this.criteria(builder.criteria);
			else c1.and(c2);
			return this;
		}
		public CriteriaBuilderRequireParameter get(Field field) {
			if(criteria() == null) criteria(new Criteria(field));
			criteria().and(field);
			return new CriteriaBuilderRequireParameter(this);
		}
		public CriteriaBuilderRequireParameter get(String field) {
			if(criteria() == null) criteria(new Criteria(field));
			criteria().and(field);
			return new CriteriaBuilderRequireParameter(this);
		}
		public CriteriaBuilderPageable pageable(Pageable pageable) {
			return new CriteriaBuilderPageable(this, pageable);
		}
		public long count() {
			Query query = criteria()!=null?new CriteriaQuery(criteria()): Query.findAll();
			return em.count(query, index);
		}
		public SearchHits<Object> search() {
			Query query = criteria()!=null?new CriteriaQuery(criteria()): Query.findAll();
			return em.search(query, Object.class, index);
		}
	}
	public final class CriteriaBuilderRequireParameter extends CriteriaBuilder {
		private CriteriaBuilderRequireParameter(Criteria criteria) {
			super(criteria);
		}
		private CriteriaBuilderRequireParameter(CriteriaBuilder builder) {
			this(builder.criteria);
		}
		public CriteriaBuilderAbstract contains(String value) {
			criteria().contains(value);
			return new CriteriaBuilderAbstract(this);
		}
		public CriteriaBuilderAbstract is(String value) {
			criteria().is(value);
			return new CriteriaBuilderAbstract(this);
		}
	}
	final class CriteriaBuilderPageable extends CriteriaBuilder {
		private Pageable pageable;
		private CriteriaBuilderPageable(CriteriaBuilder builder, Pageable pageable) {
			super(builder.criteria);
			this.pageable = pageable;
		}
		public SearchHits<Object> search() {
			Query query = criteria()!=null?new CriteriaQuery(criteria()): Query.findAll();
			query.setPageable(pageable);
			return em.search(query, Object.class, index);
		}
	}
}
