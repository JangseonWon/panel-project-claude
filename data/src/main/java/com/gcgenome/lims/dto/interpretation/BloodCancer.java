package com.gcgenome.lims.dto.interpretation;

import jsinterop.annotations.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Arrays;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class BloodCancer {
	@JsProperty(name="cancer_type")
	private String cancerType;
	private Result[] results;
	@JsProperty(name="qc_dna")
	private String qcDna;
	@JsProperty(name="qc_library")
	private String qcLibrary;
	@JsProperty(name="qc_sequencing")
	private String qcSequencing;
	@JsProperty(name="mean_depth")
	private String meanDepth;
	private String coverage;
	@JsProperty(name="drug_phenotypes")
	private DrugPhenotype[] drugPhenotypes;

	@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
	@Setter(onMethod_={@JsOverlay, @JsIgnore})
	@Getter(onMethod_={@JsOverlay, @JsIgnore})
	@Accessors(fluent=true)
	public static final class Result {
		private String tier;
		private Variant[] variants;
		private String interpretation;
		@JsOverlay
		@JsIgnore
		public Tier tier() {
			if(tier == null) return null;
			return Arrays.stream(Tier.values()).filter(c->c.name().equals(tier)).findFirst().orElse(null);
		}
		@JsOverlay
		@JsIgnore
		public Result tier(Tier tier) {
			this.tier = tier.name();
			return this;
		}
	}
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
		private Double vaf;
		private Double depth;
		private String cosmic;
		private String interpretation;
		@JsOverlay
		@JsIgnore
		public Variant depth(int depth) {
			this.depth = depth + 0.0;
			return this;
		}
		@JsOverlay
		@JsIgnore
		public Integer depth() {
			if(depth == null) return null;
			else return depth.intValue();
		}
	}
	public enum Tier {
		Tier1, Tier2, Tier3
	}
	@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
	@Setter(onMethod_={@JsOverlay, @JsIgnore})
	@Getter(onMethod_={@JsOverlay, @JsIgnore})
	@Accessors(fluent = true)
	public static final class DrugPhenotype {
		private String gene;
		private String diplotype;
		@JsProperty(name="allele_status")
		private String alleleStatus;
		private String phenotype;
		private String result;
		private String interpretation;
	}
}
