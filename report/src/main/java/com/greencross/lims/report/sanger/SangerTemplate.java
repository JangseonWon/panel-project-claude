package com.greencross.lims.report.sanger;

import com.gcgenome.lims.test.sanger.TestInfo;
import com.greencross.lims.report.builder.AbstractReportTemplate;

import java.awt.*;

public interface SangerTemplate extends AbstractReportTemplate<SangerResource> {
	TestInfo testInfo();
	default Color resultToColor(SangerDto.Result result) {
		switch(result) {
			case DETECTED: return new Color(135,51,61);
			case NOT_DETECTED, HYPHEN: return Color.decode("#0077BF");
			default: return null;
		}
	}
	default String lblResult() {
		return "RESULT";
	}
	default String[] lblGene() {
		return new String[]{"Gene", "DNA change", "Predicted\nAA change", "Zygosity", "Class", "Result"};
	}
	default String lblInterpretation() {
		return "INTERPRETATION";
	}
	default String lblMethods() {
		return "TEST INFORMATION";
	}
	default String[] lblMethodList() {
		return new String[]{"Specimen", "Methods"};
	}
	default String lblLimitation() {
		return "LIMITATION";
	}
	default String lblReferences() {
		return "REFERENCES";
	}
}
