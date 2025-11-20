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
public final class MrdScreen {
	private String date;
	@JsProperty(name="cancer_type")
	private String cancerType;
	@JsProperty(name="input_dna")
	private Double inputDna;
	private MrdScreenGeneResult[] results;
	@JsProperty(name="somatic_mutations")
	private SomaticMutation[] somaticMutations;
	@JsOverlay
	@JsIgnore
	public long nucleatedCells() {
		return Math.round(inputDna/6.5 * 1000);
	}
	@JsOverlay
	@JsIgnore
	public MrdScreen inputDna(int inputDna) {
		this.inputDna = inputDna +0.0;
		return this;
	}
	@JsOverlay
	@JsIgnore
	public Integer inputDna() {
		if(inputDna==null) return null;
		else return inputDna.intValue();
	}

	@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
	@Setter(onMethod_={@JsOverlay, @JsIgnore})
	@Getter(onMethod_={@JsOverlay, @JsIgnore})
	@Accessors(fluent=true)
	public static final class MrdScreenGeneResult {
		private String gene;
		private MrdScreenCloneResult[] clones;
		@JsProperty(name="depth_total")
		private Double depthTotal;
		@JsProperty(name="depth_lqic")
		private Double depthLqic;
		@JsProperty(name="length_lqic")
		private Double lengthLqic;
		private String interpretation;
		@JsOverlay
		@JsIgnore
		public MrdScreenGeneResult depthTotal(long depthTotal) {
			this.depthTotal = depthTotal +0.0;
			return this;
		}
		@JsOverlay
		@JsIgnore
		public Long depthTotal() {
			if(depthTotal==null) return null;
			else return depthTotal.longValue();
		}
		@JsOverlay
		@JsIgnore
		public Long depthClonal() {
			if(clones==null) return null;
			else {
				long sum = 0;
				for(MrdScreenCloneResult clone: clones) if(clone.depth!=null) sum += clone.depth;
				return sum;
			}
		}
		@JsOverlay
		@JsIgnore
		public MrdScreenGeneResult depthLqic(long depthLqic) {
			this.depthLqic = depthLqic +0.0;
			return this;
		}
		@JsOverlay
		@JsIgnore
		public Long depthLqic() {
			if(depthLqic==null) return null;
			else return depthLqic.longValue();
		}
		@JsOverlay
		@JsIgnore
		public MrdScreenGeneResult lengthLqic(long lengthLqic) {
			this.lengthLqic = lengthLqic +0.0;
			return this;
		}
		@JsOverlay
		@JsIgnore
		public Long lengthLqic() {
			if(lengthLqic==null) return null;
			else return lengthLqic.longValue();
		}
		@JsOverlay
		@JsIgnore
		public double totalClonalCells() {
			if(clones==null || clones.length <=0) return 0;
			else return Arrays.stream(clones).mapToDouble(c->c.coverage(depthLqic(), bCells())).sum();
		}
		@JsOverlay
		@JsIgnore
		public long bCells() {
			return Math.round(depthTotal() * 100.0 / depthLqic() - 100);
		}
	}
	@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
	@Setter(onMethod_={@JsOverlay, @JsIgnore})
	@Getter(onMethod_={@JsOverlay, @JsIgnore})
	@Accessors(fluent=true)
	public static final class MrdScreenCloneResult {
		private String no;
		@JsProperty(name="region_v")
		private String regionV;
		@JsProperty(name="region_j")
		private String regionJ;
		private Double length;
		private Double depth;
		private String sequence;
		@JsOverlay
		@JsIgnore
		public MrdScreenCloneResult length(long length) {
			this.length = length +0.0;
			return this;
		}
		@JsOverlay
		@JsIgnore
		public MrdScreenCloneResult depth(long depth) {
			this.depth = depth +0.0;
			return this;
		}
		@JsOverlay
		@JsIgnore
		public Integer length() {
			if(length==null) return null;
			else return length.intValue();
		}
		@JsOverlay
		@JsIgnore
		public Long depth() {
			if(depth==null) return null;
			else return depth.longValue();
		}
		@JsOverlay
		@JsIgnore
		public double coverage(long depthLqic, long bCells) {
			return equivalent(depthLqic) * 100.0 / bCells;
		}
		@JsOverlay
		@JsIgnore
		public long equivalent(long depthLqic) {
			return Math.round(depth * 100.0 / depthLqic);
		}
	}
	public enum Result {
		DETECTED, NOT_DETECTED
	}
	@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
	@Setter(onMethod_={@JsOverlay, @JsIgnore})
	@Getter(onMethod_={@JsOverlay, @JsIgnore})
	@Accessors(fluent=true)
	public static final class SomaticMutation {
		@JsProperty(name="clone")
		private String clone;
		@JsProperty(name="hyper_mutation")
		private String hyperMutation;
		@JsProperty(name="mutation_rate")
		private String mutationRate;
	}
}
