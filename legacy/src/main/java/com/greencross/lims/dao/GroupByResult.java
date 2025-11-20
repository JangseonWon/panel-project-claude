package com.greencross.lims.dao;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class GroupByResult {
	private String key;
	private long count;

	@Builder
	private GroupByResult(Object key, long count) {
		if(key instanceof String) this.key = (String)key;
		else if(key!=null) this.key = String.valueOf(key);
		else this.key = "N/A";
		this.count = count;
	}
}
