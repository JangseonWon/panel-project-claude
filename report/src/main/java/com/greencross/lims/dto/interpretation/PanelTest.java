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
public class PanelTest {
	protected String result;
	@JsonProperty("result_text")
	private String resultText;
	@JsonProperty("reason_for_referral")
	protected String reasonForReferral;
	@JsonProperty("abbreviation_reference")
	protected String abbreviationReference;
	@JsonProperty("abbreviation_disease")
	protected String abbreviationDisease;
	protected String abbreviation;
	protected String interpretation;
	@JsonProperty("mean_depth")
	private String meanDepth;
	private String coverage;
	protected Variant[] variants;
	private com.greencross.lims.dto.interpretation.PanelTest addendum;
	@JsonProperty("mlpa_result")
	public Mlpa mlpaResult;

	@Setter
	@Getter
	@Accessors(fluent=true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class Variant {
		private String gene;
		private String hgvsc;
		private String hgvsp;
		private String zygosity;
		private String disease;
		private String inheritance;
		@JsonProperty("class")
		private String clazz;
	}

	@Setter
	@Getter
	@Accessors(fluent=true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Mlpa {
		public String result;
		public String exons;
		public String zygosity;
		@JsonProperty("del_dup")
		public String delDup;
	}
}