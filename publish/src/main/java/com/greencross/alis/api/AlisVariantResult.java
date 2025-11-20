package com.greencross.alis.api;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(fluent = true)
public class AlisVariantResult {
	private final int row;
	private final String key;
	private final String value;
	public String value() {
		if(value == null) return "";
		else return value;
	}
}
