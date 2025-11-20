package com.gcgenome.lims.dto;

import jsinterop.annotations.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class File {
	private Double sample;
	private String service;
	private Double sequence;
	private String name;
	private String extension;
	private Double size;
	private String url;
	@JsProperty(name="create_at")
	private Double createAt;
	@JsProperty(name="creator")
	private String creator;
	@JsOverlay
	@JsIgnore
	public File sample(long sample) {
		this.sample = sample + 0.0;
		return this;
	}
	@JsOverlay
	@JsIgnore
	public long sample() {
		return sample.longValue();
	}
	@JsOverlay
	@JsIgnore
	public File sequence(int sequence) {
		this.sequence = sequence + 0.0;
		return this;
	}
	@JsOverlay
	@JsIgnore
	public Integer sequence() {
		if(this.sequence == null) return null;
		else return this.sequence.intValue();
	}
	@JsOverlay
	@JsIgnore
	public File size(long size) {
		this.size = size + 0.0;
		return this;
	}
	@JsOverlay
	@JsIgnore
	public Long size() {
		if(this.size == null) return null;
		else return this.size.longValue();
	}
	@JsOverlay
	@JsIgnore
	public File createAt(Long createAt) {
		if(createAt == null) this.createAt = null;
		else this.createAt = createAt.doubleValue();
		return this;
	}
	@JsOverlay
	@JsIgnore
	public Long createAt() {
		if(createAt == null) return null;
		else return createAt.longValue();
	}
}
