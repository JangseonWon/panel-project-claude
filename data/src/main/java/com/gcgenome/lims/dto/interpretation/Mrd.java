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
public final class Mrd {
	// For MRD
	@JsProperty(name="cancer_type")
	private String cancerType;
	private MrdHistory[] histories;

	// For MRD-Screen
	private String date;
	@JsProperty(name="input_dna")
	private Double inputDna;
	private MrdScreen.MrdScreenGeneResult[] results;

	// Common
	@JsProperty(name="mutation_rate")
	private String mutationRate;
	@JsOverlay
	@JsIgnore
	public Mrd inputDna(Integer inputDna) {
		if(inputDna == null) this.inputDna = null;
		else this.inputDna = inputDna +0.0;
		return this;
	}
	@JsOverlay
	@JsIgnore
	public Integer inputDna() {
		if(inputDna==null) {
			if(histories!=null && histories.length > 0) return last().inputDna();
			else return null;
		} else return inputDna.intValue();
	}

	@JsOverlay
	@JsIgnore
	public MrdHistory last() {
		return histories[0];
	}

	@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
	@Setter(onMethod_={@JsOverlay, @JsIgnore})
	@Getter(onMethod_={@JsOverlay, @JsIgnore})
	@Accessors(fluent=true)
	public static final class MrdHistory {
		private String date;
		@JsProperty(name="input_dna")
		private Double inputDna;
		private MrdGeneResult[] results;
		@JsOverlay
		@JsIgnore
		public MrdHistory inputDna(int inputDna) {
			this.inputDna = inputDna +0.0;
			return this;
		}
		@JsOverlay
		@JsIgnore
		public Integer inputDna() {
			if(inputDna==null) return null;
			else return inputDna.intValue();
		}
		@JsOverlay
		@JsIgnore
		public long nucleatedCells() {
			return Math.round(inputDna/6.5 * 1000);
		}
		@JsOverlay
		@JsIgnore
		public double pctClonalNucelatedCells(String gene) {
			return Arrays.stream(results).filter(r->gene.equalsIgnoreCase(r.gene())).findAny().get().equivalent() / (double) nucleatedCells();
		}
	}
	@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
	@Setter(onMethod_={@JsOverlay, @JsIgnore})
	@Getter(onMethod_={@JsOverlay, @JsIgnore})
	@Accessors(fluent=true)
	public static final class MrdGeneResult {
		private String gene;
		private MrdCloneResult target;
		private MrdCloneResult lqic;
		private String result;
		private String interpretation;
		@JsOverlay
		@JsIgnore
		public MrdGeneResult result(Result result) {
			this.result = result!=null?result.name():null;
			return this;
		}
		@JsOverlay
		@JsIgnore
		public Result result() {
			if(result==null) return null;
			else return Result.valueOf(result);
		}
		@JsOverlay
		@JsIgnore
		public long bCells() {
			return Math.round(target.readDepth() * 100.0 / lqic.readDepth() - 100);
		}
		@JsOverlay
		@JsIgnore
		public long equivalent() {
			return Math.round(target.clonalDepth() * 100.0 / lqic.readDepth());
		}
		@JsOverlay
		@JsIgnore
		public float pctClonalBCells() {
			return equivalent() / (float) bCells();
		}
	}
	@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
	@Setter(onMethod_={@JsOverlay, @JsIgnore})
	@Getter(onMethod_={@JsOverlay, @JsIgnore})
	@Accessors(fluent=true)
	public static final class MrdCloneResult {
		@JsProperty(name="read_depth")
		private Double readDepth;
		@JsProperty(name="clonal_depth")
		private Double clonalDepth;
		@JsOverlay
		@JsIgnore
		public MrdCloneResult readDepth(long readDepth) {
			this.readDepth = readDepth +0.0;
			return this;
		}
		@JsOverlay
		@JsIgnore
		public MrdCloneResult clonalDepth(long clonalDepth) {
			this.clonalDepth = clonalDepth +0.0;
			return this;
		}
		@JsOverlay
		@JsIgnore
		public Long readDepth() {
			if(readDepth==null) return null;
			else return readDepth.longValue();
		}
		@JsOverlay
		@JsIgnore
		public Long clonalDepth() {
			if(clonalDepth==null) return null;
			else return clonalDepth.longValue();
		}
	}
	public enum Result {
		DETECTED, NOT_DETECTED, NA, CUSTOM
	}
}
