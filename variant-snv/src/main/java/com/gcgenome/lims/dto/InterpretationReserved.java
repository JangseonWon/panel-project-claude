package com.gcgenome.lims.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent=true)
public final class InterpretationReserved {
	private String snv;
	private String service;
	@JsonProperty("create_at")
	private Long createAt;
	@JsonProperty("create_by")
	private String createBy;
	private String interpretation;
}
