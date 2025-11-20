package com.greencross.lims.service.report.genomescreen;

import lombok.Getter;
import lombok.experimental.Accessors;

import static com.greencross.lims.service.report.genomescreen.GenomeScreenTemplateN088.DiseaseSubN088.*;

public interface GenomeScreenTemplateN088 extends com.greencross.lims.service.report.genomescreen.GenomeScreenTemplate {
	@Getter
	@Accessors(fluent = true)
	enum DiseaseN088 implements Disease {
		고콜레스테롤혈증(
			가족성_고콜레스테롤혈증,
			뇌건황색종증,
			시토스테롤혈증,
			고알파지질단백혈증),
		약물적합도(
				스타틴_부작용),
		지질단백결핍(
				저베타지질단백혈증,
				저알파지질단백혈증),
		복합형_이상지질혈증(
				이상베타지질단백혈증,
				복합_고지질혈증),
		고중성지방혈증(
				가족성_지질단백_지질분해효소_결핍증,
				DiseaseSubN088.고중성지방혈증,
				알스트롬_증후군);
		private DiseaseSub[] subs;
		DiseaseN088(DiseaseSubN088... diseaseSubs) {
			this.subs = diseaseSubs;
		}
	}
	@Getter
	@Accessors(fluent = true)
	enum DiseaseSubN088 implements DiseaseSub {
		가족성_고콜레스테롤혈증(GeneN088.APOB,GeneN088.APOA2,GeneN088.LDLR,GeneN088.LDLRAP1,GeneN088.PCSK9,GeneN088.STAP1),
		뇌건황색종증(GeneN088.CYP27A1),
		시토스테롤혈증(GeneN088.ABCG5,GeneN088.ABCG8),
		고알파지질단백혈증(GeneN088.APOC3,GeneN088.CETP,GeneN088.SCARB1),
		스타틴_부작용(GeneN088.SLCO1B1),
		저베타지질단백혈증(GeneN088.MTTP,GeneN088.APOB,GeneN088.SAR1B,GeneN088.ANGPTL3),
		저알파지질단백혈증(GeneN088.ABCA1,GeneN088.APOA1,GeneN088.LCAT),
		이상베타지질단백혈증(GeneN088.APOE),
		복합_고지질혈증(GeneN088.LPL,GeneN088.LIPA,GeneN088.LIPC),
		가족성_지질단백_지질분해효소_결핍증(GeneN088.APOC2,GeneN088.APOA5,GeneN088.GPIHBP1,GeneN088.LMF1,GeneN088.LPL),
		고중성지방혈증(GeneN088.CREB3L3,GeneN088.CYP7A1,GeneN088.GPD1,GeneN088.GPIHBP1),
		알스트롬_증후군(GeneN088.ALMS1);
		private GeneN088[] genes;
		DiseaseSubN088(GeneN088... genes) {
			this.genes = genes;
		}
	}
	@Getter
	@Accessors(fluent = true)
	enum GeneN088 implements Gene {
		ABCA1(GenomeScreenTemplate.Tier.Tier2, "NM_005502.3"),
		ABCG5(GenomeScreenTemplate.Tier.Tier2, "NM_022436.2"),
		ABCG8(GenomeScreenTemplate.Tier.Tier2, "NM_022437.2"),
		ALMS1(GenomeScreenTemplate.Tier.Tier2, "NM_015120.4"),
		ANGPTL3(GenomeScreenTemplate.Tier.Tier2, "NM_014495.3"),
		APOA1(GenomeScreenTemplate.Tier.Tier2, "NM_000039.2"),
		APOA2(GenomeScreenTemplate.Tier.Tier2, "NM_001643.1"),
		APOA5(GenomeScreenTemplate.Tier.Tier2, "NM_052968.4"),
		APOB(GenomeScreenTemplate.Tier.Tier2, "NM_000384.2"),
		APOC2(GenomeScreenTemplate.Tier.Tier2, "NM_000483.4"),
		APOC3(GenomeScreenTemplate.Tier.Tier2, "NM_000040.2"),
		APOE(GenomeScreenTemplate.Tier.Tier3, "NM_000041.3"),
		CETP(GenomeScreenTemplate.Tier.Tier2, "NM_000078.2"),
		CREB3L3(GenomeScreenTemplate.Tier.Tier2, "NM_032607.2"),
		CYP27A1(GenomeScreenTemplate.Tier.Tier2, "NM_000784.3"),
		CYP7A1(GenomeScreenTemplate.Tier.Tier2, "NM_000780.4"),
		GPD1(GenomeScreenTemplate.Tier.Tier2, "NM_005276.3"),
		GPIHBP1(GenomeScreenTemplate.Tier.Tier2, "NM_178172.5"),
		LCAT(GenomeScreenTemplate.Tier.Tier2, "NM_000229.1"),
		LDLR(GenomeScreenTemplate.Tier.Tier1,"NM_000527.4"),
		LDLRAP1(GenomeScreenTemplate.Tier.Tier2, "NM_015627.2"),
		LIPA(GenomeScreenTemplate.Tier.Tier2, "NM_000235.3"),
		LIPC(GenomeScreenTemplate.Tier.Tier2, "NM_000236.2"),
		LMF1(GenomeScreenTemplate.Tier.Tier2, "NM_022773.3"),
		LPL(GenomeScreenTemplate.Tier.Tier2, "NM_000237.2"),
		MTTP(GenomeScreenTemplate.Tier.Tier2, "NM_000253.3"),
		PCSK9(GenomeScreenTemplate.Tier.Tier2, "NM_174936.3"),
		SAR1B(GenomeScreenTemplate.Tier.Tier2, "NM_001033503.2"),
		SCARB1(GenomeScreenTemplate.Tier.Tier2, "NM_005505.4"),
		SLCO1B1(GenomeScreenTemplate.Tier.Tier3, "NM_006446.4"),
		STAP1(GenomeScreenTemplate.Tier.Tier2, "NM_012108.3");
		private GenomeScreenTemplate.Tier tier;
		private String transcript;
		GeneN088(GenomeScreenTemplate.Tier tier, String transcript) {
			this.tier = tier;
			this.transcript = transcript;
		}
	}
}
