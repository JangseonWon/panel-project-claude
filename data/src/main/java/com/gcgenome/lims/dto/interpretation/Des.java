package com.gcgenome.lims.dto.interpretation;

import jsinterop.annotations.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class Des {
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
	private String recommendation;
	@JsProperty(name="mean_depth")
	private String meanDepth;
	private String coverage;
	private PanelTest.Variant[] variants;
	@JsProperty(name="consent_incidental_findings")
	private Boolean consentIncidentalFindings;
	@JsProperty(name="incidental_findings")
	private Des incidentalFindings;
	private String reporter;
	private String comment1;
	private String reviewer;
	private String comment2;
	private Boolean revision;
}
