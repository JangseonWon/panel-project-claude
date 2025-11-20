package com.gcgenome.lims.test.single;

import com.gcgenome.lims.test.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Arrays;

@Data
@Accessors(fluent = true)
@Builder
public class TestInfo implements HasCode, HasGene, HasReferralDefault, MayBeNationalInsurance, I18N {
	public enum Category {
		CANCER, BRCA, ETC, WITH_MLPA, CANCER_WITH_RD_REPORT
	}
	private final String code;
	private final String referralDefault;
	@Builder.Default
	private final Category category = Category.ETC;
	private final String title;
	private final String name;
	@Builder.Default
	private final String specimen = "Genomic DNA isolated from peripheral blood leukocytes";
	private final String target;
	private final String gene;
	private final String[] tier2;

	@Builder.Default
	private final String method = "Sequencing of all coding exons";// "PCR & Direct sequencing (All coding exons)";
	private final String penetrance;
	@Builder.Default
	private final boolean isNationalInsuranceTest = false;
	@Builder.Default
	private final String[] limitations = new String[] {
			"본 검사는 염기서열분석법으로 시행되었으며, promotor/deep intron 영역의 변이 또는 대규모의 결실/중복, 역위 등을 포함한 구조적 이상은 검출할 수 없습니다.",
			"본 검사에서 발견된 변이는 2015 ACMG/AMP guideline (Genet Med 2015;17:405-24)에 따라 \"pathogenic\", \"likely pathogenic\", \"uncertain significance\", \"likely benign\", \"benign\"의 다섯 가지 카테고리로 분류되며, Benign/Likely benign variant 결과는 보고하지 않습니다.",
			"관련 문헌과 데이터베이스의 추가적인 연구 결과에 따라 해당 변이의 분류가 변경될 수 있습니다."
	};
	@Builder.Default
	private final String[] references = new String[] {
			"GeneReviews (https://www.ncbi.nlm.nih.gov/books/NBK1116/)",
			"The Human Gene Mutation Database (http://www.hgmd.org)"
	};
	@Builder.Default
	private final String i18n = "KOKR";
	public static final TestInfo N027 = TestInfo.builder()
			.code("N027")
			.title("SLC26A4 gene mutation 결과보고서")
			.name("SLC26A4 gene mutation")
			.gene("SLC26A4").target("SLC26A4 on Chromosome 7q22.3")
			.tier2(new String[]{"ABHD12","ABCC1","ACOX1","ACTG1","ADGRV1","AIFM1","ALMS1","AP1S1","ATP11A","ATP2B2","ATP6V1B1","ATP6V1B2","BCS1L","BSND","CABP2","CCDC50","CDC14A","CDH23","CEACAM16","CEP250","CEP78","CHD7","CIB2","CISD2","CLDN14","CLDN9","CLIC5","CLPP","CLRN1","CLRN2","COCH","COG4","COL11A1","COL11A2","COL2A1","COL4A5","COL4A6","COL9A1","COL9A2","COL9A3","CRLS1","CRYM","DIABLO","DIAPH1","DIAPH3","DMXL2","DNAJC3","DNMT1","DSPP","EDN3","EDNRB","ELMOD3","EPS8","EPS8L2","ESPN","ESRP1","ESRRB","EYA1","EYA4","FDXR","FGF3","FOXF2","FOXI1","GATA3","GGPS1","GIPC3","GJB2","GJB3","GJB6","GPR156","GPSM2","GREB1L","GRHL2","GRXCR1","GSDME","HAAO","HARS2","HGF","HOMER2","HOXA2","HSD17B4","ILDR1","KARS1","KCNE1","KCNJ10","KCNJ16","KCNQ1","KCNQ4","KDM3B","KIT","KITLG","LARS2","LETM1","LHFPL5","LMX1A","LOXHD1","LRTOMT","MARVELD2","MASP1","MET","MITF","MN1","MORC2","MPZL2","MSRB3","MYH14","MYH9","MYO15A","MYO3A","MYO6","MYO7A","NARS2","OGDHL","OPA1","OSBPL2","OTOA","OTOF","OTOG","OTOGL","OXR1","P2RX2","PAX2","PAX3","PBX1","PCDH15","PDSS1","PDZD7","PJVK","PLS1","PMP22","PNPT1","POU3F4","POU4F3","PPIP5K2","PRPS1","PSMC3","PTPRQ","RDX","RIPOR2","RNF220","ROR1","S1PR2","SALL1","SALL4","SERAC1","SERPINB6","SGPL1","SIX1","SLC12A2","SLC17A8","SLC26A5","SLC4A11","SLC52A2","SLC52A3","SLITRK6","SMPX","SNAI2","SOX10","SOX2","SPATA5","SPATA5L1","SPATC1L","SPNS2","SPTBN4","STRC","STX4","STXBP3","SYNE4","TBC1D24","TECTA","THOC1","TIMM8A","TMC1","TMIE","TMPRSS3","TMTC2","TNC","TOP2B","TPRN","TRIOBP","USH1C","USH1G","USH2A","USP48","WBP2","WFS1","WHRN","YARS1"})
			.isNationalInsuranceTest(true).build();
	public static final TestInfo S030 = TestInfo.builder()
			.code("S030")
			.referralDefault("Hemophilia A")
			.title("F8 gene mutation 결과보고서")
			.name("F8 gene mutation")
			.gene("F8").target("F8 on Chromosome Xq28")
			.tier2(new String[]{"ABCG5","ABCG8","ACTB","ACTN1","ACVRL1","ADAMTS13","ANKRD26","ANO6","AP3B1","AP3D1","ARPC1B","BLOC1S3","BLOC1S5","BLOC1S6","BMPR2","CDC42","CHST14","COL1A1","COL3A1","COL5A1","COL5A2","CYCS","DIAPH1","DTNBP1","ENG","EPHB2","ETV6","F10","F11","F12","F13A1","F13B","F2","F2R","F5","F7","F9","FERMT3","FGA","FGB","FGG","FLI1","FLNA","FYB1","GALE","GATA1","GBA","GFI1B","GGCX","GNE","GP1BA","GP1BB","GP6","GP9","HOXA11","HPS1","HPS3","HPS4","HPS5","HPS6","HRG","IKZF5","ITGA2B","ITGB3","KDSR","KLKB1","KNG1","LAT","LMAN1","LYST","MCFD2","MECOM","MPIG6B","MPL","MRTFA","MYH9","NBEA","NBEAL2","ORAI1","P2RX1","P2RY12","PLA2G4A","PLAT","PLAU","PLG","PROC","PROS1","PTGS1","PTPN11","PTPRJ","RASGRP2","RBM8A","RGS2","RUNX1","SERPINC1","SERPIND1","SERPINE1","SERPINF2","SLC35A1","SLC45A2","SLFN14","SMAD4","SRC","STIM1","STXBP2","TBXA2R","TBXAS1","THBD","THPO","TNXB","TPM4","TRPM7","TUBB1","UNC13D","VIPAS39","VKORC1","VPS33B","VWF","WAS"})
			.isNationalInsuranceTest(true).build();
	public static final TestInfo S032 = TestInfo.builder()
			.code("S032")
			.referralDefault("Hemophilia B")
			.title("F9 gene mutation 결과보고서")
			.name("F9 gene mutation")
			.gene("F9").target("F9 on Chromosome Xq27.1")
			.tier2(new String[]{"ABCG5","ABCG8","ACTB","ACTN1","ACVRL1","ADAMTS13","ANKRD26","ANO6","AP3B1","AP3D1","ARPC1B","BLOC1S3","BLOC1S5","BLOC1S6","BMPR2","CDC42","CHST14","COL1A1","COL3A1","COL5A1","COL5A2","CYCS","DIAPH1","DTNBP1","ENG","EPHB2","ETV6","F10","F11","F12","F13A1","F13B","F2","F2R","F5","F7","F8","FERMT3","FGA","FGB","FGG","FLI1","FLNA","FYB1","GALE","GATA1","GBA","GFI1B","GGCX","GNE","GP1BA","GP1BB","GP6","GP9","HOXA11","HPS1","HPS3","HPS4","HPS5","HPS6","HRG","IKZF5","ITGA2B","ITGB3","KDSR","KLKB1","KNG1","LAT","LMAN1","LYST","MCFD2","MECOM","MPIG6B","MPL","MRTFA","MYH9","NBEA","NBEAL2","ORAI1","P2RX1","P2RY12","PLA2G4A","PLAT","PLAU","PLG","PROC","PROS1","PTGS1","PTPN11","PTPRJ","RASGRP2","RBM8A","RGS2","RUNX1","SERPINC1","SERPIND1","SERPINE1","SERPINF2","SLC35A1","SLC45A2","SLFN14","SMAD4","SRC","STIM1","STXBP2","TBXA2R","TBXAS1","THBD","THPO","TNXB","TPM4","TRPM7","TUBB1","UNC13D","VIPAS39","VKORC1","VPS33B","VWF","WAS"})
			.isNationalInsuranceTest(true).build();
	public static final TestInfo S034 = TestInfo.builder()
			.code("S034")
			.title("VWF gene mutation 결과보고서")
			.name("VWF gene mutation")
			.gene("VWF").target("VWF on Chromosome 12p13.31")
			.tier2(new String[]{"ABCG5","ABCG8","ACTB","ACTN1","ACVRL1","ADAMTS13","ANKRD26","ANO6","AP3B1","AP3D1","ARPC1B","BLOC1S3","BLOC1S5","BLOC1S6","BMPR2","CDC42","CHST14","COL1A1","COL3A1","COL5A1","COL5A2","CYCS","DIAPH1","DTNBP1","ENG","EPHB2","ETV6","F10","F11","F12","F13A1","F13B","F2","F2R","F5","F7","F8","F9","FERMT3","FGA","FGB","FGG","FLI1","FLNA","FYB1","GALE","GATA1","GBA","GFI1B","GGCX","GNE","GP1BA","GP1BB","GP6","GP9","HOXA11","HPS1","HPS3","HPS4","HPS5","HPS6","HRG","IKZF5","ITGA2B","ITGB3","KDSR","KLKB1","KNG1","LAT","LMAN1","LYST","MCFD2","MECOM","MPIG6B","MPL","MRTFA","MYH9","NBEA","NBEAL2","ORAI1","P2RX1","P2RY12","PLA2G4A","PLAT","PLAU","PLG","PROC","PROS1","PTGS1","PTPN11","PTPRJ","RASGRP2","RBM8A","RGS2","RUNX1","SERPINC1","SERPIND1","SERPINE1","SERPINF2","SLC35A1","SLC45A2","SLFN14","SMAD4","SRC","STIM1","STXBP2","TBXA2R","TBXAS1","THBD","THPO","TNXB","TPM4","TRPM7","TUBB1","UNC13D","VIPAS39","VKORC1","VPS33B","WAS"})
			.isNationalInsuranceTest(true).build();
	public static final TestInfo S053 = TestInfo.builder()
			.code("S053")
			.referralDefault("F7 deficiency")
			.title("F7 gene mutation 결과보고서")
			.name("F7 gene mutation")
			.gene("F7").target("F7 on Chromosome 13q34")
			.tier2(new String[]{"ABCG5","ABCG8","ACTB","ACTN1","ACVRL1","ADAMTS13","ANKRD26","ANO6","AP3B1","AP3D1","ARPC1B","BLOC1S3","BLOC1S5","BLOC1S6","BMPR2","CDC42","CHST14","COL1A1","COL3A1","COL5A1","COL5A2","CYCS","DIAPH1","DTNBP1","ENG","EPHB2","ETV6","F10","F11","F12","F13A1","F13B","F2","F2R","F5","F8","F9","FERMT3","FGA","FGB","FGG","FLI1","FLNA","FYB1","GALE","GATA1","GBA","GFI1B","GGCX","GNE","GP1BA","GP1BB","GP6","GP9","HOXA11","HPS1","HPS3","HPS4","HPS5","HPS6","HRG","IKZF5","ITGA2B","ITGB3","KDSR","KLKB1","KNG1","LAT","LMAN1","LYST","MCFD2","MECOM","MPIG6B","MPL","MRTFA","MYH9","NBEA","NBEAL2","ORAI1","P2RX1","P2RY12","PLA2G4A","PLAT","PLAU","PLG","PROC","PROS1","PTGS1","PTPN11","PTPRJ","RASGRP2","RBM8A","RGS2","RUNX1","SERPINC1","SERPIND1","SERPINE1","SERPINF2","SLC35A1","SLC45A2","SLFN14","SMAD4","SRC","STIM1","STXBP2","TBXA2R","TBXAS1","THBD","THPO","TNXB","TPM4","TRPM7","TUBB1","UNC13D","VIPAS39","VKORC1","VPS33B","VWF","WAS"})
			.isNationalInsuranceTest(true).build();
	public static final TestInfo S054 = TestInfo.builder()
			.code("S054")
			.referralDefault("F11 deficiency")
			.title("F11 gene mutation 결과보고서")
			.name("F11 gene mutation")
			.gene("F11").target("F11 on Chromosome 4q35.2")
			.tier2(new String[]{"ABCG5","ABCG8","ACTB","ACTN1","ACVRL1","ADAMTS13","ANKRD26","ANO6","AP3B1","AP3D1","ARPC1B","BLOC1S3","BLOC1S5","BLOC1S6","BMPR2","CDC42","CHST14","COL1A1","COL3A1","COL5A1","COL5A2","CYCS","DIAPH1","DTNBP1","ENG","EPHB2","ETV6","F10","F12","F13A1","F13B","F2","F2R","F5","F7","F8","F9","FERMT3","FGA","FGB","FGG","FLI1","FLNA","FYB1","GALE","GATA1","GBA","GFI1B","GGCX","GNE","GP1BA","GP1BB","GP6","GP9","HOXA11","HPS1","HPS3","HPS4","HPS5","HPS6","HRG","IKZF5","ITGA2B","ITGB3","KDSR","KLKB1","KNG1","LAT","LMAN1","LYST","MCFD2","MECOM","MPIG6B","MPL","MRTFA","MYH9","NBEA","NBEAL2","ORAI1","P2RX1","P2RY12","PLA2G4A","PLAT","PLAU","PLG","PROC","PROS1","PTGS1","PTPN11","PTPRJ","RASGRP2","RBM8A","RGS2","RUNX1","SERPINC1","SERPIND1","SERPINE1","SERPINF2","SLC35A1","SLC45A2","SLFN14","SMAD4","SRC","STIM1","STXBP2","TBXA2R","TBXAS1","THBD","THPO","TNXB","TPM4","TRPM7","TUBB1","UNC13D","VIPAS39","VKORC1","VPS33B","VWF","WAS"})
			.isNationalInsuranceTest(true).build();
	public static final TestInfo S055 = TestInfo.builder()
			.code("S055")
			.referralDefault("F12 deficiency")
			.title("F12 gene mutation 결과보고서")
			.name("F12 gene mutation")
			.gene("F12").target("F12 on Chromosome 5q35.3")
			.tier2(new String[]{"ABCG5","ABCG8","ACTB","ACTN1","ACVRL1","ADAMTS13","ANKRD26","ANO6","AP3B1","AP3D1","ARPC1B","BLOC1S3","BLOC1S5","BLOC1S6","BMPR2","CDC42","CHST14","COL1A1","COL3A1","COL5A1","COL5A2","CYCS","DIAPH1","DTNBP1","ENG","EPHB2","ETV6","F10","F11","F13A1","F13B","F2","F2R","F5","F7","F8","F9","FERMT3","FGA","FGB","FGG","FLI1","FLNA","FYB1","GALE","GATA1","GBA","GFI1B","GGCX","GNE","GP1BA","GP1BB","GP6","GP9","HOXA11","HPS1","HPS3","HPS4","HPS5","HPS6","HRG","IKZF5","ITGA2B","ITGB3","KDSR","KLKB1","KNG1","LAT","LMAN1","LYST","MCFD2","MECOM","MPIG6B","MPL","MRTFA","MYH9","NBEA","NBEAL2","ORAI1","P2RX1","P2RY12","PLA2G4A","PLAT","PLAU","PLG","PROC","PROS1","PTGS1","PTPN11","PTPRJ","RASGRP2","RBM8A","RGS2","RUNX1","SERPINC1","SERPIND1","SERPINE1","SERPINF2","SLC35A1","SLC45A2","SLFN14","SMAD4","SRC","STIM1","STXBP2","TBXA2R","TBXAS1","THBD","THPO","TNXB","TPM4","TRPM7","TUBB1","UNC13D","VIPAS39","VKORC1","VPS33B","VWF","WAS"})
			.isNationalInsuranceTest(true).build();
	public static final TestInfo S061 = TestInfo.builder()
			.code("S061").category(Category.CANCER)
			.title("MEN1 gene mutation 결과보고서")
			.name("MEN1 gene mutation")
			.gene("MEN1").target("MEN1 on Chromosome 11q13.1")
			.isNationalInsuranceTest(true).build();
	public static final TestInfo S062 = TestInfo.builder()
			.code("S062").category(Category.CANCER)
			.title("NF2 gene mutation 결과보고서")
			.name("NF2 gene mutation")
			.gene("NF2").target("NF2 on Chromosome 22q12.2")
			.isNationalInsuranceTest(true).build();
	public static final TestInfo S063 = TestInfo.builder()
			.code("S063").category(Category.CANCER)
			.title("PTEN gene mutation 결과보고서")
			.name("PTEN gene mutation")
			.gene("PTEN").target("PTEN on Chromosome 10q23.31")
			.isNationalInsuranceTest(true).build();
	public static final TestInfo S064 = TestInfo.builder()
			.code("S064")
			.title("PTPN11 gene mutation 결과보고서")
			.name("PTPN11 gene mutation")
			.gene("PTPN11").target("PTPN11 on Chromosome 12q24.13")
			.tier2(new String[]{"BRAF","CBL","HRAS","KRAS","LZTR1","MAP2K1","MAP2K2","MRAS","NF1","NRAS","PPP1CB","RAF1","RIT1","RRAS2","SHOC2","SOS1","SOS2","SPRED1","RASA2","RRAS","SPRED2"})
			.isNationalInsuranceTest(true).build();
	public static final TestInfo S065 = TestInfo.builder()
			.code("S065")
			.referralDefault("Wilson disease")
			.title("ATP7B gene mutation 결과보고서")
			.name("ATP7B gene mutation")
			.gene("ATP7B").target("ATP7B on Chromosome 13q14.3")
			.tier2(new String[]{"ATP13A2","ATP1A3","C19orf12","CSF1R","DCTN1","DNAJC6","FBXO7","FTL","GBA","GCH1","GRN","LRRK2","LYST","MAPT","OPA3","PANK2","PARK7","PINK1","PLA2G6","PRKN","PRKRA","PTRHD1","RAB39B","SLC30A10","SLC39A14","SLC6A3","SNCA","SPG11","SPR","SYNJ1","TH","TUBB4A","VPS13A","VPS35","WDR45","ANG","CHCHD2","CLN3","COASY","CP","DNAJC12","DNAJC5","EPHB4","PDE8B","PDGFB","PDGFRB","SLC20A2","TAF1","TWNK","VPS13C","XPR1"})
			.isNationalInsuranceTest(true).build();
	public static final TestInfo S067 = TestInfo.builder()
			.code("S067")
			.title("HPRT1 gene mutation 결과보고서")
			.name("HPRT1 gene mutation")
			.gene("HPRT1").target("HPRT1 on Chromosome Xq26.2-q26.3")
			.tier2(new String[]{"AARS2","AASS","ABAT","ABCA1","ABCB11","ABCB4","ABCB7","ABCD1","ABCD4","ABCG5","ABCG8","ABHD12","ABHD5","ACAD8","ACAD9","ACADM","ACADS","ACADSB","ACADVL","ACAT1","ACO2","ACOX1","ACSF3","ACY1","ADA","ADAR","ADSL","AFG3L2","AGA","AGK","AGL","AGPS","AGXT","AHCY","AIFM1","AKR1D1","ALAD","ALAS2","ALDH18A1","ALDH3A2","ALDH4A1","ALDH5A1","ALDH6A1","ALDH7A1","ALDOA","ALDOB","ALG1","ALG11","ALG12","ALG14","ALG3","ALG6","ALG8","ALG9","ALPL","AMACR","AMN","AMT","ANO10","APOA1","APOA5","APOB","APOC2","APOE","APRT","APTX","ARG1","ARSA","ARSB","ARSL","ASAH1","ASL","ASPA","ASS1","ATAD3A","ATIC","ATP13A2","ATP6AP1","ATP6V0A2","ATP7A","ATP7B","ATP8B1","ATPAF2","AUH","B3GALNT2","B3GALT6","B3GAT3","B3GLCT","B4GALT1","B4GALT7","BAAT","BCKDHA","BCKDHB","BCKDK","BCS1L","BOLA3","BTD","C19orf12","CA5A","CAT","CBLIF","CBS","CCDC115","CHCHD10","CHKB","CHST14","CHST3","CHST6","CHSY1","CISD2","CLDN16","CLDN19","CLN3","CLN5","CLN6","CLN8","CLPB","CLPP","CNNM2","COA8","COG1","COG4","COG5","COG6","COG7","COG8","COQ2","COQ4","COQ6","COQ7","COQ8A","COQ8B","COQ9","COX10","COX14","COX15","COX20","COX6A1","COX6B1","COX7B","CP","CPOX","CPS1","CPT1A","CPT2","CRPPA","CTH","CTNS","CTSA","CTSC","CTSD","CTSK","CUBN","CYC1","CYP27A1","CYP7B1","D2HGDH","DARS1","DARS2","DBH","DBT","DCXR","DDC","DGUOK","DHCR24","DHCR7","DHFR","DHODH","DHTKD1","DLAT","DLD","DNA2","DNAJC12","DNAJC19","DNAJC5","DNM1L","DNM2","DOLK","DPAGT1","DPM1","DPM2","DPM3","DPYD","DPYS","DYM","EARS2","EBP","ECHS1","ELAC2","ENO3","EPG5","EPM2A","ETFA","ETFB","ETFDH","ETHE1","EXT1","EXT2","FA2H","FAH","FAR1","FARS2","FASTKD2","FBP1","FBXL4","FDX2","FECH","FGFR2","FH","FKRP","FKTN","FMO3","FOLR1","FOXRED1","FTCD","FUCA1","FUT8","FXN","G6PC1","G6PC3","GAA","GABRG2","GALC","GALE","GALK1","GALNS","GALNT3","GALT","GAMT","GARS1","GATM","GBA","GBE1","GCDH","GCH1","GCLC","GDAP1","GFER","GFM1","GFPT1","GK","GLA","GLB1","GLDC","GLRA1","GLRX5","GLUD1","GLUL","GLYCTK","GM2A","GMPPB","GNE","GNMT","GNPAT","GNPTAB","GNPTG","GNS","GPD1","GPHN","GRHPR","GSS","GTPBP3","GUSB","GYG1","GYS1","GYS2","HAAO","HADH","HADHA","HADHB","HAMP","HARS2","HCCS","HCFC1","HEXA","HEXB","HFE","HGD","HGSNAT","HIBCH","HJV","HLCS","HMBS","HMGCL","HMGCS2","HOGA1","HPD","HPS1","HS2ST1","HSD17B10","HSD17B4","HSD3B7","HSPD1","HTRA2","HYAL1","IARS2","IBA57","IDH2","IDS","IDUA","IER3IP1","ISCA2","ISCU","ITPA","IVD","KARS1","KYNU","L2HGDH","LAMP2","LARGE1","LARS1","LARS2","LBR","LCAT","LCT","LDHA","LDLR","LDLRAP1","LIAS","LIPA","LIPT1","LMBRD1","LONP1","LPIN1","LPL","LRPPRC","MAGT1","MAN1B1","MAN2B1","MANBA","MAOA","MARS2","MAT1A","MCCC1","MCCC2","MCEE","MCOLN1","MFF","MFN2","MFSD8","MGAT2","MGME1","MLYCD","MMAA","MMAB","MMACHC","MMADHC","MMUT","MOCS1","MOCS2","MOGS","MPDU1","MPI","MPV17","MRPL3","MRPS22","MSMO1","MTFMT","MTHFR","MTO1","MTPAP","MTR","MTRFR","MTRR","MTTP","MVK","NAGA","NAGLU","NAGS","NARS2","NDUFA1","NDUFA10","NDUFA11","NDUFA2","NDUFAF1","NDUFAF2","NDUFAF3","NDUFAF4","NDUFAF5","NDUFAF6","NDUFB11","NDUFB3","NDUFS1","NDUFS2","NDUFS3","NDUFS4","NDUFS6","NDUFS7","NDUFS8","NDUFV1","NDUFV2","NEU1","NFU1","NGLY1","NHLRC1","NNT","NPC1","NPC2","NSDHL","NT5C3A","NUBPL","OAT","OCRL","OPA1","OPA3","OTC","OXCT1","PAH","PANK2","PC","PCBD1","PCCA","PCCB","PCK1","PCSK9","PDHA1","PDHB","PDHX","PDP1","PDSS1","PDSS2","PEPD","PET100","PEX1","PEX10","PEX11B","PEX12","PEX13","PEX14","PEX16","PEX19","PEX2","PEX26","PEX3","PEX5","PEX6","PEX7","PFKM","PGAM2","PGAP2","PGAP3","PGK1","PGM1","PGM3","PHGDH","PHKA1","PHKA2","PHKB","PHKG2","PHYH","PIGA","PIGL","PIGN","PIGO","PIGT","PIGV","PINK1","PLA2G6","PMM2","PMPCA","PNP","PNPO","PNPT1","POLG","POLG2","POMGNT1","POMGNT2","POMT1","POMT2","POR","PPA2","PPOX","PPT1","PRKAG2","PRODH","PRPS1","PSAP","PSAT1","PTS","PUS1","PYCR1","PYGL","PYGM","QDPR","RARS2","RBCK1","RBP4","RFT1","RMND1","RNASEH1","RPIA","RPL10","RRM2B","RXYLT1","SACS","SAMHD1","SAR1B","SARS2","SC5D","SCO1","SCO2","SCP2","SDHA","SDHAF1","SDHB","SDHD","SEC23B","SERAC1","SETX","SGSH","SI","SKIV2L","SLC12A3","SLC16A1","SLC17A5","SLC18A2","SLC19A2","SLC19A3","SLC22A5","SLC25A1","SLC25A12","SLC25A13","SLC25A15","SLC25A19","SLC25A20","SLC25A22","SLC25A26","SLC25A3","SLC25A38","SLC25A4","SLC25A46","SLC2A1","SLC2A2","SLC30A10","SLC35A1","SLC35A2","SLC35C1","SLC35D1","SLC37A4","SLC39A14","SLC39A4","SLC39A8","SLC3A1","SLC40A1","SLC46A1","SLC52A2","SLC52A3","SLC5A1","SLC6A19","SLC6A20","SLC6A3","SLC6A8","SLC7A7","SLC7A9","SMPD1","SPG7","SPR","SPTLC1","SPTLC2","SRD5A3","SSR4","ST3GAL3","ST3GAL5","STS","SUCLA2","SUCLG1","SUMF1","SUOX","SURF1","TACO1","TAFAZZIN","TALDO1","TANGO2","TAT","TCN2","TFR2","TIMM8A","TK2","TMEM165","TMEM70","TPK1","TPP1","TRAP1","TREX1","TRIM37","TRMU","TRNT1","TRPM6","TSFM","TTC19","TTC37","TTPA","TUFM","TUSC3","TWNK","TYMP","UGT1A1","UMOD","UMPS","UQCRB","UROD","UROS","VARS2","VIPAS39","VKORC1","VPS33B","WDR45","WFS1","XDH","XYLT1","XYLT2","YARS2","ALG13","ATP5F1A","ATP5F1E","COX4I2","CSTB","DHDDS","GLS","HSPA9","LIPC","MRPS16","NDUFA12","NDUFB9","NDUFC2","OPLAH","PDK3","PIGM","PSPH","RANBP2","RNASET2","RYR1","SDHAF2","SDHC","STAT2","TH","UQCRQ","UROC1"})
			.isNationalInsuranceTest(true).build();
	public static final TestInfo S123 = TestInfo.builder()
			.code("S123")
			.title("PHEX gene mutation 결과보고서")
			.name("PHEX gene mutation")
			.gene("PHEX").target("PHEX on Chromosome Xp22.2-p22.1")
			.isNationalInsuranceTest(true).build();
	public static final TestInfo S125 = TestInfo.builder()
			.code("S125")
			.title("OPA1 gene mutation 결과보고서")
			.name("OPA1 gene mutation")
			.gene("OPA1").target("OPA1 on Chromosome 3q29")
			.referralDefault("Optic atrophy 1")
			.isNationalInsuranceTest(true).build();
	public static final TestInfo S126 = TestInfo.builder()
			.code("S126")
			.title("SCN4A gene mutation 결과보고서")
			.name("SCN4A gene mutation")
			.gene("SCN4A").target("SCN4A on Chromosome 17q23.3")
			.referralDefault("Skeletal Muscle Channelopathy")
			.isNationalInsuranceTest(true).build();
	public static final TestInfo S127 = TestInfo.builder()
			.code("S127")
			.title("SLC12A3 gene mutation 결과보고서")
			.name("SLC12A3 gene mutation")
			.gene("SLC12A3").target("SLC12A3 on Chromosome 16q13")
			.referralDefault("Skeletal Muscle Channelopathy")
			.isNationalInsuranceTest(true).build();
	public static final TestInfo S128 = TestInfo.builder()
			.code("S128").category(Category.CANCER_WITH_RD_REPORT)
			.title("TSC1 gene mutation 결과보고서")
			.name("TSC1 gene mutation")
			.gene("TSC1").target("TSC1 on Chromosome 9q34.13")
			.referralDefault("Tuberous sclerosis complex")
			.isNationalInsuranceTest(true).build();
	public static final TestInfo S129 = TestInfo.builder()
			.code("S129").category(Category.CANCER_WITH_RD_REPORT)
			.title("TSC2 gene mutation 결과보고서")
			.name("TSC2 gene mutation")
			.gene("TSC2").target("TSC2 on Chromosome 16p13.3")
			.referralDefault("Tuberous sclerosis complex")
			.isNationalInsuranceTest(true).build();
	public static final TestInfo X009 = TestInfo.builder()
			.code("X009").category(Category.BRCA)
			.title("TP53 gene mutation 결과보고서")
			.name("TP53 gene mutation")
			.gene("TP53").target("TP53 on Chromosome 17p13.1")
			.isNationalInsuranceTest(true).build();
	public static final TestInfo Z141 = TestInfo.builder()
			.code("Z141").category(Category.CANCER)
			.title("MLH1 gene mutation 결과보고서")
			.name("MLH1 gene mutation")
			.gene("MLH1").target("MLH1 on Chromosome 3p22.2")
			.penetrance("Colorectal cancer(52%-82%), Endometrial cancer(25%-60%), Gastric cancer(6%-13%), Ovarian cancer(4-12%)")
			.isNationalInsuranceTest(true).build();
	public static final TestInfo Z962 = TestInfo.builder()
			.code("Z962").category(Category.CANCER)
			.title("MSH2 gene mutation 결과보고서")
			.name("MSH2 gene mutation")
			.gene("MSH2").target("MSH2 on Chromosome 2p21-p16")
			.penetrance("Colorectal cancer(52%-82%), Endometrial cancer(25%-60%), Gastric cancer(6%-13%), Ovarian cancer(4-12%)")
			.isNationalInsuranceTest(true).build();
	public static final TestInfo Z964 = TestInfo.builder()
			.code("Z964").category(Category.CANCER)
			.title("APC gene mutation 결과보고서")
			.name("APC gene mutation")
			.gene("APC").target("APC on Chromosome 5q22.2")
			.isNationalInsuranceTest(true).build();
	public static final TestInfo Z137 = TestInfo.builder()
			.code("Z137").category(Category.BRCA)
			.title("BRCA1 gene mutation 결과보고서")
			.name("BRCA1 gene mutation")
			.gene("BRCA1").target("BRCA1 on Chromosome 17q21.31")
			.penetrance("Breast cancer(50-80%), Secondary Breast cancer(27% within 5 yrs), Ovarian cancer(24-40%)")
			.limitations(new String[] {
					"유방암은 유전성, 가족성, 산발성으로 분류되며, 이 중 유전성 유방암은 전체 유방암의 약 5-10%를 차지합니다. KOHBRA study에 의하면 BRCA 병원성 변이는 유방암/난소암 가족력이 있는 유방암 환자의 약 22% 정도에서 발견됩니다 (Fam Cancer 2013;12:75-81).",
					"BRCA 유전자 검사 시 Variant of uncertain clinical significance (VUS)는 2-5% 정도에서 발견되며 유방암/난소암과의 연관성이 아직 확실히 밝혀지지 않은 변이입니다. 이 변이의 임상적 의미를 확인하기 위해서는 가족 검사를 통한 segregation analysis, control study 등이 필요합니다.",
					"검사에서 발견된 변이는 2015 ACMG/AMP guideline (Genet Med 2015;17:405-24)에 따라 \"pathogenic\", \"likely pathogenic\", \"uncertain significance\", \"likely benign\", \"benign\" 의 다섯가지 카테고리로 분류되며, \"likely benign\"과 \"benign\"에 합당한 변이는 보고하지 않고 있습니다. 또한, 관련 문헌과 데이터베이스의 추가적인 연구 결과에 따라 해당 변이의 분류가 변경될 수 있습니다."
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo Z138 = TestInfo.builder()
			.code("Z138").category(Category.BRCA)
			.title("BRCA2 gene mutation 결과보고서")
			.name("BRCA2 gene mutation")
			.gene("BRCA2").target("BRCA2 on Chromosome 13q13.1")
			.penetrance("Breast cancer(40-70%), Secondary breast cancer(12% within 5 yrs, 40-50% at 20 yrs), Ovarian cancer(11-18%)")
			.limitations(new String[] {
					"유방암은 유전성, 가족성, 산발성으로 분류되며, 이 중 유전성 유방암은 전체 유방암의 약 5-10%를 차지합니다. KOHBRA study에 의하면 BRCA 병원성 변이는 유방암/난소암 가족력이 있는 유방암 환자의 약 22% 정도에서 발견됩니다 (Fam Cancer 2013;12:75-81).",
					"BRCA 유전자 검사 시 Variant of uncertain clinical significance (VUS)는 2-5% 정도에서 발견되며 유방암/난소암과의 연관성이 아직 확실히 밝혀지지 않은 변이입니다. 이 변이의 임상적 의미를 확인하기 위해서는 가족 검사를 통한 segregation analysis, control study 등이 필요합니다.",
					"검사에서 발견된 변이는 2015 ACMG/AMP guideline (Genet Med 2015;17:405-24)에 따라 \"pathogenic\", \"likely pathogenic\", \"uncertain significance\", \"likely benign\", \"benign\" 의 다섯가지 카테고리로 분류되며, \"likely benign\"과 \"benign\"에 합당한 변이는 보고하지 않고 있습니다. 또한, 관련 문헌과 데이터베이스의 추가적인 연구 결과에 따라 해당 변이의 분류가 변경될 수 있습니다."
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo G068 = TestInfo.builder()
			.code("G068").category(Category.BRCA)
			.title("BRCA1 gene mutation / 전용 결과보고서")
			.name("BRCA1 gene mutation / 전용")
			.gene("BRCA1").target("BRCA1 on Chromosome 17q21.31")
			.penetrance("Breast cancer (50-80%), Secondary Breast cancer (27% within 5 yrs), Ovarian cancer (24-40%)")
			.limitations(new String[] {
					"유방암은 유전성, 가족성, 산발성으로 분류되며, 이 중 유전성 유방암은 전체 유방암의 약 5-10%를 차지합니다. KOHBRA study에 의하면 BRCA 병원성 변이는 유방암/난소암 가족력이 있는 유방암 환자의 약 22% 정도에서 발견됩니다 (Fam Cancer 2013;12:75-81).",
					"BRCA 유전자 검사 시 Variant of uncertain clinical significance (VUS)는 2-5% 정도에서 발견되며 유방암/난소암과의 연관성이 아직 확실히 밝혀지지 않은 변이입니다. 이 변이의 임상적 의미를 확인하기 위해서는 가족 검사를 통한 segregation analysis, control study 등이 필요합니다.",
					"검사에서 발견된 변이는 2015 ACMG/AMP guideline (Genet Med 2015;17:405-24)에 따라 \"pathogenic\", \"likely pathogenic\", \"uncertain significance\", \"likely benign\", \"benign\" 의 다섯가지 카테고리로 분류되며, \"likely benign\"과 \"benign\"에 합당한 변이는 보고하지 않고 있습니다. 또한, 관련 문헌과 데이터베이스의 추가적인 연구 결과에 따라 해당 변이의 분류가 변경될 수 있습니다."
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo G069 = TestInfo.builder()
			.code("G069").category(Category.BRCA)
			.title("BRCA2 gene mutation / 전용 결과보고서")
			.name("BRCA2 gene mutation / 전용")
			.gene("BRCA2").target("BRCA2 on Chromosome 13q13.1")
			.penetrance("Breast cancer (40-70%), Secondary breast cancer (12% within 5 yrs, 40-50% at 20 yrs), Ovarian cancer (11-18%)")
			.limitations(new String[] {
					"유방암은 유전성, 가족성, 산발성으로 분류되며, 이 중 유전성 유방암은 전체 유방암의 약 5-10%를 차지합니다. KOHBRA study에 의하면 BRCA 병원성 변이는 유방암/난소암 가족력이 있는 유방암 환자의 약 22% 정도에서 발견됩니다 (Fam Cancer 2013;12:75-81).",
					"BRCA 유전자 검사 시 Variant of uncertain clinical significance (VUS)는 2-5% 정도에서 발견되며 유방암/난소암과의 연관성이 아직 확실히 밝혀지지 않은 변이입니다. 이 변이의 임상적 의미를 확인하기 위해서는 가족 검사를 통한 segregation analysis, control study 등이 필요합니다.",
					"검사에서 발견된 변이는 2015 ACMG/AMP guideline (Genet Med 2015;17:405-24)에 따라 \"pathogenic\", \"likely pathogenic\", \"uncertain significance\", \"likely benign\", \"benign\" 의 다섯가지 카테고리로 분류되며, \"likely benign\"과 \"benign\"에 합당한 변이는 보고하지 않고 있습니다. 또한, 관련 문헌과 데이터베이스의 추가적인 연구 결과에 따라 해당 변이의 분류가 변경될 수 있습니다."
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo N257 = TestInfo.builder()
			.code("N257").category(Category.WITH_MLPA)
			.title("NF1 [Sequencing&MLPA] Analysis 결과보고서")
			.name("NF1 [Sequencing&MLPA]")
			.gene("NF1").target("NF1 on Chromosome 17q11.2")
			.method("Sequencing of all coding exons, MLPA (Multiplex Ligation-dependent Probe Amplification)")
			.limitations(new String[] {
					"본 검사는 염기서열분석법 및 MLPA (Multiplex Ligation dependent Probe Amplification) 방법을 이용하여 NF1 유전자의 모든 exons에 대해 SNP, deletion 및 duplication을 확인하는 검사입니다.",
					"한계는 promotor / deep intron 영역의 변이 또는 역위 등을 포함한 구조적 이상은 검출 할 수 없으며, Probe에 붙은 염기서열부위의 변화가 위양성 결과를 초래할 수도 있습니다.",
					"본 검사에서 발견된 변이는 2015 ACMG/AMP guideline (Genet Med 2015;17:405-24)에 따라 \"pathogenic\", \"likely pathogenic\", \"uncertain significance\", \"likely benign\", \"benign\"의 다섯 가지 카테고리로 분류되며, Benign/Likely benign variant 결과는 보고하지 않습니다.",
					"관련 문헌과 데이터베이스의 추가적인 연구 결과에 따라 해당 변이의 분류가 변경될 수 있습니다."
			}).isNationalInsuranceTest(true).build();

	// DES 우회코드
	public static final TestInfo N057 = TestInfo.builder()
			.code("N057")
			.title("GALNS gene mutation_WES / 전용 결과보고서")
			.name("GALNS gene mutation_WES / 전용")
			.gene("GALNS").target("GALNS on Chromosome 16q24.3")
			.isNationalInsuranceTest(true).build();
	public static final TestInfo N059 = TestInfo.builder()
			.code("N059")
			.title("IDUA gene mutation / 전용 결과보고서")
			.name("IDUA gene mutation / 전용")
			.gene("IDUA").target("IDUA on Chromosome 4p16.3")
			.isNationalInsuranceTest(true).build();
	public static final TestInfo S104 = TestInfo.builder()
			.code("S104")
			.title("GBA gene mutation / 전용 결과보고서")
			.name("GBA gene mutation / 전용")
			.gene("GBA").target("GBA on Chromosome 1q22")
			.isNationalInsuranceTest(true).build();
	public static final TestInfo[] TESTS = new TestInfo[] {
			N027,
			N057, N059,
			S030, S032, S034,
			S053, S054, S055,
			S061, S062, S063, S064, S065, S067,
			S104,
			S123,
			S125, S126, S127, S128, S129,
			X009, //BRCA
			Z137, Z138, //BRCA
			Z141, Z962, Z964,  //CANCER
			G068, G069, //BRCA
			N257
	};
	public static final TestInfo[] TESTS_WITH_REASON_FOR_REFERRAL = Arrays.stream(TESTS).filter(t->t.referralDefault!=null).toArray(TestInfo[]::new);
}
