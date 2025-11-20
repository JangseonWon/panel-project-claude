package com.greencross.lims.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(fluent=true)
public final class Batch {
    private String sheet;
    private Double id;
    @JsonProperty("create_time")
    private Double createTime;
    private String user;
    private String title;
    @JsonProperty("sample_cnt")
    private Double sampleCnt;
    private Object values;
    private Request.Service[] services;

    public Batch id(Integer id) {
        if(id == null) this.id = null;
        else this.id = id.doubleValue();
        return this;
    }

    public Integer id() {
        if(id == null) return null;
        else return id.intValue();
    }

    public Batch createTime(long createTime) {
        this.createTime = createTime + 0.0;
        return this;
    }
    public long createTime() {
        if(createTime == null) throw new RuntimeException();
        else return createTime.longValue();
    }

    public Batch sampleCnt(long sampleCnt) {
        this.sampleCnt = sampleCnt + 0.0;
        return this;
    }

    public long sampleCnt() {
        if(sampleCnt == null) return 0;
        else return sampleCnt.longValue();
    }
    // Called by server-side only
    public Batch values(Map<?, String> values) {
        this.values = values;
        return this;
    }
}
