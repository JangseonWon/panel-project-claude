package com.greencross.lims.report.mrd;

import com.gcgenome.lims.test.mrd.TestInfo;
import com.greencross.lims.report.builder.AbstractReportTemplate;
import com.greencross.lims.report.builder.LogoType;

public interface MrdTemplate extends AbstractReportTemplate<MrdResource> {
	LogoType logoType();
	default String lblCancerType() {
		return "Cancer Type";
	}
	String lblSummary();
	default String lblGene() {
		return "Gene";
	}
	String lblResultDetected();
	default String lblMutationRate() { return "Mutation Rate : "; }
	default String lblClonalCellNumberTotalBCells() {
		return "Estimated % of\nClonal/Total " + testInfo().cell() + "s";
	}
	default String lblClonalCellNumberTotalNucleatedCells() {
		return "Estimated % of\nClonal/Total Nucleated Cells";
	}
	String lblDetails();
	default String tmplMrdHistoryByGene() {
		return "%g MRD History";
	}
	String tmplInterpretation(MrdDto.Result result);
	default String lblHistoryTableNo() {
		return "f/u No";
	}
	default String lblHistoryTableDate() {
		return "Date\nRequest";
	}
	default String tmplHistoryTableTotalReadDepth() {
		return "Total %g\nRead Depth (X)";
	}
	default String tmplHistoryTableClonalReadDepth() {
		return "Clonal %g\nRead Depth (X)";
	}
	default String lblCloneTableEquivalent() {
		return "Clonal Cell\nEquivalent*";
	}
	default String lblHistoryTableDepthGenePct() {
		return "Clonal/Total\n" + testInfo().cell() + "s (%)**";
	}
	default String lblHistoryTableDepthNucelatedPct() {
		return "Clonal/Total\nNucleated\nCells (%)***";
	}
	default String lblInterpretation() {
		return "Interpretation";
	}
	String[] lblComments();
	String lblTestInfo();
	String lblTestMethod();
	String lblTestQc();
	String lblTestLimitation();
	default String lblTestMethodEnrichment() {
		return "Target enrichment method";
	}
	default String lblTestPipeline() {
		return "Bioinformatic pipeline";
	}
	default String lblTestPanel() {
		return "Tested Panel";
	}
	default String lblTestSequencing() {
		return "Massively Parallel Sequencing";
	}
	default String lblTestReference() {
		return "Reference Genome";
	}
	default String lblInputDna() {
		return "Input DNA(ng)";
	}
	default String tmplTotalDepth() {
		return "Total %g Read Depth(X)";
	}
	default String lblTotalNucleatedCells() {
		return "Estimated Total Nucleated Cells\n(cell equivalents)*";
	}
	default String tmplTotalBCell() {
		return "Estimated %g\nTotal " + testInfo().cell() + " Count\n(cell equivalents)**";
	}
	String[] lblQcComments();
	String[] lblLimitations();
	TestInfo testInfo();
	default String toString(MrdDto.Result result) {
		if(result == null) return "-";
		switch(result) {
			case DETECTED:		return "Detected";
			case NOT_DETECTED:	return "Not Detected";
			case NA:			return "N/A";
			default: return "-";
		}
	}
}
