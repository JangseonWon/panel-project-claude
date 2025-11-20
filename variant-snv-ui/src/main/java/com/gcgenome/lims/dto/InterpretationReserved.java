package com.gcgenome.lims.dto;

import jsinterop.annotations.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class InterpretationReserved {
	private String snv;
	private String service;
	@JsProperty(name="create_at")
	private Double createAt;
	@JsProperty(name="create_by")
	private String createBy;
	private String interpretation;
	@JsOverlay
	@JsIgnore
	public long createAt() {
		return createAt.longValue();
	}
}
