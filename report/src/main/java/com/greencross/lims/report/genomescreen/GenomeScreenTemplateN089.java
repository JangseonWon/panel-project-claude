package com.greencross.lims.report.genomescreen;

import lombok.Getter;
import lombok.experimental.Accessors;

import static com.greencross.lims.report.genomescreen.GenomeScreenTemplate.Tier.*;
import static com.greencross.lims.report.genomescreen.GenomeScreenTemplateN089.GeneN089.*;

public interface GenomeScreenTemplateN089<R extends GenomeScreenResource> extends GenomeScreenTemplate<R> {
	@Getter
	@Accessors(fluent = true)
	enum DiseaseN089 implements Disease {
		흉부대동맥류와_박리증(DiseaseSubN089.엘러스_단로스_증후군, DiseaseSubN089.마르판_증후군, DiseaseSubN089.로이_디에츠_증후군, DiseaseSubN089.가족성_흉부대동맥류와_박리증),
		유전성_부정맥(DiseaseSubN089.카테콜아민성_다형성_심실성_빈맥, DiseaseSubN089.심장_긴간격_증후군, DiseaseSubN089.브루가다_증후군),
		유전성_심근병증(DiseaseSubN089.비후성_심근병증, DiseaseSubN089.확장성_심근병증, DiseaseSubN089.파브리병, DiseaseSubN089.부정맥_유발성_우심실_심근병증, DiseaseSubN089.애머리_드라이푸스_증후군),
		고콜레스테롤혈증_및_혈전증(DiseaseSubN089.가족성_고콜레스테롤혈증, DiseaseSubN089.고호모시스테인혈전증),
		동맥비틀림증후군(DiseaseSubN089.동맥비틀림증후군);
		private DiseaseSub[] subs;
		DiseaseN089(GenomeScreenTemplateN089.DiseaseSubN089... diseaseSubs) {
			this.subs = diseaseSubs;
		}
	}
	@Override
	default String diseaseNameScientific(Disease disease) {
		if(disease instanceof DiseaseN089) switch ((DiseaseN089)disease) {
			case 흉부대동맥류와_박리증:		return "Thoracic aortic aneurysms and dissections";
			case 유전성_부정맥:				return "Hereditary arrhythmia";
			case 유전성_심근병증:				return "Hereditary cardiomyopathy";
			case 고콜레스테롤혈증_및_혈전증:	return "Hypercholesterolemia and Thrombophilia";
			case 동맥비틀림증후군:			return "Arterial tortuosity syndrome";
			default: return null;
		}
		return null;
	}
	@Getter
	@Accessors(fluent = true)
	enum DiseaseSubN089 implements DiseaseSub {
		엘러스_단로스_증후군(COL3A1),
		마르판_증후군(FBN1),
		로이_디에츠_증후군(TGFBR1, TGFBR2, SMAD3, TGFB2, TGFB3),
		가족성_흉부대동맥류와_박리증(ACTA2, TGFBR2, TGFBR1, FBN1, SMAD3, MYH11, MYLK),
		비후성_심근병증(MYBPC3, MYH7, TNNT2, TNNI3, TPM1, MYL3, MYL2, CSRP3, PRKAG2),
		확장성_심근병증(ACTC1, MYH7, LMNA, BAG3, DES),
		파브리병(GLA),
		부정맥_유발성_우심실_심근병증(PKP2, DSP, DSC2, TMEM43, DSG2),
		애머리_드라이푸스_증후군(EMD, FHL1),
		카테콜아민성_다형성_심실성_빈맥(RYR2),
		심장_긴간격_증후군(KCNQ1, KCNH2, SCN5A),
		브루가다_증후군(SCN5A),
		가족성_고콜레스테롤혈증(LDLR, APOB, PCSK9),
		고호모시스테인혈전증(CBS),
		동맥비틀림증후군(SLC2A10);
		private GeneN089[] genes;
		DiseaseSubN089(GeneN089... genes) {
			this.genes = genes;
		}
	}
	@Getter
	@Accessors(fluent = true)
	enum GeneN089 implements Gene {
		ACTA2(Tier2, "NM_001613.2"),
		ACTC1(Tier2, "NM_005159.4"),
		APOB(Tier2, "NM_000384.2"),
		BAG3(Tier3, "NM_004281.3"),
		CBS(Tier3, "NM_000071.2"),
		COL3A1(Tier1, "NM_000090.3"),
		CSRP3(Tier3, "NM_003476.4"),
		DES(Tier3, "NM_001927.3"),
		DSC2(Tier2, "NM_024422.3"),
		DSG2(Tier2, "NM_001943.3"),
		DSP(Tier3, "NM_004415.2"),
		EMD(Tier3, "NM_000117.2"),
		FBN1(Tier1, "NM_000138.4"),
		FHL1(Tier3, "NM_001449.4"),
		GLA(Tier1, "NM_000169.2"),
		KCNH2(Tier1, "NM_000238.3"),
		KCNQ1(Tier1, "NM_000218.2"),
		LDLR(Tier1, "NM_000527.4"),
		LMNA(Tier3, "NM_170707.3"),
		MYBPC3(Tier2, "NM_000256.3"),
		MYH11(Tier2, "NM_002474.2"),
		MYH7(Tier1, "NM_000257.2"),
		MYL2(Tier2, "NM_000432.3"),
		MYL3(Tier2, "NM_000258.2"),
		MYLK(Tier3, "NM_053025.3"),
		PCSK9(Tier2, "NM_174936.3"),
		PKP2(Tier2, "NM_004572.3"),
		PRKAG2(Tier2, "NM_016203.3"),
		RYR2(Tier2, "NM_001035.2"),
		SCN5A(Tier1, "NM_198056.2"),
		SLC2A10(Tier3, "NM_030777.3"),
		SMAD3(Tier2, "NM_005902.3"),
		TGFB2(Tier3, "NM_003238.3"),
		TGFB3(Tier3, "NM_003239.3"),
		TGFBR1(Tier1, "NM_004612.2"),
		TGFBR2(Tier1, "NM_003242.5"),
		TMEM43(Tier2, "NM_024334.2"),
		TNNI3(Tier2, "NM_006757.3"),
		TNNT2(Tier2, "NM_001001430.1"),
		TPM1(Tier2, "NM_001018005.1");
		private Tier tier;
		private String transcript;
		GeneN089(Tier tier, String transcript) {
			this.tier = tier;
			this.transcript = transcript;
		}
	}
	@Override
	default String clinicalReference(Gene gene) {
		if(gene instanceof GeneN089) switch((GeneN089) gene) {
			case ACTA2:		return "Eur J Hum Genet. 2011 May;19(5):520-4.";
			case ACTC1:		return "Int J Cardiol. 2016 Dec 1;224:461-472.";
			case APOB:		return "PLoS One. 2013 Apr 8;8(4):e60729.";
			case DSC2:		return "Circ Cardiovasc Genet. 2015;8(3):437-46.";
			case DSG2:		return "Circ Cardiovasc Genet. 2015;8(3):437-46.";
			case MYBPC3:	return "Mol Biol Rep. 2013 Jun;40(6):3969-76.";
			case MYH11:		return "Eur J Hum Genet. 2013 May;21(5):487-93.";
			case MYL2:		return "Mol Biol Rep. 2013 Jun;40(6):3969-76.";
			case MYL3:		return "Circulation. 2002 May 21;105(20):2337-40.";
			case PCSK9:		return "PLoS One. 2013 Apr 8;8(4):e60729.";
			case PKP2:		return "Circ Cardiovasc Genet. 2015;8(3):437-46.";
			case PRKAG2:	return "Neuromuscul Disord. 2006 Mar;16(3):178-82.";
			case RYR2:		return "Heart. 2017;103(12):901-909.";
			case SMAD3:		return "Orphanet J Rare Dis. 2015 Feb 3;10:9.";
			case TMEM43:	return "Hum Genet. 2013 Nov;132(11):1245-52.";
			case TNNI3:		return "J Clin Invest. 2003 Jan;111(2):209-16";
			case TNNT2:		return "Mol Biol Rep. 2013 Jun;40(6):3969-76.";
			case TPM1:		return "Mol Biol Rep. 2013 Jun;40(6):3969-76.";
			default:		return null;
		}
		return null;
	}
	@Override
	default String diseaseSubNameScientific(DiseaseSub diseaseSub) {
		if(diseaseSub instanceof DiseaseSubN089) switch ((DiseaseSubN089)diseaseSub) {
			case 엘러스_단로스_증후군:			return "Vascular Ehlers-Danlos Syndrome";
			case 마르판_증후군:				return "Marfan Syndrome";
			case 로이_디에츠_증후군:			return "Loeys-Dietz Syndrome";
			case 가족성_흉부대동맥류와_박리증:	return "Familial thoracic aortic aneurysms and dissections";
			case 카테콜아민성_다형성_심실성_빈맥:return "Catecholaminergic Polymorphic Ventricular Tachycardia";
			case 심장_긴간격_증후군:			return "Long QT Syndrome";
			case 브루가다_증후군:				return "Brugada Syndrome";
			case 비후성_심근병증:				return "Hypertrophic cardiomyopathy";
			case 확장성_심근병증:				return "Dilated cardiomyopathy";
			case 파브리병:					return "Fabry Disease";
			case 부정맥_유발성_우심실_심근병증:	return "Arrhythmogenic right ventricular cardiomyopathy";
			case 애머리_드라이푸스_증후군:		return "Emery-dreifuss syndrome";
			case 가족성_고콜레스테롤혈증:		return "Familial hypercholesterolemia";
			case 고호모시스테인혈전증:			return "Hyperhomocysteinemia";
			case 동맥비틀림증후군:			return "Arterial tortuosity syndrome";
		}
		return null;
	}
	@Override
	default DiseaseN089[] diseases() {
		return DiseaseN089.values();
	}
}
