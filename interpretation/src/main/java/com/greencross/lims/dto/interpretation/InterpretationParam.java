package com.greencross.lims.dto.interpretation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@Accessors(fluent=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InterpretationParam {
	@JsonProperty("previous")
	private Map<?, ?> json;
	@JsonProperty("disease")
	private Map<String, List<InterpretationParamDisease>> disease;
	@JsonProperty("suffix")
	private String suffix;

	@Setter
	@Getter
	@Accessors(fluent=true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class InterpretationParamDisease {
		@JsonProperty("full_name")
		private String fullName;
		private String abbreviation;
		private List<String> inheritance;
	}
}
