package com.greencross.lims.report.wes;

import com.gcgenome.lims.test.wes.TestInfo;
import com.gcgenome.lims.test.wes.TestWithSingleInfo;
import com.greencross.lims.report.Revisionable;
import com.greencross.lims.report.builder.AbstractReportTemplate;

import java.awt.*;

public interface WesTemplate extends AbstractReportTemplate<WesResource>, Revisionable {
	TestInfo testInfo();
	default String resultToString(WesDto.Result result) {
		switch(result) {
			case P: return "POSITIVE";
			case N: return "NEGATIVE";
			case I: return "INCONCLUSIVE";
			default: return null;
		}
	}
	default Color resultToColor(WesDto.Result result) {
		switch(result) {
			case P: return new Color(135,51,61);
			case N: return Color.decode("#0077BF");
			case I: return Color.decode("#548235");
			default: return null;
		}
	}
	default String lblTestPerformed() {
		return "TEST PERFORMED";
	}
	default String lblReasonForRR() {
		return "REASON FOR REFERRAL";
	}
	default String lblResult() {
		return "RESULT";
	}
	default String[] lblGene() {
		return new String[]{"Gene", "DNA change", "Predicted\nAA change", "Zygosity", "OMIM Disease", "Inherit", "Class"};
	}
	default String lblNoDisease() {
		return "No disease-related variant";
	}
	default String lblReference() {
		return "Reference sequence";
	}
	default String lblOMIM() {
		return "OMIM disease";
	}
	default String lblAbbreviation(){
		return "Abbreviation";
	}
	default String lblInterpretation() {
		return "INTERPRETATION";
	}
	default String lblIncidentalFindingResult() {
		return "INCIDENTAL FINDINGS";
	}
	default String lblIncidentalFindingInfo() { return String.format(
			"* Investigation of %d genes recommended by ACMG SF v3.3 (Genet Med. 2025.)",
			TestWithSingleInfo.GENE_INCIDENTAL_FINDINGS.length);
	}
	default String lblMethods() {
		return "METHODS";
	}
	default String lblAnalysisStatistic() { return "ANALYSIS STATISTIC"; }
	default String lblMeanDepth() { return "Mean depth of coverage"; }
	default String lblX10Coverage() { return "% of > 10x"; }
	default String lblLimitation() {
		return "LIMITATIONS";
	}
}
