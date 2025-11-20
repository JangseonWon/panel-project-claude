package com.greencross.lims.report.genomescreen;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(fluent = true)
public class GenomeScreenWithRiskScreenDto extends GenomeScreenDto {
	private Map<RiskScreenTemplate.Snv, String> variantsRiskScreen;
}
