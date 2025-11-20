package com.gcgenome.lims.dto;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class Plugin {
	private String id;
	private PluginType type;
	private String[] sheets;
	private String name;
	private String icon;
	private String order;
	private String color;
	private String colorBg;
	private String description;
	private int slot;
	private int exec;
	public enum PluginType {
		UNIT, BATCH, HOOK
	}

}
