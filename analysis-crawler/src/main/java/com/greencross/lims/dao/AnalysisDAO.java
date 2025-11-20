package com.greencross.lims.dao;

import com.greencross.lims.entity.Analysis;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public class AnalysisDAO extends AbstractJpaDAO<Analysis> {
    private final static String FIND_BY_BATCH = "SELECT * FROM panel.analysis S WHERE batch=':batch'";
    public Stream<Analysis> findByBatch(String batch) {
        return em().createNativeQuery(FIND_BY_BATCH.replace(":batch", batch), Analysis.class).getResultStream();
    }
}
