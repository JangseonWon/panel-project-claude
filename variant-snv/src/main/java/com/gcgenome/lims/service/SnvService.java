package com.gcgenome.lims.service;

import com.gcgenome.lims.dto.QueryServerside;
import com.gcgenome.lims.dto.RequestSnv;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly=true)
public class SnvService {
	private final SnvReportedDao dao;
	private final AnalysisSnvDao analysisDao;
	public SnvService(SnvReportedDao dao, AnalysisSnvDao analysisDao) {
		this.dao = dao;
		this.analysisDao = analysisDao;
	}
	public Optional<Object> get(String id) {
		Criteria criteria = new Criteria("snv");
		Query query = new CriteriaQuery(criteria.is(id));
		query.setPageable(PageRequest.of(0, 1).withSort(Sort.by(Sort.Direction.DESC, "create_at")));
		return analysisDao.em().search(query, Object.class, IndexCoordinates.of("analysis-snv*")).stream().map(SearchHit::getContent).findFirst();
	}

	public Page<RequestSnv> samples(String id, QueryServerside query) {
		query.filters(List.of(new QueryServerside.Filter().key("snv").value(id), new QueryServerside.Filter().key("reported")));
		return dao.search(query).map(SnvToDTO::map);
	}

	public Page<Object> requests(String id, QueryServerside query) {
		query.filters(List.of(new QueryServerside.Filter().key("snv_exact").value(id)));
		return analysisDao.search(query).map(SearchHit::getContent);
	}
}
