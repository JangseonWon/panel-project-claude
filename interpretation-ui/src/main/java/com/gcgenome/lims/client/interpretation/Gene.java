package com.gcgenome.lims.client.interpretation;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@Builder
public final class Gene {
	private final String symbol;
	private final String abbr;
	private final String disease;
	private final String inheritance;
}
