package com.gcgenome.lims.dto;

import jsinterop.annotations.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class Report {
	private Double sample;
	private String service;
	private String type;
	@JsProperty(name="create_at")
	private Double createAt;
	@JsProperty(name="file_name")
	private String fileName;
	@JsProperty(name="file_size")
	private Double fileSize;
	@JsProperty(name="file_url")
	private String fileUrl;
	@JsProperty(name="creator")
	private String creator;
	@JsProperty(name="publish_at")
	private Double publishAt;
	@JsProperty(name="publisher")
	private String publisher;
	@JsProperty(name="description")
	private String description;
	@JsProperty(name="short_form_text")
	private String shortFormText;
	@JsProperty(name="long_form_text")
	private String longFormText;
	@JsOverlay
	@JsIgnore
	public Report sample(long sample) {
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
	public Report createAt(Long createAt) {
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
	@JsOverlay
	@JsIgnore
	public Report publishAt(Long publishAt) {
		if(publishAt == null) this.publishAt = null;
		else this.publishAt = publishAt.doubleValue();
		return this;
	}
	@JsOverlay
	@JsIgnore
	public Long publishAt() {
		if(publishAt == null) return null;
		else return publishAt.longValue();
	}
	@JsOverlay
	@JsIgnore
	public Report fileSize(Integer fileSize) {
		if(fileSize == null) this.fileSize = null;
		else this.fileSize = fileSize.doubleValue();
		return this;
	}
	@JsOverlay
	@JsIgnore
	public Integer fileSize() {
		if(fileSize == null) return null;
		else return fileSize.intValue();
	}
}
