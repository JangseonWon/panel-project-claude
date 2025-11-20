package com.greencross.lims.dto.interpretation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Sanger {
	private String interpretation;
	private Variant[] variants;
	private String inspector;
	private String reporter;
	private String reviewer;

	@Setter
	@Getter
	@Accessors(fluent=true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class Variant {
		private String gene;
		private String hgvsc;
		private String hgvsp;
		private String zygosity;
		private String result;
		@JsonProperty("class")
		private String clazz;
		private String sanger;
	}
}
