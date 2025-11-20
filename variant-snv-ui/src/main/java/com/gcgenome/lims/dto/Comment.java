package com.gcgenome.lims.dto;

import jsinterop.annotations.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class Comment {
	private String snv;
	@JsProperty(name="create_at")
	private Double createAt;
	@JsProperty(name="create_by")
	private String createBy;
	@JsProperty(name="last_modify_at")
	private Double lastModifyAt;
	@JsProperty(name="last_modify_by")
	private String lastModifyBy;
	private String comment;
	@JsOverlay
	@JsIgnore
	public long createAt() {
		return createAt.longValue();
	}
	@JsOverlay
	@JsIgnore
	public String lastModifyBy() {
		if(lastModifyBy == null) return createBy();
		else return lastModifyBy;
	}
	@JsOverlay
	@JsIgnore
	public long lastModifyAt() {
		if(lastModifyAt == null) return createAt();
		else return lastModifyAt.longValue();
	}
}
