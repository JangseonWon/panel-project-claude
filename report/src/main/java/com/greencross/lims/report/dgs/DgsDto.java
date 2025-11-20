package com.greencross.lims.report.dgs;

import com.greencross.lims.report.HasServiceCode;
import com.greencross.lims.report.builder.AbstractReportDto;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class DgsDto extends AbstractReportDto implements HasServiceCode {
	private String code;
	// Page1
	private String reasonFR;    // reason for referral
	private Result result;  // POSITIVE OR NEGATIVE
	private Report report;  // 기본 보고 유전자 결과지
	private String recommendation;
	//private String incidentalFindings;// Addendum 결과지(없으면 NULL)
	private boolean consentIncidentalFindings = false;
	private Report incidentalFindings;

	// Page2
	private String meanDepth;
	private String x10Coverage;
	private String inspector;
	private String reporter;
	private String reviewer;
	private String relation; //해외용 relation 데이터
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
