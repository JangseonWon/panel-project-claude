package com.gcgenome.lims.dto;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class RequestReference {
    private Double sample;
    private String service;
    private String serial;
    private int row;
    @JsOverlay
    @JsIgnore
    public Long sample() {
        return sample.longValue();
    }
    @JsOverlay
    @JsIgnore
    public RequestReference sample(long sample) {
        this.sample = sample + 0.0;
        return this;
    }
}
