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
public final class Hrd {
	@JsonProperty("cancer_type")
	private String cancerType;
	private String gi;
	private String snv;
	private String cnv;
	private String brca;
	@JsonProperty("gi_score")
	private Integer giScore;
	private String interpretation;
	private GeneResult[] results;

	@Setter
	@Getter
	@Accessors(fluent=true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class GeneResult {
		private String tier;
		private Variant[] variants;
		private String interpretation;
	}
	@Setter
	@Getter
	@Accessors(fluent=true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class Variant {
		private String gene;
		private String hgvsc;
		private String hgvsp;
		private Double vaf;
		private Integer depth;
		@JsonProperty("cosmic_id")
		private String cosmicId;
		private String dna;
		private String protein;
	}
}