package com.greencross.lims.entity;

import lombok.Value;
import lombok.With;
import lombok.experimental.Accessors;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.util.UUID;

@Value
@Accessors(fluent = true)
public class WorklistPK implements Serializable {
	@With
	private UUID sheet;
	@With
	@Column("worklist")
	private Integer worklistId;
}
