package com.greencross.lims.report.geneplus;

import com.gcgenome.lims.test.geneplus.TestInfo;
import com.greencross.lims.report.builder.AbstractReportTemplate;
import com.greencross.lims.report.builder.LogoType;

import java.awt.*;

public interface GenePlusTemplate extends AbstractReportTemplate<GenePlusResource> {
	LogoType logoType();
	TestInfo testInfo();
	default String resultToString(GenePlusDto.Result result) {
		switch(result) {
			case P: return "POSITIVE";
			case N: return "NEGATIVE";
			case I: return "INCONCLUSIVE";
			default: return null;
		}
	}
	default Color resultToColor(GenePlusDto.Result result) {
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
	default String lblTestInformation() {
		return "TEST INFORMATION";
	}
	default String[] lblTestInformationList() {
		return new String[]{"Specimen", "Analysed Gene", "Methods", "Penetrance"};
	}
	default String lblRemark() {
		return "REMARK";
	}
	default String lblReferences() {
		return "REFERENCES";
	}
}
