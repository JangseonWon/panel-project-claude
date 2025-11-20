package com.gcgenome.lims.dto;

import jsinterop.annotations.*;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class Analysis2 {
	private String batch;
	private Double row;
	private String file;
	private String panel;
	@JsProperty(name="create_at")
	private Double createAt;
	@JsProperty(name="last_modify_at")
	private Double lastModifyAt;
	private String serial;
	private String editor;
	private Object values;
	private String result;
	private Double sample;
	@JsProperty(name="patient_name")
	private String patientName;
	@JsProperty(name="service_name")
	private String serviceName;
	private String service;
	private String state;
	@JsOverlay
	@JsIgnore
	public Integer row() {
		if(row == null) return null;
		else return row.intValue();
	}
	@JsOverlay
	@JsIgnore
	public long createAt() {
		if(createAt == null) throw new RuntimeException();
		else return createAt.longValue();
	}
	@JsOverlay
	@JsIgnore
	public Analysis2 sample(long sample) {
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
	public String get(String key) {
		if(values == null) return null;
		JsPropertyMap<Object> map = Js.asPropertyMap(values);
		if(map.has(key)) return (String) map.get(key);
		else return null;
	}
	@JsOverlay
	@JsIgnore
	public Analysis2 put(String key, String value) {
		if(values == null) this.values = new Object();
		Js.asPropertyMap(values).set(key, value);
		return this;
	}
}