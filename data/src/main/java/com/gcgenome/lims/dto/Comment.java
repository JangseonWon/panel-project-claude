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
	@JsProperty(name="create_time")
	private Double createTime;
	private String user;
	private String content;
	@JsOverlay
	@JsIgnore
	public Comment createTime(long createTime) {
		this.createTime = createTime + 0.0;
		return this;
	}
	@JsOverlay
	@JsIgnore
	public long createTime() {
		if(createTime == null) throw new RuntimeException();
		else return createTime.longValue();
	}
}
