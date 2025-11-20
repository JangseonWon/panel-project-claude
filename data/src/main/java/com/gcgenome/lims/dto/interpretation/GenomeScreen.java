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
public final class GenomeScreen {
	private String summary;
	private String interpretation;
	private PanelTest.Variant[] variants;
	private Disease[] diseases;
	private Genotype[] genotypes;
	@JsProperty(name="mean_depth")
	private String meanDepth;
	private String coverage;
	@JsOverlay
	@JsIgnore
	public GenomeScreen put(String gene, String result) {
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
	public static final class Disease {
		private String name;
		private Gene[] values;
	}
	@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
	@Setter(onMethod_={@JsOverlay, @JsIgnore})
	@Getter(onMethod_={@JsOverlay, @JsIgnore})
	@Accessors(fluent=true)
	public static final class Gene {
		private String name;
		private Boolean value;
	}
	@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
	@Setter(onMethod_={@JsOverlay, @JsIgnore})
	@Getter(onMethod_={@JsOverlay, @JsIgnore})
	@Accessors(fluent=true)
	public static final class Genotype {
		private String gene;
		private String pos;
		private String genotype;
	}
}
