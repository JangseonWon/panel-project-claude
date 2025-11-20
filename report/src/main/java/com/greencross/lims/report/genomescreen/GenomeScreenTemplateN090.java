package com.greencross.lims.report.genomescreen;

import lombok.Getter;
import lombok.experimental.Accessors;

import static com.greencross.lims.report.genomescreen.GenomeScreenTemplate.Tier.*;
import static com.greencross.lims.report.genomescreen.GenomeScreenTemplateN090.GeneN090.*;

public interface GenomeScreenTemplateN090<R extends GenomeScreenResource> extends GenomeScreenTemplate<R> {
	@Getter
	@Accessors(fluent = true)
	enum DiseaseN090 implements Disease {
		유전성_유방암_난소암_증후군(DiseaseSubN090.유전성_유방암_난소암_증후군),
		유방암_감수성(DiseaseSubN090.유방암_감수성),
		난소암_감수성(DiseaseSubN090.난소암_감수성),
		전립선암_감수성(DiseaseSubN090.전립선암_감수성),
		리_프라우메니_증후군(DiseaseSubN090.리_프라우메니_증후군),
		유전성_미만성_위암(DiseaseSubN090.위암),
		포이츠_제거스_증후군(DiseaseSubN090.포이츠_제거스_증후군),
		린치_증후군(DiseaseSubN090.린치_증후군),
		용종증_증후군(DiseaseSubN090.가족성_선종성_용종증, DiseaseSubN090.MUTYH_연관_용종증, DiseaseSubN090.연소성_용종증_증후군),
		폰히펠_린다우_증후군(DiseaseSubN090.폰히펠_린다우_증후군),
		다발성_내분비선종증(DiseaseSubN090.제1형_다발성_내분비선종증, DiseaseSubN090.제2형_다발성_내분비선종증),
		PTEN_과오종_증후군(DiseaseSubN090.PTEN_과오종_증후군),
		망막모세포종(DiseaseSubN090.망막모세포종),
		유전성_부신경절종_갈색세포종(DiseaseSubN090.유전성_부신경절종_갈색세포종),
		결절성_경화증(DiseaseSubN090.결절성_경화증),
		WT1_연관_윌름스_종양(DiseaseSubN090.WT1_연관_윌름스_종양),
		제2형_신경섬유종증(DiseaseSubN090.제2형_신경섬유종증);
		private DiseaseSub[] subs;
		DiseaseN090(DiseaseSubN090... diseaseSubs) {
			this.subs = diseaseSubs;
		}
	}
	@Override
	default String diseaseNameScientific(Disease disease) {
		if(disease instanceof DiseaseN090) switch ((DiseaseN090)disease) {
			case 유전성_유방암_난소암_증후군:	return "Hereditary Breast and Ovarian Cancer Syndrome";
			case 유방암_감수성:				return "Breast Cancer Susceptibility";
			case 난소암_감수성: 				return "Ovarian Cancer Susceptibility";
			case 전립선암_감수성: 				return "Prostate Cancer Susceptibility";
			case 리_프라우메니_증후군: 		return "Li–fraumeni syndrome";
			case 유전성_미만성_위암: 			return "Hereditary Diffuse Gastric Cancer";
			case 포이츠_제거스_증후군: 		return "Peutz Jeghers syndrome";
			case 린치_증후군: 				return "Lynch syndrome";
			case 용종증_증후군: 				return "Polyposis syndrome";
			case 폰히펠_린다우_증후군: 		return "Von hippel-lindau syndrome";
			case 다발성_내분비선종증: 			return "Multiple endocrine neoplasia";
			case PTEN_과오종_증후군: 			return "PTEN hamartoma tumor syndrome";
			case 망막모세포종: 				return "Retinoblastoma";
			case 유전성_부신경절종_갈색세포종: 	return "Hereditary paraganglioma pheochromocytoma syndrome";
			case 결절성_경화증: 				return "Tuberous sclerosis complex";
			case WT1_연관_윌름스_종양: 		return "WT1-related wilms tumor";
			case 제2형_신경섬유종증: 			return "Neurofibromatosis type 2";
			default:						return null;
		}
		return null;
	}
	@Getter
	@Accessors(fluent = true)
	enum DiseaseSubN090 implements DiseaseSub {
		유전성_유방암_난소암_증후군(BRCA1, BRCA2),
		유방암_감수성(ATM, CDH1, CHEK2, NBN, NF1, PALB2),
		난소암_감수성(BRIP1, RAD51C, RAD51D),
		전립선암_감수성(ATM, CHEK2, MLH1, MSH2, MSH6, PALB2, PMS2),
		리_프라우메니_증후군(TP53),
		위암(CDH1),
		포이츠_제거스_증후군(STK11),
		린치_증후군(EPCAM, MLH1, MSH2, MSH6, PMS2),
		가족성_선종성_용종증(APC),
		MUTYH_연관_용종증(MUTYH),
		연소성_용종증_증후군(BMPR1A, SMAD4),
		폰히펠_린다우_증후군(VHL),
		제1형_다발성_내분비선종증(MEN1),
		제2형_다발성_내분비선종증(RET),
		PTEN_과오종_증후군(PTEN),
		망막모세포종(RB1),
		유전성_부신경절종_갈색세포종(SDHAF2, SDHB, SDHC, SDHD),
		결절성_경화증(TSC1, TSC2),
		WT1_연관_윌름스_종양(WT1),
		제2형_신경섬유종증(NF2);
		private GeneN090[] genes;
		DiseaseSubN090(GeneN090... genes) {
			this.genes = genes;
		}
	}
	@Getter
	@Accessors(fluent = true)
	enum GeneN090 implements Gene {
		APC(Tier1, "NM_000038.5"),
		ATM(Tier3, "NM_000051.3"),
		BMPR1A(Tier2, "NM_004329.2"),
		BRCA1(Tier1, "NM_007294.3"),
		BRCA2(Tier1, "NM_000059.3"),
		BRIP1(Tier3, "NM_032043.2"),
		CDH1(Tier1, "NM_004360.3"),
		CHEK2(Tier3, "NM_007194.3"),
		EPCAM(Tier3, "NM_002354.2"),
		MEN1(Tier1, "NM_130799.2"),
		MLH1(Tier1, "NM_000249.3"),
		MSH2(Tier1, "NM_000251.2"),
		MSH6(Tier2, "NM_000179.2"),
		MUTYH(Tier2, "NM_001128425.1"),
		NBN(Tier3, "NM_002485.4"),
		NF1(Tier1, "NM_000267.3"),
		NF2(Tier1, "NM_000268.3"),
		PALB2(Tier2, "NM_024675.3"),
		PMS2(Tier2, "NM_000535.5"),
		PTEN(Tier1, "NM_000314.4"),
		RAD51C(Tier3, "NM_058216.2"),
		RAD51D(Tier3, "NM_002878.3"),
		RB1(Tier1, "NM_000321.2"),
		RET(Tier1, "NM_020975.4"),
		SDHAF2(Tier2, "NM_017841.2"),
		SDHB(Tier1, "NM_003000.2"),
		SDHC(Tier2, "NM_003001.3"),
		SDHD(Tier1, "NM_003002.2"),
		SMAD4(Tier1, "NM_005359.5"),
		STK11(Tier1, "NM_000455.4"),
		TP53(Tier1, "NM_000546.5"),
		TSC1(Tier1, "NM_000368.4"),
		TSC2(Tier1, "NM_000548.3"),
		VHL(Tier1, "NM_000551.3"),
		WT1(Tier2, "NM_024426.4");
		private Tier tier;
		private String transcript;
		GeneN090(Tier tier, String transcript) {
			this.tier = tier;
			this.transcript = transcript;
		}
	}
	@Override
	default String diseaseSubNameScientific(DiseaseSub diseaseSub) {
		if(diseaseSub instanceof DiseaseSubN090) switch ((DiseaseSubN090)diseaseSub) {
			case 유전성_유방암_난소암_증후군: 			return "Breast cancer, Ovarian cancer, Prostate cancer";
			case 유방암_감수성:						return "Breast cancer";
			case 난소암_감수성:						return "Ovarian cancer";
			case 전립선암_감수성:						return "Prostate cancer";
			case 리_프라우메니_증후군:					return "Breast cancer, Brain tumor, leukemia,\nAdrenocortical carcinoma etc.";
			case 위암:								return "Gastric cancer";
			case 포이츠_제거스_증후군:					return "Colorectal cancer, Gastric cancer";
			case 린치_증후군:							return "Colorectal cancer, Endometrial cancer,\nGastric cancer, Ovarian cancer etc.";
			case 가족성_선종성_용종증 :				return "Familial adenomatous polyposis";
			case MUTYH_연관_용종증 :					return "MUTYH-associated polyposis";
			case 연소성_용종증_증후군 :				return "Juvenile polyposis syndrome";
			case 폰히펠_린다우_증후군:					return "CNS hemangioblastoma, Retinal hemangioblastoma,\nPheochromocytoma";
			case 제1형_다발성_내분비선종증 :			return "Multiple Endocrine Neoplasia Type 1";
			case 제2형_다발성_내분비선종증 :			return "Multiple Endocrine Neoplasia Type 2";
			case PTEN_과오종_증후군:					return "Breast cancer, Thyroid cancer";
			case 망막모세포종 :						return "Retinoblastoma";
			case 유전성_부신경절종_갈색세포종:			return "Paraganglioma, Pheochromocytoma";
			case 결절성_경화증:						return "Retinal tumor, Brain tumor, Lung lymphoma etc.";
			case WT1_연관_윌름스_종양:					return "Renal cell carcinoma";
			case 제2형_신경섬유종증:					return "Acoustic Neuroma";
			default:								return null;
		}
		return null;
	}
	@Override
	default Disease[] diseases() {
		return DiseaseN090.values();
	}
	@Override
	default String clinicalReference(Gene gene) {
		if(gene instanceof GeneN090) switch((GeneN090)gene) {
			case BMPR1A:	return "J Med Genet. 2007 Nov;44(11):702-9.";
			case MSH6:		return "J Med Genet. 2010 Sep;47(9):579-85.";
			case MUTYH:		return "Nat Genet. 2002 Feb;30(2):227-32.";
			case PALB2:		return "Cancer Genet. 2015;208(9):455-63.";
			case PMS2:		return "Gastroenterology. 2008 Aug;135(2)419-28.";
			case SDHAF2:	return "Lancet Oncol. 2010 Apr;11(4):366-72.";
			case SDHC:		return "J Clin Endocrinol Metab. 2009 Aug;94(8):2817-27.";
			case WT1:		return "Br J Cancer. 2015 Mar 17;112(6):1121-33.";
			default:		return null;
		}
		return null;
	}
}
