package com.gcgenome.lims.dto.interpretation;

import jsinterop.annotations.*;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class Hereditary {
	private String interpretation;
	private Variant[] variants;
	private Gene[] genes;
	@JsOverlay
	@JsIgnore
	public Hereditary put(String gene, String result) {
		JsPropertyMap<Object> map = Js.asPropertyMap(this);
		map.set(gene, result);
		return this;
	}
	@JsOverlay
	@JsIgnore
	public String get(String gene) {
		JsPropertyMap<Object> map = Js.asPropertyMap(this);
		return (String)map.get(gene);
	}

	@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
	@Setter(onMethod_={@JsOverlay, @JsIgnore})
	@Getter(onMethod_={@JsOverlay, @JsIgnore})
	@Accessors(fluent=true)
	public static final class Variant {
		private String snv;
		private String analysis;
		private String gene;
		private String hgvsc;
		private String hgvsp;
		private String zygosity;
		@JsProperty(name="class")
		private String clazz;
		private String interpretation;
	}

	@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
	@Setter(onMethod_={@JsOverlay, @JsIgnore})
	@Getter(onMethod_={@JsOverlay, @JsIgnore})
	@Accessors(fluent=true)
	public static final class Gene {
		private String name;
		private Boolean value;
	}
}
