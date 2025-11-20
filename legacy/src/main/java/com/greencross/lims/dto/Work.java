package com.greencross.lims.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(fluent=true)
public final class Work {
    private String id;
    @JsonProperty("worklist_title")
    private String worklistTitle;
    private Double worklist;
    @JsonProperty("create_time")
    private Double createTime;
    @JsonProperty("date_request")
    private Double dateRequest;
    @JsonProperty("date_start")
    private Double dateStart;
    @JsonProperty("date_due")
    private Double dateDue;
    @JsonProperty("date_reception")
    private Double dateReception;
    @JsonProperty("date_due_publish")
    private Double dateDuePublish;
    private Double tat;
    private Request.Service service;
    @JsonProperty("patient_code")
    private String patientCode;
    @JsonProperty("patient_name")
    private String patientName;
    @JsonProperty("customer_name")
    private String customerName;
    private String mrn;
    private Double sample;
    @JsonProperty("sample_type")
    private String sampleType;
    private String remark;
    private Object values;

    public Work worklist(Integer worklist) {
        if(worklist == null) this.worklist = null;
        else this.worklist = worklist.doubleValue();
        return this;
    }

    public Integer worklist() {
        if(worklist == null) return null;
        else return worklist.intValue();
    }

    public Work createTime(long createTime) {
        this.createTime = createTime + 0.0;
        return this;
    }

    public long createTime() {
        if(createTime == null) throw new RuntimeException();
        else return createTime.longValue();
    }

    public Work dateRequest(long dateRequest) {
        this.dateRequest = dateRequest + 0.0;
        return this;
    }

    public Long dateRequest() {
        if(dateRequest == null) return null;
        else return dateRequest.longValue();
    }

    public Work dateStart(long dateStart) {
        this.dateStart = dateStart + 0.0;
        return this;
    }

    public Long dateStart() {
        if(dateStart == null) return null;
        else return dateStart.longValue();
    }

    public Work dateDue(long dateDue) {
        this.dateDue = dateDue + 0.0;
        return this;
    }

    public Long dateDue() {
        if(dateDue == null) return null;
        else return dateDue.longValue();
    }

    public Long dateReception() {
        if(dateReception == null) return null;
        else return dateReception.longValue();
    }

    public Long dateDuePublish() {
        if(dateDuePublish == null) return null;
        else return dateDuePublish.longValue();
    }

    public Work tat(int tat) {
        this.tat = tat + 0.0;
        return this;
    }

    public Integer tat() {
        if(tat == null) return null;
        else return tat.intValue();
    }

    public Work sample(long sample) {
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
    // Called by server-side only

    public Work values(Map<?, String> values) {
        this.values = values;
        return this;
    }
}