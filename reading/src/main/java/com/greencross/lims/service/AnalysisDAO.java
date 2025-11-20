package com.greencross.lims.service;

import com.greencross.lims.dao.AbstractJpaDAO;
import com.greencross.lims.dao.GroupableJpa;
import com.greencross.lims.dto.QueryServerside;
import com.greencross.lims.entity.Analysis;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.entity.UserActivated;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Objects;
import java.util.stream.Stream;

@Repository
public class AnalysisDAO extends AbstractJpaDAO<Analysis> implements GroupableJpa<Analysis> {
	@Override
	public Predicate map(CriteriaBuilder cb, Root<Analysis> c, QueryServerside.Filter filter) {
		return map(cb, c, filter.key(), filter.value());
	}
	public Predicate map(CriteriaBuilder cb, Root<Analysis> c,  String key, String value) {
		if(key == null || key.trim().isEmpty()) {
			Predicate[] predicates = Stream.of(
					map(cb, c, "batch", value),
					map(cb, c, "id", value),
					map(cb, c, "sample", value),
					map(cb, c, "patient", value),
					map(cb, c, "panel", value),
					map(cb, c, "requestAt", value),
					map(cb, c, "date_range", value)
			).filter(Objects::nonNull).toArray(Predicate[]::new);
			return cb.or(predicates);
		} else if("batch".equalsIgnoreCase(key)) return cb.like(c.get("batch"), "%" + value + "%");
		else if("id".equalsIgnoreCase(key)) return cb.equal(parse(c,"serial"), value);
		else if("sample".equalsIgnoreCase(key)) {
			if(value == null) return null;
			if(value.contains("-")) value = value.replace("-", "");
			if(value.matches("\\d+")) return cb.equal(parse(c,"sample"), em().getReference(Sample.class, Long.parseLong(value)));
			else return null;
		} else if("patient".equalsIgnoreCase(key)) return cb.like(parse(c,"sample.patient.name"), "%" + value + "%");
		else if("requestAt".equalsIgnoreCase(key)) return cb.like(parse(c, "sample.id").as(String.class), value+"%");
		else if("date_range".equalsIgnoreCase(key)) {
			if(value==null || !value.contains("~")) return null;
			String[] split = value.split("~", -1);
			var from	= Integer.parseInt(split[0].trim().replace("-", "").replace("/", ""));
			var to	= Integer.parseInt(split[1].trim().replace("-", "").replace("/", ""));
			var prefix = cb.function("substring", String.class, parse(c, "sample.id").as(String.class), cb.literal(1), cb.literal(8));
			return cb.between(prefix.as(Integer.class), from, to);
		} else if("panel".equalsIgnoreCase(key)) return cb.equal(parse(c,"panel"), value);
		else if("type".equalsIgnoreCase(key)) return cb.equal(parse(c,"sheet.name"), value);
		else if("editor".equalsIgnoreCase(key)) return cb.equal(c.get("editor"), em().getReference(UserActivated.class, value));
		else if("except_control".equalsIgnoreCase(key) && "true".equalsIgnoreCase(value)) return cb.isNotNull(parse(c,"sample"));
		else if("not_complete".equalsIgnoreCase(key) && "true".equalsIgnoreCase(value)) return cb.in(parse(c, "state")).value("BI 분석완료").value("판독 중").value("판독완료");
		else if("reception".equalsIgnoreCase(key) && "true".equalsIgnoreCase(value)) return cb.isNotNull(parse(c,"requests.dateReception"));
		return cb.equal(c.get(key), value);
	}
}
