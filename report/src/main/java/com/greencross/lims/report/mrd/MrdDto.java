package com.greencross.lims.report.mrd;

import com.greencross.lims.report.HasServiceCode;
import com.greencross.lims.report.builder.AbstractReportDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Accessors(fluent = true)
public class MrdDto extends AbstractReportDto implements HasServiceCode {
	private String code;
	private String cancerType;
	private String mutationRate;
	private MrdHistory[] histories;
	public MrdHistory last() {
		List<MrdHistory> sorted = Arrays.stream(histories).sorted(Comparator.comparing(MrdHistory::date)).collect(Collectors.toList());
		return sorted.get(sorted.size()-1);
	}
	@Data
	@Accessors(fluent = true)
	public static class MrdHistory {
		private String no;
		private LocalDate date;
		private int inputDna;
		private Map<String, MrdDtoGeneResult> results;

		public long nucleatedCells() {
			return Math.round(inputDna/6.5 * 1000);
		}
		public double pctClonalNucelatedCells(String gene) {
			return results.get(gene).equivalent() / (double) nucleatedCells();
		}
	}
	@Data
	@Accessors(fluent = true)
	public static class MrdDtoGeneResult {
		private String gene;
		private MrdDtoCloneResult target;
		private MrdDtoCloneResult lqic;
		private Result result;
		private String interpretation;
		public long bCells() {
			return Math.round(target.readDepth() * 100.0 / lqic.readDepth() - 100);
		}
		public long equivalent() {
			return Math.round(target.clonalDepth() * 100.0 / lqic.readDepth());
		}
		public float pctClonalBCells() {
			return equivalent() / (float) bCells();
		}
	}
	@Data
	@Accessors(fluent = true)
	public static class MrdDtoCloneResult {
		private long readDepth;
		private long clonalDepth;
		// private long equivalent;
	}
	public enum Result {
		DETECTED, NOT_DETECTED, NA, CUSTOM
	}
}
