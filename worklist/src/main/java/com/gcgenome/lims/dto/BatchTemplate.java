package com.gcgenome.lims.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent=true)
public class BatchTemplate extends Sheet {
	private boolean active;
}
