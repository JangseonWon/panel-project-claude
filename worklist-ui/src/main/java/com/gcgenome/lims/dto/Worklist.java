package com.gcgenome.lims.dto;

import jsinterop.annotations.*;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class Worklist {
    private Double worklist;
    @JsProperty(name="create_time")
    private Double createTime;
    private String state;
    private String user;
    private String title;
    private Object values;
    @JsProperty(name="sample_cnt")
    private Double sampleCnt;
    private Request.Service[] services;
    @JsOverlay
    @JsIgnore
    public Worklist worklist(Integer worklist) {
        if(worklist == null) this.worklist = null;
        else this.worklist = worklist.doubleValue();
        return this;
    }
    @JsOverlay
    @JsIgnore
    public Integer worklist() {
        if(worklist == null) return null;
        else return worklist.intValue();
    }
    @JsOverlay
    @JsIgnore
    public Worklist createTime(long createTime) {
        this.createTime = createTime + 0.0;
        return this;
    }
    @JsOverlay
    @JsIgnore
    public long createTime() {
        if(createTime == null) throw new RuntimeException();
        else return createTime.longValue();
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
    public Worklist put(String key, String value) {
        if(values == null) this.values = new Object();
        Js.asPropertyMap(values).set(key, value);
        return this;
    }
    @JsOverlay
    @JsIgnore
    public Worklist sampleCnt(long sampleCnt) {
        this.sampleCnt = sampleCnt + 0.0;
        return this;
    }
    @JsOverlay
    @JsIgnore
    public long sampleCnt() {
        if(sampleCnt == null) return 0;
        else return sampleCnt.longValue();
    }
    // Called by server-side only
    @JsOverlay
    @JsIgnore
    public Worklist values(Map<?, String> values) {
        this.values = values;
        return this;
    }
}
