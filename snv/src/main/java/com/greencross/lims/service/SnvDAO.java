package com.greencross.lims.service;

import com.greencross.lims.dao.AbstractElasticsearchDAO;
import com.greencross.lims.dao.SearchableElasticsearch;
import com.greencross.lims.dto.QueryServerside;
import com.greencross.lims.entity.Analysis;
import com.greencross.lims.entity.Gene;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class SnvDAO extends AbstractElasticsearchDAO implements SearchableElasticsearch {
	private final PanelDAO panelDAO;
	public SnvDAO(ElasticsearchRestTemplate em, PanelDAO panelDAO) {
		super(em, "analysis-snv-");
		this.panelDAO = panelDAO;
	}
	public Page<SearchHit<Document>> search(Analysis.AnalysisPK analysis, QueryServerside query) {
		assert query != null;
		if(query.filters()==null) query.filters(List.of());
		String id = analysis.sheet() + ":" + analysis.batch() + ":" + String.format("%03d", analysis.row());
		QueryServerside.Filter filter = new QueryServerside.Filter().key("analysis").value(id);
		query.filters(Stream.concat(query.filters().stream(), Stream.of(filter)).collect(Collectors.toList()));
		return search(analysis.batch(), query);
	}

	public Page<SearchHit<Document>> search(String batch, QueryServerside query) {
		assert query != null;
		Sort sort = Sort.by(Sort.Order.asc("class_order"),Sort.Order.asc("_id"), Sort.Order.asc("snv"));
		Pageable pageable = PageRequest.of(query.page(), query.limit(), sort);
		return new PageImpl<>(list(batch, query, pageable).stream().collect(Collectors.toList()), pageable, count(batch, query));
	}
	@Override
	public void where(String suffix, AbstractElasticsearchDAO.CriteriaBuilderAbstract cb, QueryServerside.Filter filter) {
		switch(filter.key().toLowerCase()) {
			case "analysis":cb.and(criteria(suffix).get(filter.key().trim()).is(filter.value().trim())); break;
			case "gene":	cb.and(criteria(suffix).get("gene.refgene").contains(filter.value().trim())); break;
			case "hgvsc":	cb.and(criteria(suffix).get("hgvsc_in_mane").contains(filter.value().trim())); break;
			case "hgvsp":	cb.and(criteria(suffix).get("hgvsp_in_mane").contains(filter.value().trim())); break;
			case "genes":	{
				String[] split = filter.value().trim().split(",");
				includes(suffix, cb, "gene.refgene", split);
				break;
			} case "panel":	{
				String panelId = filter.value().trim();
				panelDAO.find(panelId).ifPresent(panel->{
					includes(suffix, cb, "gene.refgene", panel.genes().stream().map(Gene::gene).map(SnvDAO::escapeSlash).toArray(String[]::new));
				});
				break;
			}
			// Slash Analyzer 는 검색 값으로 사용시 작동하지 않는다(특수문자 Analyzer는 작동함).
			case "codon":
			case "exon":
			case "exon_in_mane":	cb.and(criteria(suffix).get(filter.key().trim().toLowerCase()).contains(escapeSlash(filter.value().trim()))); break;
			// Slash Analyzer 는 검색 값으로 사용시 작동하지 않는다(특수문자 Analyzer는 작동함).
			default:				if(isVariantFilter(filter)) {
				String key = filter.key().toLowerCase();
				String value = filter.value();
				String op = value.substring(0, value.indexOf(","));
				String param = value.substring(value.indexOf(",")+1);
				if("includes".equalsIgnoreCase(op)) includes(suffix, cb, key, escapeSlash(param).split(","));
				else if("excludes".equalsIgnoreCase(op)) excludes(suffix, cb, key, escapeSlash(param).split(","));
				else if("is empty".equalsIgnoreCase(op)) isEmpty(suffix, cb, key);
				else if("has value".equalsIgnoreCase(op)) isNotEmpty(suffix, cb, key);
				else {
					Number n = param.contains(".")?Double.parseDouble(param):Long.parseLong(param);
					if("=".equalsIgnoreCase(op)) equals(suffix, cb, key, n);
					else if("<".equalsIgnoreCase(op)) less(suffix, cb, key, n);
					else if("<=".equalsIgnoreCase(op)) lessOrEquals(suffix, cb, key, n);
					else if(">=".equalsIgnoreCase(op)) greaterOrEquals(suffix, cb, key, n);
					else if(">".equalsIgnoreCase(op)) greater(suffix, cb, key, n);
				}
			} else if(filter.key()!=null && !filter.key().trim().isEmpty()) cb.and(criteria(suffix).get(filter.key().trim().toLowerCase()).contains(filter.value().trim()));
			else cb.and(criteria(suffix).get("*").contains(filter.value().trim()));break;
		}
	}
	private boolean isVariantFilter(QueryServerside.Filter filter) {
		String value = filter.value();
		if(value==null || !value.contains(",")) return false;
		String op = value.substring(0, value.indexOf(","));
		if("=".equals(op)) return true;
		if("<".equals(op)) return true;
		if("<=".equals(op)) return true;
		if(">=".equals(op)) return true;
		if(">".equals(op)) return true;
		if("includes".equalsIgnoreCase(op)) return true;
		if("excludes".equalsIgnoreCase(op)) return true;
		if("is empty".equalsIgnoreCase(op)) return true;
		return "has value".equalsIgnoreCase(op);
	}
	private void equals(String suffix, CriteriaBuilderAbstract cb, String column, Number value) {
		cb.and(criteria(suffix).get(column.trim().toLowerCase()).equal(value));
	}
	private void less(String suffix, CriteriaBuilderAbstract cb, String column, Number value) {
		cb.and(criteria(suffix).get(column.trim().toLowerCase()).less(value));
	}
	private void lessOrEquals(String suffix, CriteriaBuilderAbstract cb, String column, Number value) {
		cb.and(criteria(suffix).get(column.trim().toLowerCase()).lessOrEquals(value));
	}
	private void greater(String suffix, CriteriaBuilderAbstract cb, String column, Number value) {
		cb.and(criteria(suffix).get(column.trim().toLowerCase()).greater(value));
	}
	private void greaterOrEquals(String suffix, CriteriaBuilderAbstract cb, String column, Number value) {
		cb.and(criteria(suffix).get(column.trim().toLowerCase()).greaterOrEquals(value));
	}
	private void includes(String suffix, CriteriaBuilderAbstract cb, String column, String... within) {
		cb.and(criteria(suffix).get(column.trim().toLowerCase()).in(within));
	}
	private void excludes(String suffix, CriteriaBuilderAbstract cb, String column, String... within) {
		cb.and(criteria(suffix).get(column.trim().toLowerCase()).notIn(within));
	}
	private void isEmpty(String suffix, CriteriaBuilderAbstract cb, String column) {
		cb.and(criteria(suffix).get(column.trim().toLowerCase()).isEmpty());
	}
	private void isNotEmpty(String suffix, CriteriaBuilderAbstract cb, String column) {
		cb.and(criteria(suffix).get(column.trim().toLowerCase()).isNotEmpty());
	}
	public static String escapeSlash(String value) {
		value = value.replace("/", "_slash_");
		return value;
	}
}
