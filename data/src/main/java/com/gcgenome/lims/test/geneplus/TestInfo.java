package com.gcgenome.lims.test.geneplus;

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
		BRCA, ETC
	}
	private final String code;
	private final String gene;
	private final String referralDefault;
	@Builder.Default
	private final TestInfo.Category category = TestInfo.Category.ETC;
	private final String name;
	private final String title;
	@Builder.Default
	private final String specimen = "Genomic DNA isolated from peripheral blood leukocytes";
	private final String target;
	private final String[] tier2;
	@Builder.Default
	private final String method = "Sequencing of all coding exons";
	private final String penetrance;
	@Builder.Default
	private final boolean isNationalInsuranceTest = false;
	@Builder.Default
	private final String[] limitations = new String[] {
			"본 검사는 염기서열분석법으로 시행되었으며, Large deletion/duplication 검출은 제한적입니다.",
			"본 검사에서 발견된 변이는 2015 ACMG/AMP Guidelines (Genet Med 2015;17:405-24)에 따라 'Pathogenic variant (PV)', 'Likely pathogenic variant (LPV)', 'Variant of uncertain significance (VUS)', 'Likely benign variant (LBV)', 'Benign variant (BV)'의 다섯가지 카테고리로 분류되며, LBV와 BV는 보고하지 않습니다. 또한, 추가 연구 결과에 따라 해당 변이의 분류가 변경 될 수 있습니다."
	};
	@Builder.Default
	private final String[] references = new String[] {
			"GeneReviews (https://www.ncbi.nlm.nih.gov/books/NBK1116/)",
			"The Human Gene Mutation Database (http://www.hgmd.org)"
	};
	@Builder.Default
	private final String i18n = "KOKR";

	public static final TestInfo S077 = TestInfo.builder()
			.code("S077")
			.referralDefault("Cardiac arrhythmia")
			.title("SCN5A gene plus 결과보고서")
			.name("SCN5A gene plus")
			.gene("SCN5A")
			.tier2(new String[]{"ANK2","CACNA1C","CALM1","CALM2","CALM3","CASQ2","KCNE1","KCNE2","KCNH2","KCNJ2","KCNQ1","RYR2","TRDN","CACNA2D1","CACNB2","SLC4A3","TANGO2","TECRL"})
			.target("SCN5A on Chromosome 3p22.2")
			.limitations(new String[] {
					"SCN5A 유전자의 변이는 유전성 부정맥 질환의 원인 유전자 중 하나입니다. 특히 SCN5A 유전자 변이는 유전성 부정맥 질환 중 브루가다 증후군(Brugada syndrome) 및 긴 QT 증후군(long QT syndrome) 관련 주요 유전자로서, 심장에 구조적인 이상이 없는 환자에게서 심실 부정맥을 야기하여 급사를 일으킬 수 있습니다. SCN5A는 브루가다 증후군의 약 25%와 긴 QT 증후군의 약 5~10%의 원인으로 알려져 있습니다.",
					"SCN5A 유전자는 심근 세포막에 존재하는 sodium channel의 alpha subunit를 암호화하며, 상염색체 우성의 유전양상을 보입니다.",
					"본 검사는 염기서열분석법으로 시행되었으며, Large deletion/duplication 검출은 제한적입니다.",
					"본 검사에서 발견된 변이는 2015 ACMG/AMP Guidelines (Genet Med 2015;17:405-24)에 따라 'Pathogenic variant (PV)', 'Likely pathogenic variant (LPV)', 'Variant of uncertain significance (VUS)', 'Likely benign variant (LBV)', 'Benign variant (BV)'의 다섯가지 카테고리로 분류되며, LBV와 BV는 보고하지 않습니다. 또한, 추가 연구 결과에 따라 해당 변이의 분류가 변경 될 수 있습니다."
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo S078 = TestInfo.builder()
			.code("S078")
			.referralDefault("Cardiomyopathy")
			.title("MYH7 gene plus 결과보고서")
			.name("MYH7 gene plus")
			.gene("MYH7")
			.tier2(new String[]{"AARS2","ABCC9","ACAD9","ACADVL","ACTA1","ACTC1","ACTN2","AGK","AGL","ALMS1","ALPK3","ARSB","ATP5F1D","ATPAF2","BAG3","BRAF","CACNA1C","CBL","CDH2","COA5","COA6","COX10","COX14","COX15","COX20","COX6B1","CPT2","CSRP3","DES","DMD","DNAJC19","DOLK","DSC2","DSG2","DSP","EMD","EPG5","FAH","FHL1","FHOD3","FKTN","FLNC","GAA","GLB1","GUSB","HADHA","HADHB","HCN4","HRAS","IDH2","IDS","IDUA","JPH2","JUP","KRAS","LAMP2","LMNA","LRPPRC","LZTR1","MAP2K1","MAP2K2","MIB1","MLYCD","MMUT","MRPL44","MYBPC3","MYH6","MYL2","MYL3","MYPN","NDUFA1","NDUFA10","NDUFA11","NDUFA2","NDUFAF1","NDUFAF2","NDUFAF3","NDUFAF4","NDUFAF5","NDUFB11","NDUFB3","NDUFS1","NDUFS2","NDUFS3","NDUFS4","NDUFS6","NDUFS7","NDUFS8","NDUFV1","NDUFV2","NEXN","NF1","NKX2-5","NONO","NRAS","NUBPL","PCCA","PCCB","PDLIM3","PKP2","PLN","PNPLA2","PPA2","PPCS","PPP1CB","PPP1R13L","PRKAG2","PTPN11","RAF1","RBM20","RIT1","RYR2","SCN5A","SCO1","SCO2","SDHA","SDHAF1","SDHD","SGCD","SHOC2","SLC22A5","SLC25A20","SLC25A4","SOS1","SOS2","SURF1","TAFAZZIN","TMEM126B","TMEM43","TMEM70","TNNC1","TNNI3","TNNI3K","TNNT2","TPM1","TSFM","TTN","TTR","VCL","ANK2","COX7B","CRYAB","EYA4","FASTKD2","FKRP","FLII","FNIP1","FOXRED1","GATA6","GLA","HFE","HGSNAT","LDB3","MMACHC","MRAS","MYLK3","NAA15","NAGLU","NDUFA4","NDUFB8","NRAP","PET100","PLD1","RASA2","RHBDF1","RPL3L","SGSH","SHMT2","SLC30A5","SPEG","TOR1AIP1","UQCC2","HAMP","SLC40A1","TFR2","TRIM63","LMOD2","SPRED2","ANKRD1","PRDM16","RRAGC","RRAGD","SLC6A6","TAB2","TBX20","TBX5","TCAP","GATAD1","PSEN1","PSEN2","SCN1B","ATAD3A","GYG1","MYLK2","RPS6KB1","TULP3","CAP2","CRLS1","ELAC2","GSN","LETM1","NAA10","RNF220"})
			.target("MYH7 on Chromosome 14q11.2")
			.limitations(new String[] {
					"MYH7 유전자의 변이는 비후성 심근병증(hypertrophic cardiomyopathy)의 원인 유전자 중 하나입니다. 비후성 심근병증은 유전성 심장질환으로 심실 비후를 유발할만한 대동맥판 협착증이나 고혈압과 같은 다른 증세 없이 심실벽이 두꺼워지는 질환입니다. 전체 인구 약 500명당 1명에서 발견되며, 다양한 형태의 심실 비후 소견이 관찰됩니다.",
					"MYH7 유전자는 beta-myosin heavy chain을 암호화하며, 상염색체 우성의 유전양상을 보인다.",
					"본 검사는 염기서열분석법으로 시행되었으며, Large deletion/duplication 검출은 제한적입니다.",
					"본 검사에서 발견된 변이는 2015 ACMG/AMP Guidelines (Genet Med 2015;17:405-24)에 따라 'Pathogenic variant (PV)', 'Likely pathogenic variant (LPV)', 'Variant of uncertain significance (VUS)', 'Likely benign variant (LBV)', 'Benign variant (BV)'의 다섯가지 카테고리로 분류되며, LBV와 BV는 보고하지 않습니다. 또한, 추가 연구 결과에 따라 해당 변이의 분류가 변경 될 수 있습니다."
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo S080 = TestInfo.builder()
			.code("S080")
			.referralDefault("Genetic epilepsy")
			.title("SCN1A gene plus 결과보고서")
			.name("SCN1A gene plus")
			.gene("SCN1A")
			.tier2(new String[] {"AARS1","ABAT","ACOX1","ACTL6B","ADAR","ADGRG1","ADPRS","ADSL","AFF3","AIMP1","AKT3","ALDH5A1","ALDH7A1","ALG1","ALG11","ALG13","ALG3","ALG6","ALG8","ALG9","ALPL","AMPD2","AMT","AP2M1","AP3B2","ARFGEF2","ARG1","ARHGEF9","ARID1B","ARV1","ARX","ASAH1","ASPA","ATN1","ATP1A1","ATP1A2","ATP1A3","ATP6V0A2","ATP6V1A","ATP7A","ATRX","BCKDHA","BCKDHB","BCS1L","BOLA3","BRAF","BRAT1","BSCL2","BTD","C12orf57","CACNA1A","CACNA1B","CACNA1D","CACNA1E","CACNA1G","CACNA2D2","CAD","CASK","CC2D2A","CDKL5","CHD2","CHRNA2","CHRNA4","CHRNB2","CIC","CLCN4","CLN3","CLN8","CLTC","CNKSR2","CNNM2","CNPY3","CNTNAP2","COG7","COL18A1","COL4A1","COL4A2","COQ2","COQ4","COQ9","CPA6","CREBBP","CSNK2B","CSTB","CTNNA2","CTSD","CUL4B","CUX2","CYFIP2","D2HGDH","DBT","DCX","DDX3X","DEAF1","DEGS1","DENND5A","DEPDC5","DHDDS","DHPS","DHX30","DIAPH1","DNM1","DNM1L","DOCK7","DPAGT1","DPM1","DPYD","DYNC1H1","DYRK1A","EARS2","EEF1A2","EFTUD2","EHMT1","EIF2B1","EIF2B2","EIF2B3","EIF2B4","EIF2B5","EIF2S3","EIF3F","EML1","EPG5","EPM2A","ETHE1","EXOSC3","FARS2","FBXL4","FBXO11","FGF12","FKTN","FLNA","FOLR1","FOXG1","FRRS1L","FUCA1","FUT8","GABBR2","GABRA1","GABRA2","GABRA5","GABRB2","GABRB3","GABRG2","GALC","GAMT","GBA","GFAP","GLB1","GLDC","GLUD1","GLUL","GM2A","GNAO1","GNAQ","GNB1","GNB5","GOSR2","GOT2","GPAA1","GPHN","GRIA4","GRIN1","GRIN2A","GRIN2B","GRIN2D","GTPBP2","HACE1","HAX1","HCFC1","HCN1","HCN2","HECW2","HEPACAM","HEXA","HEXB","HMGCL","HNRNPH2","HNRNPR","HNRNPU","HRAS","HSD17B4","HTRA2","IER3IP1","IFIH1","IKBKG","IQSEC2","IRF2BPL","ITPA","KARS1","KCNA2","KCNB1","KCNC1","KCNJ10","KCNJ11","KCNK4","KCNMA1","KCNQ2","KCNQ3","KCNQ5","KCNT1","KCNT2","KCTD3","KCTD7","KIAA1109","KIF1A","KIF2A","KIF5C","KMT2E","KRAS","LGI1","LIAS","MACF1","MAF","MAGI2","MAP2K1","MAP2K2","MBD5","MBOAT7","MDH2","MECP2","MED12","MEF2C","MFF","MFSD8","MLC1","MMACHC","MMADHC","MOCS1","MOCS2","MOGS","MPDU1","MTHFR","MTOR","NACC1","NAGA","NARS2","NBEA","NDE1","NDUFA1","NDUFA10","NDUFAF2","NDUFAF5","NDUFS4","NDUFS8","NDUFV1","NEXMIF","NGLY1","NHLRC1","NPRL2","NPRL3","NRXN1","NSD1","NSDHL","NTRK2","NUS1","OCLN","OPHN1","OTUD6B","P4HTM","PACS1","PACS2","PAFAH1B1","PAH","PAK1","PARS2","PCCA","PCCB","PCDH12","PCDH19","PDHA1","PDHX","PET100","PHACTR1","PHGDH","PIGA","PIGB","PIGC","PIGG","PIGH","PIGN","PIGO","PIGQ","PIGT","PIGU","PIGW","PIK3R2","PLAA","PLCB1","PLPBP","PMM2","PNKP","PNPO","POLG","POMGNT1","POMT1","PPP2CA","PPP3CA","PPT1","PRICKLE1","PRMT7","PRRT2","PSAP","PTPN23","PTS","PURA","QARS1","RAB11B","RAB18","RALA","RARS2","RELN","RFT1","RHOBTB2","RMND1","RNASEH2A","RNASEH2B","RNASEH2C","RNASET2","ROGDI","RORA","RORB","RTN4IP1","RTTN","SAMHD1","SCARB2","SCN1B","SCN2A","SCN3A","SCN8A","SEPSECS","SETBP1","SGSH","SIK1","SLC12A5","SLC13A5","SLC16A2","SLC1A2","SLC1A4","SLC25A1","SLC25A12","SLC25A22","SLC2A1","SLC35A2","SLC6A1","SLC6A8","SLC9A6","SMARCA2","SMARCC2","SMC1A","SMS","SNAP25","SPATA5","SPTAN1","ST3GAL5","STAG1","STAMBP","STRADA","STX1B","STXBP1","SUCLA2","SUOX","SURF1","SYN1","SYNGAP1","SYNJ1","SZT2","TANGO2","TBC1D24","TBCD","TBCK","TBL1XR1","TCF4","TDP2","TPP1","TRAK1","TREX1","TRIM8","TRPM6","TSC1","TSC2","TSEN54","TUBA1A","TUBB2A","TUBB2B","TUBB3","TUBB4A","TUBG1","UBA5","UBE2A","UBE3A","UFM1","UNC80","VAMP2","VARS1","VPS11","WARS2","WASF1","WDR37","WDR45","WDR73","WWOX","YWHAG","ZBTB18","ZDHHC9","ZEB2","ZNF142","ABCA2","ADAM22","ADARB1","ADAT3","AIMP2","ALG14","ALKBH8","ANKRD11","APC2","ASNS","ATP5F1A","ATP6AP2","CACNA1H","CACNB4","CARS2","CCDC88A","CCDC88C","CDC42BPB","CDK19","CELF2","CEP85L","CERS1","CLN6","CLPB","COG4","COG6","COG8","COQ6","COX10","COX15","CPLX1","CRPPA","CTSF","CTU2","CUL3","CYP27A1","DDC","DHCR24","DHCR7","DHX16","DLL1","DMXL2","DNAJC5","DNAJC6","DOLK","DPM2","EFHC1","EIF2AK2","EMC10","EMX2","EXOC7","EXT2","FAM50A","FAR1","FASTKD2","FBXO28","FCSK","FDFT1","FGF13","FGFR3","FH","FKRP","FOXRED1","GABRB1","GAD1","GALNT2","GCH1","GFM1","GLI3","GLRA1","GLS","GLYCTK","GRIA2","GRM7","GRN","GSS","GTPBP3","GUF1","H3-3A","H3-3B","HCCS","HEATR5B","HERC2","HLCS","HOXA1","HPDL","HPRT1","HSPD1","KAT5","KAT8","KATNB1","KCNA1","KCNH1","KIFBP","KPTN","LARGE1","LARS1","LIPT1","LIPT2","LMAN2L","LMBRD2","LMNB1","LNPK","LSS","LYST","MADD","MANBA","MAST1","MED17","MED27","MINPP1","MTHFS","MTR","NARS1","NCDN","NDP","NDUFA2","NDUFAF3","NDUFAF4","NDUFS1","NDUFS2","NDUFS6","NDUFS7","NECAP1","NEDD4L","NEUROD2","NR4A2","NRROS","NSF","NUBPL","NUP214","OTX2","OXR1","PCDHGC4","PCLO","PCYT2","PDSS2","PEX1","PEX10","PEX12","PEX13","PEX19","PEX2","PEX3","PEX5","PEX6","PEX7","PIGK","PIGP","PIGS","PLA2G6","PMPCB","PNPT1","POLG2","POMT2","PPIL1","PRODH","PSAT1","PSPH","PTF1A","PUM1","QDPR","RAB3GAP1","RAB3GAP2","RALGAPA1","RALGAPB","RANBP2","RARS1","RNF113A","RNF13","RNF2","RPIA","RRM2B","RUSC2","RYR2","SAMD12","SATB1","SATB2","SCAF4","SCAMP5","SCO1","SCO2","SDHA","SEMA6B","SERPINI1","SETD1A","SETD1B","SETD5","SIX3","SLC35A1","SLC35A3","SLC39A8","SLC45A1","SLC5A6","SNIP1","SNX27","SPR","SPTBN1","SPTBN4","ST3GAL3","STARD7","SUCLG1","TANC2","TBC1D20","TBC1D2B","TELO2","TET3","TFE3","TIMM50","TMEM106B","TMEM222","TMEM70","TMX2","TNK2","TRAF7","TRAPPC12","TRAPPC4","TRAPPC6B","TRIP13","TRPM3","TRRAP","TSEN15","TSEN2","TSEN34","TSFM","TUBA8","TUBB","TUBGCP2","TXNRD1","UBR7","UFC1","UFSP2","UGDH","UGP2","USP18","USP7","VLDLR","WDR45B","WDR62","YIF1B","YIPF5","ZMIZ1","ZNF335","AP1G1","ARF1","ARFGEF1","ASH1L","ATP6V0A1","CACNA1C","CACNA1I","CHD5","CHKA","CLCN3","DROSHA","DTYMK","GABRD","GLRA2","GRIK2","HID1","KCNC2","KCND2","NAPB","NSRP1","OGDHL","PGM2L1","PIDD1","PRPF8","PTEN","SLC32A1","SLC38A3","SPATA5L1","TAF8","TIAM1","TNPO2","TRA2B","UBAP2L","WNK3","ADD1","AGO1","ARF3","ASL","ATP2B1","ATP6V0C","BLOC1S1","CACNA2D1","CNOT9","COG3","COLGALT1","COX11","CPSF3","CRELD1","CSNK1G1","DALRD3","EIF4A2","ESAM","HECTD4","JAKMIP1","KCNH5","KDM6B","LETM1","MAST4","PABPC1","PARP6","PIGM","PIP5K1C","PPP1R3F","PTCD3","RAB11A","RAB5C","RAC3","RHEB","SARS1","SHQ1","SLC31A1","SYNCRIP","TEFM","TMEM63B","TRIT1","U2AF2","VPS50","XK","ZBTB47","ZMYM2"})
			.target("SCN1A on Chromosome 2q24.3")
			.limitations(new String[] {
					"SCN1A 유전자는 2번 염색체 장완(2q24.3)에 위치하며 뇌전증 증후군(Epilepsy syndromes)의 원인 유전자 중 하나입니다. SCN1A 유전자 변이는 다양한 형태의 발작 장애를 초래하며, 가장 일반적인 형태의 발작 장애는 영아기 중증 근간대성 뇌전증(Severe Myoclonic Epilepsy of Infancy; SMEI)와 전신 긴장 간대 발작(intractable childhood epilepsy with generalized tonic–clonic seizures; ICEGTC) 입니다. SMEI는 드라베 증후군(Dravet Syndrome)이라고도 하며, 임상적으로 드라베 증후군 환자의 70-80%는 SCN1A 유전자의 병원성 변이로 인해 발생합니다.",
					"SCN1A 유전자는 상염색체 우성 유전양상을 보입니다.",
					"본 검사는 염기서열분석법으로 시행되었으며, Large deletion/duplication 검출은 제한적입니다.",
					"본 검사에서 발견된 변이는 2015 ACMG/AMP Guidelines (Genet Med 2015;17:405-24)에 따라 'Pathogenic variant (PV)', 'Likely pathogenic variant (LPV)', 'Variant of uncertain significance (VUS)', 'Likely benign variant (LBV)', 'Benign variant (BV)'의 다섯가지 카테고리로 분류되며, LBV와 BV는 보고하지 않습니다. 또한, 추가 연구 결과에 따라 해당 변이의 분류가 변경 될 수 있습니다."
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo S081 = TestInfo.builder()
			.code("S081")
			.referralDefault("Spastic paraplegia")
			.title("SPG11 gene plus 결과보고서")
			.name("SPG11 gene plus")
			.gene("SPG11")
			.tier2(new String[] {"ABCD1","ADAR","AFG3L2","AIMP1","ALDH18A1","ALS2","AP4B1","AP4E1","AP4M1","AP4S1","ARG1","ATL1","ATP13A2","B4GALNT1","BSCL2","C19orf12","CAPN1","CYP27A1","CYP2U1","CYP7B1","DDHD1","DDHD2","ERLIN1","ERLIN2","FA2H","FARS2","GBA2","HACE1","HIKESHI","HSPD1","KIDINS220","KIF1A","KIF5A","L1CAM","MTRFR","NIPA1","NKX6-2","NT5C2","OPA3","PCYT2","PLP1","PNPLA6","POLR3A","REEP1","RNASEH2B","RTN2","SACS","SERAC1","SLC16A2","SLC1A4","SLC25A46","SLC2A1","SPART","SPAST","SPG21","SPG7","TFG","TUBB4A","UBAP1","WASHC5","WDR45B","ZFYVE26","ARSA","CDK16","DARS1","EIF2B5","GCH1","IBA57","KDM5C","KIF1C","LYST","MAG","MARS2","MTPAP","POLR3K","REEP2","SARS2","SLC33A1","SOD1","AP5Z1","COQ4","CPT1C","FBXO7","FXN","GALC","GBE1","GJA1","KCNA2","PSEN1","SLC25A15","SPTAN1","ABHD16A","ACER3","ALDH3A2","BCAS3","CTNNB1","DDX3X","ELOVL1","FAR1","GLRX5","GPT2","HPDL","IFIH1","KPNA3","MAPK8IP3","NDUFA12","NSRP1","RNF170","SPATA5L1","STN1","TAF8","TMEM63C","AMPD2","ARL6IP1","CHP1","DSTYK","ENTPD1","GAD1","GJC2","PRNP","RAB3GAP2","TECPR2","UCHL1","VAMP1","VPS37A","WDR48","ALK","AMFR","ATAD3A","BLOC1S1","CCDC82","CLDN11","EXOSC3","HECTD4","LETM1","NRCAM","PI4KA","PPFIBP1","RETREG1","RHOB","SPTSSA"})
			.target("SPG11 on Chromosome 15q21.1")
			.limitations(new String[] {
					"SPG11 유전자는 15번 염색체 장완(15q21.1)에 위치하며 유전성 강직성 하반신 마비(hereditary spastic paraplegia; HSP)의 원인 유전자 중 하나입니다. 강직성 하반신 마비의 발병은 주로 영아기 또는 청소년기에 발생하며, 다리의 점진적인 쇠약과 경직을 특징으로 하는 유전성 장애로 질병 진행 초기에는 가벼운 보행 장애와 뻣뻣함이 있을 수 있습니다.",
					"SPG11 유전자는 상염색체 열성 유전양상을 보입니다.",
					"본 검사는 염기서열분석법으로 시행되었으며, Large deletion/duplication 검출은 제한적입니다.",
					"본 검사에서 발견된 변이는 2015 ACMG/AMP Guidelines (Genet Med 2015;17:405-24)에 따라 'Pathogenic variant (PV)', 'Likely pathogenic variant (LPV)', 'Variant of uncertain significance (VUS)', 'Likely benign variant (LBV)', 'Benign variant (BV)'의 다섯가지 카테고리로 분류되며, LBV와 BV는 보고하지 않습니다. 또한, 추가 연구 결과에 따라 해당 변이의 분류가 변경 될 수 있습니다."
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo S082 = TestInfo.builder()
			.code("S082")
			.referralDefault("Muscular dystrophy or myopathy")
			.title("DYSF gene plus 결과보고서")
			.name("DYSF gene plus")
			.gene("DYSF")
			.tier2(new String[] {"ACAD9","ACADM","ACADVL","ACTA1","ACTN2","ADSS1","AGL","AGRN","ALDOA","ALG14","ALG2","ANO5","AR","ASAH1","B3GALNT2","B4GAT1","BAG3","BICD2","BIN1","CACNA1S","CAPN3","CAV3","CCDC78","CFL2","CHAT","CHCHD10","CHKB","CHRNA1","CHRNB1","CHRND","CHRNE","CHRNG","CNBP","COL12A1","COL13A1","COL6A1","COL6A2","COL6A3","COLQ","CPT2","CRPPA","CRYAB","DAG1","DES","DMD","DNAJB6","DNM2","DOK7","DOLK","DPAGT1","DPM2","DYNC1H1","ECEL1","EMD","ENO3","EPG5","ETFA","ETFB","ETFDH","EXOSC3","FHL1","FKBP14","FKRP","FKTN","FLNC","FXR1","GAA","GBE1","GFPT1","GMPPB","GNE","GYG1","GYS1","HADHA","HADHB","HNRNPDL","HSPB1","HSPB8","IGHMBP2","INPP5K","ISCU","ITGA7","KBTBD13","KLHL40","KLHL41","LAMA2","LAMP2","LARGE1","LDB3","LDHA","LMNA","LMOD3","LPIN1","LRP4","MAP3K20","MATR3","MEGF10","MICU1","MSTO1","MTM1","MUSK","MYBPC1","MYH2","MYH3","MYH7","MYH8","MYL1","MYMK","MYO18B","MYO9A","MYOT","MYPN","NEB","ORAI1","PAX7","PFKM","PGAM2","PGK1","PGM1","PHKA1","PHKB","PIEZO2","PLEC","POLG","POLG2","POMGNT1","POMGNT2","POMK","POMT1","POMT2","PRKAG2","PYGM","PYROXD1","RAPSN","RBCK1","RRM2B","RXYLT1","RYR1","RYR3","SCN4A","SELENON","SGCA","SGCB","SGCD","SGCG","SIL1","SLC18A3","SLC22A5","SLC25A1","SLC25A4","SLC52A2","SLC52A3","SLC5A7","SMN1","SPEG","SPG11","SQSTM1","STAC3","STIM1","SUCLA2","SYNE1","SYNE2","SYT2","TCAP","TIA1","TK2","TNNI2","TNNT1","TNNT3","TNPO3","TPM2","TPM3","TRAPPC11","TRIM32","TRIP4","TRPV4","TSEN54","TSFM","TTN","TYMP","UBA1","VAMP1","VCP","VMA21","VRK1","ALS2","ASCC3","ATP7A","CASQ1","CAVIN1","CNTN1","COL4A1","DHX16","DPM1","DPM3","EXOSC8","FDX2","GFER","GGPS1","GIPC1","GOLGA2","HACD1","HNRNPA2B1","JAG2","KY","LRIF1","MTMR14","MYBPC3","MYF5","MYL2","MYOD1","NEFL","POGLUT1","POPDC3","PPA2","REEP1","SETX","SLC25A42","SMCHD1","SVIL","TANGO2","TNNC2","TOR1AIP1","TRDN","UNC45B","VAPB","AMPD1","COQ4","COQ8A","COX6A2","DGUOK","FLAD1","MLIP","OBSCN","ABHD5","ASCC1","BET1","BVES","COL25A1","DNAJB4","DTNA","GOSR2","HMGCR","LETM1","MYH1","PNPLA2","SMPX","SPTBN4","TAMM41","ZC4H2"})
			.target("DYSF on Chromosome 2p13.2")
			.limitations(new String[] {
					"DYSF 유전자는 2번 염색체 단완(2p13.2)에 위치하며 미요시 근병증(Miyoshi myopathy)과 지대근디스트로피 2B형(limb-girdle muscular dystrophy 2B)의 원인 유전자입니다. 미요시 근육병증은 점진적인 근력감소로 인한 보행능력의 상실과 호흡 근력의 약화, 심장 기능의 약화 등을 특징으로 하는 질환이며, LGMD2B는 골반 및 어깨의 근쇠약과 위축을 유발합니다.",
					"DYSF 유전자는 근육에서 칼슘의 세포막 이동을 조절하고 손상된 근육 세포를 회복시키는 기능과 연관이 있는 것으로 알려져 있으며 DYSF 유전자는 상염색체 열성 유전양상을 보입니다.",
					"본 검사는 염기서열분석법으로 시행되었으며, Large deletion/duplication 검출은 제한적입니다.",
					"본 검사에서 발견된 변이는 2015 ACMG/AMP Guidelines (Genet Med 2015;17:405-24)에 따라 'Pathogenic variant (PV)', 'Likely pathogenic variant (LPV)', 'Variant of uncertain significance (VUS)', 'Likely benign variant (LBV)', 'Benign variant (BV)'의 다섯가지 카테고리로 분류되며, LBV와 BV는 보고하지 않습니다. 또한, 추가 연구 결과에 따라 해당 변이의 분류가 변경 될 수 있습니다."
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo S083 = TestInfo.builder()
			.code("S083")
			.referralDefault("Genetic ataxia")
			.title("CACNA1A gene plus 결과보고서")
			.name("CACNA1A gene plus")
			.gene("CACNA1A")
			.tier2(new String[] {"AAAS","ABCB7","ABHD12","AFG3L2","AMPD2","ANO10","AP1S2","APTX","ARSA","ATCAY","ATM","ATP1A3","CA8","CACNA1G","CACNA2D2","CAMTA1","CASK","CHMP1A","CLCN2","CLN6","CLP1","COQ8A","COX20","CP","CWF19L1","CYP27A1","CYP2U1","DARS2","DDHD2","DNAJC19","DNAJC5","DNMT1","EIF2B1","EIF2B2","EIF2B3","EIF2B4","EIF2B5","ELOVL4","EPM2A","EXOSC3","FGF14","FLVCR1","FMR1","FOLR1","FXN","GBA2","GFAP","GJC2","GOSR2","GPAA1","GRID2","GRM1","HEXA","HEXB","ITPR1","KCNA1","KCNC3","KCND3","KCNJ10","KIF1C","MARS2","MMACHC","MRE11","MTTP","NHLRC1","NKX6-2","NPC1","NPC2","OPA3","OPHN1","PAX6","PDYN","PEX16","PLA2G6","PMPCA","PNKP","PNPLA6","POLG","POLR3A","PRKCG","PRNP","PRRT2","RARS2","RNF170","RNF216","SACS","SCN8A","SEPSECS","SETX","SIL1","SLC1A3","SLC2A1","SLC9A6","SNX14","SPG7","SPTBN2","SRD5A3","STUB1","SYNE1","TGM6","TMEM240","TPP1","TSEN2","TSEN54","TTBK2","TTC19","TTPA","TUBB4A","TWNK","UCHL1","VLDLR","VPS13D","VRK1","WDR73","WDR81","WFS1","WWOX","XRCC1","COG5","DMXL2","EEF2","MVK","NUS1","PEX7","POLR3K","SAR1B","SLC52A2","VAMP1","ACO2","ADCY5","ADGRG1","ADPRS","AHI1","ALDH5A1","ALG1","ALG11","ALG12","ALG14","ALG3","ALG6","ALG8","ALG9","ARL13B","ARMC9","ATAD3A","ATG7","ATP1A2","ATP6AP1","ATP6V0A1","ATP6V0A2","ATP7B","ATP8A2","AUH","B3GALNT2","B3GALT6","B3GAT3","B3GLCT","B4GALNT1","B4GALT1","B4GALT7","B4GAT1","B9D2","BBS1","BRF1","C2CD3","CAD","CAPN1","CBY1","CC2D2A","CCDC115","CENPF","CEP104","CEP290","CEP41","CHST14","CHST3","CHST6","CHSY1","CILK1","CLN5","COA7","COG1","COG4","COG6","COG7","COG8","COQ4","CPLANE1","CRB2","CRPPA","CSGALNACT1","CSPP1","CSTB","CTBP1","DAGLA","DDX59","DHCR7","DHDDS","DKC1","DOCK3","DOLK","DPAGT1","DPM1","DPM2","DPM3","DPYSL5","EBF3","EDEM3","EOGT","ERCC4","EVC","EVC2","EXOSC8","EXOSC9","EXT1","EXT2","FA2H","FBXL4","FEM1C","FKRP","FKTN","FRMD5","FUT8","G6PC3","GALNT2","GALNT3","GEMIN5","GFPT1","GLI3","GLRA1","GLRB","GMPPA","GMPPB","GNE","GORAB","GRN","HYLS1","IFT74","INPP5E","INTS11","IRF2BPL","KCNA2","KCNN2","KCNQ2","KIAA0586","KIAA0753","KIF1A","KIF7","LAMA1","LARGE1","LARS2","MAG","MAGT1","MAN1B1","MAPK8IP3","MFN2","MGAT2","MINPP1","MKS1","MOGS","MORC2","MPDU1","MPI","MSTO1","MTCL1","MTFMT","NAXE","NFASC","NGLY1","NKX2-1","NPHP1","NPHP3","NPTX1","OFD1","OGDHL","OPA1","PACS2","PEX6","PGAP2","PGAP3","PGM1","PGM3","PI4KA","PIBF1","PIGA","PIGL","PIGN","PIGO","PIGS","PIGT","PIGV","PIGW","PITRM1","PMM2","PMPCB","PNKD","POLR3B","POMGNT1","POMGNT2","POMK","POMT1","POMT2","POU4F1","PRDM13","PRDX3","PTF1A","PTRH2","PUM1","RELN","RFT1","RNF220","ROBO3","RORA","RPGRIP1L","RXYLT1","SAMD9L","SCLT1","SCN1A","SCN2A","SCYL1","SEC23B","SLC17A5","SLC25A46","SLC35A1","SLC35A2","SLC35C1","SLC35D1","SLC37A4","SLC39A8","SLC44A1","SLC9A1","SNAP25","SPR","SPTAN1","SQSTM1","SSR4","ST3GAL3","ST3GAL5","STT3A","SUFU","SYNGAP1","TBC1D23","TCTN1","TCTN2","TCTN3","TDP2","TECPR2","TINF2","TMEM107","TMEM138","TMEM165","TMEM199","TMEM216","TMEM218","TMEM231","TMEM237","TMEM67","TOE1","TOGARAM1","TSEN34","TUBA1A","TUBB2B","TUBB3","TUSC3","TXNDC15","UBA5","UBTF","VPS13B","VPS41","XYLT1","XYLT2","ZFYVE26","ZSWIM6","AARS1","ABCA2","AGTPBP1","ARL3","ASL","ATP2B3","CAPRIN1","CHP1","CLPP","COASY","COG3","DAG1","DCC","DLG4","DNAJC3","DYNC1H1","ELOVL5","EN1","EXOSC5","FAM149B1","FCSK","GALC","GDAP2","GLS","HARS1","HEATR5B","KCNQ3","LETM1","LIG3","LNPK","MAN2B2","MTPAP","PEX2","PHGDH","PIGM","POC1B","PRICKLE1","PRPS1","RFXANK","SMPD4","SSR3","SVBP","TDP1","TERT","THG1L","TMEM106B","TRAPPC11","TSEN15","TUBA8","VPS53","ZNF423"})
			.target("CACNA1A on Chromosome 19p13.13")
			.limitations(new String[] {
					"CACNA1A 유전자는 19번 염색체 단완(19p13.13)에 위치하며, 제2형 발작성 운동실조증(episodic ataxia 2; EA2)의 원인 유전자입니다. CACNA1A 유전자에 변이가 생기면 CaV2.1 채널의 생성을 감소 시키거나 채널이 칼슘 이온을 운반하는 데 필요한 세포막에 도달하는 것을 방해합니다. 이러한 채널이 수가 감소하면 칼슘 이온의 뉴런으로의 총 흐름이 감소하여 뇌에서 신경 전달 물질의 방출을 방해하여 질환이 발생하게 됩니다. 제2형 발작성 운동실조증은 진행성 소뇌 위축, 현기증, 시각 장애 등 증상을 보이며, 이러한 증상은 몇 초에 서 몇 분까지 지속되는 운동 실조증 1형과 달리 몇 시간에서 며칠까지 지속됩니다.",
					"CACNA1A 유전자는 상염색체 우성 유전양상을 보입니다.",
					"본 검사는 염기서열분석법으로 시행되었으며, Large deletion/duplication 검출은 제한적입니다.",
					"본 검사에서 발견된 변이는 2015 ACMG/AMP Guidelines (Genet Med 2015;17:405-24)에 따라 'Pathogenic variant (PV)', 'Likely pathogenic variant (LPV)', 'Variant of uncertain significance (VUS)', 'Likely benign variant (LBV)', 'Benign variant (BV)'의 다섯가지 카테고리로 분류되며, LBV와 BV는 보고하지 않습니다. 또한, 추가 연구 결과에 따라 해당 변이의 분류가 변경 될 수 있습니다."
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo S084 = TestInfo.builder()
			.code("S084")
			.referralDefault("Dystonia")
			.title("VPS13A gene plus 결과보고서")
			.name("VPS13A gene plus")
			.gene("VPS13A")
			.tier2(new String[] {"ADAR","ADCY5","ANO3","APTX","ATM","ATP13A2","ATP1A3","ATP7B","BCAP31","C19orf12","CHMP2B","COASY","CP","CSTB","DCAF17","DDC","DLAT","FA2H","FBXO7","FTL","GCH1","GNAO1","HPCA","HTRA2","KMT2B","MECR","NKX6-2","PANK2","PINK1","PLA2G6","PNKD","PRKN","PRKRA","PRRT2","SERAC1","SGCE","SLC2A1","SLC30A10","SLC6A3","SPR","SYNJ1","TH","THAP1","TOR1A","TUBB4A","VAC14","WDR45","WDR73","XK","YY1","CACNA1B","CIZ1","COL6A3","GNAL","KCTD17","RELN","TAF1","ABAT","ACER3","ACOX1","ACTB","AFG3L2","ALDH18A1","AP1S2","ARFGEF3","ATP1A2","ATP5MC3","BCS1L","CACNA1A","CACNA1G","CACNB4","CAMK4","CLN3","CLN5","CLPB","COX10","COX15","COX20","CSF1R","CYP27A1","DCTN1","DHDDS","DLD","DNAJC12","DNAJC6","ECHS1","EIF2AK2","FITM2","FOLR1","FOXRED1","FUCA1","FXN","GBA","GCDH","GFAP","GJC2","GLRA1","GLRB","GM2A","GNB1","GRIN1","GRN","GTPBP2","HCFC1","HECW2","HEXA","HIBCH","HNRNPH1","HPRT1","HSPD1","IFIH1","IMPDH2","IRF2BPL","KCNA1","KCNMA1","KCNQ2","KIF1C","LRPPRC","LRRK2","LYST","MAPT","MARS2","MED27","MRE11","MTFMT","MYORG","NDUFA1","NDUFA10","NDUFA12","NDUFA2","NDUFAF5","NDUFAF6","NDUFS1","NDUFS4","NDUFS7","NDUFS8","NDUFV1","NGLY1","NKX2-1","NPC1","NPC2","NUP54","OCLN","OPA3","PARK7","PCCA","PCCB","PDE10A","PDGFB","PDGFRB","PDHA1","PDHX","PET100","PNKP","PNPT1","POLR3A","PPP2R5D","PRNP","RAB39B","RNASEH2B","RNASEH2C","RNASET2","RNF216","SAMHD1","SCN1A","SCN8A","SETX","SLC16A2","SLC19A3","SLC20A2","SLC39A14","SLC6A8","SNCA","SPATA5L1","SPG11","SUCLA2","SUOX","SURF1","TARS2","TBK1","TIMM8A","TPK1","TREX1","UBTF","VAMP1","VPS13D","VPS16","VPS35","VPS4A","XPR1","YIF1B","ZSWIM6","AAAS","AASS","ABCB7","ACSF3","ANO10","ARSA","ARX","ASL","ATCAY","AUH","CA8","CHCHD2","CLN8","CTSD","CWF19L1","DNAJC5","EIF4G1","ELOVL4","FGF14","FOXG1","GLB1","GRID2","GRM1","HTT","ITPR1","KCNC3","KCND3","KIF1A","L2HGDH","MAL","PCDH12","PDE2A","PDYN","PLP1","PRKCG","PTS","QDPR","RNASEH2A","SACS","SHQ1","SIL1","SLC18A2","SLC1A3","SLC30A9","SLC6A5","SNX14","SPG7","SQSTM1","STUB1","SYT1","TBC1D24","TGM6","TMEM151A","TMEM240","TPP1","TSPOAP1","TTBK2","UCHL1","VAMP2","VPS41","WFS1","WWOX"})
			.target("VPS13A on Chromosome 9q21.2")
			.limitations(new String[] {
					"VPS13A 유전자는 9번 염색체 장완(9q21.2)에 위치하며 무도병-유극적혈구증가증(chorea-acanthocytosis)의 원인 유전자 중 하나입니다. Chorea-acanthocytosis는 무의식적인 경련운동(chorea), 비정상적인 별 모양의 적혈구(acanthocytosis), 사지, 얼굴, 입, 혀 및 목의 근육과 같은 다양한 근육의 근긴장 이상 증상을 보입니다.",
					"VPS13A 유전자는 상염색체 열성 유전양상을 보입니다.",
					"본 검사는 염기서열분석법으로 시행되었으며, Large deletion/duplication 검출은 제한적입니다.",
					"본 검사에서 발견된 변이는 2015 ACMG/AMP Guidelines (Genet Med 2015;17:405-24)에 따라 'Pathogenic variant (PV)', 'Likely pathogenic variant (LPV)', 'Variant of uncertain significance (VUS)', 'Likely benign variant (LBV)', 'Benign variant (BV)'의 다섯가지 카테고리로 분류되며, LBV와 BV는 보고하지 않습니다. 또한, 추가 연구 결과에 따라 해당 변이의 분류가 변경 될 수 있습니다."
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo S085 = TestInfo.builder()
			.code("S085")
			.referralDefault("Genetic Parkinson disease")
			.title("LRRK2 gene plus 결과보고서")
			.name("LRRK2 gene plus")
			.gene("LRRK2")
			.tier2(new String[] {"ATP13A2","ATP1A3","C19orf12","CSF1R","DCTN1","DNAJC6","FBXO7","FTL","GBA","GCH1","GRN","LYST","MAPT","OPA3","PANK2","PARK7","PINK1","PLA2G6","PRKN","PRKRA","PTRHD1","RAB39B","SLC30A10","SLC39A14","SLC6A3","SNCA","SPG11","SPR","SYNJ1","TH","TUBB4A","VPS13A","VPS35","WDR45","ANG","ATP7B","CHCHD2","CLN3","COASY","CP","DNAJC12","DNAJC5","EPHB4","PDE8B","PDGFB","PDGFRB","SLC20A2","TAF1","TWNK","VPS13C","XPR1"})
			.target("LRRK2 on Chromosome 12q12")
			.limitations(new String[] {
					"LRRK2 유전자는 12번 염색체 장완(12q12)에 위치하며 파키슨병(Parkinson disease)의 원인 유전자 중 하나입니다. LRRK2 유전자에 병원성 변이가 발생하게 되면 인산화효소(kinase) 활성화로 인해 도파민성 신경 세포 사멸, 도파민 신경 전달 장애, 단백질 합성 및 분해 결함, 염증 반응 및 산화 손상으로 파키슨병을 일으킬 수 있습니다. 파키슨병의 대표적인 3대 증상은 떨림, 느린 움직임, 근육 경직입니다.",
					"LRRK2 유전자는 상염색체 우성 유전양상을 보입니다.",
					"본 검사는 염기서열분석법으로 시행되었으며, Large deletion/duplication 검출은 제한적입니다.",
					"본 검사에서 발견된 변이는 2015 ACMG/AMP Guidelines (Genet Med 2015;17:405-24)에 따라 'Pathogenic variant (PV)', 'Likely pathogenic variant (LPV)', 'Variant of uncertain significance (VUS)', 'Likely benign variant (LBV)', 'Benign variant (BV)'의 다섯가지 카테고리로 분류되며, LBV와 BV는 보고하지 않습니다. 또한, 추가 연구 결과에 따라 해당 변이의 분류가 변경 될 수 있습니다."
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo S086 = TestInfo.builder()
			.code("S086")
			.referralDefault("Presenile dementia")
			.title("APP gene plus 결과보고서")
			.name("APP gene plus")
			.gene("APP")
			.tier2(new String[]{"CHMP2B","CSF1R","DNAJC5","DNMT1","EPM2A","GRN","ITM2B","MAPT","NHLRC1","NOTCH3","PRNP","PSEN1","PSEN2","TARDBP","TBK1","TYROBP","UBQLN2","VCP","ABCA7","SORL1","TUBA4A","XK","APOE"})
			.target("APP on Chromosome 21q21.3")
			.limitations(new String[] {
					"APP 유전자는 21번 염색체 장완(21q21.3)에 위치하며 조기 발병형 알츠하이머병(early onset familial Alzheimer disease; EOFAD)의 원인 유전자중 하나입니다. 알츠하이머 병은 치매를 일으키는 뇌의 퇴행성 질환으로, 점진적인 기억력, 판단력 및 기능 능력의 상실 증상을 보이며, 증상이 나타난 후 8~10년 동안 생존하지만 질병의 진행 기간은 1~25년 입니다. APP 유전자 변이에 의한 EOFAD 발병률은 10% 미만입니다.",
					"APP 유전자는 상염색체 우성 유전양상을 보입니다.",
					"본 검사는 염기서열분석법으로 시행되었으며, Large deletion/duplication 검출은 제한적입니다.",
					"본 검사에서 발견된 변이는 2015 ACMG/AMP Guidelines (Genet Med 2015;17:405-24)에 따라 'Pathogenic variant (PV)', 'Likely pathogenic variant (LPV)', 'Variant of uncertain significance (VUS)', 'Likely benign variant (LBV)', 'Benign variant (BV)'의 다섯가지 카테고리로 분류되며, LBV와 BV는 보고하지 않습니다. 또한, 추가 연구 결과에 따라 해당 변이의 분류가 변경 될 수 있습니다."
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo S087 = TestInfo.builder()
			.code("S087")
			.referralDefault("Skeletal dysplasia")
			.title("COL2A1 gene plus 결과보고서")
			.name("COL2A1 gene plus")
			.gene("COL2A1")
			.tier2(new String[] {"ABCC9","ACAN","ACP5","ACVR1","ADAMTS10","ADAMTS17","ADAMTSL2","AGA","AGPS","ALG12","ALG3","ALG9","ALPL","ALX1","ALX3","ALX4","AMER1","ANKH","ANKRD11","ANO5","ANTXR2","ARHGAP31","ARL6","ARSB","ARSL","ASXL1","ASXL2","ATP6V0A2","ATP7A","B3GALT6","B3GAT3","B3GLCT","B4GALT7","BBS1","BBS10","BBS12","BBS2","BBS4","BBS5","BBS7","BBS9","BHLHA9","BMP1","BMP2","BMPER","BMPR1B","BPNT2","C2CD3","CA2","CANT1","CASR","CC2D2A","CCDC8","CCN6","CCNQ","CDC45","CDH3","CDKN1C","CDT1","CEP120","CEP290","CFAP410","CHST14","CHST3","CHSY1","CILK1","CLCN5","CLCN7","COG1","COL10A1","COL11A1","COL11A2","COL1A1","COL1A2","COL9A1","COL9A2","COL9A3","COLEC11","COMP","CREB3L1","CREBBP","CRTAP","CSPP1","CTSA","CTSC","CTSK","CUL7","CYP27B1","CYP2R1","DDR2","DHCR24","DHCR7","DHODH","DIS3L2","DLL3","DLL4","DLX3","DLX5","DMP1","DNMT3A","DOCK6","DPAGT1","DPM1","DSPP","DVL1","DVL3","DYM","DYNC2H1","DYNC2I1","DYNC2I2","DYNC2LI1","DYNLT2B","EBP","EED","EFTUD2","EIF2AK3","ENPP1","EOGT","ERF","ESCO2","EVC","EVC2","EXT1","EXT2","EXTL3","EZH2","FAM111A","FAM20C","FBN1","FBN2","FERMT3","FGF10","FGF16","FGF23","FGFR1","FGFR2","FGFR3","FIG4","FKBP10","FLNA","FLNB","FN1","FUCA1","FZD2","GALNS","GALNT3","GDF5","GDF6","GHR","GJA1","GLB1","GLI3","GNAS","GNPAT","GNPTAB","GNPTG","GNS","GORAB","GPC6","GSC","GUSB","HDAC8","HES7","HGSNAT","HOXA13","HOXD13","HPGD","HSPG2","IDH1","IDS","IDUA","IFIH1","IFITM5","IFT122","IFT140","IFT172","IFT43","IFT52","IFT80","IFT81","IHH","IKBKG","IL11RA","IL1RN","INPPL1","KAT6B","KIAA0753","KIF22","KIF7","KMT2D","LBR","LEMD3","LIFR","LMBR1","LMNA","LMX1B","LONP1","LPIN2","LRP4","LRP5","LTBP3","MAFB","MAN2B1","MAP3K7","MASP1","MATN3","MEGF8","MEOX1","MESP2","MGP","MKKS","MKS1","MMP13","MMP2","MNX1","MPDU1","MSX2","MYCN","NAGLU","NANS","NBAS","NEK1","NEU1","NF1","NFIX","NIPBL","NKX3-2","NLRP3","NOG","NOTCH1","NOTCH2","NPR2","NSD1","NSDHL","OBSL1","OFD1","ORC1","ORC4","ORC6","OSTM1","P3H1","P4HB","PAPSS2","PAX3","PCNT","PCYT1A","PDE3A","PDE4D","PEX5","PEX7","PGM3","PHEX","PHGDH","PIGT","PIGV","PIK3C2A","PIK3R1","PITX1","PLOD2","PLS3","POC1A","POLR1A","POLR1C","POLR1D","POP1","POR","PPIB","PRKAR1A","PRMT7","PSAT1","PSPH","PTDSS1","PTH1R","PTHLH","PTPN11","PUF60","PYCR1","RAB23","RAB33B","RASGRP2","RBM8A","RBPJ","RECQL4","RFT1","RMRP","ROR2","RPGRIP1L","RPL13","RUNX2","SALL1","SALL4","SBDS","SCARF2","SEC24D","SERPINF1","SERPINH1","SETD2","SF3B4","SFRP4","SGSH","SH3BP2","SH3PXD2B","SHOX","SKI","SLC10A7","SLC17A5","SLC26A2","SLC29A3","SLC34A1","SLC34A3","SLC35C1","SLC35D1","SLC39A13","SLCO2A1","SMAD3","SMAD4","SMARCAL1","SMC1A","SMC3","SMOC1","SNRPB","SNX10","SOST","SOX9","SP7","SPARC","SUMF1","TALDO1","TAPT1","TBCE","TBX15","TBX3","TBX4","TBX5","TBX6","TBXAS1","TCIRG1","TCOF1","TCTN2","TCTN3","TENT5A","TERT","TGFB1","TGFB2","TGFBR2","TMCO1","TMEM165","TMEM216","TMEM231","TMEM38B","TNFRSF11A","TNFRSF11B","TNFSF11","TP63","TRAPPC2","TREM2","TRIP11","TRPS1","TRPV4","TRPV6","TTC21B","TTC8","TWIST1","TYROBP","VDR","WDPCP","WDR19","WDR35","WNT1","WNT10B","WNT5A","WNT7A","XRCC4","XYLT1","XYLT2","YY1","ZMPSTE24","ZSWIM6","ABL1","ANAPC1","ARCN1","B9D1","COG4","CSGALNACT1","DCC","EFNB1","EN1","FBLN1","GNPNAT1","GPX4","GZF1","HDAC4","HHAT","HNRNPK","HS2ST1","IGF1R","KAT6A","KDELR2","KIAA1217","LTBP1","LYSET","MANBA","MBTPS1","MMP9","MTX2","MYO18B","NEPRO","NMNAT1","NPR3","NXN","PAM16","PFN1","PISD","PKDCC","PLEKHM1","POLR1B","PRKG2","RAD21","RINT1","SCUBE3","SMAD6","TCF12","TGFBR1","TONSL","TWIST2","UBA2","UFSP2","VPS35L","WBP11","ZIC1","AFF3","COPB2","DDRGK1","DVL2","FGF9","KIF24","LRRK1","MESD","MYH3","SGMS2","STT3A","UNC45A","ZNF687","ARSK","AXIN1","CSNK1G1","DROSHA","ERI1","FBXW11","HEATR3","KIF5B","MBTPS2","NRCAM","PDIA6","PSMC3","SETD5","SLC35B2","SUCO"})
			.target("COL2A1 on Chromosome 12q13.11")
			.limitations(new String[] {
					"COL2A1 유전자는 12번 염색체 장완(12q13.11)에 위치하며 선천성 스티클러 증후군(Stickler syndrome), 척추골간단이형성증(spondyloepiphyseal dysplasia congenital; SEDC), 니스트 이형성증(Kniest dysplasia), 연골발생저하증(hypochondrogenesis), 연골무발생증(achondrogenesis) 등 다양한 골이형성증(skeletal dysplasia) 질환의 원인 유전자입니다.",
					"COL2A1 유전자는 상염색체 우성 또는 열성 유전양상을 보입니다.",
					"본 검사는 염기서열분석법으로 시행되었으며, Large deletion/duplication 검출은 제한적입니다.",
					"본 검사에서 발견된 변이는 2015 ACMG/AMP Guidelines (Genet Med 2015;17:405-24)에 따라 'Pathogenic variant (PV)', 'Likely pathogenic variant (LPV)', 'Variant of uncertain significance (VUS)', 'Likely benign variant (LBV)', 'Benign variant (BV)'의 다섯가지 카테고리로 분류되며, LBV와 BV는 보고하지 않습니다. 또한, 추가 연구 결과에 따라 해당 변이의 분류가 변경 될 수 있습니다."
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo S088 = TestInfo.builder()
			.code("S088")
			.referralDefault("Inherited metabolic disorder")
			.title("PCCA gene plus 결과보고서")
			.name("PCCA gene plus")
			.gene("PCCA")
			.tier2(new String[] {"AARS2","AASS","ABAT","ABCA1","ABCB11","ABCB4","ABCB7","ABCD1","ABCD4","ABCG5","ABCG8","ABHD12","ABHD5","ACAD8","ACAD9","ACADM","ACADS","ACADSB","ACADVL","ACAT1","ACO2","ACOX1","ACSF3","ACY1","ADA","ADAR","ADSL","AFG3L2","AGA","AGK","AGL","AGPS","AGXT","AHCY","AIFM1","AKR1D1","ALAD","ALAS2","ALDH18A1","ALDH3A2","ALDH4A1","ALDH5A1","ALDH6A1","ALDH7A1","ALDOA","ALDOB","ALG1","ALG11","ALG12","ALG14","ALG3","ALG6","ALG8","ALG9","ALPL","AMACR","AMN","AMT","ANO10","APOA1","APOA5","APOB","APOC2","APOE","APRT","APTX","ARG1","ARSA","ARSB","ARSL","ASAH1","ASL","ASPA","ASS1","ATAD3A","ATIC","ATP13A2","ATP6AP1","ATP6V0A2","ATP7A","ATP7B","ATP8B1","ATPAF2","AUH","B3GALNT2","B3GALT6","B3GAT3","B3GLCT","B4GALT1","B4GALT7","BAAT","BCKDHA","BCKDHB","BCKDK","BCS1L","BOLA3","BTD","C19orf12","CA5A","CAT","CBLIF","CBS","CCDC115","CHCHD10","CHKB","CHST14","CHST3","CHST6","CHSY1","CISD2","CLDN16","CLDN19","CLN3","CLN5","CLN6","CLN8","CLPB","CLPP","CNNM2","COA8","COG1","COG4","COG5","COG6","COG7","COG8","COQ2","COQ4","COQ6","COQ7","COQ8A","COQ8B","COQ9","COX10","COX14","COX15","COX20","COX6A1","COX6B1","COX7B","CP","CPOX","CPS1","CPT1A","CPT2","CRPPA","CTH","CTNS","CTSA","CTSC","CTSD","CTSK","CUBN","CYC1","CYP27A1","CYP7B1","D2HGDH","DARS1","DARS2","DBH","DBT","DCXR","DDC","DGUOK","DHCR24","DHCR7","DHFR","DHODH","DHTKD1","DLAT","DLD","DNA2","DNAJC12","DNAJC19","DNAJC5","DNM1L","DNM2","DOLK","DPAGT1","DPM1","DPM2","DPM3","DPYD","DPYS","DYM","EARS2","EBP","ECHS1","ELAC2","ENO3","EPG5","EPM2A","ETFA","ETFB","ETFDH","ETHE1","EXT1","EXT2","FA2H","FAH","FAR1","FARS2","FASTKD2","FBP1","FBXL4","FDX2","FECH","FGFR2","FH","FKRP","FKTN","FMO3","FOLR1","FOXRED1","FTCD","FUCA1","FUT8","FXN","G6PC1","G6PC3","GAA","GABRG2","GALC","GALE","GALK1","GALNS","GALNT3","GALT","GAMT","GARS1","GATM","GBA","GBE1","GCDH","GCH1","GCLC","GDAP1","GFER","GFM1","GFPT1","GK","GLA","GLB1","GLDC","GLRA1","GLRX5","GLUD1","GLUL","GLYCTK","GM2A","GMPPB","GNE","GNMT","GNPAT","GNPTAB","GNPTG","GNS","GPD1","GPHN","GRHPR","GSS","GTPBP3","GUSB","GYG1","GYS1","GYS2","HAAO","HADH","HADHA","HADHB","HAMP","HARS2","HCCS","HCFC1","HEXA","HEXB","HFE","HGD","HGSNAT","HIBCH","HJV","HLCS","HMBS","HMGCL","HMGCS2","HOGA1","HPD","HPRT1","HPS1","HS2ST1","HSD17B10","HSD17B4","HSD3B7","HSPD1","HTRA2","HYAL1","IARS2","IBA57","IDH2","IDS","IDUA","IER3IP1","ISCA2","ISCU","ITPA","IVD","KARS1","KYNU","L2HGDH","LAMP2","LARGE1","LARS1","LARS2","LBR","LCAT","LCT","LDHA","LDLR","LDLRAP1","LIAS","LIPA","LIPT1","LMBRD1","LONP1","LPIN1","LPL","LRPPRC","MAGT1","MAN1B1","MAN2B1","MANBA","MAOA","MARS2","MAT1A","MCCC1","MCCC2","MCEE","MCOLN1","MFF","MFN2","MFSD8","MGAT2","MGME1","MLYCD","MMAA","MMAB","MMACHC","MMADHC","MMUT","MOCS1","MOCS2","MOGS","MPDU1","MPI","MPV17","MRPL3","MRPS22","MSMO1","MTFMT","MTHFR","MTO1","MTPAP","MTR","MTRFR","MTRR","MTTP","MVK","NAGA","NAGLU","NAGS","NARS2","NDUFA1","NDUFA10","NDUFA11","NDUFA2","NDUFAF1","NDUFAF2","NDUFAF3","NDUFAF4","NDUFAF5","NDUFAF6","NDUFB11","NDUFB3","NDUFS1","NDUFS2","NDUFS3","NDUFS4","NDUFS6","NDUFS7","NDUFS8","NDUFV1","NDUFV2","NEU1","NFU1","NGLY1","NHLRC1","NNT","NPC1","NPC2","NSDHL","NT5C3A","NUBPL","OAT","OCRL","OPA1","OPA3","OTC","OXCT1","PAH","PANK2","PC","PCBD1","PCCB","PCK1","PCSK9","PDHA1","PDHB","PDHX","PDP1","PDSS1","PDSS2","PEPD","PET100","PEX1","PEX10","PEX11B","PEX12","PEX13","PEX14","PEX16","PEX19","PEX2","PEX26","PEX3","PEX5","PEX6","PEX7","PFKM","PGAM2","PGAP2","PGAP3","PGK1","PGM1","PGM3","PHGDH","PHKA1","PHKA2","PHKB","PHKG2","PHYH","PIGA","PIGL","PIGN","PIGO","PIGT","PIGV","PINK1","PLA2G6","PMM2","PMPCA","PNP","PNPO","PNPT1","POLG","POLG2","POMGNT1","POMGNT2","POMT1","POMT2","POR","PPA2","PPOX","PPT1","PRKAG2","PRODH","PRPS1","PSAP","PSAT1","PTS","PUS1","PYCR1","PYGL","PYGM","QDPR","RARS2","RBCK1","RBP4","RFT1","RMND1","RNASEH1","RPIA","RPL10","RRM2B","RXYLT1","SACS","SAMHD1","SAR1B","SARS2","SC5D","SCO1","SCO2","SCP2","SDHA","SDHAF1","SDHB","SDHD","SEC23B","SERAC1","SETX","SGSH","SI","SKIV2L","SLC12A3","SLC16A1","SLC17A5","SLC18A2","SLC19A2","SLC19A3","SLC22A5","SLC25A1","SLC25A12","SLC25A13","SLC25A15","SLC25A19","SLC25A20","SLC25A22","SLC25A26","SLC25A3","SLC25A38","SLC25A4","SLC25A46","SLC2A1","SLC2A2","SLC30A10","SLC35A1","SLC35A2","SLC35C1","SLC35D1","SLC37A4","SLC39A14","SLC39A4","SLC39A8","SLC3A1","SLC40A1","SLC46A1","SLC52A2","SLC52A3","SLC5A1","SLC6A19","SLC6A20","SLC6A3","SLC6A8","SLC7A7","SLC7A9","SMPD1","SPG7","SPR","SPTLC1","SPTLC2","SRD5A3","SSR4","ST3GAL3","ST3GAL5","STS","SUCLA2","SUCLG1","SUMF1","SUOX","SURF1","TACO1","TAFAZZIN","TALDO1","TANGO2","TAT","TCN2","TFR2","TIMM8A","TK2","TMEM165","TMEM70","TPK1","TPP1","TRAP1","TREX1","TRIM37","TRMU","TRNT1","TRPM6","TSFM","TTC19","TTC37","TTPA","TUFM","TUSC3","TWNK","TYMP","UGT1A1","UMOD","UMPS","UQCRB","UROD","UROS","VARS2","VIPAS39","VKORC1","VPS33B","WDR45","WFS1","XDH","XYLT1","XYLT2","YARS2","ALG13","ATP5F1A","ATP5F1E","COX4I2","CSTB","DHDDS","GLS","HSPA9","LIPC","MRPS16","NDUFA12","NDUFB9","NDUFC2","OPLAH","PDK3","PIGM","PSPH","RANBP2","RNASET2","RYR1","SDHAF2","SDHC","STAT2","TH","UQCRQ","UROC1","ATP5F1D","ATP5MC3","ATP5PO","B4GALNT1","BCAT2","C1QBP","CARS2","COA6","COA7","CRLS1","CSGALNACT1","EHBP1L1","EOGT","FDXR","FLAD1","GALM","GALNT2","GCSH","GFM2","GMPPA","GORAB","ISCA1","LETM1","LIPT2","LYRM4","LYRM7","MDH2","MECR","MICOS13","MICU1","MIPEP","MPC1","MRPL44","MRPS2","MRPS34","MSTO1","NADK2","NAXD","NAXE","NDUFA13","NDUFA4","NDUFA6","NDUFA8","NDUFA9","NDUFAF8","NDUFB10","NDUFB8","NFS1","NSUN3","OGDH","PARS2","PIGS","PMPCB","PNPLA2","PNPLA8","POMK","QRSL1","RTN4IP1","SFXN4","SHMT2","SLC25A32","SLC25A42","SLC5A6","SSBP1","STT3A","TARS2","TFAM","TIMM50","TIMMDC1","TMEM126B","TMEM199","TOP3A","TRIT1","TRMT10C","TRMT5","UPB1","UQCC2","UQCRC2","UQCRFS1","WARS2","XPNPEP3","ACACA","ALG2","ARSK","ATP5F1B","CLCN7","COA3","COASY","COG3","COX5A","CTSF","CYCS","DCC","EDEM3","EHHADH","ERAL1","FCSK","GATC","GPIHBP1","GRN","LDHD","LMF1","MAN2B2","MRM2","MRPS14","MRPS7","NDUFB7","NUS1","PET117","PITRM1","PTCD3","QARS1","SLC31A1","SSR3","TKFC","TMEM65","TRAPPC11","UQCC3","UQCRC1","VPS16","VPS33A","YME1L1"})
			.target("PCCA on Chromosome 13q32.3")
			.limitations(new String[] {
					"PCCA 유전자는 13번 염색체 장완(13q32.3)에 위치하며 프로피온산혈증(propionic academia)의 원인 유전자 중 하나입니다. PCCA 유전자의 변이는 propionyl-CoA carboxylase 생성을 방해하거나 효소의 활성을 감소시켜 신체에 propionyl-CoA 및 기타 잠재적으로 유해한 화합물이 독성 수준까지 축적되어 뇌와 신경계를 손상시켜 프로피온산혈증을 유발할 수 있습니다. 가장 흔한 형태인 신생아 발병형은 출생후 각성 감소가 특징이며, 원인을 알 수 없는 진행성 뇌병증이 뒤 따르고, 젖산증, 케톤뇨증, 저혈당증, 고 암모니아혈증 및 세포 감소증을 동반한 대상성 산증이 자주 동반되는 증상을 보입니다.",
					"PCCA 유전자는 상염색체 열성 유전양상을 보입니다.",
					"본 검사는 염기서열분석법으로 시행되었으며, Large deletion/duplication 검출은 제한적입니다.",
					"본 검사에서 발견된 변이는 2015 ACMG/AMP Guidelines (Genet Med 2015;17:405-24)에 따라 'Pathogenic variant (PV)', 'Likely pathogenic variant (LPV)', 'Variant of uncertain significance (VUS)', 'Likely benign variant (LBV)', 'Benign variant (BV)'의 다섯가지 카테고리로 분류되며, LBV와 BV는 보고하지 않습니다. 또한, 추가 연구 결과에 따라 해당 변이의 분류가 변경 될 수 있습니다."
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo S089 = TestInfo.builder()
			.code("S089")
			.referralDefault("Lysosomal storage disease")
			.title("GAA gene plus 결과보고서")
			.name("GAA gene plus")
			.gene("GAA")
			.tier2(new String[] {"AGA","ARSA","ARSB","ASAH1","CLN3","CLN5","CLN6","CLN8","CTNS","CTSA","CTSD","CTSK","DNAJC5","FUCA1","GALC","GALNS","GBA","GLA","GLB1","GM2A","GNE","GNPTAB","GNPTG","GNS","GUSB","HEXA","HEXB","HGSNAT","HYAL1","IDS","IDUA","LAMP2","LIPA","MAN2B1","MANBA","MCOLN1","MFSD8","NAGA","NAGLU","NEU1","NPC1","NPC2","PPT1","PSAP","SGSH","SLC17A5","SMPD1","SUMF1","TPP1","ABCD1","AGL","ALDOB","ARSG","ATP13A2","CTSF","G6PC1","GALE","GALK1","GALK2","GALT","GBE1","HPRT1","PEX1","PEX10","PHKA2","PYGL","PYGM","SLC37A4","VPS16","VPS33A","CLCN7"})
			.target("GAA on Chromosome 17q25.3")
			.limitations(new String[] {
					"GAA 유전자는 17번 염색체 장완(17q25.3)에 위치하며 폼페병(Pompe disease) 원인 유전자입니다. 폼페병은 당원이 축적되어 생기는 질환으로 몸의 에너지 원천인 당원을 분해하는데 필수적인 알파-1,4 글루코시다아제(acid alpha-glucosidase) 효소의 선천적 결핍이 원인으로 발생합니다. 영아는 심비대, 긴장 저하, 심근 병증, 호흡 곤란, 근육 약화 등 증상을 보이며, 성인의 경우 유아에 비해 진행 속도가 느리며 기침 장애, 재발성 흉부 감염, 근력 저하, 진행성 근육 약화 등의 증상을 보입니다.",
					"GAA 유전자는 상염색체 열성 유전양상을 보입니다.",
					"본 검사는 염기서열분석법으로 시행되었으며, Large deletion/duplication 검출은 제한적입니다.",
					"본 검사에서 발견된 변이는 2015 ACMG/AMP Guidelines (Genet Med 2015;17:405-24)에 따라 'Pathogenic variant (PV)', 'Likely pathogenic variant (LPV)', 'Variant of uncertain significance (VUS)', 'Likely benign variant (LBV)', 'Benign variant (BV)'의 다섯가지 카테고리로 분류되며, LBV와 BV는 보고하지 않습니다. 또한, 추가 연구 결과에 따라 해당 변이의 분류가 변경 될 수 있습니다."
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo S090 = TestInfo.builder()
			.code("S090")
			.referralDefault("MODY")
			.title("ABCC8 gene plus 결과보고서")
			.name("ABCC8 gene plus")
			.gene("ABCC8")
			.tier2(new String[] {"AKT2","APPL1","CEL","CISD2","DCAF17","DNAJC3","DYRK1B","GATA4","GATA6","GCK","HNF1A","HNF1B","HNF4A","INS","INSR","KCNJ11","LMNA","NEUROD1","PAX6","PCBD1","PDX1","PIK3R1","PLIN1","POLD1","PPARG","PPP1R15B","RFX6","SLC29A3","TRMT10A","WFS1","ZBTB20","ZFP57","ZMPSTE24","AGPAT2","ALMS1","BLK","BLM","BSCL2","CAVIN1","DMXL2","EIF2AK3","ENPP1","FGFR3","FOXC2","FOXP3","GLIS3","HAMP","HFE","HJV","IER3IP1","IL2RA","KLF11","LIPC","LRBA","MNX1","NEUROG3","NKX2-2","NSMCE2","PAX4","PCNT","PCYT1A","POC1A","PPP1R3A","PSMB8","PTF1A","PTPRD","SLC19A2","SLC2A2","SLC40A1","STAT1","STAT3","SYT9","TFR2","WRN","EIF2B1","EIF2S3","YIPF5","ZNF808","DUT","CNOT1","FICD","ONECUT1","PDIA6"})
			.target("ABCC8 on Chromosome 11p15.1 ")
			.limitations(new String[] {
					"ABCC8 유전자는 11번 염색체 단완(11p15.1)에 위치하며 선천성 고인슐린증의 원인 유전자 중 하나이며, 가족성 고인슐린증 및 영유아기 지속성 저혈당 관련 주요 유전자입니다. ABCC8 유전자는 췌장 세포의 ATP-sensitive K channel 복합체의 일부인 sulfonylurea receptor (SUR1)를 encoding하는 유전자로 SUR1은 ATP에 민감한 칼륨 채널과 인슐린 방출을 조절하는 역할을 합니다.",
					"ABCC8 유전자는 상염색체 우성 또는 열성 유전 양상을 보입니다.",
					"본 검사는 염기서열분석법으로 시행되었으며, Large deletion/duplication 검출은 제한적입니다.",
					"본 검사에서 발견된 변이는 2015 ACMG/AMP Guidelines (Genet Med 2015;17:405-24)에 따라 'Pathogenic variant (PV)', 'Likely pathogenic variant (LPV)', 'Variant of uncertain significance (VUS)', 'Likely benign variant (LBV)', 'Benign variant (BV)'의 다섯가지 카테고리로 분류되며, LBV와 BV는 보고하지 않습니다. 또한, 추가 연구 결과에 따라 해당 변이의 분류가 변경 될 수 있습니다."
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo S091 = TestInfo.builder()
			.code("S091")
			.referralDefault("Hypogonadotropic hypogonadism")
			.title("CHD7 gene plus 결과보고서")
			.name("CHD7 gene plus")
			.gene("CHD7")
			.tier2(new String[] {"AMMECR1","ANKRD11","BLM","BRAF","BTK","CBL","CCDC8","CDC6","CDKN1C","CDT1","CENPJ","CREBBP","CRIPT","CUL7","DHCR7","EP300","ERCC6","ERCC8","FANCA","FANCB","FANCC","FANCD2","FANCE","FANCF","FANCG","FANCI","FANCL","FGD1","FGF8","FGFR1","FOXA2","GH1","GHR","GHRHR","GHSR","GLI2","GLI3","GNRHR","HDAC8","HESX1","HRAS","IGF1","IGF1R","IGFALS","IGSF1","INSR","KDM6A","KMT2D","KRAS","LHX3","LHX4","LIG4","MAP2K1","MAP2K2","NBN","NIPBL","NRAS","OBSL1","ORC1","ORC4","ORC6","OTX2","PAPPA2","PCNT","PIK3R1","PITX2","PNPLA6","POU1F1","PROKR2","PROP1","PTPN11","RAD21","RAF1","RBBP8","RIT1","ROR2","RPL10","RPS6KA3","SAMD9","SHOC2","SHOX","SMC1A","SMC3","SOS1","SOX2","SOX3","SRCAP","STAT5B","TBCE","TBX19","TRIM37","WRN","XRCC4","ZFP57","ANOS1","ARCN1","BBS1","BBS10","BBS12","BBS2","BBS9","CDON","FSHB","GNRH1","HS6ST1","IL17RD","KANSL1","KCNQ1","KISS1","KISS1R","LHB","MKKS","NR0B1","NSMF","PROK2","RNPC3","SEMA3A","SHH","SPRY4","TAC3","TACR3","TCF7L1","CUL4B","DCAF17","FEZF1","HAMP","HFE","KLB","SLC29A3","SLC40A1","SOX10","TFR2","WDR11","LEP","LEPR","NDNF","PLXNA3","SEMA3F","CCDC141","CLPP","ARHGAP35","CPE","DUSP6","FGF17","IGSF10","SOX11","TCF12"})
			.target("CHD7 on Chromosome 8q12.2")
			.limitations(new String[] {
					"CHD7 유전자는 8번 염색체 장완(8q12.2)에 위치하며 CHARGE 증후군의 원인 유전자입니다. CHARGE 증후군은 안조직결손(coloboma), 심장결함(heart defect), 후비공폐쇄증(atresia of the choanae), 성장과 발달의 지연(retarded growth and development), 생식기관의 저형성(genital hypoplasia), 귀의 기형(ear abnormalities)을 임상적 특징으로 하는 선천 기형 질환입니다.",
					"CHD7 유전자는 상염색체 우성 유전 양상을 보입니다.",
					"본 검사는 염기서열분석법으로 시행되었으며, Large deletion/duplication 검출은 제한적입니다.",
					"본 검사에서 발견된 변이는 2015 ACMG/AMP Guidelines (Genet Med 2015;17:405-24)에 따라 'Pathogenic variant (PV)', 'Likely pathogenic variant (LPV)', 'Variant of uncertain significance (VUS)', 'Likely benign variant (LBV)', 'Benign variant (BV)'의 다섯가지 카테고리로 분류되며, LBV와 BV는 보고하지 않습니다. 또한, 추가 연구 결과에 따라 해당 변이의 분류가 변경 될 수 있습니다."
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo S092 = TestInfo.builder()
			.code("S092")
			.referralDefault("Congenital hypothyroidism")
			.title("DUOX2 gene plus 결과보고서")
			.name("DUOX2 gene plus")
			.gene("DUOX2")
			.tier2(new String[]{"DUOXA2","FOXE1","GLIS3","GNAS","HESX1","IGSF1","IRS4","IYD","LHX3","LHX4","NKX2-1","OTX2","PAX8","POU1F1","PRKAR1A","PROP1","SECISBP2","SLC16A2","SLC26A4","SLC26A7","SLC5A5","TBL1X","TG","THRA","THRB","TPO","TRHR","TSHB","TSHR","CDCA8","NKX2-5","TRH","TUBB1"})
			.target("DUOX2 on Chromosome 15q21.1")
			.limitations(new String[] {
					"DUOX2 유전자는 15번 염색체 장완(15q21.1)에 위치하며 선천성 갑상선 기능저하증의 원인 유전자 중 하나입니다. 선천성 갑상선 기능저하증의 원인은 갑상선호르몬의 생합성에 장애가 있거나 분비에 문제가 있는 갑상선호르몬 합성장애입니다. DUOX2 유전자의 변이는 H2O2 생성 장애로 갑상샘종은 나타나지 않으면서 요오드의 유기화 장애를 야기합니다.",
					"DUOX2 유전자는 대부분 상염색체 열성 유전 양상을 보입니다.",
					"본 검사는 염기서열분석법으로 시행되었으며, Large deletion/duplication 검출은 제한적입니다.",
					"본 검사에서 발견된 변이는 2015 ACMG/AMP Guidelines (Genet Med 2015;17:405-24)에 따라 'Pathogenic variant (PV)', 'Likely pathogenic variant (LPV)', 'Variant of uncertain significance (VUS)', 'Likely benign variant (LBV)', 'Benign variant (BV)'의 다섯가지 카테고리로 분류되며, LBV와 BV는 보고하지 않습니다. 또한, 추가 연구 결과에 따라 해당 변이의 분류가 변경 될 수 있습니다."
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo S093 = TestInfo.builder()
			.code("S093")
			.referralDefault("Connective tissue disease")
			.title("FBN1 gene plus 결과보고서")
			.name("FBN1 gene plus")
			.gene("FBN1")
			.tier2(new String[] {"ABL1","ACTA2","ADAMTS2","ADAMTSL4","AEBP1","ALDH18A1","ATP6V0A2","ATP6V1A","ATP7A","B3GALT6","B4GALT7","BGN","C1R","C1S","CBS","CHST14","COL12A1","COL1A1","COL1A2","COL3A1","COL5A1","COL5A2","COL6A1","COL6A2","COL6A3","DSE","EFEMP2","ELN","FBLN5","FBN2","FKBP14","FLCN","FLNA","GORAB","IPO8","LOX","LTBP4","MYH11","MYLK","NOTCH1","PLOD1","PRDM5","PRKG1","PYCR1","RIN2","ROBO3","SKI","SLC2A10","SLC39A13","SMAD2","SMAD3","SMAD4","TGFB2","TGFB3","TGFBR1","TGFBR2","TNXB","ZNF469","ADAMTS10","ADAMTSL2","DCC","EFEMP1","GGCX","LTBP1","PIEZO2","SERPINA1","TSC1","TSC2","ARIH1","LTBP2","ADAMTS17","COL2A1","COL4A5","COLGALT1","ENPP1","FOXE3","GATA5","HCN4","MED12","MFAP5","SMAD6","ZDHHC9"})
			.target("FBN1 on Chromosome 15q21.1")
			.limitations(new String[] {
					"FBN1 유전자는 마르팡증후군(Marfan syndrome), 수정체 편위(ectopia lentis), 말단왜소 이형성증(acromicric dysplasia)과 관련된 유전자로 15번 염색체 장완(15q21.1)에 위치하고 있습니다. 이 유전자는 66개의 엑손(exon)으로 구성된 매우 큰 유전자로 서열 변이(sequence variant), 결실/중복(deletion/duplication) 등 1,300가지 이상의 다양한 변이가 보고되어 있습니다.",
					"FBN1 유전자는 상염색체 우성 유전양상을 보입니다.",
					"본 검사는 염기서열분석법으로 시행되었으며, Large deletion/duplication 검출은 제한적입니다.",
					"본 검사에서 발견된 변이는 2015 ACMG/AMP Guidelines (Genet Med 2015;17:405-24)에 따라 'Pathogenic variant (PV)', 'Likely pathogenic variant (LPV)', 'Variant of uncertain significance (VUS)', 'Likely benign variant (LBV)', 'Benign variant (BV)'의 다섯가지 카테고리로 분류되며, LBV와 BV는 보고하지 않습니다. 또한, 추가 연구 결과에 따라 해당 변이의 분류가 변경 될 수 있습니다."
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo S096 = TestInfo.builder()
			.code("S096").category(Category.BRCA)
			.referralDefault("유전성 암")
			.title("BRCA1 gene plus / 전용 결과보고서")
			.name("BRCA1 gene plus / 전용")
			.gene("BRCA1")
			.tier2("AIP,APC,ATM,AXIN2,BAP1,BARD1,BMPR1A,BRCA1,BRCA2,BRIP1,CDC73,CDH1,CDK4,CDKN1B,CDKN2A,CHEK2,CTNNA1,DICER1,EPCAM,EXT1,EXT2,FH,FLCN,GREM1,HOXB13,HRAS,KIT,MAX,MEN1,MET,MLH1,MSH2,MSH3,MSH6,MUTYH,NBN,NF1,NF2,NTHL1,PALB2,PMS2,POLD1,POLE,PTCH1,PTEN,RAD51C,RAD51D,RAD54L,RB1,RET,SDHA,SDHAF2,SDHB,SDHC,SDHD,SMAD4,SMARCA4,SMARCB1,STK11,SUFU,TMEM127,TP53,TSC1,TSC2,VHL,WT1,CDK12,CHEK1,FANCL,RAD51B,RAD50,MRE11,PPP2R2A".split(","))
			.penetrance("Breast cancer (50-80%), secondary breast cancer (27% within 5 yrs), ovarian cancer (24-40%)")
			.target("BRCA1 on Chromosome 17q21")
			.limitations(new String[] {
					"유방암은 유전성, 가족성, 산발성으로 분류되며, 이 중 유전성 유방암은 전체 유방암의 약 5-10%를차지합니다. KOHBRA study에 의하면 BRCA 병원성 변이는 유방암/난소암 가족력이 있는 유방암 환자의약 22% 정도에서 발견됩니다(Fam Cancer 2013;12:75-81).",
					"BRCA1 병원성 변이를 보유하고 있는 여성의 경우 유방암 발생 위험이 50-80% 정도이며, 이차 유방암발생 위험도는 5년 내에 27% 정도, 난소암 발생 위험도는 24-40% 정도로 알려져 있습니다. 남성의 경우 유방암 발생 위험도는 1-2% 이며, 전립선암, 췌장암 발생 위험도가 증가하는 것으로 알려져 있습니다.",
					"BRCA1 병원성 변이는 상염색체 우성 유전 방식을 따르며 자녀에서 돌연변이 보유 확률은 50% 정도입니다. 가족 검사 및 유전 상담이 추천됩니다.",
					"본 검사는 염기서열분석법으로 시행되었으며, Large deletion/duplication 검출은 제한적입니다.",
					"본 검사에서 발견된 변이는 2015 ACMG/AMP Guidelines (Genet Med 2015;17:405-24)에 따라 'Pathogenic variant (PV)', 'Likely pathogenic variant (LPV)', 'Variant of uncertain significance (VUS)', 'Likely benign variant (LBV)', 'Benign variant (BV)'의 다섯가지 카테고리로 분류되며, LBV와 BV는 보고하지 않습니다. 또한, 추가 연구 결과에 따라 해당 변이의 분류가 변경 될 수 있습니다."
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo S097 = TestInfo.builder()
			.code("S097").category(Category.BRCA)
			.referralDefault("유전성 암")
			.title("BRCA2 gene plus / 전용 결과보고서")
			.name("BRCA2 gene plus / 전용")
			.gene("BRCA2")
			.tier2("AIP,APC,ATM,AXIN2,BAP1,BARD1,BMPR1A,BRCA1,BRCA2,BRIP1,CDC73,CDH1,CDK4,CDKN1B,CDKN2A,CHEK2,CTNNA1,DICER1,EPCAM,EXT1,EXT2,FH,FLCN,GREM1,HOXB13,HRAS,KIT,MAX,MEN1,MET,MLH1,MSH2,MSH3,MSH6,MUTYH,NBN,NF1,NF2,NTHL1,PALB2,PMS2,POLD1,POLE,PTCH1,PTEN,RAD51C,RAD51D,RAD54L,RB1,RET,SDHA,SDHAF2,SDHB,SDHC,SDHD,SMAD4,SMARCA4,SMARCB1,STK11,SUFU,TMEM127,TP53,TSC1,TSC2,VHL,WT1,CDK12,CHEK1,FANCL,RAD51B,RAD50,MRE11,PPP2R2A".split(","))
			.penetrance("Breast cancer (40-70%), secondary breast cancer (12% within 5 yrs, 40-50% at 20 yrs), ovarian cancer (11-18%)")
			.target("BRCA2 on Chromosome 13q12.3")
			.limitations(new String[] {
					"유방암은 유전성, 가족성, 산발성으로 분류되며, 이 중 유전성 유방암은 전체 유방암의 약 5-10%를 차지합니다. KOHBRA study에 의하면 BRCA 병원성 변이는 유방암/난소암 가족력이 있는 유방암 환자의 약 22% 정도에서 발견됩니다(Fam Cancer 2013;12:75-81).",
					"BRCA2 병원성 변이를 보유하고 있는 여성의 경우 유방암 발생 위험이 40-70% 정도이며, 이차 유방암발생 위험도는 5년 내에 12% 정도 (20년 내에 45-50%), 난소암 발생 위험도는 11-18% 정도로 알려져 있습니다. 남성의 경우 유방암 발생 위험도는 5-10% 이며, 전립선암, 췌장암 발생 위험도가증가하는 것으로 알려져 있습니다",
					"BRCA2 병원성 변이는 상염색체 우성 유전 방식을 따르며 자녀에서 돌연변이 보유 확률은 50% 정도 입니다. 가족 검사 및 유전 상담이 추천됩니다.",
					"본 검사는 염기서열분석법으로 시행되었으며, Large deletion/duplication 검출은 제한적입니다.",
					"본 검사에서 발견된 변이는 2015 ACMG/AMP Guidelines (Genet Med 2015;17:405-24)에 따라 'Pathogenic variant (PV)', 'Likely pathogenic variant (LPV)', 'Variant of uncertain significance (VUS)', 'Likely benign variant (LBV)', 'Benign variant (BV)'의 다섯가지 카테고리로 분류되며, LBV와 BV는 보고하지 않습니다. 또한, 추가 연구 결과에 따라 해당 변이의 분류가 변경 될 수 있습니다."
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo S102 = TestInfo.builder()
			.code("S102")
			.referralDefault("Polycystic kidney disease")
			.title("PKD1 gene plus 결과보고서")
			.name("PKD1 gene plus")
			.gene("PKD1")
			.tier2(new String[] {"ANKS6","CEP164","CEP83","COL4A1","DNAJB11","DZIP1L","GANAB","HNF1B","INVS","MAPKBP1","NPHP1","NPHP3","NPHP4","PKD2","PKHD1","TMEM67","TSC1","TSC2","TTC21B","UMOD","VHL","WDR19","ALG8","ALG9","CEP290","COL4A4","ETFA","FLCN","LRP5","NOTCH2","PAX2","PMM2","SEC61A1","ALG5","GLA","IFT140","TULP3","XPNPEP3","NEK8","PRKCSH","SEC63","ZNF423"})
			.target("PKD1 on Chromosome 16p13.3")
			.limitations(new String[] {
					"PKD1 유전자는 상염색체 우성 다낭성 신장질환(Autosomal dominant polycystic kidney disease, ADPKD) 원인의 ~78%를 차지하는 유전자입니다. 상염색체 우성 다낭성 신장질환은 주로 성인기에 발병하는 유전 질환으로, 신장이 수많은 낭종으로 대체되게 되면서 정상 신기능의 장애가 발생하는 질환입니다. 전체 인구 약 400 ~ 1000명당 1명에서 발견되며, 이환된 환자의 약 50%가 투석이나 이식을 필요로 하는 말기 신부전으로 이행하는 것으로 알려져 있습니다.",
					"PKD1 유전자는 Polycystin-1 을 암호화하며, 상염색체 우성 유전양상을 보입니다.",
					"본 검사는 염기서열분석법으로 시행되었으며, Large deletion/duplication 검출은 제한적입니다.",
					"본 검사에서 발견된 변이는 2015 ACMG/AMP Guidelines (Genet Med 2015;17:405-24)에 따라 'Pathogenic variant (PV)', 'Likely pathogenic variant (LPV)', 'Variant of uncertain significance (VUS)', 'Likely benign variant (LBV)', 'Benign variant (BV)'의 다섯가지 카테고리로 분류되며, LBV와 BV는 보고하지 않습니다. 또한, 추가 연구 결과에 따라 해당 변이의 분류가 변경될 수 있습니다."
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo S103 = TestInfo.builder()
			.code("S103")
			.referralDefault("CdLS or Growth retardation")
			.title("NIPBL gene plus 결과보고서")
			.name("NIPBL gene plus")
			.gene("NIPBL")
			.tier2(new String[] {"ACAN","ANKRD11","BLM","BRAF","BRCA2","BRIP1","CBL","CCDC8","CDKN1C","CUL7","ERCC4","FANCA","FANCB","FANCC","FANCD2","FANCE","FANCF","FANCG","FANCI","FANCL","FGFR3","HMGA2","HRAS","IGF1","IGF1R","IGF2","KRAS","LZTR1","MAP2K1","MAP2K2","NBN","NRAS","OBSL1","PALB2","PIK3R1","PLAG1","PPP1CB","PTPN11","RAF1","RIT1","SHOC2","SLX4","SOS1","SOS2","SRCAP","TOP3A","TRIM37","UBE2T","ANAPC1","CCDC186","CEP57","COG4","FOXP4","INTS1","KDM3B","MSTO1","MTX2","NBAS","NHLRC2","NLRP2","NLRP5","NLRP7","PADI6","PLK4","RAP1B","RNPC3","SETD5","ZFP57","AMMECR1","CDC6","CDT1","CENPJ","CHD7","CREBBP","CRIPT","DHCR7","EP300","ERCC6","ERCC8","FGD1","FGF8","FGFR1","GH1","GHRHR","GLI2","GLI3","HDAC8","HESX1","INSR","KANSL1","KDM6A","KMT2D","LHX3","LHX4","LIG4","NIPBL","ORC1","ORC4","ORC6","OTX2","PAPPA2","PCNT","PITX2","PNPLA6","POU1F1","PROKR2","PROP1","RAD21","RBBP8","ROR2","RPL10","RPS6KA3","SAMD9","SMC1A","SMC3","SOX2","SOX3","TBCE","WRN","XRCC4","GHR","IGFALS","NPR2","RRAS2","SHOX","SPRED2","STAT5B","ARCN1","BTK","GGPS1","MAPK1","MRAS","NF1","RAD51","RASA2","VPS50","ZNF668"})
			.target("NIPBL on Chromosome 5p13.2")
			.limitations(new String[] {
					"NIPBL 유전자는 코넬리아 드랑에 증후군(Cornelia de Lange syndrome)의 원인 유전자 중 하나입니다. 코넬리아 드랑에 증후군은 특징적인 얼굴 형태와 발달장애, 지적장애, 성장 장애, 다모증, 상지 기형 등을 보이는 선천성 희귀질환으로, 비교적 가벼운 증상부터 심한 기형까지 환자에 따라 다양한 중증도를 나타내며, 발생 빈도는 1만~3만 명당 1명의 비율입니다.",
					"NIPBL 유전자는 delangin 단백질 합성에 관여하며, 상염색체 우성 유전양상을 보입니다.",
					"본 검사는 염기서열분석법으로 시행되었으며, Large deletion/duplication 검출은 제한적입니다.",
					"본 검사에서 발견된 변이는 2015 ACMG/AMP Guidelines (Genet Med 2015;17:405-24)에 따라 'Pathogenic variant (PV)', 'Likely pathogenic variant (LPV)', 'Variant of uncertain significance (VUS)', 'Likely benign variant (LBV)', 'Benign variant (BV)'의 다섯가지 카테고리로 분류되며, LBV와 BV는 보고하지 않습니다. 또한, 추가 연구 결과에 따라 해당 변이의 분류가 변경될 수 있습니다."
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo S109 = TestInfo.builder()
			.code("S109")
			.referralDefault("Noonan syndrome")
			.title("산전 PTPN11 gene plus 결과보고서")
			.name("산전 PTPN11 gene plus")
			.gene("PTPN11")
			// S051과 동일
			.tier2("BRAF,CBL,HRAS,KRAS,LZTR1,MAP2K1,MAP2K2,MRAS,NF1,NRAS,PPP1CB,PTPN11,RAF1,RIT1,RRAS2,SHOC2,SOS1,SOS2,SPRED1,RASA2,RRAS".split(","))
			.target("PTPN11 on Chromosome 12q24.13")
			.limitations(new String[] {
					"PNPN11은 12번 염색체의 장완(12q24.13)에 위치하는 유전자로, Noonan syndrome의 가장 흔한 원인 유전자입니다. Noonan syndrome은 short stature (단신), congenital heart detect (선천성 심내 결손증), webbed neck (익상경), unusual chest shape (가슴 모양의 기형), developmental delay (발달 지연) 등을 특징으로 하는 질환입니다.",
					"PNPN11 유전자는 상염색체 우성 유전양상을 보입니다.",
					"산전 검체의 특성상 모체세포 오염 가능성을 배제할 수 없으며 이것이 결과값에 영향을 줄 수도 있습니다.",
					"본 검사는 염기서열분석법으로 시행되었으며, Large deletion/duplication 검출은 제한적입니다.",
					"본 검사에서 발견된 변이는 2015 ACMG/AMP Guidelines (Genet Med 2015;17:405-24)에 따라 'Pathogenic variant (PV)', 'Likely pathogenic variant (LPV)', 'Variant of uncertain significance (VUS)', 'Likely benign variant (LBV)', 'Benign variant (BV)'의 다섯가지 카테고리로 분류되며, LBV와 BV는 보고하지 않습니다. 또한, 추가 연구 결과에 따라 해당 변이의 분류가 변경 될 수 있습니다."
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo S110 = TestInfo.builder()
			.code("S110")
			.referralDefault("Skeletal dysplsia")
			.title("산전 FGFR3 gene plus 결과보고서")
			.name("산전 FGFR3 gene plus")
			.gene("FGFR3")
			// S041과 동일
			.tier2("ABCC9,ACAN,ACP5,ACVR1,ADAMTS10,ADAMTS17,ADAMTSL2,AGA,AGPS,ALG12,ALG3,ALG9,ALPL,ALX1,ALX3,ALX4,AMER1,ANKH,ANKRD11,ANO5,ANTXR2,ARHGAP31,ARL6,ARSB,ARSL,ASXL1,ASXL2,ATP6V0A2,ATP7A,B3GALT6,B3GAT3,B3GLCT,B4GALT7,BBS1,BBS10,BBS12,BBS2,BBS4,BBS5,BBS7,BBS9,BHLHA9,BMP1,BMP2,BMPER,BMPR1B,BPNT2,C2CD3,CA2,CANT1,CASR,CC2D2A,CCDC8,CCN6,CCNQ,CDC45,CDH3,CDKN1C,CDT1,CEP120,CEP290,CFAP410,CHST14,CHST3,CHSY1,CILK1,CLCN5,CLCN7,COG1,COL10A1,COL11A1,COL11A2,COL1A1,COL1A2,COL2A1,COL9A1,COL9A2,COL9A3,COLEC11,COMP,CREB3L1,CREBBP,CRTAP,CSPP1,CTSA,CTSC,CTSK,CUL7,CYP27B1,CYP2R1,DDR2,DHCR24,DHCR7,DHODH,DIS3L2,DLL3,DLL4,DLX3,DLX5,DMP1,DNMT3A,DOCK6,DPAGT1,DPM1,DSPP,DVL1,DVL3,DYM,DYNC2H1,DYNC2I1,DYNC2I2,DYNC2LI1,DYNLT2B,EBP,EED,EFTUD2,EIF2AK3,ENPP1,EOGT,ERF,ESCO2,EVC,EVC2,EXT1,EXT2,EXTL3,EZH2,FAM111A,FAM20C,FBN1,FBN2,FERMT3,FGF10,FGF16,FGF23,FGFR1,FGFR2,FGFR3,FIG4,FKBP10,FLNA,FLNB,FN1,FUCA1,FZD2,GALNS,GALNT3,GDF5,GDF6,GHR,GJA1,GLB1,GLI3,GNAS,GNPAT,GNPTAB,GNPTG,GNS,GORAB,GPC6,GSC,GUSB,HDAC8,HES7,HGSNAT,HOXA13,HOXD13,HPGD,HSPG2,IDH1,IDS,IDUA,IFIH1,IFITM5,IFT122,IFT140,IFT172,IFT43,IFT52,IFT80,IFT81,IHH,IKBKG,IL11RA,IL1RN,INPPL1,KAT6B,KIAA0753,KIF22,KIF7,KMT2D,LBR,LEMD3,LIFR,LMBR1,LMNA,LMX1B,LONP1,LPIN2,LRP4,LRP5,LTBP3,MAFB,MAN2B1,MAP3K7,MASP1,MATN3,MEGF8,MEOX1,MESP2,MGP,MKKS,MKS1,MMP13,MMP2,MNX1,MPDU1,MSX2,MYCN,NAGLU,NANS,NBAS,NEK1,NEU1,NF1,NFIX,NIPBL,NKX3-2,NLRP3,NOG,NOTCH1,NOTCH2,NPR2,NSD1,NSDHL,OBSL1,OFD1,ORC1,ORC4,ORC6,OSTM1,P3H1,P4HB,PAPSS2,PAX3,PCNT,PCYT1A,PDE3A,PDE4D,PEX5,PEX7,PGM3,PHEX,PHGDH,PIGT,PIGV,PIK3C2A,PIK3R1,PITX1,PLOD2,PLS3,POC1A,POLR1A,POLR1C,POLR1D,POP1,POR,PPIB,PRKAR1A,PRMT7,PSAT1,PSPH,PTDSS1,PTH1R,PTHLH,PTPN11,PUF60,PYCR1,RAB23,RAB33B,RASGRP2,RBM8A,RBPJ,RECQL4,RFT1,RMRP,RNU4ATAC,ROR2,RPGRIP1L,RPL13,RUNX2,SALL1,SALL4,SBDS,SCARF2,SEC24D,SERPINF1,SERPINH1,SETD2,SF3B4,SFRP4,SGSH,SH3BP2,SH3PXD2B,SHOX,SKI,SLC10A7,SLC17A5,SLC26A2,SLC29A3,SLC34A1,SLC34A3,SLC35C1,SLC35D1,SLC39A13,SLCO2A1,SMAD3,SMAD4,SMARCAL1,SMC1A,SMC3,SMOC1,SNRPB,SNX10,SOST,SOX9,SP7,SPARC,SUMF1,TALDO1,TAPT1,TBCE,TBX15,TBX3,TBX4,TBX5,TBX6,TBXAS1,TCIRG1,TCOF1,TCTN2,TCTN3,TENT5A,TERT,TGFB1,TGFB2,TGFBR2,TMCO1,TMEM165,TMEM216,TMEM231,TMEM38B,TNFRSF11A,TNFRSF11B,TNFSF11,TP63,TRAPPC2,TREM2,TRIP11,TRPS1,TRPV4,TRPV6,TTC21B,TTC8,TWIST1,TYROBP,VDR,WDPCP,WDR19,WDR35,WNT1,WNT10B,WNT5A,WNT7A,XRCC4,XYLT1,XYLT2,YY1,ZMPSTE24,ZSWIM6,ABL1,ANAPC1,ARCN1,B9D1,COG4,CSGALNACT1,DCC,EFNB1,EN1,FBLN1,GNPNAT1,GPX4,GZF1,HDAC4,HHAT,HNRNPK,HS2ST1,IGF1R,KAT6A,KDELR2,KIAA1217,LTBP1,MANBA,MBTPS1,MIR17HG,MMP9,MTX2,MYO18B,NEPRO,NMNAT1,NPR3,NXN,PAM16,PFN1,PISD,PKDCC,PLEKHM1,POLR1B,PRKG2,RAD21,RINT1,SCUBE3,SMAD6,TCF12,TGFBR1,TMEM251,TONSL,TWIST2,UBA2,UFSP2,VPS35L,WBP11,ZIC1".split(","))
			.target("FGFR3 on Chromosome 4p16.3")
			.limitations(new String[] {
					"FGFR3는 4번 염색체 단완(4p16.3)에 위치하는 유전자로, 연골무형성증(Achondroplasia) 및 연골저형성증(Hypochondroplasia)의 주요 원인입니다. 연골무형성증(Achondroplasia) 및 연골저형성증(Hypochondroplasia)의 일차적인 특징으로 작은 키, 균형(비율)이 안 맞는 짧은 팔과 다리, 경증에서 중증 정도의 짧은 손가락과 발가락, 넓고 짧은 손과 발을 관찰할 수 보입니다.",
					"FGFR3 유전자는 상염색체 우성 유전 양상을 보이나, 대다수 환자에서는 부모에서 없던 변이가 출생 과정에서 De novo로 발생합니다.",
					"산전 검체의 특성상 모체세포 오염 가능성을 배제할 수 없으며 이것이 결과값에 영향을 줄 수도 있습니다.",
					"본 검사는 염기서열분석법으로 시행되었으며, Large deletion/duplication 검출은 제한적입니다.",
					"본 검사에서 발견된 변이는 2015 ACMG/AMP Guidelines (Genet Med 2015;17:405-24)에 따라 'Pathogenic variant (PV)', 'Likely pathogenic variant (LPV)', 'Variant of uncertain significance (VUS)', 'Likely benign variant (LBV)', 'Benign variant (BV)'의 다섯가지 카테고리로 분류되며, LBV와 BV는 보고하지 않습니다. 또한, 추가 연구 결과에 따라 해당 변이의 분류가 변경 될 수 있습니다."
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo S113 = TestInfo.builder()
			.code("S113")
			.referralDefault("Retinitis pigmentosa")
			.title("EYS gene plus 결과보고서")
			.name("EYS gene plus")
			.gene("EYS")
			.tier2(Arrays.stream("ABCA4,ABHD12,ACO2,ADAM9,ADAMTS18,ADGRV1,AGBL5,AHI1,AIPL1,AIRE,ALMS1,ARHGEF18,ARL2BP,ARL6,ATF6,ATOH7,BBS1,BBS10,BBS12,BBS2,BBS4,BBS5,BBS7,BBS9,BEST1,C1QTNF5,CABP4,CACNA1F,CACNA2D4,CAPN5,CC2D2A,CDH23,CDH3,CDHR1,CEP164,CEP290,CEP78,CERKL,CFAP410,CFAP418,CFH,CHM,CIB2,CLN3,CLN5,CLN6,CLN8,CLRN1,CNGA1,CNGA3,CNGB1,CNGB3,CNNM4,COL18A1,COL4A1,CRB1,CRX,CSPP1,CTNNB1,CTSD,CWC27,CYP4V2,DHDDS,EFEMP1,ELOVL4,ERCC6,ERCC8,FAM161A,FLVCR1,FZD4,GNAT1,GNAT2,GNPTG,GPR143,GPR179,GRK1,GRM6,GUCA1A,GUCA1B,GUCY2D,HARS1,HCCS,HGSNAT,HMX1,IDH3A,IDH3B,IFT140,IKBKG,IMPDH1,IMPG1,IMPG2,INPP5E,IQCB1,KCNJ13,KCNV2,KIAA1549,KIF11,KIZ,KLHL7,LCA5,LRAT,LRIT3,LRP2,LRP5,LZTFL1,MAK,MERTK,MFRP,MFSD8,MKKS,MKS1,MYO7A,NDP,NMNAT1,NPHP1,NPHP3,NPHP4,NR2E3,NRL,NYX,OAT,OFD1,OPN1LW,OPN1MW,OTX2,PANK2,PCARE,PCDH15,PCYT1A,PDE6A,PDE6B,PDE6C,PDE6G,PEX1,PEX2,PEX7,PHYH,PLA2G5,POC1B,PPT1,PRCD,PROM1,PRPF3,PRPF31,PRPF4,PRPF6,PRPF8,PRPH2,PRPS1,RAB28,RAX2,RBP3,RBP4,RCBTB1,RD3,RDH12,RDH5,REEP6,RGS9,RHO,RLBP1,RP1,RP1L1,RP2,RP9,RPE65,RPGR,RPGRIP1,RPGRIP1L,RS1,SAG,SCAPER,SDCCAG8,SLC24A1,SLC38A8,SNRNP200,SPATA7,SRD5A3,TIMM8A,TIMP3,TMEM237,TOPORS,TPP1,TRIM32,TRPM1,TSPAN12,TTC8,TTLL5,TUB,TULP1,USH1C,USH1G,USH2A,VCAN,VPS13B,WDPCP,WDR19,WHRN,ZNF408,ZNF423,ABCC6,ACBD5,ADIPOR1,AFG3L2,AHR,ALPK1,AMACR,ARL13B,ARL3,ARSG,ASRGL1,ATXN7,CA4,CCT2,CEP19,CEP250,CLCC1,CLUAP1,CTC1,CTNNA1,CYP2R1,DHX38,DMD,DRAM2,ELOVL1,ESPN,EXOSC2,GDF6,GNB3,GRN,HK1,IFT172,IFT27,IFT74,IFT81,JAG1,KIF3B,LAMA1,LIG3,MAPKAPK3,MIR204,MMACHC,MSTO1,MTRFR,MTTP,MVK,NBAS,NEUROD1,OPN1SW,P3H2,PAX2,PDE6H,PEX6,PGK1,PLK4,PNPLA6,POC5,POMGNT1,PRDM13,RDH11,RGR,RIMS2,ROM1,RTN4IP1,SAMD11,SEMA4A,SLC25A46,SLC37A3,SLC6A6,SPP2,SSBP1,TINF2,TLCD3B,TMEM216,TMEM231,TRAF3IP1,TREX1,TRNT1,TTPA,TUBB4B,TUBGCP4,TUBGCP6,UNC119,USP45,ZFYVE26".split(",")).sorted().toArray(String[]::new))
			.target("EYS on Chromosome 6q12")
			.limitations(new String[] {
					"EYS는 상염색체 열성 유전 양상의 retinitis pigmentosa의 흔한 원인 유전자로 알려져 있습니다.",
					"본 검사는 염기서열분석법으로 시행되었으며, promotor/deep intron 영역의 변이 또는 대규모의 결실/중복, 역위 등을 포함한 구조적 이상은 검출할 수 없습니다.",
					"본 검사에서 발견된 변이는 2015 ACMG/AMP guideline (Genet Med 2015;17:405-24)에 따라 \"pathogenic\", \"likely pathogenic\",\"uncertain significance\", \"likely benign\", \"benign\"의 다섯 가지 카테고리로 분류되며, Benign/Likely benign variant 결과는 보고하지 않습니다.",
					"관련 문헌과 데이터베이스의 추가적인 연구 결과에 따라 해당 변이의 분류가 변경될 수 있습니다."
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo S119 = TestInfo.builder()
			.code("S119")
			.referralDefault("Myopathy")
			.title("TTN gene plus 결과보고서")
			.name("TTN gene plus")
			.gene("TTN")
			.tier2(Arrays.stream("ACTA1,ACTN2,ADSS1,BIN1,CACNA1S,CCDC78,CFL2,COL12A1,COL6A1,COL6A2,COL6A3,DNM2,DOK7,ECEL1,EPG5,FKBP14,FXR1,KBTBD13,KLHL40,KLHL41,LMNA,LMOD3,MAP3K20,MEGF10,MICU1,MTM1,MYBPC1,MYH2,MYH3,MYH7,MYH8,MYL1,MYMK,MYO18B,MYPN,NEB,ORAI1,PAX7,PIEZO2,PYROXD1,RYR1,RYR3,SCN4A,SELENON,SLC25A4,SPEG,STAC3,STIM1,TNNI2,TNNT1,TNNT3,TPM2,TPM3,TRIP4,VMA21,ASCC3,CASQ1,CHRNE,CNTN1,DHX16,FLNC,GAA,GFER,HACD1,HNRNPA2B1,KY,LAMP2,MTMR14,MYBPC3,MYF5,MYL2,MYOD1,NEFL,PFKM,PPA2,PYGM,RAPSN,SLC25A42,SVIL,TNNC2,TRDN,UNC45B,COX6A2,ASCC1,COL13A1,COL25A1,DNAJB4,GBE1,LETM1,MLIP,SPTBN4,ZC4H2".split(",")).sorted().toArray(String[]::new))
			.target("TTN on Chromosome 2q31.2")
			.limitations(new String[] {
					"TTN 유전자의 변이는 유전성 근육병(Myopathy)의 원인 유전자 중 하나입니다. 근육병증이란 점진적인 근력 감소로 인해 사지 위약, 보행장애 등이 발생하는 질환이며, 종류에 따라 안면 근력 약화 혹은 심장 및 호흡 기능 약화를 동반하기도 합니다. 근육병은 그 원인이 다른 질환에 비해 다양하기 때문에 정확한 진단이 쉽지 않은 경우가 많아 유전성 근육병의 경우 정확한 진단을 위해서는 근육조직 검사 및 유전자 검사가 필수적입니다.",
					"TTN은 상염색체 우성 또는 열성 유전 양상을 보입니다.",
					"본 검사는 염기서열분석법으로 시행되었으며, Large deletion/duplication 검출은 제한적일 수 있습니다.",
					"본 검사에서 발견된 변이는 2015 ACMG/AMP Guidelines (Genet Med 2015;17:405-24)에 따라 'Pathogenic variant (PV)', 'Likely pathogenic variant (LPV)', 'Variant of uncertain significance (VUS)', 'Likely benign variant (LBV)', 'Benign variant (BV)'의 다섯 가지 카테고리로 분류되며, LBV와 BV는 보고하지 않습니다. 또한, 추가 연구 결과에 따라 해당 변이의 분류가 변경 될 수 있습니다."
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo S120 = TestInfo.builder()
			.code("S120")
			.referralDefault("Stroke")
			.title("COL4A2 gene plus 결과보고서")
			.name("COL4A2 gene plus")
			.gene("COL4A2")
			.tier2(Arrays.stream("ABCC6,ACTA2,ACVRL1,APP,ATP1A2,CACNA1A,CCM2,COL3A1,COL4A1,ENG,FOXC1,GLA,GUCY1A1,HTRA1,KRIT1,NOTCH3,PDCD10,RASA1,RNF213,SAMHD1,SLC2A10,SMAD4,TREX1,YY1AP1,ADA2,ANGPTL6,ATR,CBL,CBS,CEP152,CHD4,CNOT3,EPHB4,FLVCR2,GDF2,HBB,IRAG1,MYH11,NF1,PCNT,PKD1,PKD2,SETD5,SMAD9,THSD1,APOE".split(",")).sorted().toArray(String[]::new))
			.target("COL4A2 on Chromosome 13q34")
			.limitations(new String[] {
					"COL4A2 유전자의 변이는 유전성 뇌졸중(Stroke, STK)의 원인 유전자 중 하나입니다. 뇌졸중은 뇌 조직으로 공급되는 혈관이 갑자기 막혀 뇌 혈류가 차단돼 몸을 관장하는 뇌 기능이 손상되면서 나타나는 질환이며 일부 유전적 원인이 알려져 있습니다.",
					"COL4A2는 상염색체 우성 유전 양상을 보입니다.",
					"본 검사는 염기서열분석법으로 시행되었으며, Large deletion/duplication 검출은 제한적일 수 있습니다.",
					"본 검사에서 발견된 변이는 2015 ACMG/AMP Guidelines (Genet Med 2015;17:405-24)에 따라 'Pathogenic variant (PV)', 'Likely pathogenic variant (LPV)', 'Variant of uncertain significance (VUS)', 'Likely benign variant (LBV)', 'Benign variant (BV)'의 다섯 가지 카테고리로 분류되며, LBV와 BV는 보고하지 않습니다. 또한, 추가 연구 결과에 따라 해당 변이의 분류가 변경 될 수 있습니다."
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo N121 = TestInfo.builder()
			.code("N121")
			.referralDefault("Familial hypercholesterolemia")
			.title("LDLR gene mutation 결과보고서")
			.name("LDLR gene mutation")
			.gene("LDLR")
			.tier2(new String[] {"LDLRAP1", "APOB", "PCSK9"})
			.target("LDLR on Chromosome 19p13.2")
			.limitations(new String[] {
					"본 검사는 염기서열분석법으로 시행되었으며, promotor/deep intron 영역의 변이 또는 대규모의 결실/중복, 역위 등을 포함한 구조적 이상은 검출할 수 없습니다.",
					"본 검사에서 발견된 변이는 2015 ACMG/AMP guideline (Genet Med 2015;17:405-24)에 따라 \"pathogenic\", \"likely pathogenic\", \"uncertain significance\", \"likely benign\", \"benign\"의 다섯 가지 카테고리로 분류되며, Benign/Likely benign variant 결과는 보고하지 않습니다.",
					"관련 문헌과 데이터베이스의 추가적인 연구 결과에 따라 해당 변이의 분류가 변경될 수 있습니다."
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo[] TESTS_ETC = new TestInfo[] {
			S077, S078,
			S080, S081, S082, S083, S084, S085, S086, S087, S088, S089,
			S090, S091, S092, S093,
			S102, S103, S109, S110,
			S113, S119, S120,
			N121
	};
	public static final TestInfo[] TESTS_BRCA = new TestInfo[] {
			S096, S097
	};
}