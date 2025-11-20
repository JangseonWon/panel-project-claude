package com.greencross.lims.report.tmp;

import com.greencross.lims.report.HasServiceCode;
import com.greencross.lims.report.builder.AbstractReportDto;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class N159Dto extends AbstractReportDto implements HasServiceCode {
	private String code;
	// Page1
	private Result result;  // POSITIVE OR NEGATIVE
	private Report report;  // 기본 보고 유전자 결과지

	public enum Result {
		P, N;
		static Result from(String str) {
			if("Positive".equalsIgnoreCase(str)) return P;
			else if("Negative".equalsIgnoreCase(str)) return N;
			else return null;
		}
	}
	@Data
	@Accessors(fluent = true)
	public static final class Report {
		private String resultText;
		private Variant[] variants;
		// 변이유전자 표 아래
		private String reference;
		private String omimDisease;
		private String abbreviation;
		private String interpretation;
	}
	@Data
	@Accessors(fluent = true)
	public static final class Variant {
		private String gene;
		private String dnaChange;
		private String predictedAa;
		private Double vaf;
		private Integer depth;
		private String cosmic;
		private String tier;
	}
}
