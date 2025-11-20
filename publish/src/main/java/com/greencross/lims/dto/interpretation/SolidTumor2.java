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
public class SolidTumor2 {
	@JsonProperty("cancer_category")
	private String cancerCategory;
	@JsonProperty("cancer_type")
	private String cancerType;
	private Variant[] variants;
	private Hypermutability hypermutability;
	private Qc qc;
	private Method method;
	@Setter
	@Getter
	@Accessors(fluent=true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Method {
		private String region;
		private String panel;
		private String method;
		private String sequencing;
		private String pipeline;
		private String reference;
		@JsonProperty("gene_sets")
		private GeneSet[] geneSets;
		@JsonProperty("immunotherapy_info")
		private String immunotherapyInfo;
		@JsonProperty("qc_info")
		private String qcInfo;
		@JsonProperty("fusion_info")
		private String fusionInfo;
		private String[] limitations;
	}
	@Setter
	@Getter
	@Accessors(fluent=true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Variant {
		private String kind;
		private Tier tier;
		private String significance;
		private String interpretation;
		private String snv;
		private String analysis;
		private String gene;
		private String hgvsc;
		private String hgvsp;
		private String vaf;
		private Integer depth;

		private String cnv;
		private String type;
		private String copyNumber;

		private String fusion;
		private String readCount;
	}
	public enum Tier {
		Tier1, Tier2, Tier3
	}
	@Setter
	@Getter
	@Accessors(fluent=true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class GeneSet {
		private String label;
		private String[] genes;
	}
	@Setter
	@Getter
	@Accessors(fluent=true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class Hypermutability {
		private String tmb;
		private String msi;
		private Double msiScore;
	}
	@Data
	@Accessors(fluent=true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class Qc {
		private String snvQc;
		private String cnvQc;
		private String msiQc;
		private String rnaQc;
		private String interpretation;
		private Double purity;
		@JsonProperty("pct_exon_over_100x")
		private Double pctExonOver100X;
		@JsonProperty("pct_exon_over_1000x")
		private Double pctExonOver1000X;
		private Double medianExonCoverage;
		private Double mad;
		private Double mbc;
		private Double usableMsi;
		private Double onTargetReads;
		@JsonProperty("median_cv_over_500x")
		private Double medianCvOver500X;
		private Double msaf;
	}
}
