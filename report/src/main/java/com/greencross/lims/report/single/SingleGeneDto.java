package com.greencross.lims.report.single;

import com.greencross.lims.report.HasServiceCode;
import com.greencross.lims.report.builder.AbstractReportDto;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class SingleGeneDto extends AbstractReportDto implements HasServiceCode {
	private String code;
	private String reasonFR; // reason for referral
	private Result result; // POSITIVE OR NEGATIVE
	private Report report; // 기본 보고 유전자 결과지
	private Report addendum; // Addendum 결과지(없으면 NULL)
	private Mlpa mlpaResult;

	public enum Result {
		P, N, I;
		static Result from(String str) {
			if("Positive".equalsIgnoreCase(str)) return P;
			else if("Negative".equalsIgnoreCase(str)) return N;
			else if("INCONCLUSIVE".equalsIgnoreCase(str)) return I;
			else return null;
		}
		String toFullText() {
			if(this.equals(P)) return "POSITIVE";
			else if(this.equals(N)) return "NEGATIVE";
			else if(this.equals(I)) return "INCONCLUSIVE";
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
	public static final class Variant implements Comparable<Variant> {
		private String gene;
		private String dnaChange;
		private String predictedAa;
		private String zygosity;
		private String omim;
		private String inherit;
		private String clazz;

		@Override
		public int compareTo(Variant other) {
			return Integer.compare(getPriority(this.clazz), getPriority(other.clazz));
		}
		private int getPriority(String clazz) {
			switch (clazz) {
				case "PV":
					return 1;
				case "LPV":
					return 2;
				case "VUS":
					return 3;
				default:
					return Integer.MAX_VALUE;
			}
		}
	}
	@Data
	@Accessors(fluent = true)
	public static final class Mlpa {
		private String result;
		private String exons;
		private String zygosity;
		private String delDup;
	}
}