package com.gcgenome.lims.dto.interpretation.tmp;

import jsinterop.annotations.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class N159Dto {
	private String result;
	@JsProperty(name="result_text")
	private String resultText;
	@JsProperty(name="abbreviation_reference")
	private String abbreviationReference;
	private String interpretation;
	private Variant[] variants;

	@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
	@Setter(onMethod_={@JsOverlay, @JsIgnore})
	@Getter(onMethod_={@JsOverlay, @JsIgnore})
	@Accessors(fluent=true)
	public static final class Variant {
		private String snv;
		private String analysis;
		private String gene;
		@JsProperty(name="origin_hgvsc")
		private String originHgvsc;
		private String hgvsc;
		@JsProperty(name="origin_hgvsp")
		private String originHgvsp;
		private String hgvsp;
		private Double vaf;
		private Double depth;
		private String cosmic;
		private String tier;
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
		@JsOverlay
		@JsIgnore
		public Variant tier(Tier tier) {
			if(tier == null) this.tier = null;
			else this.tier = tier.name();
			return this;
		}
		@JsOverlay
		@JsIgnore
		public Tier tier() {
			if(tier == null) return null;
			else return Tier.valueOf(tier);
		}
	}
	public enum Tier {
		Tier12, Tier3, Tier4
	}
}
