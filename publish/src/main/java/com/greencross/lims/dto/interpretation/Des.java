package com.greencross.lims.dto.interpretation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Des {
	private String result;
	@JsonProperty("result_text")
	private String resultText;
	@JsonProperty("reason_for_referral")
	private String reasonForReferral;
	@JsonProperty("abbreviation_reference")
	private String abbreviationReference;
	@JsonProperty("abbreviation_disease")
	private String abbreviationDisease;
	private String abbreviation;
	private String interpretation;
	@JsonProperty("mean_depth")
	private String meanDepth;
	private String coverage;
	private Variant[] variants;
	@JsonProperty("consent_incidental_findings")
	private Boolean consentIncidentalFindings;
	@JsonProperty("incidental_findings")
	private com.greencross.lims.dto.interpretation.Des incidentalFindings;
	private String reporter;
	private String reviewer;

	@Setter
	@Getter
	@Accessors(fluent=true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class Variant {
		private String snv;
		private String gene;
		private String hgvsc;
		private String hgvsp;
		private String zygosity;
		private String disease;
		private String inheritance;
		@JsonProperty("class")
		private String clazz;
	}
}