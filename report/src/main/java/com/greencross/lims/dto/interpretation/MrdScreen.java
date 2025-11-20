package com.greencross.lims.dto.interpretation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MrdScreen {
	private String cancerType;
	// private String mutationRate;
	private SomaticMutation[] somaticMutations;
	private Integer inputDna;
	private MrdScreenGeneResult[] results;

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
