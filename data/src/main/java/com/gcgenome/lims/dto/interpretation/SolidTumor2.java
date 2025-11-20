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
public final class SolidTumor2 {
	@JsProperty(name="cancer_type")
	private String cancerType;
	private Result[] results;
	private Hypermutability hypermutability;
	private Qc qc;

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
		private String vaf;
		private Double depth;
		private String significance;
		private String interpretation;
		@JsOverlay
		@JsIgnore
		public Variant depth(Integer depth) {
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
	public enum Tier {
		Tier1, Tier2, Tier3
	}
	@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
	@Setter(onMethod_={@JsOverlay, @JsIgnore})
	@Getter(onMethod_={@JsOverlay, @JsIgnore})
	@Accessors(fluent=true)
	public static final class Hypermutability {
		private String tmb;
		private String msi;
		private Double score;
	}
	@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
	@Setter(onMethod_={@JsOverlay, @JsIgnore})
	@Getter(onMethod_={@JsOverlay, @JsIgnore})
	@Accessors(fluent=true)
	public static final class Qc {
		private String interpretation;
	}
}
