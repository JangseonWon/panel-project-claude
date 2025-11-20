package com.gcgenome.lims.dto;

import jsinterop.annotations.*;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class Request {
    @JsProperty(name="date_request")
    private Double dateRequest;
    @JsProperty(name="date_start")
    private Double dateStart;
    @JsProperty(name="date_due")
    private Double dateDue;
    @JsProperty(name="date_reception")
    private Double dateReception;
    @JsProperty(name="date_due_publish")
    private Double dateDuePublish;
    private Double tat;
    private Service service;
    @JsProperty(name="patient_code")
    private String patientCode;
    @JsProperty(name="patient_name")
    private String patientName;
    @JsProperty(name="patient_sex")
    private String patientSex;
    @JsProperty(name="customer_name")
    private String customerName;
    private String mrn;
    private Double sample;
    @JsProperty(name="sample_type")
    private String sampleType;
    private String remark;
    private Double barcode;
    private Object values;
    @JsOverlay
    @JsIgnore
    public Request dateRequest(long dateRequest) {
        this.dateRequest = dateRequest + 0.0;
        return this;
    }
    @JsOverlay
    @JsIgnore
    public Long dateRequest() {
        if(dateRequest == null) return null;
        else return dateRequest.longValue();
    }
    @JsOverlay
    @JsIgnore
    public Request dateStart(long dateStart) {
        this.dateStart = dateStart + 0.0;
        return this;
    }
    @JsOverlay
    @JsIgnore
    public Long dateStart() {
        if(dateStart == null) return null;
        else return dateStart.longValue();
    }
    @JsOverlay
    @JsIgnore
    public Request dateDue(long dateDue) {
        this.dateDue = dateDue + 0.0;
        return this;
    }
    @JsOverlay
    @JsIgnore
    public Long dateDue() {
        if(dateDue == null) return null;
        else return dateDue.longValue();
    }
    @JsOverlay
    @JsIgnore
    public Long dateReception() {
        if(dateReception == null) return null;
        else return dateReception.longValue();
    }
    @JsOverlay
    @JsIgnore
    public Long dateDuePublish() {
        if(dateDuePublish == null) return null;
        else return dateDuePublish.longValue();
    }
    @JsOverlay
    @JsIgnore
    public Request tat(int tat) {
        this.tat = tat + 0.0;
        return this;
    }
    @JsOverlay
    @JsIgnore
    public Integer tat() {
        if(tat == null) return null;
        else return tat.intValue();
    }
    @JsOverlay
    @JsIgnore
    public Request sample(long sample) {
        this.sample = sample + 0.0;
        return this;
    }
    @JsOverlay
    @JsIgnore
    public Long sample() {
        if(sample == null) return null;
        else return sample.longValue();
    }
    @JsOverlay
    @JsIgnore
    public Request barcode(long barcode) {
        this.barcode = barcode + 0.0;
        return this;
    }
    @JsOverlay
    @JsIgnore
    public Long barcode() {
        if(barcode == null) return null;
        else return barcode.longValue();
    }
    @JsOverlay
    @JsIgnore
    public String serviceGroup() {
        if(service!=null) return this.service.group();
        return null;
    }
    @JsOverlay
    @JsIgnore
    public String serviceGroupColor() {
        if(service!=null) return this.service.groupColor();
        return null;
    }
    @JsOverlay
    @JsIgnore
    public String serviceCode() {
        if(service!=null) return this.service.id();
        return null;
    }
    @JsOverlay
    @JsIgnore
    public String serviceName() {
        if(service!=null) return this.service.name();
        return null;
    }

    @JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
    @Setter(onMethod_={@JsOverlay, @JsIgnore})
    @Getter(onMethod_={@JsOverlay, @JsIgnore})
    @Accessors(fluent=true)
    public final static class Service {
        private String id;
        private String name;
        private String group;
        private String order;
        @JsProperty(name="group_color")
        private String groupColor;
    }
    @JsOverlay
    @JsIgnore
    public List<String> keys() {
        if(values == null) return null;
        JsPropertyMap<Object> map = Js.asPropertyMap(values);
        List<String> key = new LinkedList<>();
        map.forEach(k->key.add(k));
        return key;
    }
    @JsOverlay
    @JsIgnore
    public String get(String key) {
        if(values == null) return null;
        JsPropertyMap<Object> map = Js.asPropertyMap(values);
        if(map.has(key)) return (String) map.get(key);
        else return null;
    }
    @JsOverlay
    @JsIgnore
    public Request put(String key, String value) {
        if(values == null) this.values = new Object();
        Js.asPropertyMap(values).set(key, value);
        return this;
    }
    // Called by server-side only
    @JsOverlay
    @JsIgnore
    public Request values(Map<?, String> values) {
        this.values = values;
        return this;
    }
}

