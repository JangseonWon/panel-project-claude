package com.greencross.lims.report.bloodcancer;

import com.gcgenome.lims.test.bloodcancer.TestInfo;
import com.greencross.lims.report.builder.AbstractReportTemplate;

public interface BloodCancerTemplate extends AbstractReportTemplate<BloodCancerResource> {
	default String lblCancerType() {
		return "Cancer Type";
	}
	String lblSummary();
	default String lblTier(BloodCancerDto.Tier tier) {
		switch(tier) {
			case Tier1: return "Tier 1";
			case Tier2: return "Tier 2";
			case Tier3: return "Tier 3";
			case Tier4: return "Tier 4";
		}
		return null;
	}
	default String lblEvidenceLevel(BloodCancerDto.EvidenceLevel evidenceLevel) {
		switch(evidenceLevel) {
			case LevelA: return "Level A";
			case LevelB: return "Level B";
			case LevelC: return "Level C";
			case LevelD: return "Level D";
		}
		return null;
	}
	default String[] lblReferences() {
		return new String[] {
			"COSMIC(http://cancer.sanger.ac.uk)",
			"c-bioportal(http://www.cbioportal.org)",
			"Cancer Hotspots(http://cancerhotspots.org)",
			"OncoKB(http://oncokb.org)",
			"My Cancer Genome(https://www.mycancergenome.org/)",
			"The Clinical Knowledgebase(https://ckb.jax.org/)",
			"WHO classification of tumours of haematopoietic and lymphoid tissues(revised 4th edition)",
			"NCCN guidelines®"
		};
	}
	default String lblVariant() {
		return "Variant";
	}
	default String lblGene() {
		return "Gene";
	}
	String lblDetails();
	default String lblTierSignificance(BloodCancerDto.Tier tier) {
		switch(tier) {
			case Tier1: return "Variants of Strong Clinical Significance";
			case Tier2: return "Variants of Potential Clinical Significance";
			case Tier3: return "Variants of Unknown Clinical Significance";
		}
		return null;
	}
	default String[] lblVariantTable() {
		return new String[]{"No", "Gene", "DNA", "Protein", "VAF(%)", "Depth(X)", "COSMIC ID"};
	}
	default String lblInterpretation() {
		return "INTERPRETATION";
	}
	default String lblNoVariant() {
		return "No variant";
	}
	String lblTestInfo();
	String lblTestMethod();
	String lblTestQc();
	String lblTestLimitation();
	String lblTestGeneInfo();
	String[] lblTestLimitations();
	default String lblTestRegion() {
		return "Target Region";
	}
	default String lblTestPanel() {
		return "Tested Panel";
	}
	default String lblTestMethodEnrichment() {
		return "Target Enrichment Method";
	}
	default String lblTestSequencing() {
		return "Massively Parallel Sequencing";
	}
	default String lblTestPipeline() {
		return "Bioinformatic Pipeline";
	}
	default String lblTestReference() {
		return "Reference Genome";
	}

	default String lblQcSample() {
		return "Sample(DNA) QC";
	}
	default String lblQcLibrary() {
		return "Library QC";
	}
	default String lblQcSequencing() {
		return "Sequencing QC";
	}
	default String lblQcMeanDepth() {
		return "Mean Coverage of Depth(X)";
	}
	default String lblQcCoverage() {
		return "% of Target Bases ≥ 250X";
	}
	String lblTestVariantCategorizations();
	String lblTestVariantCategorizationDetails();
	String lblTierSignificanceCategory(BloodCancerDto.Tier tier);
	String lblTierSignificanceDetails(BloodCancerDto.Tier tier);

	default String lblEvidenceLevel() {
		return "EVIDENCE LEVEL";
	}
	String lblEvidenceLevelDetails(BloodCancerDto.EvidenceLevel evidenceLevel);
	default String lblReference() {
		return "REFERENCES";
	}
	default String[] lblGeneInfoHeaders() {
		return new String[] {"Gene", "Target Exon No.", "Reference\nTranscript"};
	}
	String lblGeneEssential();
	String lblGeneSelective();
	TestInfo testInfo();
}
