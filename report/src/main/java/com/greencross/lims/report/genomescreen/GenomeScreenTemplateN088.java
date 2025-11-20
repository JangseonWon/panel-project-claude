package com.greencross.lims.report.genomescreen;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.greencross.lims.report.genomescreen.GenomeScreenTemplateN088.DiseaseSubN088.*;
import static com.greencross.lims.report.genomescreen.GenomeScreenTemplateN088.DiseaseSubRiskScreenN088.*;
import static com.greencross.lims.report.genomescreen.GenomeScreenTemplateN088.SnvRiskScreenN088.*;

public interface GenomeScreenTemplateN088<R extends GenomeScreenResource & RiskScreenResource> extends GenomeScreenTemplate<R>, RiskScreenTemplate<R> {
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
	default String diseaseNameScientificN088(DiseaseN088 disease) {
		switch (disease) {
			case 고콜레스테롤혈증:	return "Hypercholesterolemia";
			case 약물적합도:			return "Medication Fitness";
			case 지질단백결핍:		return "Lipoprotein Deficiency";
			case 복합형_이상지질혈증:	return "Combined Dyslipidemia";
			case 고중성지방혈증:		return "Hypertriglyceridemia";
			default:				return "";
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
	default String diseaseSubNameScientificN088(DiseaseSubN088 diseaseSub) {
		switch (diseaseSub) {
			case 가족성_고콜레스테롤혈증:			return "Familial Hypercholesterolemia";
			case 뇌건황색종증:					return "Cerebrotendinous Xanthomatosis";
			case 시토스테롤혈증:					return "Sitosterolemia";
			case 고알파지질단백혈증:				return "Hyperalphalipoproteinemia";
			case 스타틴_부작용:					return "Statin Response";
			case 저베타지질단백혈증:				return "Hypobetalipoproteinemia\nAbetalipoproteinemia(ABL)\nChylomicron Retention Disease(CMRD)";
			case 저알파지질단백혈증:				return "Hypoalphalipoproteinemia\n(Familial HDL Deficiency)";
			case 이상베타지질단백혈증:				return "Dysbetalipoproteinemia\nHyperlipoproteinemia Type 3";
			case 복합_고지질혈증:					return "Combined Hyperlipidemia";
			case 가족성_지질단백_지질분해효소_결핍증:	return "Familial Lipoprotein Lipase Deficiency";
			case 고중성지방혈증:					return "Hypertriglyceridemia";
			case 알스트롬_증후군:					return "Alstrom Syndrome";
			default:							return "";
		}
	}
	default String clinicalReferenceN088(GeneN088 gene) {
		switch(gene) {
			case ABCA1:		return "Medicine (Baltimore). 2018 Dec;97(50):e13521.";
			case ABCG5:		return "J Lipid Res. 2009 Mar; 50(3): 565–573. ";
			case ABCG8:		return "Clin Chem Lab Med. 2008;46(11):1581-5";
			case ALMS1:		return "Nat Genet. 2002 May;31(1):74-8. Epub 2002 Apr 8";
			case ANGPTL3:	return "J Lipid Res. 2013 Dec;54(12):3481-90.";
			case APOA1:		return "Lipids Health Dis. 2018 May 10;17(1):105.";
			case APOA2:		return "J Hum Genet. 2002;47(12):656-64 ";
			case APOA5:		return "PLoS One. 2014 Oct 14;9(10):e110258.";
			case APOB:		return "PLoS One. 2013 Apr 8;8(4):e60729.";
			case APOC2:		return "Lipids Health Dis. 2016; 15: 12. ";
			case APOC3:		return "Lipids Health Dis. 2015 Apr 18;14:32.";
			case CETP:		return "Genes & Genomics (2012) 34: 231-235.";
			case CREB3L3:	return "Circ Cardiovasc Genet. 2012 Feb 1;5(1):66-72";
			case CYP27A1:	return "J Biol Chem. 1991 Apr 25;266(12):7779-83 ";
			case CYP7A1:	return "J Nutr. 2004 Sep;134(9):2200-4. ";
			case GPD1:		return "Am J Hum Genet. 2012 Jan 13;90(1):49-60 ";
			case GPIHBP1:	return "J Pediatr Gastroenterol Nutr. 2014 Jul;59(1):17-21. ";
			case LCAT:		return "Hum Mutat. 2011 Nov;32(11):1290-8.";
			case LDLRAP1:	return "Atherosclerosis. 2018 Feb;269:1-5. ";
			case LIPA:		return "Genomics. 1996 Apr 1;33(1):85-93.";
			case LIPC:		return "Arterioscler Thromb Vasc Biol. 2000 Jul;20(7):1789-95. ";
			case LMF1:		return "Nat Genet. 2007 Dec;39(12):1483-";
			case LPL:		return "Lipids Health Dis. 2006; 5: 19. ";
			case MTTP:		return "Mol Genet Metab. 2007 Apr;90(4):453-7 ";
			case PCSK9:		return "PLoS One. 2013 Apr 8;8(4):e60729.";
			case SAR1B:		return "Nat Genet. 2003 May;34(1):29-31";
			case SCARB1:	return "Int J Med Sci. 2012; 9(8): 715–724.";
			case STAP1:		return "Eur J Hum Genet. 2016 Feb;24(2):191-7.";
			default:		return null;
		}
	}
	@Override
	default DiseaseN088[] diseases() {
		return DiseaseN088.values();
	}
	@Override
	default String diseaseNameScientific(Disease disease) {
		if(disease instanceof DiseaseN088) return diseaseNameScientificN088((DiseaseN088) disease);
		return null;
	}
	@Override
	default String diseaseSubNameScientific(DiseaseSub diseaseSub) {
		if(diseaseSub instanceof DiseaseSubN088) return diseaseSubNameScientificN088((DiseaseSubN088) diseaseSub);
		return null;
	}
	@Override
	default String clinicalReference(Gene gene) {
		if(gene instanceof GeneN088) return clinicalReferenceN088((GeneN088) gene);
		return null;
	}
	@Override
	default String clinicalReference(GeneRiskScreen gene) {
		if(gene instanceof GeneRiskScreenN088) return clinicalReferenceN088((GeneRiskScreenN088) gene);
		return null;
	}

	@Getter
	@Accessors(fluent = true)
	enum DiseaseRiskScreenN088 implements DiseaseRiskScreen {
		고지혈증(LDL콜레스테롤, 중성지방),
		치료제부작용(스타틴_약물_복용시_부작용);
		private DiseaseSubRiskScreenN088[] subs;
		DiseaseRiskScreenN088(DiseaseSubRiskScreenN088... subs) {
			this.subs = subs;
		}
	}

	@Getter
	@Accessors(fluent = true)
	enum DiseaseSubRiskScreenN088 implements DiseaseSubRiskScreen {
		LDL콜레스테롤(Map.entry(GeneRiskScreenN088.APOE,
					   Map.ofEntries(entry(APOE_,
										   Type.of("e2e2", 0.01).lessThen(true),
										   Type.of("e2e3", 0.10),
										   Type.of("e2e4", 0.03),
										   Type.of("e3e3", 0.71),
										   Type.of("e3e4", 0.15),
										   Type.of("e4e4", 2.1, 0.01))))),
		중성지방(Map.entry(GeneRiskScreenN088.APOE,
					   Map.ofEntries(entry(APOE_,
										   Type.of("e2e2", 2.7, 0.01).lessThen(true),
										   Type.of("e2e3", 0.10),
										   Type.of("e2e4", 0.03),
										   Type.of("e3e3", 0.71),
										   Type.of("e3e4", 0.15),
										   Type.of("e4e4", 0.01)))),
			 Map.entry(GeneRiskScreenN088.APOA5,
					   Map.ofEntries(entry(APOA5_553,
										   Type.of("GG", 0.92),
										   Type.of("GT", 4.4, 0.07),
										   Type.of("TT", 4.4, 0.01)),
									 entry(APOA5_56,
										   Type.of("CC", 1.0),
										   Type.of("CG", 6.5, 0.00001).lessThen(true),
										   Type.of("GG", 6.5, 0.00001).lessThen(true))))),
		스타틴_약물_복용시_부작용(Map.entry(GeneRiskScreenN088.COQ2,
						Map.ofEntries(entry(COQ2_c779_1022,
											Type.of("GG", 0.721),
											Type.of("GC", 2.6, 0.127),
											Type.of("CC", 2.6, 0.152)))));
		private GeneRiskScreen[] genes;
		private Map<SnvRiskScreenN088, List<Type>> types;
		DiseaseSubRiskScreenN088(Map.Entry<GeneRiskScreen, Map<SnvRiskScreenN088, List<Type>>>... genes) {
			types = new HashMap<>();
			List<GeneRiskScreen> t = new LinkedList<>();
			for(Map.Entry<GeneRiskScreen, Map<SnvRiskScreenN088, List<Type>>> gene: genes) {
				types.putAll(gene.getValue());
				t.add(gene.getKey());
			}
			this.genes = t.stream().toArray(GeneRiskScreen[]::new);
		}
		@Override
		public List<Type> types(Snv snv) {
			return types.get(snv);
		}
	}
	@Getter
	@Accessors(fluent = true)
	enum RiskFactorN088 implements RiskFactor {
		허혈성_뇌졸중(2.1), 관상동맥(1.5), 죽상경화성_심혈관질환(1.4), 암(1.5);
		private double risk;
		RiskFactorN088(double risk) {
			this.risk = risk;
		}
	}
	default RiskFactor[] riskfactors() {
		return RiskFactorN088.values();
	}
	@Getter
	@Accessors(fluent = true)
	enum GeneRiskScreenN088 implements GeneRiskScreen {
		APOE(RiskScreenTemplate.Tier.Tier3, APOE_),
		APOA5(RiskScreenTemplate.Tier.Tier3, APOA5_553, APOA5_56),
		COQ2(RiskScreenTemplate.Tier.Tier3, COQ2_c779_1022);
		private RiskScreenTemplate.Tier tier;
		private Snv[] snvs;
		GeneRiskScreenN088(RiskScreenTemplate.Tier tier, SnvRiskScreenN088... snvs) {
			this.tier = tier;
			this.snvs = snvs;
		}
	}
	default String clinicalReferenceN088(GeneRiskScreenN088 gene) {
		switch(gene) {
			default: return null;
		}
	}
	private static <K, V> Map.Entry<K, List<V>> entry(K key, V... values) {
		return Map.entry(key, List.of(values));
	}
	@Getter
	@Accessors(fluent = true)
	enum SnvRiskScreenN088 implements Snv {
		APOE_(""),
		APOA5_553("c.553"),
		APOA5_56("c.56"),
		COQ2_c779_1022("c.779-1022");
		private String pos;
		SnvRiskScreenN088(String pos) {
			this.pos = pos;
		}
	}
	default DiseaseRiskScreenN088[] riskscreens() {
		return DiseaseRiskScreenN088.values();
	}
	String lblGraphDiseaseInfo();
	String lblGraphSummary();
	String lblGraphDisease();
	String lblGraphRisk();
	String lblGraphGene();

	String lblGuideTermTitle();
	String[] lblGuideTerms();

	@Override
	default boolean isLifestyleResultPositive(GenomeScreenWithRiskScreenDto dto) {
		return DiseaseRiskScreenN088.고지혈증.isPositive(dto);
	}
}
