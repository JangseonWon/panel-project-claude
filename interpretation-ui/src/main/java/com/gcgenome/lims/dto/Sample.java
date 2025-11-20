package com.gcgenome.lims.dto;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

@JsType(isNative = true, namespace = "", name = "Object")
public final class Sample {
    private Double id;
    @JsProperty(name = "sample_type") private String sampleType;
    @JsProperty(name = "date_sampling") private Double dateSampling;
    private String remark;
    private Double barcode;
    @JsProperty(name = "patient_code") private String patientCode;
    @JsProperty(name = "patient_name") private String patientName;
    @JsProperty(name = "patient_sex")
    private String patientSex;
    @JsProperty(name = "customer_name")
    private String customerName;
    private String mrn;
    private Object values;

    @JsOverlay
    @JsIgnore
    public Sample id(long id) {
        this.id = (double)id + 0.0;
        return this;
    }

    @JsOverlay
    @JsIgnore
    public Long id() {
        return this.id == null ? null : this.id.longValue();
    }

    @JsOverlay
    @JsIgnore
    public Sample barcode(long barcode) {
        this.barcode = (double)barcode + 0.0;
        return this;
    }

    @JsOverlay
    @JsIgnore
    public Long barcode() {
        return this.barcode == null ? null : this.barcode.longValue();
    }

    @JsOverlay
    @JsIgnore
    public Sample dateSampling(long dateSampling) {
        this.dateSampling = (double)dateSampling + 0.0;
        return this;
    }

    @JsOverlay
    @JsIgnore
    public Long dateSampling() {
        return this.dateSampling == null ? null : this.dateSampling.longValue();
    }

    @JsOverlay
    @JsIgnore
    public List keys() {
        if (this.values == null) {
            return null;
        } else {
            JsPropertyMap map = Js.asPropertyMap(this.values);
            List key = new LinkedList();
            map.forEach((k) -> {
                key.add(k);
            });
            return key;
        }
    }

    @JsOverlay
    @JsIgnore
    public String get(String key) {
        if (this.values == null) {
            return null;
        } else {
            JsPropertyMap map = Js.asPropertyMap(this.values);
            return map.has(key) ? (String)map.get(key) : null;
        }
    }

    @JsOverlay
    @JsIgnore
    public Sample put(String key, String value) {
        if (this.values == null) {
            this.values = new Object();
        }

        Js.asPropertyMap(this.values).set(key, value);
        return this;
    }

    @JsOverlay
    @JsIgnore
    public Sample values(Map values) {
        this.values = values;
        return this;
    }

    @JsOverlay
    @JsIgnore
    public Sample sampleType(String sampleType) {
        this.sampleType = sampleType;
        return this;
    }

    @JsOverlay
    @JsIgnore
    public Sample remark(String remark) {
        this.remark = remark;
        return this;
    }

    @JsOverlay
    @JsIgnore
    public Sample patientCode(String patientCode) {
        this.patientCode = patientCode;
        return this;
    }

    @JsOverlay
    @JsIgnore
    public Sample patientName(String patientName) {
        this.patientName = patientName;
        return this;
    }

    @JsOverlay
    @JsIgnore
    public Sample patientSex(String patientSex) {
        this.patientSex = patientSex;
        return this;
    }

    @JsOverlay
    @JsIgnore
    public Sample customerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    @JsOverlay
    @JsIgnore
    public Sample mrn(String mrn) {
        this.mrn = mrn;
        return this;
    }

    @JsOverlay
    @JsIgnore
    public String sampleType() {
        return this.sampleType;
    }

    @JsOverlay
    @JsIgnore
    public String remark() {
        return this.remark;
    }

    @JsOverlay
    @JsIgnore
    public String patientCode() {
        return this.patientCode;
    }

    @JsOverlay
    @JsIgnore
    public String patientName() {
        return this.patientName;
    }

    @JsOverlay
    @JsIgnore
    public String patientSex() {
        return this.patientSex;
    }

    @JsOverlay
    @JsIgnore
    public String customerName() {
        return this.customerName;
    }

    @JsOverlay
    @JsIgnore
    public String mrn() {
        return this.mrn;
    }

    @JsOverlay
    @JsIgnore
    public Object values() {
        return this.values;
    }
}