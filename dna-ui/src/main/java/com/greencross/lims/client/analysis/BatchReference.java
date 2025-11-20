package com.greencross.lims.client.analysis;

import lombok.Builder;
import lombok.experimental.Accessors;

import java.util.Map;

@lombok.Data
@Builder
@Accessors(fluent = true)
final class BatchReference {
    private int batch;
    private Map<String, String> values;
}
