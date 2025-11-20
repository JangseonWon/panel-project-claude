package com.greencross.lims.publish;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Builder
@Data
@Accessors(fluent = true)
public final class VariantReference {
	private String snv;
	private String clazz;
}
