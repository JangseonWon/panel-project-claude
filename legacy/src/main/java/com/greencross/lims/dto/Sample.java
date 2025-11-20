package com.greencross.lims.dto;

import jsinterop.annotations.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class Sample {
    private Double id;
    @JsProperty(name="sample_type")
    private String sampleType;
    @JsProperty(name="date_sampling")
    private Double dateSampling;
    private String remark;
    private Double barcode;
    @JsProperty(name="patient_code")
    private String patientCode;
    @JsProperty(name="patient_name")
    private String patientName;
    @JsProperty(name="patient_sex")
    private String patientSex;
    @JsProperty(name="customer_name")
    private String customerName;
    private String mrn;
    private Object values;
    @JsOverlay
    @JsIgnore
    public Sample id(long id) {
        this.id = id + 0.0;
        return this;
    }
    @JsOverlay
    @JsIgnore
    public Long id() {
        if(id == null) return null;
        else return id.longValue();
    }
    @JsOverlay
    @JsIgnore
    public Sample barcode(long barcode) {
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
    public Sample dateSampling(long dateSampling) {
        this.dateSampling = dateSampling + 0.0;
        return this;
    }
    @JsOverlay
    @JsIgnore
    public Long dateSampling() {
        if(dateSampling == null) return null;
        else return dateSampling.longValue();
    }
    // Called by server-side only
    @JsOverlay
    @JsIgnore
    public Sample values(Map<?, String> values) {
        this.values = values;
        return this;
    }
}
