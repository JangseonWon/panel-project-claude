package com.greencross.lims.report.genomescreen.kokr;

import com.greencross.lims.report.genomescreen.RiskScreenResource;
import com.greencross.lims.report.genomescreen.RiskScreenTemplate;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public interface RiskScreenTemplateKoKr<R extends RiskScreenResource> extends RiskScreenTemplate<R> {
	default String diseaseSubRiskPositive() {
		return "주의";
	}
	default String diseaseSubRiskNegative() {
		return "표준";
	}
	default String lblLifestyleDisease() {
		return "질환";
	}
	NumberFormat fmtPrecision = new DecimalFormat("0");
	default String risk(double coefficient) {
		fmtPrecision.setMaximumFractionDigits(2);
		if(coefficient <= 1) return "표준";
		else return "약 " + fmtPrecision.format(coefficient) + "배 증가";
	}
	default String lblGuideDietTitle() {
		return "식이요법의 필요성";
	}
	default String lblGuideDietGood() {
		return "좋아요";
	}
	default String lblGuideDietBad() {
		return "나빠요";
	}
	default String lblGuideAthleticTitle() {
		return "운동 가이드라인";
	}
	default String[] lblAthletics() {
		return new String[] {
			"걷기", "뛰기", "스트레칭", "사이클", "수영"
		};
	}
}
