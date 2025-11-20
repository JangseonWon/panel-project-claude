package com.gcgenome.lims.dto.interpretation;

import jsinterop.annotations.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class Hrd {
	@JsProperty(name="cancer_type")
	private String cancerType;
	private String gi;                      // 유전체 불안정성
	private String snv;
	private String cnv;
	@JsProperty(name="gi_score")
	private Double giScore;
	private String interpretation;
	private GeneResult[] results;

	@JsOverlay
	@JsIgnore
	public Hrd giScore(int giScore) {
		this.giScore = giScore + 0.0;
		return this;
	}
	@JsOverlay
	@JsIgnore
	public Integer giScore() {
		if(giScore == null) return null;
		else return giScore.intValue();
	}

	@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
	@Setter(onMethod_={@JsOverlay, @JsIgnore})
	@Getter(onMethod_={@JsOverlay, @JsIgnore})
	@Accessors(fluent=true)
	public static final class GeneResult {
		private String tier;
		private String gene;
		private String result;
		private Variant[] variants;
		private String interpretation;
		@JsOverlay
		@JsIgnore
		public String tier() {
			if(tier == null) return gene;
			else return tier;
		}
		@JsOverlay
		@JsIgnore
		public GeneResult tier(String tier) {
			this.tier = tier;
			this.gene = tier;
			return this;
		}
	}
	@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
	@Setter(onMethod_={@JsOverlay, @JsIgnore})
	@Getter(onMethod_={@JsOverlay, @JsIgnore})
	@Accessors(fluent=true)
	public static final class Variant {
		private String gene;
		private String dna;
		private String protein;
		private Double vaf;
		private Double depth;
		@JsProperty(name="cosmic_id")
		private String cosmicId;
		@JsProperty(name="class")
		private String clazz;
		@JsOverlay
		@JsIgnore
		public Hrd.Variant depth(Integer depth) {
			if(depth!=null) this.depth = depth + 0.0;
			else this.depth = null;
			return this;
		}
		@JsOverlay
		@JsIgnore
		public Integer depth() {
			if(depth == null) return null;
			else return depth.intValue();
		}
	}
}
