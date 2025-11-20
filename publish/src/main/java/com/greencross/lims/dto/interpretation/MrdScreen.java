package com.greencross.lims.dto.interpretation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Arrays;

@Setter
@Getter
@Accessors(fluent=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MrdScreen {
	private String cancerType;
	private Integer inputDna;
	private SomaticMutation[] somaticMutations;
	private MrdScreenGeneResult[] results;
	public long nucleatedCells() {
		return Math.round(inputDna/6.5 * 1000);
	}
	@Setter
	@Getter
	@Accessors(fluent=true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class MrdScreenGeneResult {
		private String gene;
		private MrdScreenCloneResult[] clones;
		private Long depthTotal;
		private Long depthLqic;
		private Long lengthLqic;
		private String interpretation;
		public double totalClonalCells() {
			if(clones==null || clones.length <=0) return 0;
			else return Arrays.stream(clones).mapToDouble(c->c.coverage(depthLqic, bCells())).sum();
		}
		public long bCells() {
			return Math.round(depthTotal * 100.0 / depthLqic - 100);
		}
	}

	@Setter
	@Getter
	@Accessors(fluent=true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class MrdScreenCloneResult {
		private String regionV;
		private String regionJ;
		private Integer length;
		private Long depth;
		private String sequence;
		public double coverage(long depthLqic, long bCells) {
			return equivalent(depthLqic) * 100.0 / bCells;
		}
		public long equivalent(long depthLqic) {
			return Math.round(depth * 100.0 / depthLqic);
		}
	}
	@Setter
	@Getter
	@Accessors(fluent=true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class SomaticMutation {
		private String clone;
		private String hyperMutation;
		private String mutationRate;
	}
}
