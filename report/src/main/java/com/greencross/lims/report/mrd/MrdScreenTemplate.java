package com.greencross.lims.report.mrd;

import com.gcgenome.lims.test.mrd.TestInfo;
import com.greencross.lims.report.builder.AbstractReportTemplate;
import com.greencross.lims.report.builder.LogoType;

public interface MrdScreenTemplate extends AbstractReportTemplate<MrdScreenResource> {
	LogoType logoType();
	default String lblCancerType() {
		return "Cancer Type";
	}
	String lblSummary();
	default String lblGene() {
		return "Gene";
	}
	String lblTotalClone();
	default String lblClonalCellNumber() {
		return "Estimated % of\nClonal/Total " + testInfo().cell() + "s";
	}
	// default String lblMutationRate() { return "Mutation Rate : "; }
	default String lblSomaticHypermutationStatus() { return "[Somatic hypermutation status]"; }
	String lblDetails();
	String tmplCountClonesByGene();
	String tmplInterpretationPositive();
	String tmplInterpretationNegative();
	default String lblCloneTableNo() {
		return "No";
	}
	default String lblCloneTableRegionV() {
		return "V region";
	}
	default String lblCloneTableRegionJ() {
		return "J region";
	}
	default String lblCloneTableLength() {
		return "Length(bp)";
	}
	default String tmplCloneTableDepth() {
		return "Clonal %g\nRead Depth (X)";
	}
	default String lblCloneTableEquivalent() {
		return "Clonal Cell\nEquivalent*";
	}
	default String tmplCloneTableDepthPct() {
		return "Clonal/Total\n" + testInfo().cell() + "s (%)**";
	}
	default String lblEmptyResult() { return "No clone detected"; }
	default String lblInterpretation() {
		return "Interpretation";
	}
	String[] lblComments();
	default String tmplClonalSequence() {
		return "Clonal %g Sequence";
	}
	String lblTestInfo();
	String lblTestMethod();
	String lblTestQc();
	String lblTestLimitation();
	default String lblTestMethodEnrichment() {
		return "Target Enrichment Method";
	}
	default String lblTestPipeline() {
		return "Bioinformatic Pipeline";
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
}
