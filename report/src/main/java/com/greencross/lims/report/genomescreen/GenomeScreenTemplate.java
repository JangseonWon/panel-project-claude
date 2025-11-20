package com.greencross.lims.report.genomescreen;

import com.gcgenome.lims.report.TextBlock;
import com.greencross.lims.report.builder.AbstractReportTemplate;

public interface GenomeScreenTemplate<R extends GenomeScreenResource> extends AbstractReportTemplate<R> {
	interface Disease {
		DiseaseSub[] subs();
	}
	interface DiseaseSub {
		Gene[] genes();
	}
	interface Gene {
		String name();
		String transcript();
		Tier tier();
	}
	enum Tier {
		Tier1, Tier2, Tier3
	}
	String lblSummaryTestIntroTitle();
	String lblSummaryTestIntroTitleInfo();
	String lblSummaryInfo();
	String lblSummaryTitle();
	String fmtMyResult(GenomeScreenDto dto);
	String[] lblVariantTable();
	String lblPositiveInterpretationTitle();
	String lblPositiveDiseaseInfoTitle();
	String lblPositiveDisease();
	String lblPositiveDiseaseInfo();
	String lblDetailsTitle();
	String lblDetailsDiseaseName();
	String lblDetailsGene();
	String lblDetailsPathogenicVariant();
	String lblDetailsPositive();
	String lblDetailsNegative();
	Disease[] diseases();
	String diseaseName(Disease disease);
	String diseaseNameScientific(Disease disease);
	String diseaseInfo(Disease disease);
	String diseaseSubName(DiseaseSub diseaseSub);
	String diseaseSubNameScientific(DiseaseSub diseaseSub);
	String diseaseSubReference(DiseaseSub disease);
	String diseaseSubInfo(DiseaseSub disease);

	String lblGuideTestTitle();
	TextBlock[] guideTest();
	String lblGuideDiseaseTitle();
	TextBlock[] guideDisease();
	String lblLimitationTitle();
	TextBlock[] limitations();
	String lblTestInfoTitle();
	String lblTestInfoSpecimen();
	String testInfoSpecimen();
	String lblTestInfoMethod();
	String testInfoMethod();
	String lblTestInfoNgs();
	String testInfoNgs();
	String lblSangerIgnored();
	String lblReferenceTranscriptTitle();
	String lblReferenceTitle();
	TextBlock[] references();
	String lblGeneListTitle();
	String lblGeneListInfo();
	String lblClinicalMeanings();
	String lblClinicalMeanings(Tier tier);
	String lblClinicalMeaningInfo();
	String clinicalReference(Gene gene);
	String clinicalMeaning(Gene gene);
}
