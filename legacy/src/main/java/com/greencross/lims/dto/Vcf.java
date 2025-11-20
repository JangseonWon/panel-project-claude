package com.greencross.lims.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent=true)
public final class Vcf {
    private Double batch;
    private Double row;
    private String serial;
    private Analysis.AnalysisRequest request;
    private String tier;
    private String genotype;
    private Double vaf;
    private Double depth;
    private Variant variant;

    public Vcf depth(long createTime) {
        this.depth = depth + 0.0;
        return this;
    }

    public long depth() {
        if(depth == null) throw new RuntimeException();
        else return depth.longValue();
    }
}
