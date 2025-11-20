package com.greencross.lims.service.report.genomescreen;

import lombok.Getter;
import lombok.experimental.Accessors;

import static com.greencross.lims.service.report.genomescreen.GenomeScreenTemplateN087.DiseaseSubN087.*;

public interface GenomeScreenTemplateN087 extends GenomeScreenTemplate {
	@Getter
	@Accessors(fluent = true)
	enum DiseaseN087 implements Disease {
		대동맥과_소동맥_폐핵으로_인한_허혈성_뇌졸중(베타_지중해혈증, 고호모시스테인혈증, 파브리병, 탄력섬유거짓황색종),
		소동맥_폐색으로_인한_허혈성_뇌졸중(DiseaseSubN087.카다실, 카라실, 백색질_형성장애_동반_망막_혈관병증),
		심장_색전성_뇌졸중(심장_긴간격_증후군),
		기타_원인으로_인한_허혈성_뇌졸중(혈관성_엘러스_단로스_증후군, 마르판_증후군, 결절성_다발동맥염, 동맥_비틀림_증후군),
		뇌혈관질환_편두통(가족성_편마비_편두통),
		모야모야병(DiseaseSubN087.모야모야병),
		뇌내출혈(아밀로이드_뇌_혈관병증, 뇌_소혈관질환, 뇌구멍증),
		고콜레스테롤혈증(가족성_고콜레스테롤혈증),
		혈전증(FactorVLeiden혈전증, 프로트롬빈_관련_혈전증, 항트롬빈_결핍_혈전증, 단백질C_결핍_혈전증, 단백질S_결핍_혈전증);
		private DiseaseSub[] subs;
		DiseaseN087(DiseaseSubN087... diseaseSubs) {
			this.subs = diseaseSubs;
		}
	}
	@Getter
	@Accessors(fluent = true)
	enum DiseaseSubN087 implements DiseaseSub {
		베타_지중해혈증(GeneN087.HBB),
		고호모시스테인혈증(GeneN087.CBS),
		파브리병(GeneN087.GLA),
		탄력섬유거짓황색종(GeneN087.ABCC6),
		카다실(GeneN087.NOTCH3),
		카라실(GeneN087.HTRA1),
		백색질_형성장애_동반_망막_혈관병증(GeneN087.TREX1),
		심장_긴간격_증후군(GeneN087.KCNQ1, GeneN087.KCNJ2, GeneN087.SCN5A),
		혈관성_엘러스_단로스_증후군(GeneN087.COL3A1),
		마르판_증후군(GeneN087.FBN1),
		결절성_다발동맥염(GeneN087.ADA2),
		동맥_비틀림_증후군(GeneN087.SLC2A10),
		가족성_편마비_편두통(GeneN087.CACNA1A, GeneN087.ATP1A2, GeneN087.SCN1A),
		모야모야병(GeneN087.RNF213, GeneN087.ACTA2, GeneN087.GUCY1A1),
		아밀로이드_뇌_혈관병증(GeneN087.APP, GeneN087.CST3, GeneN087.ITM2B),
		뇌_소혈관질환(GeneN087.COL4A1),
		뇌구멍증(GeneN087.COL4A2),
		가족성_고콜레스테롤혈증(GeneN087.LDLR, GeneN087.APOB, GeneN087.PCSK9, GeneN087.APOE),
		FactorVLeiden혈전증(GeneN087.F5),
		프로트롬빈_관련_혈전증(GeneN087.F2),
		항트롬빈_결핍_혈전증(GeneN087.SERPINC1),
		단백질C_결핍_혈전증(GeneN087.PROC),
		단백질S_결핍_혈전증(GeneN087.PROS1);
		private GeneN087[] genes;
		DiseaseSubN087(GeneN087... genes) {
			this.genes = genes;
		}
	}
	@Getter
	@Accessors(fluent = true)
	enum GeneN087 implements Gene {
		ABCC6(GenomeScreenTemplate.Tier.Tier2, "NM_001171.5"),
		ACTA2(GenomeScreenTemplate.Tier.Tier2, "NM_001613.2"),
		ADA2(GenomeScreenTemplate.Tier.Tier2, "NM_001282225.1"),
		APOB(GenomeScreenTemplate.Tier.Tier3, "NM_000384.2"),
		APOE(GenomeScreenTemplate.Tier.Tier3, "NM_000041.3"),
		APP(GenomeScreenTemplate.Tier.Tier2, "NM_000484.3"),
		ATP1A2(GenomeScreenTemplate.Tier.Tier2, "NM_000702.3"),
		CACNA1A(GenomeScreenTemplate.Tier.Tier1, "NM_001127221.1"),
		CBS(GenomeScreenTemplate.Tier.Tier2, "NM_000071.2"),
		COL3A1(GenomeScreenTemplate.Tier.Tier1, "NM_000090.3"),
		COL4A1(GenomeScreenTemplate.Tier.Tier2, "NM_001845.5"),
		COL4A2(GenomeScreenTemplate.Tier.Tier2, "NM_001846.2"),
		CST3(GenomeScreenTemplate.Tier.Tier2, "NM_000099.3"),
		F2(GenomeScreenTemplate.Tier.Tier3, "NM_000506.3"),
		F5(GenomeScreenTemplate.Tier.Tier3, "NM_000130.4"),
		FBN1(GenomeScreenTemplate.Tier.Tier1, "NM_000138.4"),
		GLA(GenomeScreenTemplate.Tier.Tier1, "NM_000169.2"),
		GUCY1A1(GenomeScreenTemplate.Tier.Tier2, "NM_000856.5"),
		HBB(GenomeScreenTemplate.Tier.Tier1, "NM_000518.4"),
		HTRA1(GenomeScreenTemplate.Tier.Tier1, "NM_002775.4"),
		ITM2B(GenomeScreenTemplate.Tier.Tier2, "NM_021999.4"),
		KCNJ2(GenomeScreenTemplate.Tier.Tier3, "NM_000891.2"),
		KCNQ1(GenomeScreenTemplate.Tier.Tier1, "NM_000218.2"),
		LDLR(GenomeScreenTemplate.Tier.Tier1, "NM_000527.4"),
		NOTCH3(GenomeScreenTemplate.Tier.Tier1, "NM_000435.2"),
		PCSK9(GenomeScreenTemplate.Tier.Tier2, "NM_174936.3"),
		PROC(GenomeScreenTemplate.Tier.Tier1, "NM_000312.3"),
		PROS1(GenomeScreenTemplate.Tier.Tier1, "NM_000313.3"),
		RNF213(GenomeScreenTemplate.Tier.Tier1, "NM_001256071.2"),
		SCN1A(GenomeScreenTemplate.Tier.Tier2, "NM_001165963.1"),
		SCN5A(GenomeScreenTemplate.Tier.Tier1, "NM_198056.2"),
		SERPINC1(GenomeScreenTemplate.Tier.Tier3, "NM_000488.3"),
		SLC2A10(GenomeScreenTemplate.Tier.Tier2, "NM_030777.3"),
		TREX1(GenomeScreenTemplate.Tier.Tier2, "NM_033629.4");
		private GenomeScreenTemplate.Tier tier;
		private String transcript;
		GeneN087(GenomeScreenTemplate.Tier tier, String transcript) {
			this.tier = tier;
			this.transcript = transcript;
		}
	}
}
