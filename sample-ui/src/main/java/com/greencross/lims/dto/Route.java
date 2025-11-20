package com.greencross.lims.dto;

import jsinterop.annotations.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class Route {
	private String location;
	@JsProperty(name="should_update")
	private boolean shouldUpdate;
	@JsProperty(name="should_replace")
	private boolean shouldReplace;
}
