package com.gcgenome.lims.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(fluent=true)
public class WorklistTemplate extends Sheet {
	private boolean active;
	private List<ColumnDefinition> values;
}
