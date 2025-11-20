package com.greencross.lims.report.genomescreen;

import com.greencross.lims.report.HasServiceCode;
import com.greencross.lims.report.builder.AbstractReportDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Accessors(fluent = true)
public class GenomeScreenDto extends AbstractReportDto implements HasServiceCode {
	private String code;
	private String summary;
	private String interpretation;
	private Map<GenomeScreenTemplate.Gene, List<Variant>> variants;
	private Map<GenomeScreenTemplate.DiseaseSub, Map<GenomeScreenTemplate.Gene, Boolean>> diseases;
	@Data
	@Accessors(fluent = true)
	public static final class Variant {
		private String gene;
		private String dnaChange;
		private String predictedAa;
		private String zygosity;
		private String clazz;
	}
}
