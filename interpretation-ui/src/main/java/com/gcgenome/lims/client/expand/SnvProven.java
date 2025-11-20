package com.gcgenome.lims.client.expand;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@Builder
public final class SnvProven<V> {
	private V variant;
	private boolean proven;
}
