package com.greencross.lims.service;

import com.greencross.lims.dao.AbstractElasticsearchDAO;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Repository;

@Repository
public class SnvDAO extends AbstractElasticsearchDAO {
	public SnvDAO(ElasticsearchRestTemplate em) {
		super(em, "analysis-snv-");
	}
}
