package com.greencross.lims.report.hrd;

import com.gcgenome.lims.test.hrd.TestInfo;
import com.greencross.lims.report.builder.AbstractReportTemplate;
import com.greencross.lims.report.builder.LogoType;

public interface HrdTemplate extends AbstractReportTemplate<HrdResource> {
	LogoType logoType();
	default String lblCancerType() {
		return "Cancer Type";
	}
	String lblSummary();
	String lblHrd();
	String lblGi();
	String lblBrca();
	String lblQc();
	String lblDetails();
	default String lblQcSnv() {
		return "DNA(SNV)";
	}
	default String lblQcCnv() {
		return "DNA(CNV)";
	}
	String lblGiScore();
	String lblLohTitle();
	String lblTaiTitle();
	String lblLstTitle();
	String lblLoh();
	String lblTai();
	String lblLst();
	String tmplResultByTier(String tier);
	default String lblVariantTableNo() {
		return "No";
	}
	default String lblVariantTableGene() {
		return "Gene";
	}
	default String lblVariantTableDNA() {
		return "DNA";
	}
	default String lblVariantTableProtein() {
		return "Protein";
	}
	default String lblVariantTableVaf() {
		return "VAF(%)";
	}
	default String lblVariantTableDepth() {
		return "Depth(X)";
	}
	default String lblVariantTableCosmic() {
		return "COSMIC ID";
	}
	// default String lblVariantTableClass() { return "Class"; }
	default String lblInterpretation() { return "INTERPRETATION"; }
	String lblTestInfoTitle();
	String lblTestInfo();
	String lblHrdInfoTitle();
	String lblHrdInfo();
	String lblParpInfoTitle();
	String lblParpInfo();
	String lblTestLimitation();
	String[] lblLimitations();
	String lblTestReferences();
	String[] lblReferences();
	default String lblReference() {
		return "REFERENCES";
	}
	String lblTestGeneInfo();
	default String[] lblGeneInfoHeaders() {
		return new String[] {"Gene", "Target", "Reference\nTranscript"};
	}
	String lblGeneEssential();
	String lblGeneAdditional();
	TestInfo testInfo();
	String resultToString(HrdDto.Result result);
}
