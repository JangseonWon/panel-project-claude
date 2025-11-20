package com.greencross.lims.sequencing;

import com.greencross.lims.dao.AbstractJpaDAO;
import com.greencross.lims.dao.GroupableJpa;
import com.greencross.lims.dto.Query;
import com.greencross.lims.entity.AnalysisSequencing;
import com.greencross.lims.entity.BatchSequencing;
import com.greencross.lims.entity.Sample;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class AnalysisSequencingDAO extends AbstractJpaDAO<AnalysisSequencing> implements GroupableJpa<AnalysisSequencing> {
	@Override
	public Predicate map(CriteriaBuilder cb, Root<AnalysisSequencing> c, Query.Filter filter) {
		return map(cb, c, filter.key(), filter.value());
	}
	private Predicate map(CriteriaBuilder cb, Root<AnalysisSequencing> c, String key, String value) {
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
		} else if("file-name".equalsIgnoreCase(key)) {
			return cb.equal(c.get("fileName"), value);
		} else return null;
	}
	public Stream<AnalysisSequencing> list(int batch, Query query) {
		String sort = query.sortBy();
		boolean isAsc = query.asc();
		CriteriaBuilder cb = builder();
		CriteriaQuery<AnalysisSequencing> q = cb.createQuery(entityClass());
		Root<AnalysisSequencing> c = q.from(entityClass());
		Predicate where = cb.equal(c.get("batch"), em().getReference(BatchSequencing.class, new BatchSequencing.BatchSequencingPK(batch)));
		Stream.concat(Stream.of(where), query.filters().stream().map(f -> map(cb, c, f)).filter(Objects::nonNull)).reduce(cb::and).ifPresent(q::where);
		if(sort!=null && !sort.trim().isEmpty()) q.orderBy(isAsc?cb.asc(parse(c, sort)):cb.desc(parse(c, sort)));
		q.distinct(true).select(c);
		return em().createQuery(q)
				   .getResultStream()
				   .filter(Objects::nonNull);
	}
	public Optional<Integer> findLastRow(BatchSequencing parent) {
		CriteriaBuilder cb = builder();
		CriteriaQuery<Integer> q = cb.createQuery(Integer.class);
		Root<AnalysisSequencing> c = q.from(AnalysisSequencing.class);
		Expression<Integer> maxExp = cb.max(c.get("row"));
		q.select(maxExp).where(cb.equal(c.get("batch"), parent));
		return Optional.ofNullable(em().createQuery(q).getSingleResult());
	}
}