package com.gcgenome.lims.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent=true)
public final class SnvConsensualClass {
	private String snv;
	@JsonProperty("create_at")
	private Long createAt;
	@JsonProperty("create_by")
	private String createBy;
	@JsonProperty("last_modify_at")
	private Long lastModifyAt;
	@JsonProperty("last_modify_by")
	private String lastModifyBy;
	@JsonProperty("class")
	private String clazz;
	private String comment;
}
