package com.gcgenome.lims.dto;

import jsinterop.annotations.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class RequestSnv {
	@JsProperty(name="date_request")
	private Double dateRequest;
	@JsProperty(name="date_start")
	private Double dateStart;
	@JsProperty(name="date_due")
	private Double dateDue;
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
	public RequestSnv dateRequest(long dateRequest) {
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
	public RequestSnv dateStart(long dateStart) {
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
	public RequestSnv dateDue(long dateDue) {
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
	public RequestSnv tat(int tat) {
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
	public RequestSnv sample(long sample) {
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
	public RequestSnv barcode(long barcode) {
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

	private Double batch;
	private Double row;
	private String serial;
	private String tier;
	private String genotype;
	private Double vaf;
	private Double depth;
	// private Variant variant;
	@JsOverlay
	@JsIgnore
	public RequestSnv depth(long createTime) {
		this.depth = depth + 0.0;
		return this;
	}
	@JsOverlay
	@JsIgnore
	public long depth() {
		if(depth == null) throw new RuntimeException();
		else return depth.longValue();
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
}
