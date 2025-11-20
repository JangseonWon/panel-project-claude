package com.greencross.lims.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greencross.lims.entity.Analysis;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AnalysisService {
    @PersistenceContext
    private EntityManager em;
    private final ObjectMapper om;
    public AnalysisService(ObjectMapper om) {
        this.om = om;
    }
    @Transactional
    public String put(long sample, String service, String batch, int row, String key, Map<String, Object> value) throws IOException {
        String request = sample + ":" + service;
        Analysis analysis = em.createQuery("SELECT e FROM Analysis e WHERE batch=:batch AND row=:row AND request=:request", Analysis.class)
                .setParameter("batch", batch).setParameter("row", row).setParameter("request", request).getResultList()
                .stream().findFirst().orElseThrow(() -> new RuntimeException("Can't find Analysis: " + batch + ", " + row + ", " + request));
        if(analysis.value()==null) analysis.value(new HashMap<>());
        analysis.value().put(key, value);
        analysis = em.merge(analysis);
        return om.writeValueAsString(analysis.value().get(key));
    }
}
