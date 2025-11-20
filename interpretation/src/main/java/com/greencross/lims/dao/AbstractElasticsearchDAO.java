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
	private final String indexPrefix;
	@SuppressWarnings("unchecked")
	public AbstractElasticsearchDAO(ElasticsearchRestTemplate em, String indexPrefix) {
		this.em = em;
		this.indexPrefix = indexPrefix;
	}
	public <T> T find(String batch, String id, Class<T> clazz) {
		return em.get(id, clazz, IndexCoordinates.of((indexPrefix + batch).toLowerCase()));
	}
	public ElasticsearchOperations em() {
		return em;
	}
}
