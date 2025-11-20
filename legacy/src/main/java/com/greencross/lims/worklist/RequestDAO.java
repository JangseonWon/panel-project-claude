package com.greencross.lims.worklist;

import com.greencross.lims.dao.AbstractJpaDAO;
import com.greencross.lims.dao.GroupableJpa;
import com.greencross.lims.dto.Query;
import com.greencross.lims.entity.RequestRareDisease;
import com.greencross.lims.trans.LocalDateToEpoch;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
public class RequestDAO extends AbstractJpaDAO<RequestRareDisease> implements GroupableJpa<RequestRareDisease> {
	@Override
	public Predicate map(CriteriaBuilder cb, Root<RequestRareDisease> c, Query.Filter filter) {
		return map(cb, c, filter.key(), filter.value());
	}
	private Predicate map(CriteriaBuilder cb, Root<RequestRareDisease> c, String key, String value) {
		if("from".equalsIgnoreCase(key)) return cb.greaterThanOrEqualTo(c.get("dateRequest"), LocalDateToEpoch.map(Long.parseLong(value)));
		if("to".equalsIgnoreCase(key)) return cb.lessThanOrEqualTo(c.get("dateRequest"), LocalDateToEpoch.map(Long.parseLong(value)));
		return null;
	}
}
