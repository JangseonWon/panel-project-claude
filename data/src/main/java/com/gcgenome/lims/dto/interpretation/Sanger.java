package com.gcgenome.lims.dto.interpretation;

import jsinterop.annotations.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class Sanger {
	private String interpretation;
	private Variant[] variants;
	private String inspector;
	private String reporter;
	private String reviewer;
	@JsProperty(name="has_proband_specimen")
	private Boolean hasProbandSpecimen;

	@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
	@Setter(onMethod_={@JsOverlay, @JsIgnore})
	@Getter(onMethod_={@JsOverlay, @JsIgnore})
	@Accessors(fluent=true)
	public static final class Variant {
		private String gene;
		private String hgvsc;
		private String hgvsp;
		private String zygosity;
		private String result;
		@JsProperty(name="class")
		private String clazz;
		private String sanger;
	}
}
