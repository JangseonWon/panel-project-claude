package com.greencross.lims.service.snv;

import com.greencross.lims.dao.AbstractElasticsearchDAO;
import com.greencross.lims.dao.SearchableElasticsearch;
import com.greencross.lims.dto.QueryServerside;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Repository;

@Repository
public class SnvDao extends AbstractElasticsearchDAO implements SearchableElasticsearch {
	public SnvDao(ElasticsearchRestTemplate em) {
		super(em, IndexCoordinates.of("analysis-snv*"));
	}
	@Override
	public void where(CriteriaBuilderAbstract cb, QueryServerside.Filter filter) {
		switch(filter.key().toLowerCase()) {
			case "snv":		cb.and(criteria().get("snv").contains(filter.value().trim())); break;
			case "gene":	cb.and(criteria().get("gene.refgene").contains(filter.value().trim())); break;
			case "hgvsc":	cb.and(criteria().get("hgmd.hgvsc").contains(filter.value().trim())); break;
			case "hgvsp":	cb.and(criteria().get("hgmd.hgvsp").contains(filter.value().trim())); break;
			case "genes":	{
				String[] split = filter.value().trim().split(",");
				includes(cb, "gene.refgene", split);
				break;
			}
			// Slash Analyzer 는 검색 값으로 사용시 작동하지 않는다(특수문자 Analyzer는 작동함).
			case "codon":
			case "exon":
			case "exon_in_hgmd":	cb.and(criteria().get(filter.key().trim().toLowerCase()).contains(escapeSlash(filter.value().trim()))); break;
			// Slash Analyzer 는 검색 값으로 사용시 작동하지 않는다(특수문자 Analyzer는 작동함).
			default:				if(isVariantFilter(filter)) {
				String key = filter.key().toLowerCase();
				String value = filter.value();
				String op = value.substring(0, value.indexOf(","));
				String param = value.substring(value.indexOf(",")+1);
				if("includes".equalsIgnoreCase(op)) includes(cb, key, escapeSlash(param).split(","));
				else if("excludes".equalsIgnoreCase(op)) excludes(cb, key, escapeSlash(param).split(","));
				else if("is empty".equalsIgnoreCase(op)) isEmpty(cb, key);
				else if("has value".equalsIgnoreCase(op)) isNotEmpty(cb, key);
				else {
					Number n = param.contains(".")?Double.parseDouble(param):Long.parseLong(param);
					if("=".equalsIgnoreCase(op)) equals(cb, key, n);
					else if("<".equalsIgnoreCase(op)) less(cb, key, n);
					else if("<=".equalsIgnoreCase(op)) lessOrEquals(cb, key, n);
					else if(">=".equalsIgnoreCase(op)) greaterOrEquals(cb, key, n);
					else if(">".equalsIgnoreCase(op)) greater(cb, key, n);
				}
			} else if(filter.key()!=null && !filter.key().trim().isEmpty()) cb.and(criteria().get(filter.key().trim().toLowerCase()).contains(filter.value().trim()));
			else cb.and(criteria().get("*").contains(filter.value().trim()));break;
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
	private void equals(CriteriaBuilderAbstract cb, String column, Number value) {
		cb.and(criteria().get(column.trim().toLowerCase()).equal(value));
	}
	private void less(CriteriaBuilderAbstract cb, String column, Number value) {
		cb.and(criteria().get(column.trim().toLowerCase()).less(value));
	}
	private void lessOrEquals(CriteriaBuilderAbstract cb, String column, Number value) {
		cb.and(criteria().get(column.trim().toLowerCase()).lessOrEquals(value));
	}
	private void greater(CriteriaBuilderAbstract cb, String column, Number value) {
		cb.and(criteria().get(column.trim().toLowerCase()).greater(value));
	}
	private void greaterOrEquals(CriteriaBuilderAbstract cb, String column, Number value) {
		cb.and(criteria().get(column.trim().toLowerCase()).greaterOrEquals(value));
	}
	private void includes(CriteriaBuilderAbstract cb, String column, String... within) {
		cb.and(criteria().get(column.trim().toLowerCase()).in(within));
	}
	private void excludes(CriteriaBuilderAbstract cb, String column, String... within) {
		cb.and(criteria().get(column.trim().toLowerCase()).notIn(within));
	}
	private void isEmpty(CriteriaBuilderAbstract cb, String column) {
		cb.and(criteria().get(column.trim().toLowerCase()).isEmpty());
	}
	private void isNotEmpty(CriteriaBuilderAbstract cb, String column) {
		cb.and(criteria().get(column.trim().toLowerCase()).isNotEmpty());
	}
	public static String escapeSlash(String value) {
		value = value.replace("/", "_slash_");
		return value;
	}
}
