package com.greencross.lims.report.panels;

import com.gcgenome.lims.test.panel.TestInfo;
import com.greencross.lims.report.builder.AbstractReportTemplate;
import com.greencross.lims.report.builder.LogoType;

import java.awt.*;

public interface PanelTemplate extends AbstractReportTemplate<PanelResource> {
	LogoType logoType();
	TestInfo testInfo();
	default String resultToString(PanelDto.Result result) {
		switch(result) {
			case P: return "POSITIVE";
			case N: return "NEGATIVE";
			case I: return "INCONCLUSIVE";
			default: return null;
		}
	}
	default Color resultToColor(PanelDto.Result result) {
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
	default String lblAddendumResult() {
		return "ADDENDUM RESULT";
	}
	default String lblMethods() {
		return "METHODS";
	}
	default String[] lblMethodList() {
		return new String[]{"Test Panel", "Target region", "Target enrichment method", "Massively parallel sequencing", "Reference genome", "Bioinformatic pipeline"};
	}
	default String lblCoverage() { return "COVERAGE"; }
	default String lblMeanDepth() { return "Mean depth of coverage"; }
	default String lblX10Coverage() { return "% of > 10x"; }
	default String lblLimitation() {
		return "LIMITATION";
	}
	default String lblGeneList() {
		return "GENE LIST";
	}
	default String lblEssentialGene() {
		return "Essential Gene";
	}
	default String lblAdditionalGene() {
		return "Additional Gene";
	}
	String lblNoteAdditionalGene();
	String lblLimitations();
}
