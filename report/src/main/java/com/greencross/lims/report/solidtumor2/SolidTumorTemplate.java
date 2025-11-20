package com.greencross.lims.report.solidtumor2;

import com.greencross.lims.report.builder.AbstractReportTemplate;
import com.greencross.lims.report.builder.LogoType;
import com.gcgenome.lims.test.solidtumor2.TestInfo;

public interface SolidTumorTemplate extends AbstractReportTemplate<SolidTumorResource> {
    LogoType logoType();
    default String lblCancerCategory() {
        return "Cancer tissue";
    }
    default String lblCancerType() {
        return "Cancer type";
    }
    String lblSummary();
    String lblReferT3NextPage();
    default String lblTier(SolidTumorDto.Tier tier) {
        return switch (tier) {
            case Tier1 -> "Tier 1";
            case Tier2 -> "Tier 2";
            case Tier3 -> "Tier 3";
            case Tier4 -> "Tier 4";
        };
    }
    default String lblEvidenceLevel(SolidTumorDto.EvidenceLevel evidenceLevel) {
        return switch (evidenceLevel) {
            case LevelA -> "Level A";
            case LevelB -> "Level B";
            case LevelC -> "Level C";
            case LevelD -> "Level D";
        };
    }
    String etc();
    default String[] lblReferences() {
        return new String[] {
                "COSMIC(http://cancer.sanger.ac.uk)",
                "c-bioportal(http://www.cbioportal.org)",
                "Cancer Hotspots(http://cancerhotspots.org)",
                "My Cancer Genome(https://www.mycancergenome.org/)",
                "The Clinical Knowledgebase(https://ckb.jax.org/)",
                "WHO classification of tumours of haematopoietic and lymphoid tissues(revised 4th edition)",
                "NCCN guidelines®"
        };
    }
    default String lblGene() {
        return "Gene";
    }
    String lblQcSummary();
    String lblDetailsResult();
    String lblDetailsVariant();
    default String[] lblVariantTable() {
        return new String[]{"Tier", "Gene", "DNA", "Protein", "VAF(%)", "Depth(X)", "Clinical Significance" };
    }
    default String lblInterpretation() {
        return "INTERPRETATION";
    }
    default String lblNoVariant() {
        return "No variant";
    }
    String lblTestInfo();
    default String lblTestMethod() { return "Test Method"; }
    String lblTestQc();
    String lblTestLimitation();
    default String lblTestGeneInfo() { return "Gene List";}
    default String lblTestRegion() {
        return "Target Region";
    }
    default String lblTestPanel() {
        return "Tested Panel / Size";
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
    default String lblTestVariantCategorizations() { return "Classification"; }
    String lblTestVariantCategorizationDetails();
    String lblTierSignificanceCategory(SolidTumorDto.Tier tier);
    String lblTierSignificanceDetails(SolidTumorDto.Tier tier);

    default String lblEvidenceLevel() {
        return "EVIDENCE LEVEL";
    }
    String lblEvidenceLevelDetails(SolidTumorDto.EvidenceLevel evidenceLevel);
    default String lblReference() {
        return "REFERENCES";
    }
    TestInfo testInfo();
}
