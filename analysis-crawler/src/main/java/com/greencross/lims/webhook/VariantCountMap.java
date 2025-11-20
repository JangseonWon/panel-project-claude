package com.greencross.lims.webhook;

import lombok.AllArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class VariantCountMap {
    private final Map<String, Set<VariantCountRequest>> map = new HashMap<>();
    private final ElasticsearchRestTemplate restTemplate;
    public void addToMap(String serial, VariantCountRequest request){
        var set =  map.getOrDefault(serial, new HashSet<>());
        set.add(request);
        map.put(serial, set);
    }

    public Stream<Map.Entry<String, Set<VariantCountRequest>>> consumeMap(){
        List<Map.Entry<String, Set<VariantCountRequest>>> toStream = new ArrayList<>(map.entrySet());
        map.clear();
        return toStream.stream();
    }

    public boolean hasRequests(){
        return !map.isEmpty();
    }

    public ElasticsearchRestTemplate getRestTemplate(){
        return restTemplate;
    }
}
