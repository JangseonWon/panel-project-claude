package com.greencross.lims.dao;

import com.greencross.lims.entity.SnvConsensualClass;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public class SnvConsensualClassDao extends AbstractJpaDAO<SnvConsensualClass> {
	private final static String FIND_LAST = "SELECT * FROM panel.snv_consensual_class S WHERE (snv, create_at) IN(" +
											"SELECT snv, MAX(create_at) FROM panel.snv_consensual_class WHERE snv=S.snv" +
											" GROUP BY snv)";
	public Stream<SnvConsensualClass> findLast() {
		return em().createNativeQuery(FIND_LAST, SnvConsensualClass.class).getResultStream();
	}
}
