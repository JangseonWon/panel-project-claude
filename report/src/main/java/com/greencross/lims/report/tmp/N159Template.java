package com.greencross.lims.report.tmp;

import com.gcgenome.lims.test.tmp.N159;
import com.greencross.lims.report.builder.AbstractReportTemplate;

import java.awt.*;

public interface N159Template extends AbstractReportTemplate<N159Resource> {
	N159 testInfo();
	default String resultToString(N159Dto.Result result) {
		switch(result) {
			case P: return "POSITIVE";
			case N: return "NEGATIVE";
			default: return null;
		}
	}
	default Color resultToColor(N159Dto.Result result) {
		switch(result) {
			case P: return new Color(135,51,61);
			case N: return Color.decode("#0077BF");
			default: return null;
		}
	}
	default String lblTestPerformed() {
		return "TEST PERFORMED";
	}
	default String lblResult() {
		return "RESULT";
	}
	default String[] lblGene() {
		return new String[]{"Gene", "DNA", "Protein", "VAF(%)", "Depth(X)", "COSMIC ID", "Tier"};
	}
	default String lblNoDisease() {
		return "No disease-related variant";
	}
	default String lblReference() {
		return "Reference sequence";
	}
	default String lblInterpretation() {
		return "INTERPRETATION";
	}
	default String lblLimitation() {
		return "LIMITATION";
	}
	String[] limitations();
}
