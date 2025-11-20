package com.greencross.lims.report.genomescreen;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.greencross.lims.report.genomescreen.GenomeScreenTemplateN087.DiseaseSubN087.*;
import static com.greencross.lims.report.genomescreen.GenomeScreenTemplateN087.SnvRiskScreenN087.*;

public interface GenomeScreenTemplateN087<R extends GenomeScreenResource & RiskScreenResource> extends GenomeScreenTemplate<R>, RiskScreenTemplate<R> {
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
	private String diseaseNameScientificN087(DiseaseN087 disease) {
		switch (disease) {
			case 대동맥과_소동맥_폐핵으로_인한_허혈성_뇌졸중: return "Large artery and small artery occlusion\n(Lacunar) ischemic stroke\n";
			case 소동맥_폐색으로_인한_허혈성_뇌졸중:		return "Small artery occlusion\n(lacunar) ischemic stroke\n";
			case 심장_색전성_뇌졸중:						return "Cardioembolic stroke";
			case 기타_원인으로_인한_허혈성_뇌졸중:			return "Ischemic stroke of other etiologies\n";
			case 뇌혈관질환_편두통:						return "CADASIL D/Dx (migrane)\n";
			case 모야모야병:								return "Moyamoya disease\n";
			case 뇌내출혈:								return "Intracerebral hemorrhage";
			case 고콜레스테롤혈증:						return "Hypercholesterolemia\n";
			case 혈전증:									return "Thrombophilia\n";
			default:									return "";
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
	enum GeneN087 implements GenomeScreenTemplate.Gene {
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

	default String clinicalReferenceN087(GeneN087 gene) {
		switch(gene) {
			case ABCC6:		return "Brain Pathol. 2018 Nov;28(6):822-831";
			case ACTA2:		return "Am J Hum Genet. 2009 May;84(5):617-27.";
			case ADA2:		return "N Engl J Med. 2014 Mar 6;370(10):911-20.";
			case APP:		return "Neurology. 2004 Sep 14;63(5):910-2.";
			case ATP1A2:	return "Case Rep Neurol Med. 2013;2013:895057. ";
			case CBS:		return "J Neurol Sci. 2012 Jan 15;312(1-2):26-30.";
			case COL4A1:	return "World Neurosurg. 2018 May;113:e521-e528.";
			case COL4A2:	return "Neurology. 2017 Oct 24;89(17):1829-1839.";
			case CST3:		return "Lancet. 1988 Sep 10;2(8611):603-4.";
			case GUCY1A1:	return "Clin Genet. 2016 Oct;90(4):351-60";
			case ITM2B:		return "Proc Natl Acad Sci U S A. 2000 Apr 25;97(9):4920-5.";
			case PCSK9:		return "Sci Rep. 2015 Dec 15;5:18224.";
			case SCN1A:		return "Lancet. 2005 Jul 30-Aug 5;366(9483):371-7.";
			case SLC2A10:	return "Hum Mutat. 2008 Jan;29(1):150-8. ";
			case TREX1:		return "Rev Invest Clin. 2018;70(2):68-75.";
			default:		return null;
		}
	}
	private String diseaseSubNameScientificN087(DiseaseSubN087 diseaseSub) {
		switch (diseaseSub) {
			case 베타_지중해혈증:							return "Beta thalassemia";
			case 고호모시스테인혈증:						return "Hyperhomecysteinemia";
			case 파브리병:								return "Fabry disease";
			case 탄력섬유거짓황색종:						return "Pseudoxanthoma elasticum";
			case 카다실:									return "CADASIL";
			case 카라실:									return "CARASIL";
			case 백색질_형성장애_동반_망막_혈관병증:			return "Retinal vasculopathy with cerebral leukodystrophy";
			case 심장_긴간격_증후군:						return "Long QT syndrome - LQTS";
			case 혈관성_엘러스_단로스_증후군:				return "Vascular Ehlers-Danlos syndrome";
			case 마르판_증후군:							return "Marfan syndrome";
			case 결절성_다발동맥염:						return "Polyarteritis nodosa";
			case 동맥_비틀림_증후군:						return "Arterial tortuosity syndrome";
			case 가족성_편마비_편두통:						return "Familial hemiplegic migraine";
			case 모야모야병:								return "Mohamoya disease";
			case 아밀로이드_뇌_혈관병증:					return "Cerbral amyloid angiopathy";
			case 뇌_소혈관질환:							return "Brain small vessel disease";
			case 뇌구멍증:								return "Porencephaly";
			case 가족성_고콜레스테롤혈증:					return "Familial Hypercholesterolemia";
			case FactorVLeiden혈전증:					return "Factor V Leiden thrombophilia";
			case 프로트롬빈_관련_혈전증:					return "Thrombophilia due to thrombin defect";
			case 항트롬빈_결핍_혈전증:						return "Thrombophilia due to antithrombin III deficiency";
			case 단백질C_결핍_혈전증:						return "Thrombophilia due to activated protein C resistance";
			case 단백질S_결핍_혈전증:						return "Thrombophilia due to protein S deficiency";
			default:									return "";
		}
	}
	@Override
	default DiseaseN087[] diseases() {
		return DiseaseN087.values();
	}
	@Override
	default String diseaseNameScientific(Disease disease) {
		if(disease instanceof DiseaseN087) return diseaseNameScientificN087((DiseaseN087) disease);
		return null;
	}
	@Override
	default String diseaseSubNameScientific(DiseaseSub diseaseSub) {
		if(diseaseSub instanceof DiseaseSubN087) return diseaseSubNameScientificN087((DiseaseSubN087) diseaseSub);
		return null;
	}
	@Override
	default String clinicalReference(Gene gene) {
		if(gene instanceof GeneN087) return clinicalReferenceN087((GeneN087) gene);
		return null;
	}
	@Override
	default String clinicalReference(GeneRiskScreen gene) {
		if(gene instanceof GeneRiskScreenN087) return clinicalReferenceN087((GeneRiskScreenN087) gene);
		return null;
	}

	@Getter
	@Accessors(fluent = true)
	enum GeneRiskScreenN087 implements GeneRiskScreen {
		APOE(RiskScreenTemplate.Tier.Tier3, APOE_),
		MTHFR(RiskScreenTemplate.Tier.Tier3, MTHFR_c677, MTHFR_c1298),
		NOTCH3(RiskScreenTemplate.Tier.Tier2, NOTCH3_c1630),
		RNF213(RiskScreenTemplate.Tier.Tier2, RNF213_c14429);
		private RiskScreenTemplate.Tier tier;
		private Snv[] snvs;
		GeneRiskScreenN087(RiskScreenTemplate.Tier tier, SnvRiskScreenN087... snvs) {
			this.tier = tier;
			this.snvs = snvs;
		}
	}
	@Getter
	@Accessors(fluent = true)
	enum DiseaseRiskScreenN087 implements DiseaseRiskScreen, DiseaseSubRiskScreen {
		허혈성_뇌졸중(Map.entry(GeneRiskScreenN087.APOE,
						  Map.ofEntries(entry(APOE_,
											  Type.of("e2e2", 0.01).lessThen(true),
											  Type.of("e2e3", 0.1),
											  Type.of("e2e4", 2, 0.03),
											  Type.of("e3e3", 0.71),
											  Type.of("e3e4", 1.9, 0.15),
											  Type.of("e4e4", 3.4, 0.01)))),
				Map.entry(GeneRiskScreenN087.MTHFR,
						  Map.ofEntries(entry(MTHFR_c677,
											  Type.of("CC", 0.56),
											  Type.of("CT", 0.25),
											  Type.of("TT", 1.4, 0.19)),
										entry(MTHFR_c1298,
											  Type.of("AA", 0.827),
											  Type.of("AC", 0.14),
											  Type.of("CC", 1.5, 0.03))))),
		모야모야병(Map.entry(GeneRiskScreenN087.RNF213,
						Map.ofEntries(entry(RNF213_c14429,
											Type.of("GG", 0.99),
											Type.of("GA", 96, 0.01),
											Type.of("AA",  96, 0.0005))))),
		카다실(Map.entry(GeneRiskScreenN087.NOTCH3,
					  Map.ofEntries(entry(NOTCH3_c1630,
										  Type.of("CC", 1, 0.998),
										  Type.of("CT", 100, 0.002),
										  Type.of("TT", 100, 0.00001).lessThen(true)))));
		private GeneRiskScreen[] genes;
		private Map<SnvRiskScreenN087, List<Type>> types;
		DiseaseRiskScreenN087(Map.Entry<GeneRiskScreen, Map<SnvRiskScreenN087, List<Type>>>... genes) {
			types = new HashMap<>();
			List<GeneRiskScreen> t = new LinkedList<>();
			for(Map.Entry<GeneRiskScreen, Map<SnvRiskScreenN087, List<Type>>> gene: genes) {
				types.putAll(gene.getValue());
				t.add(gene.getKey());
			}
			this.genes = t.stream().toArray(GeneRiskScreen[]::new);
		}
		@Override
		public DiseaseSubRiskScreen[] subs() {
			return new DiseaseSubRiskScreen[] { this };
		}
		@Override
		public boolean isPositive(GenomeScreenWithRiskScreenDto dto) {
			return DiseaseSubRiskScreen.super.isPositive(dto);
		}
		@Override
		public List<Type> types(Snv snv) {
			return types.get(snv);
		}
	}
	@Getter
	@Accessors(fluent = true)
	enum RiskFactorN087 implements RiskFactor {
		고혈압(2.8), 흡연(1.9), 비만(1.4), 당뇨(1.3), 음주(2.8), 스트레스(2.8), 심혈관계_유질환자(2.8);
		private double risk;
		RiskFactorN087(double risk) {
			this.risk = risk;
		}
	}
	default RiskFactor[] riskfactors() {
		return RiskFactorN087.values();
	}
	private static <K, V> Map.Entry<K, List<V>> entry(K key, V... values) {
		return Map.entry(key, List.of(values));
	}
	@Getter
	@Accessors(fluent = true)
	enum SnvRiskScreenN087 implements Snv {
		APOE_(""),
		MTHFR_c677("c.677"),
		MTHFR_c1298("c.1298"),
		RNF213_c14429("c.14429"),
		NOTCH3_c1630("c.1630");
		private String pos;
		SnvRiskScreenN087(String pos) {
			this.pos = pos;
		}
	}
	default String clinicalReferenceN087(GeneRiskScreenN087 gene) {
		switch(gene) {
			case RNF213:	return "J Stroke Cerebrovasc Dis. 2018 Aug;27(8):2259-2270.";
			case NOTCH3:	return "J Stroke Cerebrovasc Dis. 2013 Jul;22(5):608-14.";
			default: return null;
		}
	}
	default DiseaseRiskScreenN087[] riskscreens() {
		return DiseaseRiskScreenN087.values();
	}

	String lblGraphDiseaseInfo();
	String lblGraphSummary();
	String lblGraphRisk();
	String lblGraphGene();
}
