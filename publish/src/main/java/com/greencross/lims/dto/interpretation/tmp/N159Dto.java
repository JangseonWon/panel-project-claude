package com.greencross.lims.dto.interpretation.tmp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class N159Dto {
	private String result;
	@JsonProperty("result_text")
	private String resultText;
	@JsonProperty("abbreviation_reference")
	private String abbreviationReference;
	private String interpretation;
	private Variant[] variants;

	@Setter
	@Getter
	@Accessors(fluent=true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class Variant {
		private String snv;
		private String analysis;
		private String gene;
		private String hgvsc;
		private String hgvsp;
		private Integer vaf;
		private Double depth;
		private String cosmic;
		private Tier tier;
	}

	public enum Tier {
		Tier12("Tier 1/2"), Tier3("Tier 3"), Tier4("Tier 4");
		private String displayName;
		Tier(String displayName) {
			this.displayName = displayName;
		}
		@Override
		public String toString() {
			return displayName;
		}
	}
}