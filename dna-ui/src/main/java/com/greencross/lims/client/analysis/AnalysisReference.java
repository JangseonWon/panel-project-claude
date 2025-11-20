package com.greencross.lims.client.analysis;

import lombok.Builder;
import lombok.experimental.Accessors;

import java.util.Map;

@lombok.Data
@Builder
@Accessors(fluent = true)
final class AnalysisReference {
    private int row;
    private Map<String, String> values;
}
