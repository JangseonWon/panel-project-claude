package com.gcgenome.lims.dto;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_= {@JsOverlay, @JsIgnore})
@Getter(onMethod_= {@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class Page {
	private Double index;
	private Service[] services;
	private String label;
	private String[] barcode;
	private Double score;
	private Boolean referral;
	private Boolean agreement;
	@JsOverlay
	@JsIgnore
	public Integer index() {
		return index.intValue();
	}
	@JsOverlay
	@JsIgnore
	public Page index(int page) {
		this.index = page+0.0;
		return this;
	}
}
