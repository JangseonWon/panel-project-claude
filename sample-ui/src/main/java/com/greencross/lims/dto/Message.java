package com.greencross.lims.dto;

import jsinterop.annotations.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class Message {
	@JsProperty(name="___id")
	private String id;
	private String type;
	private Object param;

	@JsOverlay
	@JsIgnore
	public Message type(MessageType type) {
		this.type = type.name();
		return this;
	}
	@JsOverlay
	@JsIgnore
	public MessageType type() {
		if(this.type == null) return null;
		return MessageType.valueOf(type);
	}

	public enum MessageType {
		REQUEST, RESPONSE,
		PROGRESS,
		COLLAPSE, STRETCH, FULLSCREEN,
		REFRESH,
		BLUR
	}
}
