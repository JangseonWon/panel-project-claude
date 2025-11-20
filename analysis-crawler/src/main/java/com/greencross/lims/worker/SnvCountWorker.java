package com.greencross.lims.worker;

import com.greencross.lims.dao.AnalysisDAO;
import com.greencross.lims.dao.PanelTypeService;
import com.greencross.lims.entity.Analysis;
import com.greencross.lims.entity.AnalysisFile;
import com.greencross.lims.entity.PanelType;
import com.greencross.lims.entity.Request;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class SnvCountWorker implements Worker {
    private final AnalysisDAO dao;
    @Autowired
    private PanelTypeService panelTypeService;
    protected SnvCountWorker(AnalysisDAO dao) {
        this.dao = dao;
    }
    protected Set<String> getPanelServices(String fileName){
        String batchPanel = analysis(fileName, null).pk().batch();
        batchPanel = batchPanel.substring(2, batchPanel.length()-3);;
        return panelTypeService.getPanelType(batchPanel).stream()
                .map(PanelType::service)
                .collect(Collectors.toSet());
    }
    @Override
    public final void process(AnalysisFile entity, File file) throws Exception {
        List<String> lines = Files.readAllLines(file.toPath());
        Map<String, Integer> headers = lines.stream().findFirst()
                .map(line->line.split("\t", -1))
                .map(h->{
                    Map<String, Integer> header = new HashMap<>();
                    for(int i = 0; i < h.length; ++i) header.put(h[i].toLowerCase(), i);
                    return header;
                }).get();
        for(int i = 1; i < lines.size()-1; ++i) {
            String line = lines.get(i);
            if(line == null || line.trim().isEmpty()) continue;
            String[] split = line.split("\t", -1);
            Map<String, Object> values = new HashMap<>();
            for(Map.Entry<String, Integer> e: headers.entrySet()) values.put(e.getKey(), split[e.getValue()]);
            List<Analysis> analysis = findEntity(split[0].trim());
            for(Analysis a: analysis) {
                Map<String, Object> map = a.value();
                if(map == null) map = new HashMap<>();
                map.putAll(values);
                dao.merge(a.value(map));
            }
        }
    }
    protected abstract List<Analysis> findEntity(String rowId);
    protected abstract Long sample(String fileName);
    protected abstract Analysis analysis(String fileName, @Nullable Request request);
}
