package com.greencross.lims.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(fluent=true)
public final class Request {
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
    private Service service;
    @JsonProperty("patient_code")
    private String patientCode;
    @JsonProperty("patient_name")
    private String patientName;
    @JsonProperty("patient_sex")
    private String patientSex;
    @JsonProperty("customer_name")
    private String customerName;
    private String mrn;
    private Double sample;
    @JsonProperty("sample_type")
    private String sampleType;
    private String remark;
    private Double barcode;
    private Object values;
    public Request dateRequest(long dateRequest) {
        this.dateRequest = dateRequest + 0.0;
        return this;
    }
    public Long dateRequest() {
        if(dateRequest == null) return null;
        else return dateRequest.longValue();
    }
    public Request dateStart(long dateStart) {
        this.dateStart = dateStart + 0.0;
        return this;
    }
    public Long dateStart() {
        if(dateStart == null) return null;
        else return dateStart.longValue();
    }
    public Request dateDue(long dateDue) {
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
    public Request tat(int tat) {
        this.tat = tat + 0.0;
        return this;
    }
    public Integer tat() {
        if(tat == null) return null;
        else return tat.intValue();
    }
    public Request sample(long sample) {
        this.sample = sample + 0.0;
        return this;
    }

    public Long sample() {
        if(sample == null) return null;
        else return sample.longValue();
    }

    public Request barcode(long barcode) {
        this.barcode = barcode + 0.0;
        return this;
    }

    public Long barcode() {
        if(barcode == null) return null;
        else return barcode.longValue();
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

    @Data
    @Accessors(fluent=true)
    public final static class Service {
        private String id;
        private String name;
        private String group;
        private String order;
        @JsonProperty("group_color")
        private String groupColor;
    }

    // Called by server-side only
    public Request values(Map<?, String> values) {
        this.values = values;
        return this;
    }
}
