package com.greencross.lims.report.wes;

import com.greencross.lims.report.HasServiceCode;
import com.greencross.lims.report.builder.AbstractReportDto;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Data
@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
public class WesDto extends AbstractReportDto implements HasServiceCode {
	private String code;
	// Page1
	private String reasonFR;    // reason for referral
	private Result result;  // POSITIVE OR NEGATIVE
	private Report report;  // 기본 보고 유전자 결과지
	private Report addendum;// Addendum 결과지(없으면 NULL)
	private String relation; //해외용 relation 데이터

	// Page2
	private String meanDepth;
	private String x10Coverage;

	private boolean consentIncidentalFindings;
	private String inspector;
	private String reporter;
	private String reviewer;
	public enum Result {
		P, N, I;
		static Result from(String str) {
			if("Positive".equalsIgnoreCase(str)) return P;
			else if("Negative".equalsIgnoreCase(str)) return N;
			else if("INCONCLUSIVE".equalsIgnoreCase(str)) return I;
			else return null;
		}
	}
	@Data
	@Accessors(fluent = true)
	@SuperBuilder(toBuilder = true)
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
		private String zygosity;
		private String omim;
		private String inherit;
		private String clazz;
	}
}
