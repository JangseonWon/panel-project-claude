package com.gcgenome.lims.service.interpretation;

import com.gcgenome.lims.dao.AbstractJpaDAO;
import com.gcgenome.lims.dao.SearchableJpa;
import com.gcgenome.lims.dto.QueryServerside;
import com.gcgenome.lims.entity.InterpretationReserved;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.stream.Stream;

@Repository
public class InterpretationReservedDao extends AbstractJpaDAO<InterpretationReserved> implements SearchableJpa<InterpretationReserved> {
	@Override
	public Predicate map(CriteriaBuilder cb, Root<InterpretationReserved> c, QueryServerside.Filter filter) {
		return map(cb, c, filter.key(), filter.value());
	}
	public Predicate map(CriteriaBuilder cb, Root<InterpretationReserved> c,  String key, String value) {
		if("snv".equalsIgnoreCase(key)) return cb.like(parse(c,"snv"), "%" + value);
		return cb.equal(c.get(key), value);
	}
	public Stream<InterpretationReserved> findLastBySnv(String id) {
		String qs = "SELECT * FROM panel.interpretation_reserved WHERE snv=:snv AND (service, create_at) IN(" +
						"SELECT service, MAX(create_at) FROM panel.interpretation_reserved WHERE snv=:snv GROUP BY service" +
					")";
		Query query = em().createNativeQuery(qs, InterpretationReserved.class).setParameter("snv", id);
		try {
			return query.getResultStream();
		} catch (Exception e) {
			return null;
		}
	}
	public int delete(String id, String service) {
		String qs = "DELETE FROM panel.interpretation_reserved WHERE snv=:snv AND service=:service";
		Query query = em().createNativeQuery(qs, InterpretationReserved.class).setParameter("snv", id).setParameter("service", service);
		try {
			return query.executeUpdate();
		} catch (Exception e) {
			return -1;
		}
	}
}
