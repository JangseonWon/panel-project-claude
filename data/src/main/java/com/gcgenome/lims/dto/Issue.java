package com.gcgenome.lims.dto;

import jsinterop.annotations.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class Issue {
	private Double sample;
	private String state;
	private String title;
	private Double parent;
	@JsProperty(name="create_time")
	private Double createTime;
	@JsProperty(name="date_request")
	private Double dateRequest;
	@JsProperty(name="date_start")
	private Double dateStart;
	@JsProperty(name="date_due")
	private Double dateDue;
	private Double tat;
	private String service;
	@JsProperty(name="patient_code")
	private String patientCode;
	@JsProperty(name="patient_name")
	private String patientName;
	@JsProperty(name="customer_name")
	private String customerName;
	private String mrn;
	@JsOverlay
	@JsIgnore
	public Issue parent(Integer parent) {
		if(parent == null) this.parent = null;
		else this.parent = parent.doubleValue();
		return this;
	}
	@JsOverlay
	@JsIgnore
	public Integer parent() {
		if(parent == null) return null;
		else return parent.intValue();
	}
	@JsOverlay
	@JsIgnore
	public Issue createTime(long createTime) {
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
	public Issue dateRequest(long dateRequest) {
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
	public Issue dateStart(long dateStart) {
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
	public Issue dateDue(long dateDue) {
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
	public Issue tat(int tat) {
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
	public Issue sample(long sample) {
		this.sample = sample + 0.0;
		return this;
	}
	@JsOverlay
	@JsIgnore
	public Long sample() {
		if(sample == null) return null;
		else return sample.longValue();
	}
	/*
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
	}*/
}
