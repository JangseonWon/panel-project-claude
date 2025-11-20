package com.gcgenome.lims.dto;

import jsinterop.annotations.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class Snv {
	@JsProperty(name="sample")
	private String analysis;
	@JsProperty(name="create_at")
	private String createAt;

	private String genotype;
	private String vaf;
	private String depth;
}
