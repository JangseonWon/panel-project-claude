package com.greencross.lims.report.bloodcancer;

import com.greencross.lims.report.HasServiceCode;
import com.greencross.lims.report.builder.AbstractReportDto;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class BloodCancerDto extends AbstractReportDto implements HasServiceCode {
	private String code;
	private String cancerType;
	private Result[] results;
	private String qcDna;
	private String qcLibrary;
	private String qcSequencing;
	private String meanDepth;
	private String coverage;
	private DrugPhenotype[] drugPhenotypes;
	private String relation; //해외용 relation 데이터

	@Data
	@Accessors(fluent = true)
	public static class Result {
		private Tier tier;
		private Variant[] variants;
		private String interpretation;
	}
	@Data
	@Accessors(fluent = true)
	public static class Variant {
		private String snv;
		private String analysis;
		private String gene;
		private String hgvsc;
		private String hgvsp;
		private Double vaf;
		private Integer depth;
		private String cosmic;
		public Variant vaf(double vaf) {
			this.vaf = vaf;
			return this;
		}
	}
	@Data
	@Accessors(fluent = true)
	public static class DrugPhenotype {
		private String gene;
		private String diplotype;
		private String alleleStatus;
		private String phenotype;
		private String result;
		private String interpretation;
	}
	public enum Tier {
		Tier1, Tier2, Tier3, Tier4
	}
	public enum EvidenceLevel {
		LevelA, LevelB, LevelC, LevelD
	}
}
