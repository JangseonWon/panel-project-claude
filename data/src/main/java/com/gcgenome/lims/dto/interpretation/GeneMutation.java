package com.gcgenome.lims.dto.interpretation;

import jsinterop.annotations.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class GeneMutation {
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
	@JsProperty(name="incidental_findings")
	private GeneMutation incidentalFindings;

	@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
	@Setter(onMethod_={@JsOverlay, @JsIgnore})
	@Getter(onMethod_={@JsOverlay, @JsIgnore})
	@Accessors(fluent=true)
	public static final class Variant {
		private String gene;
		private String hgvsc;
		private String hgvsp;
		private String zygosity;
		private String disease;
		private String inheritance;
		@JsProperty(name="class")
		private String clazz;
	}
}
