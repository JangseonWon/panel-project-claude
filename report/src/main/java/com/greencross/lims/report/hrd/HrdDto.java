package com.greencross.lims.report.hrd;

import com.greencross.lims.report.HasServiceCode;
import com.greencross.lims.report.builder.AbstractReportDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(fluent = true)
public class HrdDto extends AbstractReportDto implements HasServiceCode {
	private String code;
	private String cancerType;
	private Result gi;   // 유전체 불안정성
	private Result brcaResult; // BRCA 결과 요약
	private String snv;
	private String cnv;
	private Integer giScore;
	private String interpretation;
	private String relation; //해외용 relation 데이터
	private Map<String, HrdDtoGeneResult> details = new HashMap<>();
	public enum Result {
		P, N, F;
		public static Result fromInterpretation(String value) {
			switch (value.toLowerCase()) {
				case "positive": return P;
				case "negative": return N;
				default: return F;
			}
		}
	}
	@Data
	@Accessors(fluent = true)
	public static class HrdDtoGeneResult {
		private Result result;
		private HrdVariantResult[] variants;
		private String interpretation;
	}
	@Data
	@Accessors(fluent = true)
	public static class HrdVariantResult {
		private String gene;
		private String hgvsc;
		private String hgvsp;
		private Double vaf;
		private Integer depth;
		private String cosmic;
		private String clazz;
		private String dna;
		private String protein;
	}
}
