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
public class Dgs {
	private String result;
	@JsonProperty("result_text")
	private String resultText;
	@JsonProperty("clinical_information")
	private String clinicalInformation;
	@JsonProperty("abbreviation_reference")
	private String abbreviationReference;
	@JsonProperty("abbreviation_disease")
	private String abbreviationDisease;
	@JsonProperty("reason_for_referral")
	private String reasonForReferral;
	private String abbreviation;
	private String interpretation;
	@JsonProperty("mean_depth")
	private String meanDepth;
	private String coverage;
	private String recommendation;
	private Variant[] variants;
	@JsonProperty("consent_incidental_findings")
	private Boolean consentIncidentalFindings;
	@JsonProperty("incidental_findings")
	private com.greencross.lims.dto.interpretation.Dgs incidentalFindings;
	private String inspector;
	private String reporter;
	private String reviewer;
	private String comment1;
	private String comment2;
	private Boolean revision;
	
	@Setter
	@Getter
	@Accessors(fluent=true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class Variant {
		private String snv;
		private String analysis;
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