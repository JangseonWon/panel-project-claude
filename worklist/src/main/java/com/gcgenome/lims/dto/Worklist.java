package com.gcgenome.lims.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(fluent=true)
public final class Worklist {
    private Double worklist;
    @JsonProperty("create_time")
    private Double createTime;
    private String state;
    private String user;
    private String title;
    private Object values;
    @JsonProperty("sample_cnt")
    private Double sampleCnt;
    private Request.Service[] services;

    public Worklist worklist(Integer worklist) {
        if(worklist == null) this.worklist = null;
        else this.worklist = worklist.doubleValue();
        return this;
    }

    public Integer worklist() {
        if(worklist == null) return null;
        else return worklist.intValue();
    }

    public Worklist createTime(long createTime) {
        this.createTime = createTime + 0.0;
        return this;
    }

    public long createTime() {
        if(createTime == null) throw new RuntimeException();
        else return createTime.longValue();
    }

    public Worklist sampleCnt(long sampleCnt) {
        this.sampleCnt = sampleCnt + 0.0;
        return this;
    }

    public long sampleCnt() {
        if(sampleCnt == null) return 0;
        else return sampleCnt.longValue();
    }
    // Called by server-side only
    public Worklist values(Map<?, String> values) {
        this.values = values;
        return this;
    }
}