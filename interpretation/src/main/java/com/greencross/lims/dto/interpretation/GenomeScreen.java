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
public class GenomeScreen {
	private String summary;
	private String interpretation;
	private Variant[] variants;
	private Disease[] diseases;
	@JsonProperty("mean_depth")
	private String meanDepth;
	private String coverage;
	public static final class Variant extends BaseVariant<Variant> {}
	@Setter
	@Getter
	@Accessors(fluent=true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class Disease {
		private String name;
		private Gene[] values;
	}
	@Setter
	@Getter
	@Accessors(fluent=true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class Gene {
		private String name;
		private Boolean value;
	}
}