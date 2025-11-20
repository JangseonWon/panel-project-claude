package com.greencross.lims.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Field;
import org.springframework.data.elasticsearch.core.query.Query;

public abstract class AbstractElasticsearchDAO {
	private ElasticsearchRestTemplate em;
	private final String indexPrefix;
	public AbstractElasticsearchDAO(ElasticsearchRestTemplate em, String indexPrefix) {
		this.em = em;
		this.indexPrefix = indexPrefix;
	}
	public <T> T find(String batch, String id, Class<T> clazz) {
		return em.get(id, clazz, IndexCoordinates.of((indexPrefix + batch).toLowerCase()));
	}
	public CriteriaBuilderAbstract criteria(String batch) {
		return new CriteriaBuilderAbstract(IndexCoordinates.of((indexPrefix + batch).toLowerCase()));
	}
	public static class CriteriaBuilder {
		protected final IndexCoordinates index;
		private Criteria criteria;
		private CriteriaBuilder(IndexCoordinates index, Criteria criteria) {
			this.index = index;
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
		private CriteriaBuilderAbstract(IndexCoordinates index) {
			super(index, null);
		};
		private CriteriaBuilderAbstract(IndexCoordinates index, CriteriaBuilder builder) {
			super(index, builder.criteria);
		}
		public CriteriaBuilder and(CriteriaBuilder builder) {
			Criteria c1 = criteria();
			Criteria c2 = builder.criteria();
			if(c2 == null) return this;
			else if(c1==null) this.criteria(builder.criteria);
			else c1.and(c2);
			return this;
		}
		public CriteriaBuilderRequireParameter all() {
			criteria(new Criteria());
			return new CriteriaBuilderRequireParameter(this.index,this);
		}
		public CriteriaBuilderRequireParameter get(Field field) {
			if(criteria() == null) criteria(new Criteria(field));
			criteria().and(field);
			return new CriteriaBuilderRequireParameter(this.index,this);
		}
		public CriteriaBuilderRequireParameter get(String field) {
			if(criteria() == null) criteria(new Criteria(field));
			criteria().and(field);
			return new CriteriaBuilderRequireParameter(this.index, this);
		}
		public CriteriaBuilderPageable pageable(Pageable pageable) {
			return new CriteriaBuilderPageable(index, this, pageable);
		}
		public long count() {
			Query query = criteria()!=null?new CriteriaQuery(criteria()):Query.findAll();
			return em.count(query, index);
		}
		public SearchHits<Object> search() {
			Query query = criteria()!=null?new CriteriaQuery(criteria()):Query.findAll();
			return em.search(query, Object.class, index);
		}
	}
	public final class CriteriaBuilderRequireParameter extends CriteriaBuilder {
		private CriteriaBuilderRequireParameter(IndexCoordinates index, Criteria criteria) {
			super(index, criteria);
		}
		private CriteriaBuilderRequireParameter(IndexCoordinates index, CriteriaBuilder builder) {
			this(index, builder.criteria);
		}
		public CriteriaBuilderAbstract contains(String value) {
			criteria().contains(value);
			return new CriteriaBuilderAbstract(this.index, this);
		}
		public CriteriaBuilderAbstract in(String... values) {
			criteria().in(values);
			return new CriteriaBuilderAbstract(this.index, this);
		}
		public CriteriaBuilderAbstract notIn(String... values) {
			criteria().notIn(values);
			return new CriteriaBuilderAbstract(this.index, this);
		}
		public CriteriaBuilderAbstract isEmpty() {
			criteria().not().exists();
			return new CriteriaBuilderAbstract(index,this);
		}
		public CriteriaBuilderAbstract isNotEmpty() {
			criteria().exists();
			return new CriteriaBuilderAbstract(index,this);
		}
		public CriteriaBuilderAbstract is(String value) {
			criteria().is(value);
			return new CriteriaBuilderAbstract(index,this);
		}
		public CriteriaBuilderAbstract equal(Number n) {
			criteria().is(n);
			return new CriteriaBuilderAbstract(index,this);
		}
		public CriteriaBuilderAbstract less(Number n) {
			criteria().lessThan(n);
			return new CriteriaBuilderAbstract(index,this);
		}
		public CriteriaBuilderAbstract lessOrEquals(Number n) {
			criteria().lessThanEqual(n);
			return new CriteriaBuilderAbstract(index,this);
		}
		public CriteriaBuilderAbstract greater(Number n) {
			criteria().greaterThan(n);
			return new CriteriaBuilderAbstract(index,this);
		}
		public CriteriaBuilderAbstract greaterOrEquals(Number n) {
			criteria().greaterThanEqual(n);
			return new CriteriaBuilderAbstract(index,this);
		}
	}
	final class CriteriaBuilderPageable extends CriteriaBuilder {
		private final Pageable pageable;
		private CriteriaBuilderPageable(IndexCoordinates index, CriteriaBuilder builder, Pageable pageable) {
			super(index, builder.criteria);
			this.pageable = pageable;
		}
		public SearchHits<Document> search() {
			Query query = criteria()!=null?new CriteriaQuery(criteria()):Query.findAll();
			query.setPageable(pageable);
			return em.search(query, Document.class, index);
		}
	}
}
