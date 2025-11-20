package com.greencross.lims.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(fluent=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryServerside {
	private int page;
	private int limit;
	@JsonProperty("sort_by")
	private String sortBy;
	private boolean asc;
	private List<Filter> filters;
	public List<Filter> filters() {
		if(filters == null) return List.of();
		else return filters;
	}

	@Setter
	@Getter
	@Accessors(fluent=true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Filter {
		private String key;
		private String value;
	}
}
