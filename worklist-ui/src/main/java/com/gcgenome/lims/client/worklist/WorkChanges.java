package com.gcgenome.lims.client.worklist;

import lombok.Builder;
import lombok.experimental.Accessors;

import java.util.Map;

@lombok.Data
@Builder
@Accessors(fluent = true)
final class WorkChanges {
    private long sample;
    private String service;
    private Map<String, String> values;
}
