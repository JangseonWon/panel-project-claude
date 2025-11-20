package com.greencross.lims.report.mrd;

import com.greencross.lims.report.HasServiceCode;
import com.greencross.lims.report.builder.AbstractReportDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Data
@Accessors(fluent = true)
public class MrdScreenDto extends AbstractReportDto implements HasServiceCode {
	private String code;
	private String cancerType;
	// private String mutationRate;
	private List<SomaticMutation> somaticMutations;
	private Map<String, MrdScreenDtoGeneResult> results;
	private int inputDna;
	// private long nucleatedCells;		// nCells = inputDNA / 6.5 * 1000
	public long nucleatedCells() {
		return Math.round(inputDna/6.5 * 1000);
	}
	@Data
	@Accessors(fluent = true)
	public static class MrdScreenDtoGeneResult {
		private String gene;
		private MrdScreenDtoCloneResult lqic;
		@Getter(AccessLevel.NONE)
		private MrdScreenDtoCloneResult[] clones = new MrdScreenDtoCloneResult[0];
		private long depth;				// Total Depth(DO NOT SUM)
		// private long bCells;			// bCells = depth / lqic.depth * 100 - 100
		private String interpretation;
		public double totalClonalCells() {
			if(clones==null || clones.length <=0) return 0;
			else return Arrays.stream(clones).mapToDouble(c->c.coverage(lqic.depth(), bCells())).sum();
		}
		public long bCells() {
			return Math.round(depth * 100.0 / lqic.depth - 100);
		}
		public MrdScreenDtoGeneResult clones(MrdScreenDtoCloneResult... clones) {
			this.clones = clones;
			return this;
		}
		public MrdScreenDtoCloneResult[] clones() {
			return clones;
		}
	}
	@Data
	@Accessors(fluent = true)
	public static class MrdScreenDtoCloneResult {
		private String no;
		private String regionV;
		private String regionJ;
		private int length;
		private long depth;
		private String sequence;
		public double coverage(long depthLqic, long bCells) {
			return equivalent(depthLqic) * 100.0 / bCells;
		}
		public long equivalent(long depthLqic) {
			return Math.round(depth * 100.0 / depthLqic);
		}
	}
	@Data
	@Accessors(fluent = true)
	public static final class SomaticMutation {
		private String clone;
		private String hyperMutation;
		private String mutationRate;
	}
}
