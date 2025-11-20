package com.greencross.lims.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent=true)
public final class Variant {
    private String id;
    @JsonProperty("create_time")
    private Double createTime;
    private String organism;
    private String reference;
    private String chromosome;
    private Double pos;
    private String ref;
    private String alt;
    private String gene;
    private String hgvsc;
    private String hgvsp;
    private String exon;
    private String rsid;
    @JsonProperty("sequence_ontology")
    private String sequenceOntology;

    public Variant createTime(long createTime) {
        this.createTime = createTime + 0.0;
        return this;
    }

    public long createTime() {
        if(createTime == null) throw new RuntimeException();
        else return createTime.longValue();
    }

    public Variant pos(long pos) {
        this.pos = pos + 0.0;
        return this;
    }

    public long pos() {
        if(pos == null) throw new RuntimeException();
        else return pos.longValue();
    }
}
