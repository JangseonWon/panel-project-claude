package com.gcgenome.lims.service;

import com.gcgenome.lims.dao.AbstractJpaDAO;
import com.gcgenome.lims.dao.GroupableJpa;
import com.gcgenome.lims.dto.QueryServerside;
import com.gcgenome.lims.entity.Sample;
import com.gcgenome.lims.entity.WorkRareDisease;
import com.gcgenome.lims.entity.WorklistRareDisease;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Objects;
import java.util.stream.Stream;

@Repository
public class WorkDAO extends AbstractJpaDAO<WorkRareDisease> implements GroupableJpa<WorkRareDisease> {
	@Override
	public Predicate map(CriteriaBuilder cb, Root<WorkRareDisease> c, QueryServerside.Filter filter) {
		return map(cb, c, filter.key(), filter.value());
	}
	// Cockroachdb에 MV 가 구현되면, 아래와 같이 join할 필요 없이 MV에서 검색 후 PK를 얻어와서 목록을 뿌려주는 형식으로 변경할 수 있다.
	private Predicate map(CriteriaBuilder cb, Root<WorkRareDisease> c, String key, String value) {
		if(key == null || key.trim().isEmpty()) {
			Predicate[] predicates = Stream.of(
					map(cb, c, "id", value),
					map(cb, c, "sample", value),
					map(cb, c, "patient", value)
			).filter(Objects::nonNull).toArray(Predicate[]::new);
			return cb.or(predicates);
		} else if("id".equalsIgnoreCase(key)) {
			return cb.equal(c.get("serial"), value);
		} else if("sample".equalsIgnoreCase(key)) {
			if(value == null) return null;
			if(value.contains("-")) value = value.replace("-", "");
			if(value.matches("\\d+")) return cb.equal(c.get("sample"), em().getReference(Sample.class, Long.parseLong(value)));
			else return null;
		} else if("patient".equalsIgnoreCase(key)) {
			return cb.equal(parse(c,"sample.patient.name"), value);
		} else return null;
	}
	public Stream<WorkRareDisease> list(int work, QueryServerside query) {
		String sort = query.sortBy();
		boolean isAsc = query.asc();
		CriteriaBuilder cb = builder();
		CriteriaQuery<WorkRareDisease> q = cb.createQuery(entityClass());
		Root<WorkRareDisease> c = q.from(entityClass());
		Predicate where = cb.equal(c.get("worklist"), em().getReference(WorklistRareDisease.class, new WorklistRareDisease.WorklistRareDiseasePK(work)));
		Stream.concat(Stream.of(where), query.filters().stream().map(f -> map(cb, c, f)).filter(Objects::nonNull)).reduce(cb::and).ifPresent(q::where);
		if(sort!=null && !sort.trim().isEmpty()) q.orderBy(isAsc?cb.asc(parse(c, sort)):cb.desc(parse(c, sort)));
		q.distinct(true).select(c);
		return em().createQuery(q)
				   .getResultStream()
				   .filter(Objects::nonNull);
	}
}
