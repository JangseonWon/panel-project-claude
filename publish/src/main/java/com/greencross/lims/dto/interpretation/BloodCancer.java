package com.greencross.lims.dto.interpretation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BloodCancer {
	@JsonProperty("cancer_type")
	private String cancerType;
	private Result[] results;
	@JsonProperty("qc_dna")
	private String qcDna;
	@JsonProperty("qc_library")
	private String qcLibrary;
	@JsonProperty("qc_sequencing")
	private String qcSequencing;
	@JsonProperty("mean_depth")
	private String meanDepth;
	private String coverage;
	@JsonProperty("drug_phenotypes")
	private DrugPhenotype[] drugPhenotypes;
	@Setter
	@Getter
	@Accessors(fluent=true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Result {
		private Tier tier;
		private Variant[] variants;
		private String interpretation;
	}
	@Setter
	@Getter
	@Accessors(fluent=true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Variant {
		private String snv;
		private String analysis;
		private String gene;
		private String hgvsc;
		private String hgvsp;
		private Double vaf;
		private Integer depth;
		private String cosmic;
		public Variant vaf(double vaf) {
			this.vaf = vaf;
			return this;
		}
	}
	public enum Tier {
		Tier1, Tier2, Tier3
	}
	@Data
	@Accessors(fluent = true)
	public static class DrugPhenotype {
		private String gene;
		private String diplotype;
		private String alleleStatus;
		private String phenotype;
		private String result;
		private String interpretation;
	}
}
