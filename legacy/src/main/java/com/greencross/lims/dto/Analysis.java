package com.greencross.lims.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(fluent=true)
public final class Analysis {
    private String sheet;
    @JsonProperty("batch_title")
    private String batchTitle;
    private Double batch;
    private Double row;
    private String serial;
    private String sort;
    @JsonProperty("create_time")
    private Double createTime;
    @JsonProperty("date_reception")
    private Double dateReception;
    @JsonProperty("date_due_publish")
    private Double dateDuePublish;
    private Object values;
    @JsonProperty("patient_code")
    private String patientCode;
    @JsonProperty("patient_name")
    private String patientName;
    private Double sample;				// Origin Sample
    private AnalysisRequest[] requests;	// Related Requests

    public Analysis batch(Integer batch) {
        if(batch == null) this.batch = null;
        else this.batch = batch.doubleValue();
        return this;
    }

    public Integer batch() {
        if(batch == null) return null;
        else return batch.intValue();
    }

    public Analysis row(Integer row) {
        if(row == null) this.row = null;
        else this.row = row.doubleValue();
        return this;
    }

    public Integer row() {
        if(row == null) return null;
        else return row.intValue();
    }

    public Analysis createTime(long createTime) {
        this.createTime = createTime + 0.0;
        return this;
    }

    public long createTime() {
        if(createTime == null) throw new RuntimeException();
        else return createTime.longValue();
    }

    public Analysis sample(long sample) {
        this.sample = sample + 0.0;
        return this;
    }

    public Long sample() {
        if(sample == null) return null;
        else return sample.longValue();
    }
    // Called by server-side only
    public Analysis values(Map<?, String> values) {
        this.values = values;
        return this;
    }

    @Data
    @Accessors(fluent=true)
    public final static class AnalysisRequest {
        // Sample
        private Double sample;
        // Service
        private Request.Service service;

        public AnalysisRequest sample(long sample) {
            this.sample = sample + 0.0;
            return this;
        }

        public Long sample() {
            if(sample == null) return null;
            else return sample.longValue();
        }

        public String serviceGroup() {
            if(service!=null) return this.service.group();
            return null;
        }

        public String serviceGroupColor() {
            if(service!=null) return this.service.groupColor();
            return null;
        }

        public String serviceCode() {
            if(service!=null) return this.service.id();
            return null;
        }

        public String serviceName() {
            if(service!=null) return this.service.name();
            return null;
        }
    }
}
