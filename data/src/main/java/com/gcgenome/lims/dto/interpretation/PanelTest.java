package com.gcgenome.lims.dto.interpretation;

import jsinterop.annotations.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class PanelTest {
	private String result;
	@JsProperty(name="result_text")
	private String resultText;
	@JsProperty(name="reason_for_referral")
	private String reasonForReferral;
	@JsProperty(name="abbreviation_reference")
	private String abbreviationReference;
	@JsProperty(name="abbreviation_disease")
	private String abbreviationDisease;
	private String abbreviation;
	private String interpretation;
	@JsProperty(name="mean_depth")
	private String meanDepth;
	private String coverage;
	private Variant[] variants;
	private PanelTest addendum;

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
		@JsProperty(name ="origin_hgvsc")
		private String originHgvsc;
		@JsProperty(name ="origin_hgvsp")
		private String originHgvsp;
		private String zygosity;
		private String disease;
		private String inheritance;
		@JsProperty(name="class")
		private String clazz;
		private String interpretation;
	}
}
