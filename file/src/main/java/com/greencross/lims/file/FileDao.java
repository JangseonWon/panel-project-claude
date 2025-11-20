package com.greencross.lims.file;

import com.greencross.lims.dao.AbstractJpaDAO;
import com.greencross.lims.entity.File;
import com.greencross.lims.entity.readOnly.Request;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Objects;
import java.util.stream.Stream;

@Repository
public class FileDao extends AbstractJpaDAO<File> {
	public Stream<File> list(long sample, String service) {
		CriteriaBuilder cb = builder();
		CriteriaQuery<File> q = cb.createQuery(File.class);
		Root<File> c = q.from(File.class);
		cb.equal(c.get("request"), em().getReference(Request.class, Request.RequestPK.builder().sample(sample).service(service).build()));
		q.orderBy(cb.asc(c.get("sequence"))).distinct(true).select(c);
		return em().createQuery(q)
				   .getResultStream()
				   .filter(Objects::nonNull);
	}
}
