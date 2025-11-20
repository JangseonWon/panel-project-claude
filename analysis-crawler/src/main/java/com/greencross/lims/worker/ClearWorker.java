package com.greencross.lims.worker;

import com.greencross.lims.dao.AnalysisDAO;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

@Service
public class ClearWorker {
    private final ElasticsearchRestTemplate em;
    private final AnalysisDAO analysisDao;
    public ClearWorker(ElasticsearchRestTemplate em, AnalysisDAO analysisDao) {
        this.em = em;
        this.analysisDao = analysisDao;
    }

    public void clear(String batch) throws InterruptedException {
       deleteIndex(batch);
       analysisDao.findByBatch(batch).forEach(analysis -> {
           // SNV를 삭제한다?
           analysisDao.remove(analysis);
       });
    }
    private void deleteIndex(String batch) throws InterruptedException {
        String indexName = "analysis-snv-" + batch;
        em.indexOps(IndexCoordinates.of(indexName.toLowerCase())).delete();
        Thread.sleep(10000);
    }
}
