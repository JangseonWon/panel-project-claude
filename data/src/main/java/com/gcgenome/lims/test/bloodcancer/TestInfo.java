package com.gcgenome.lims.test.bloodcancer;

import com.gcgenome.lims.test.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;

@Data
@Accessors(fluent = true)
@Builder
public class TestInfo implements HasCode, HasGenes, HasReferralDefault, MayBeNationalInsurance, I18N {
	private final String code;
	private final String name;
	private final String referralDefault;
	private final String region;
	private final String panel;
	@Builder.Default
	private final String method = "Hybridization with oligonucleotide probes (HEMA v.2504.1)";
	@Builder.Default
	private final String sequencing = "Sequencing by synthesis (Illumina)";
	@Builder.Default
	private final String pipeline = "BI_HEM.v.1.2 (Alignment: BWA, Variant calling: VarScan2_GATK)";
	@Builder.Default
	private final String reference = "GRCh37/hg19";
	private final Gene[] genesEssential;
	private final Gene[] genesSelective;
	@Builder.Default
	private final String i18n = "KOKR";
	@Builder.Default
	private final boolean isTissueTest = false;

	@Override
	public String[] genes() {
		LinkedList<String> genes = new LinkedList<>();
		if(genesEssential!=null) for(Gene gene: genesEssential) genes.add(gene.name);
		if(genesSelective!=null) for(Gene gene: genesSelective) genes.add(gene.name);
		return genes.toArray(new String[0]);
	}
	@Builder.Default
	private final boolean isNationalInsuranceTest = false;
	@Data
	@Accessors(fluent = true)
	public static class Gene {
		private final String name;
		private final String symbol;
		private final String exon;
		private final String reference;
		@Builder
		public Gene(String name, String symbol, String exon, String reference) {
			this.name = name;
			if(symbol == null) this.symbol = name;
			else this.symbol = symbol;
			this.exon = exon;
			this.reference = reference;
		}
		@Builder
		public Gene(String name, String exon, String reference) {
			this.name = name;
			this.symbol = name;
			this.exon = exon;
			this.reference = reference;
		}
	}
	public static final TestInfo N064 = TestInfo.builder()
			.code("N064")
			.name("급성골수성백혈병 유전자 패널검사")
			.referralDefault("AML")
			.region("58 genes (필수: 9 genes, 선택: 49 genes)")
			.panel("Acute Myeloid Leukemia (AML) Panel")
			.genesEssential(new Gene[] {
					Gene.builder().name("CEBPA").exon("All coding exons").reference("NM_004364").build(),
					Gene.builder().name("FLT3").exon("14-20").reference("NM_004119").build(),
					Gene.builder().name("IDH1").exon("4").reference("NM_005896").build(),
					Gene.builder().name("IDH2").exon("4").reference("NM_002168").build(),
					Gene.builder().name("JAK2").exon("12, 14").reference("NM_004972").build(),
					Gene.builder().name("KIT").exon("8-14, 17-18").reference("NM_000222").build(),
					Gene.builder().name("NPM1").exon("10-11").reference("NM_002520").build(),
					Gene.builder().name("RUNX1").exon("All coding exons").reference("NM_001754").build(),
					Gene.builder().name("TP53").exon("All coding exons").reference("NM_000546").build()
			}).genesSelective(new Gene[] {
					Gene.builder().name("ABL1").exon("Exon 4-8").reference("NM_005157").build(),
					Gene.builder().name("ANKRD26").exon("5'UTR").reference("NM_014915").build(),
					Gene.builder().name("ASXL1").exon("12").reference("NM_015338").build(),
					Gene.builder().name("ATRX").exon("8-10, 17-31").reference("NM_000489").build(),
					Gene.builder().name("BCOR").exon("All coding exons").reference("NM_001123385").build(),
					Gene.builder().name("BCORL1").exon("All coding exons").reference("NM_021946").build(),
					Gene.builder().name("BRAF").exon("15").reference("NM_004333").build(),
					Gene.builder().name("CALR").exon("All coding exons").reference("NM_004343").build(),
					Gene.builder().name("CBL").exon("8-9").reference("NM_005188").build(),
					Gene.builder().name("CBLB").exon("9-10").reference("NM_170662").build(),
					Gene.builder().name("CSF3R").exon("14, 17").reference("NM_000760").build(),
					Gene.builder().name("DDX41").exon("All coding exons").reference("NM_016222").build(),
					Gene.builder().name("DNMT3A").exon("All coding exons").reference("NM_022552").build(),
					Gene.builder().name("ETNK1").exon("H243, N244").reference("NM_018638").build(),
					Gene.builder().name("ETV6").exon("All coding exons").reference("NM_001987").build(),
					Gene.builder().name("EZH2").exon("All coding exons").reference("NM_004456").build(),
					Gene.builder().name("GATA1").exon("All coding exons").reference("NM_002049").build(),
					Gene.builder().name("GATA2").exon("All coding exons").reference("NM_032638").build(),
					Gene.builder().name("GNB1").exon("All coding exons").reference("NM_001282539").build(),
					Gene.builder().name("HRAS").exon("2-3").reference("NM_005343").build(),
					Gene.builder().name("IKZF1").exon("All coding exons").reference("NM_006060").build(),
					Gene.builder().name("JAK3").exon("13").reference("NM_000215").build(),
					Gene.builder().name("KDM6A").exon("All coding exons").reference("NM_021140").build(),
					Gene.builder().name("KRAS").exon("2-4").reference("NM_004985").build(),
					Gene.builder().name("MLL (KMT2A)").exon("All coding exons").reference("NM_001197104").build(),
					Gene.builder().name("MPL").exon("10").reference("NM_005373").build(),
					Gene.builder().name("NF1").exon("All coding exons").reference("NM_000267").build(),
					Gene.builder().name("NOTCH1").exon("26-28, 34").reference("NM_017617").build(),
					Gene.builder().name("NRAS").exon("2-4").reference("NM_002524").build(),
					Gene.builder().name("PDGFRA").exon("12, 14, 18").reference("NM_006206").build(),
					Gene.builder().name("PHF6").exon("All coding exons").reference("NM_001015877").build(),
					Gene.builder().name("PPM1D").exon("6").reference("NM_003620").build(),
					Gene.builder().name("PRPF8").exon("All coding exons").reference("NM_006445").build(),
					Gene.builder().name("PTPN11").exon("3-4, 12-13").reference("NM_002834").build(),
					Gene.builder().name("RAD21").exon("All coding exons").reference("NM_006265").build(),
					Gene.builder().name("SAMD9").exon("Exon 3").reference("NM_017654").build(),
					Gene.builder().name("SAMD9L").exon("Exon 5").reference("NM_152703").build(),
					Gene.builder().name("SETBP1").exon("4*").reference("NM_015559").build(),
					Gene.builder().name("SF3B1").exon("6-8, 12-17").reference("NM_012433").build(),
					Gene.builder().name("SMC1A").exon("2, 11, 16-17").reference("NM_006306").build(),
					Gene.builder().name("SMC3").exon("All coding exons").reference("NM_005445").build(),
					Gene.builder().name("SRSF2").exon("1").reference("NM_003016").build(),
					Gene.builder().name("STAG1").exon("All coding exons").reference("NM_005862").build(),
					Gene.builder().name("STAG2").exon("All coding exons").reference("NM_001042749").build(),
					Gene.builder().name("STAT3").exon("20-21").reference("NM_139276").build(),
					Gene.builder().name("TET2").exon("All coding exons").reference("NM_001127208").build(),
					Gene.builder().name("U2AF1").exon("2, 6").reference("NM_006758").build(),
					Gene.builder().name("WT1").exon("2-10").reference("NM_024426").build(),
					Gene.builder().name("ZRSR2").exon("All coding exons").reference("NM_005089").build()
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo N065 = TestInfo.builder()
			.code("N065")
			.name("골수형성이상, 골수증식종양 유전자 패널검사")
			.referralDefault("MDS/MPN")
			.region("58 genes (필수: 11 genes, 선택: 47 genes)")
			.panel("Myelodysplastic syndromes (MDS) Panel")
			.genesEssential(new Gene[] {
					Gene.builder().name("ASXL1").exon("12").reference("NM_015338").build(),
					Gene.builder().name("CALR").exon("All coding exons").reference("NM_004343").build(),
					Gene.builder().name("CSF3R").exon("14, 17").reference("NM_000760").build(),
					Gene.builder().name("DNMT3A").exon("All coding exons").reference("NM_022552").build(),
					Gene.builder().name("JAK2").exon("12, 14").reference("NM_004972").build(),
					Gene.builder().name("MPL").exon("10").reference("NM_005373").build(),
					Gene.builder().name("RUNX1").exon("All coding exons").reference("NM_001754").build(),
					Gene.builder().name("SETBP1").exon("4*").reference("NM_015559").build(),
					Gene.builder().name("SF3B1").exon("6-8, 12-17").reference("NM_012433").build(),
					Gene.builder().name("SRSF2").exon("1").reference("NM_003016").build(),
					Gene.builder().name("TET2").exon("All coding exons").reference("NM_001127208").build()
			}).genesSelective(new Gene[] {
					Gene.builder().name("ABL1").exon("Exon 4-8").reference("NM_005157").build(),
					Gene.builder().name("ANKRD26").exon("5'UTR").reference("NM_014915").build(),
					Gene.builder().name("ATRX").exon("8-10, 17-31").reference("NM_000489").build(),
					Gene.builder().name("BCOR").exon("All coding exons").reference("NM_001123385").build(),
					Gene.builder().name("BCORL1").exon("All coding exons").reference("NM_021946").build(),
					Gene.builder().name("BRAF").exon("15").reference("NM_004333").build(),
					Gene.builder().name("CBL").exon("8-9").reference("NM_005188").build(),
					Gene.builder().name("CBLB").exon("9-10").reference("NM_170662").build(),
					Gene.builder().name("CEBPA").exon("All coding exons").reference("NM_004364").build(),
					Gene.builder().name("DDX41").exon("All coding exons").reference("NM_016222").build(),
					Gene.builder().name("ETNK1").exon("H243, N244").reference("NM_018638").build(),
					Gene.builder().name("ETV6").exon("All coding exons").reference("NM_001987").build(),
					Gene.builder().name("EZH2").exon("All coding exons").reference("NM_004456").build(),
					Gene.builder().name("FLT3").exon("14-20").reference("NM_004119").build(),
					Gene.builder().name("GATA1").exon("All coding exons").reference("NM_002049").build(),
					Gene.builder().name("GATA2").exon("All coding exons").reference("NM_032638").build(),
					Gene.builder().name("GNB1").exon("All coding exons").reference("NM_001282539").build(),
					Gene.builder().name("HRAS").exon("2-3").reference("NM_005343").build(),
					Gene.builder().name("IDH1").exon("4").reference("NM_005896").build(),
					Gene.builder().name("IDH2").exon("4").reference("NM_002168").build(),
					Gene.builder().name("IKZF1").exon("All coding exons").reference("NM_006060").build(),
					Gene.builder().name("JAK3").exon("13").reference("NM_000215").build(),
					Gene.builder().name("KDM6A").exon("All coding exons").reference("NM_021140").build(),
					Gene.builder().name("KIT").exon("8-14, 17-18").reference("NM_000222").build(),
					Gene.builder().name("KRAS").exon("2-4").reference("NM_004985").build(),
					Gene.builder().name("MLL (KMT2A)").exon("All coding exons").reference("NM_001197104").build(),
					Gene.builder().name("NF1").exon("All coding exons").reference("NM_000267").build(),
					Gene.builder().name("NOTCH1").exon("26-28, 34").reference("NM_017617").build(),
					Gene.builder().name("NPM1").exon("10-11").reference("NM_002520").build(),
					Gene.builder().name("NRAS").exon("2-4").reference("NM_002524").build(),
					Gene.builder().name("PDGFRA").exon("12, 14, 18").reference("NM_006206").build(),
					Gene.builder().name("PHF6").exon("All coding exons").reference("NM_001015877").build(),
					Gene.builder().name("PPM1D").exon("6").reference("NM_003620").build(),
					Gene.builder().name("PRPF8").exon("All coding exons").reference("NM_006445").build(),
					Gene.builder().name("PTPN11").exon("3-4, 12-13").reference("NM_002834").build(),
					Gene.builder().name("RAD21").exon("All coding exons").reference("NM_006265").build(),
					Gene.builder().name("SAMD9").exon("Exon 3").reference("NM_017654").build(),
					Gene.builder().name("SAMD9L").exon("Exon 5").reference("NM_152703").build(),
					Gene.builder().name("SMC1A").exon("2, 11, 16-17").reference("NM_006306").build(),
					Gene.builder().name("SMC3").exon("All coding exons").reference("NM_005445").build(),
					Gene.builder().name("STAG1").exon("All coding exons").reference("NM_005862").build(),
					Gene.builder().name("STAG2").exon("All coding exons").reference("NM_001042749").build(),
					Gene.builder().name("STAT3").exon("20-21").reference("NM_139276").build(),
					Gene.builder().name("TP53").exon("All coding exons").reference("NM_000546").build(),
					Gene.builder().name("U2AF1").exon("2, 6").reference("NM_006758").build(),
					Gene.builder().name("WT1").exon("2-10").reference("NM_024426").build(),
					Gene.builder().name("ZRSR2").exon("All coding exons").reference("NM_005089").build()
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo N082 = TestInfo.builder()
			.code("N082")
			.name("급성림프구성 백혈병 유전자 패널검사")
			.referralDefault("ALL")
			.region("54 genes (필수: 5 genes, 선택: 49 genes)")
			.panel("Acute Lymphoblastic Leukemia (ALL) Panel")
			.genesEssential(new Gene[] {
					Gene.builder().name("IKZF1").exon("All coding exons").reference("NM_006060").build(),
					Gene.builder().name("JAK2").exon("16, 20-21, 24").reference("NM_004972").build(),
					Gene.builder().name("NRAS").exon("2-3").reference("NM_002524").build(),
					Gene.builder().name("RB1").exon("4, 9, 13, 20").reference("NM_000321").build(),
					Gene.builder().name("TP53").exon("All coding exons").reference("NM_000546").build()
			}).genesSelective(new Gene[] {
					Gene.builder().name("ABL1").exon("4-10").reference("NM_005157").build(),
					Gene.builder().name("BRAF").exon("11,15").reference("NM_004333").build(),
					Gene.builder().name("BTG1").exon("2").reference("NM_001731").build(),
					Gene.builder().name("CDKN2A").exon("All coding exons").reference("NM_000077").build(),
					Gene.builder().name("CDKN2B").exon("All coding exons").reference("NM_004936").build(),
					Gene.builder().name("CREBBP").exon("4, 6, 14, 17-19,\n21, 26-28, 31").reference("NM_004380").build(),
					Gene.builder().name("DNM2").exon("All coding exons").reference("NM_001005360").build(),
					Gene.builder().name("DNMT3A").exon("All coding exons").reference("NM_022552").build(),
					Gene.builder().name("EED").exon("All coding exons").reference("NM_003797").build(),
					Gene.builder().name("EP300").exon("30").reference("NM_001429").build(),
					Gene.builder().name("ERG").exon("All coding exons").reference("NM_182918").build(),
					Gene.builder().name("ETV6").exon("All coding exons").reference("NM_001987").build(),
					Gene.builder().name("EZH2").exon("All coding exons").reference("NM_004456").build(),
					Gene.builder().name("FBXW7").exon("All coding exons").reference("NM_033632").build(),
					Gene.builder().name("FLT3").exon("5, 9, 13-16, 19-21, plus exons 13-15 (FLT3-ITD)").reference("NM_004119").build(),
					Gene.builder().name("GATA3").exon("4-6").reference("NM_002051").build(),
					Gene.builder().name("IDH1").exon("4, 7").reference("NM_005896").build(),
					Gene.builder().name("IDH2").exon("All coding exons").reference("NM_002168").build(),
					Gene.builder().name("IL7R").exon("3, 5-6").reference("NM_002185").build(),
					Gene.builder().name("JAK1").exon("10, 13, 14-23").reference("NM_002227").build(),
					Gene.builder().name("JAK3").exon("2, 4, 10-13, 18-19").reference("NM_000215").build(),
					Gene.builder().name("KDM6A").exon("15, 25").reference("NM_021140").build(),
					Gene.builder().name("KMT2A").exon("All coding exons").reference("NM_001197104").build(),
					Gene.builder().name("KMT2D").exon("All coding exons").reference("NM_003482").build(),
					Gene.builder().name("KRAS").exon("2-4").reference("NM_004985").build(),
					Gene.builder().name("LEF1").exon("3-4").reference("NM_016269").build(),
					Gene.builder().name("LMO1").exon("All coding exons").reference("NM_002315").build(),
					Gene.builder().name("MAPK1").exon("4").reference("NM_002745").build(),
					Gene.builder().name("NF1").exon("9-10, 12, 18-19, 21, 23, 25, 28-29, 31, 33-34, 36-38, 41, 44, 49, 52").reference("NM_000267").build(),
					Gene.builder().name("NOTCH1").exon("All coding exons").reference("NM_017617").build(),
					Gene.builder().name("NSD2").exon("18").reference("NM_001042424").build(),
					Gene.builder().name("NT5C2").exon("All coding exons").reference("NM_001134373").build(),
					Gene.builder().name("NUDT15").exon("1-3").reference("NM_018283").build(),
					Gene.builder().name("PAX5").exon("2-5, 7, 9").reference("NM_016734").build(),
					Gene.builder().name("PDGFRB").exon("12, 14, 18").reference("NM_002609").build(),
					Gene.builder().name("PHF6").exon("All coding exons").reference("NM_001015877").build(),
					Gene.builder().name("PTEN").exon("2, 5, 7").reference("NM_000314").build(),
					Gene.builder().name("PTPN11").exon("3, 8, 13").reference("NM_002834").build(),
					Gene.builder().name("RUNX1").exon("All coding exons").reference("NM_001754").build(),
					Gene.builder().name("SETD2").exon("All coding exons").reference("NM_014159").build(),
					Gene.builder().name("SH2B3").exon("All coding exons").reference("NM_005475").build(),
					Gene.builder().name("STAG2").exon("All coding exons").reference("NM_001042749").build(),
					Gene.builder().name("STAT3").exon("All coding exons").reference("NM_139276").build(),
					Gene.builder().name("STAT5B").exon("All coding exons").reference("NM_012448").build(),
					Gene.builder().name("SUZ12").exon("All coding exons").reference("NM_015355").build(),
					Gene.builder().name("TBL1XR1").exon("5-7, 9").reference("NM_024665").build(),
					Gene.builder().name("TCF3").exon("All coding exons").reference("NM_001136139").build(),
					Gene.builder().name("TPMT").exon("All coding exons").reference("NM_000367").build(),
					Gene.builder().name("WT1").exon("All coding exons").reference("NM_024426").build()
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo N083 = TestInfo.builder()
			.code("N083")
			.name("악성림프종 유전자 패널검사")
			.referralDefault("Lymphoma")
			.region("66 genes (필수: 3 genes, 선택: 63 genes)")
			.panel("Lymphoma Panel")
			.genesEssential(new Gene[] {
					Gene.builder().name("BRAF").exon("3, 6-8, 11-15, 17-18").reference("NM_004333").build(),
					Gene.builder().name("MYD88").exon("All coding exons").reference("NM_002468").build(),
					Gene.builder().name("TP53").exon("All coding exons").reference("NM_000546").build()
			}).genesSelective(new Gene[] {
					Gene.builder().name("ALK").exon("All coding exons").reference("NM_004304").build(),
					Gene.builder().name("ATM").exon("All coding exons").reference("NM_000051").build(),
					Gene.builder().name("B2M").exon("All coding exons").reference("NM_004048").build(),
					Gene.builder().name("BCL10").exon("All coding exons").reference("NM_003921").build(),
					Gene.builder().name("BCL2").exon("2").reference("NM_000633").build(),
					Gene.builder().name("BCL6").exon("All coding exons").reference("NM_001706").build(),
					Gene.builder().name("BIRC3").exon("All coding exons").reference("NM_182962").build(),
					Gene.builder().name("BTG2").exon("All coding exons").reference("NM_006763").build(),
					Gene.builder().name("BTK").exon("14-15").reference("NM_000061").build(),
					Gene.builder().name("CARD11").exon("3-9, 15").reference("NM_032415").build(),
					Gene.builder().name("CCND3").exon("5").reference("NM_001760").build(),
					Gene.builder().name("CD79A").exon("2-5").reference("NM_001783").build(),
					Gene.builder().name("CD79B").exon("2-5").reference("NM_001039933").build(),
					Gene.builder().name("CD83").exon("All coding exons").reference("NM_004233").build(),
					Gene.builder().name("CDKN2A").exon("All coding exons").reference("NM_000077").build(),
					Gene.builder().name("CREBBP").exon("All coding exons").reference("NM_004380").build(),
					Gene.builder().name("CXCR4").exon("All coding exons").reference("NM_003467").build(),
					Gene.builder().name("DDX3X").exon("All coding exons").reference("NM_001356").build(),
					Gene.builder().name("EGR2").exon("2").reference("NM_001136177").build(),
					Gene.builder().name("EP300").exon("All coding exons").reference("NM_001429").build(),
					Gene.builder().name("ETV6").exon("5-8").reference("NM_001987").build(),
					Gene.builder().name("EZH2").exon("12, 16, 18").reference("NM_004456").build(),
					Gene.builder().name("FAS").exon("All coding exons").reference("NM_000043").build(),
					Gene.builder().name("FAT4").exon("All coding exons").reference("NM_024582").build(),
					Gene.builder().name("FBXO11").exon("All coding exons").reference("NM_001190274").build(),
					Gene.builder().name("H1-4").exon("All coding exons").reference("NM_005321").build(),
					Gene.builder().name("ID3").exon("All coding exons").reference("NM_002167").build(),
					Gene.builder().name("IDH2").exon("4").reference("NM_002168").build(),
					Gene.builder().name("IKBKB").exon("All coding exons").reference("NM_001556").build(),
					Gene.builder().name("IKZF1").exon("All coding exons").reference("NM_006060").build(),
					Gene.builder().name("JAK3").exon("All coding exons").reference("NM_000215").build(),
					Gene.builder().name("KLF2").exon("All coding exons").reference("NM_016270").build(),
					Gene.builder().name("KLHL6").exon("All coding exons").reference("NM_130446").build(),
					Gene.builder().name("KMT2D").exon("All coding exons").reference("NM_003482").build(),
					Gene.builder().name("MEF2B").exon("2, 3").reference("NM_001145785").build(),
					Gene.builder().name("MYC").exon("All coding exons").reference("NM_002467").build(),
					Gene.builder().name("NFKBIA").exon("All coding exons").reference("NM_020529").build(),
					Gene.builder().name("NFKBIE").exon("1").reference("NM_004556").build(),
					Gene.builder().name("NOTCH1").exon("26-28, 34").reference("NM_017617").build(),
					Gene.builder().name("NOTCH2").exon("26-27, 34").reference("NM_024408").build(),
					Gene.builder().name("PIM1").exon("1-4").reference("NM_002648").build(),
					Gene.builder().name("PLCG1").exon("11, 15, 18-19").reference("NM_182811").build(),
					Gene.builder().name("PLCG2").exon("19-20, 24").reference("NM_002661").build(),
					Gene.builder().name("POT1").exon("All coding exons").reference("NM_015450").build(),
					Gene.builder().name("PRDM1").exon("All coding exons").reference("NM_001198").build(),
					Gene.builder().name("RHOA").exon("All coding exons").reference("NM_001664").build(),
					Gene.builder().name("RPS15").exon("4").reference("NM_001018").build(),
					Gene.builder().name("RRAGC").exon("1-2").reference("NM_022157").build(),
					Gene.builder().name("SF3B1").exon("14-19").reference("NM_012433").build(),
					Gene.builder().name("SGK1").exon("All coding exons").reference("NM_005627").build(),
					Gene.builder().name("SOCS1").exon("All coding exons").reference("NM_003745").build(),
					Gene.builder().name("SPEN").exon("All coding exons").reference("NM_015001").build(),
					Gene.builder().name("STAT3").exon("7, 13-14, 19-22").reference("NM_139276").build(),
					Gene.builder().name("STAT5B").exon("14-18").reference("NM_012448").build(),
					Gene.builder().name("TBL1XR1").exon("All coding exons").reference("NM_024665").build(),
					Gene.builder().name("TCF3").exon("All coding exons").reference("NM_001136139").build(),
					Gene.builder().name("TET2").exon("All coding exons").reference("NM_001127208").build(),
					Gene.builder().name("TNFAIP3").exon("All coding exons").reference("NM_006290").build(),
					Gene.builder().name("TNFRSF14").exon("All coding exons").reference("NM_003820").build(),
					Gene.builder().name("TP63").exon("All coding exons").reference("NM_003722").build(),
					Gene.builder().name("TRAF3").exon("All coding exons").reference("NM_003300").build(),
					Gene.builder().name("UBR5").exon("All coding exons").reference("NM_015902").build(),
					Gene.builder().name("XPO1").exon("15-16, 18").reference("NM_003400").build()
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo N093 = TestInfo.builder()
			.code("N093")
			.isTissueTest(true)
			.name("악성림프종 유전자 패널검사(Tissue)")
			.referralDefault("Lymphoma")
			.region("66 genes (필수: 3 genes, 선택: 63 genes)")
			.panel("Lymphoma Panel")
			.genesEssential(new Gene[] {
					Gene.builder().name("BRAF").exon("3, 6-8, 11-15, 17-18").reference("NM_004333").build(),
					Gene.builder().name("MYD88").exon("All coding exons").reference("NM_002468").build(),
					Gene.builder().name("TP53").exon("All coding exons").reference("NM_000546").build()
			}).genesSelective(new Gene[] {
					Gene.builder().name("ALK").exon("All coding exons").reference("NM_004304").build(),
					Gene.builder().name("ATM").exon("All coding exons").reference("NM_000051").build(),
					Gene.builder().name("B2M").exon("All coding exons").reference("NM_004048").build(),
					Gene.builder().name("BCL10").exon("All coding exons").reference("NM_003921").build(),
					Gene.builder().name("BCL2").exon("2").reference("NM_000633").build(),
					Gene.builder().name("BCL6").exon("All coding exons").reference("NM_001706").build(),
					Gene.builder().name("BIRC3").exon("All coding exons").reference("NM_182962").build(),
					Gene.builder().name("BTG2").exon("All coding exons").reference("NM_006763").build(),
					Gene.builder().name("BTK").exon("14-15").reference("NM_000061").build(),
					Gene.builder().name("CARD11").exon("3-9, 15").reference("NM_032415").build(),
					Gene.builder().name("CCND3").exon("5").reference("NM_001760").build(),
					Gene.builder().name("CD79A").exon("2-5").reference("NM_001783").build(),
					Gene.builder().name("CD79B").exon("2-5").reference("NM_001039933").build(),
					Gene.builder().name("CD83").exon("All coding exons").reference("NM_004233").build(),
					Gene.builder().name("CDKN2A").exon("All coding exons").reference("NM_000077").build(),
					Gene.builder().name("CREBBP").exon("All coding exons").reference("NM_004380").build(),
					Gene.builder().name("CXCR4").exon("All coding exons").reference("NM_003467").build(),
					Gene.builder().name("DDX3X").exon("All coding exons").reference("NM_001356").build(),
					Gene.builder().name("EGR2").exon("2").reference("NM_001136177").build(),
					Gene.builder().name("EP300").exon("All coding exons").reference("NM_001429").build(),
					Gene.builder().name("ETV6").exon("5-8").reference("NM_001987").build(),
					Gene.builder().name("EZH2").exon("12, 16, 18").reference("NM_004456").build(),
					Gene.builder().name("FAS").exon("All coding exons").reference("NM_000043").build(),
					Gene.builder().name("FAT4").exon("All coding exons").reference("NM_024582").build(),
					Gene.builder().name("FBXO11").exon("All coding exons").reference("NM_001190274").build(),
					Gene.builder().name("H1-4").exon("All coding exons").reference("NM_005321").build(),
					Gene.builder().name("ID3").exon("All coding exons").reference("NM_002167").build(),
					Gene.builder().name("IDH2").exon("4").reference("NM_002168").build(),
					Gene.builder().name("IKBKB").exon("All coding exons").reference("NM_001556").build(),
					Gene.builder().name("IKZF1").exon("All coding exons").reference("NM_006060").build(),
					Gene.builder().name("JAK3").exon("All coding exons").reference("NM_000215").build(),
					Gene.builder().name("KLF2").exon("All coding exons").reference("NM_016270").build(),
					Gene.builder().name("KLHL6").exon("All coding exons").reference("NM_130446").build(),
					Gene.builder().name("KMT2D").exon("All coding exons").reference("NM_003482").build(),
					Gene.builder().name("MEF2B").exon("2, 3").reference("NM_001145785").build(),
					Gene.builder().name("MYC").exon("All coding exons").reference("NM_002467").build(),
					Gene.builder().name("NFKBIA").exon("All coding exons").reference("NM_020529").build(),
					Gene.builder().name("NFKBIE").exon("1").reference("NM_004556").build(),
					Gene.builder().name("NOTCH1").exon("26-28, 34").reference("NM_017617").build(),
					Gene.builder().name("NOTCH2").exon("26-27, 34").reference("NM_024408").build(),
					Gene.builder().name("PIM1").exon("1-4").reference("NM_002648").build(),
					Gene.builder().name("PLCG1").exon("11, 15, 18-19").reference("NM_182811").build(),
					Gene.builder().name("PLCG2").exon("19-20, 24").reference("NM_002661").build(),
					Gene.builder().name("POT1").exon("All coding exons").reference("NM_015450").build(),
					Gene.builder().name("PRDM1").exon("All coding exons").reference("NM_001198").build(),
					Gene.builder().name("RHOA").exon("All coding exons").reference("NM_001664").build(),
					Gene.builder().name("RPS15").exon("4").reference("NM_001018").build(),
					Gene.builder().name("RRAGC").exon("1-2").reference("NM_022157").build(),
					Gene.builder().name("SF3B1").exon("14-19").reference("NM_012433").build(),
					Gene.builder().name("SGK1").exon("All coding exons").reference("NM_005627").build(),
					Gene.builder().name("SOCS1").exon("All coding exons").reference("NM_003745").build(),
					Gene.builder().name("SPEN").exon("All coding exons").reference("NM_015001").build(),
					Gene.builder().name("STAT3").exon("7, 13-14, 19-22").reference("NM_139276").build(),
					Gene.builder().name("STAT5B").exon("14-18").reference("NM_012448").build(),
					Gene.builder().name("TBL1XR1").exon("All coding exons").reference("NM_024665").build(),
					Gene.builder().name("TCF3").exon("All coding exons").reference("NM_001136139").build(),
					Gene.builder().name("TET2").exon("All coding exons").reference("NM_001127208").build(),
					Gene.builder().name("TNFAIP3").exon("All coding exons").reference("NM_006290").build(),
					Gene.builder().name("TNFRSF14").exon("All coding exons").reference("NM_003820").build(),
					Gene.builder().name("TP63").exon("All coding exons").reference("NM_003722").build(),
					Gene.builder().name("TRAF3").exon("All coding exons").reference("NM_003300").build(),
					Gene.builder().name("UBR5").exon("All coding exons").reference("NM_015902").build(),
					Gene.builder().name("XPO1").exon("15-16, 18").reference("NM_003400").build()
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo N094 = TestInfo.builder()
			.code("N094")
			.isTissueTest(true)
			.name("급성림프구성 백혈병 유전자 패널검사(Tissue)")
			.referralDefault("ALL")
			.region("54 genes (필수: 5 genes, 선택: 49 genes)")
			.panel("Acute Lymphoblastic Leukemia (ALL) Panel")
			.genesEssential(new Gene[] {
					Gene.builder().name("IKZF1").exon("All coding exons").reference("NM_006060").build(),
					Gene.builder().name("JAK2").exon("16, 20-21, 24").reference("NM_004972").build(),
					Gene.builder().name("NRAS").exon("2-3").reference("NM_002524").build(),
					Gene.builder().name("RB1").exon("4, 9, 13, 20").reference("NM_000321").build(),
					Gene.builder().name("TP53").exon("All coding exons").reference("NM_000546").build()
			}).genesSelective(new Gene[] {
					Gene.builder().name("ABL1").exon("4-10").reference("NM_005157").build(),
					Gene.builder().name("BRAF").exon("11,15").reference("NM_004333").build(),
					Gene.builder().name("BTG1").exon("2").reference("NM_001731").build(),
					Gene.builder().name("CDKN2A").exon("All coding exons").reference("NM_000077").build(),
					Gene.builder().name("CDKN2B").exon("All coding exons").reference("NM_004936").build(),
					Gene.builder().name("CREBBP").exon("4, 6, 14, 17-19,\n21, 26-28, 31").reference("NM_004380").build(),
					Gene.builder().name("DNM2").exon("All coding exons").reference("NM_001005360").build(),
					Gene.builder().name("DNMT3A").exon("All coding exons").reference("NM_022552").build(),
					Gene.builder().name("EED").exon("All coding exons").reference("NM_003797").build(),
					Gene.builder().name("EP300").exon("30").reference("NM_001429").build(),
					Gene.builder().name("ERG").exon("All coding exons").reference("NM_182918").build(),
					Gene.builder().name("ETV6").exon("All coding exons").reference("NM_001987").build(),
					Gene.builder().name("EZH2").exon("All coding exons").reference("NM_004456").build(),
					Gene.builder().name("FBXW7").exon("All coding exons").reference("NM_033632").build(),
					Gene.builder().name("FLT3").exon("5, 9, 13-16, 19-21, plus exons 13-15 (FLT3-ITD)").reference("NM_004119").build(),
					Gene.builder().name("GATA3").exon("4-6").reference("NM_002051").build(),
					Gene.builder().name("IDH1").exon("4, 7").reference("NM_005896").build(),
					Gene.builder().name("IDH2").exon("All coding exons").reference("NM_002168").build(),
					Gene.builder().name("IL7R").exon("3, 5-6").reference("NM_002185").build(),
					Gene.builder().name("JAK1").exon("10, 13, 14-23").reference("NM_002227").build(),
					Gene.builder().name("JAK3").exon("2, 4, 10-13, 18-19").reference("NM_000215").build(),
					Gene.builder().name("KDM6A").exon("15, 25").reference("NM_021140").build(),
					Gene.builder().name("KMT2A").exon("All coding exons").reference("NM_001197104").build(),
					Gene.builder().name("KMT2D").exon("All coding exons").reference("NM_003482").build(),
					Gene.builder().name("KRAS").exon("2-4").reference("NM_004985").build(),
					Gene.builder().name("LEF1").exon("3-4").reference("NM_016269").build(),
					Gene.builder().name("LMO1").exon("All coding exons").reference("NM_002315").build(),
					Gene.builder().name("MAPK1").exon("4").reference("NM_002745").build(),
					Gene.builder().name("NF1").exon("9-10, 12, 18-19, 21, 23, 25, 28-29, 31, 33-34, 36-38, 41, 44, 49, 52").reference("NM_000267").build(),
					Gene.builder().name("NOTCH1").exon("All coding exons").reference("NM_017617").build(),
					Gene.builder().name("NSD2").exon("18").reference("NM_001042424").build(),
					Gene.builder().name("NT5C2").exon("All coding exons").reference("NM_001134373").build(),
					Gene.builder().name("NUDT15").exon("1-3").reference("NM_018283").build(),
					Gene.builder().name("PAX5").exon("2-5, 7, 9").reference("NM_016734").build(),
					Gene.builder().name("PDGFRB").exon("12, 14, 18").reference("NM_002609").build(),
					Gene.builder().name("PHF6").exon("All coding exons").reference("NM_001015877").build(),
					Gene.builder().name("PTEN").exon("2, 5, 7").reference("NM_000314").build(),
					Gene.builder().name("PTPN11").exon("3, 8, 13").reference("NM_002834").build(),
					Gene.builder().name("RUNX1").exon("All coding exons").reference("NM_001754").build(),
					Gene.builder().name("SETD2").exon("All coding exons").reference("NM_014159").build(),
					Gene.builder().name("SH2B3").exon("All coding exons").reference("NM_005475").build(),
					Gene.builder().name("STAG2").exon("All coding exons").reference("NM_001042749").build(),
					Gene.builder().name("STAT3").exon("All coding exons").reference("NM_139276").build(),
					Gene.builder().name("STAT5B").exon("All coding exons").reference("NM_012448").build(),
					Gene.builder().name("SUZ12").exon("All coding exons").reference("NM_015355").build(),
					Gene.builder().name("TBL1XR1").exon("5-7, 9").reference("NM_024665").build(),
					Gene.builder().name("TCF3").exon("All coding exons").reference("NM_001136139").build(),
					Gene.builder().name("TPMT").exon("All coding exons").reference("NM_000367").build(),
					Gene.builder().name("WT1").exon("All coding exons").reference("NM_024426").build()
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo N104 = TestInfo.builder()
			.code("N104")
			.name("다발성 골수종 유전자 패널검사")
			.referralDefault("Multiple Myeloma")
			.region("34 genes (필수: 3 genes, 선택: 31 genes)")
			.panel("Multiple Myeloma(MM) Panel")
			.genesEssential(new Gene[] {
					Gene.builder().name("KRAS").exon("All coding exons").reference("NM_004985").build(),
					Gene.builder().name("NRAS").exon("2-3").reference("NM_002524").build(),
					Gene.builder().name("TP53").exon("All coding exons").reference("NM_000546").build()
			}).genesSelective(new Gene[] {
					Gene.builder().name("ATM").exon("All coding exons").reference("NM_000051").build(),
					Gene.builder().name("ATR").exon("All coding exons").reference("NM_001184").build(),
					Gene.builder().name("BRAF").exon("All coding exons").reference("NM_004333").build(),
					Gene.builder().name("CARD11").exon("5-9").reference("NM_032415").build(),
					Gene.builder().name("CCND1").exon("1, 5").reference("NM_053056").build(),
					Gene.builder().name("CDK4").exon("2").reference("NM_000075").build(),
					Gene.builder().name("CDKN1B").exon("All coding exons").reference("NM_004064").build(),
					Gene.builder().name("CRBN").exon("All coding exons").reference("NM_016302").build(),
					Gene.builder().name("CUL4A").exon("All coding exons").reference("NM_001008895").build(),
					Gene.builder().name("CUL4B").exon("All coding exons").reference("NM_003588").build(),
					Gene.builder().name("CXCR4").exon("All coding exons").reference("NM_003467").build(),
					Gene.builder().name("CYLD").exon("All coding exons").reference("NM_001042355").build(),
					Gene.builder().name("DIS3").exon("All coding exons").reference("NM_014953").build(),
					Gene.builder().name("EGR1").exon("All coding exons").reference("NM_001964").build(),
					Gene.builder().name("FGFR3").exon("All coding exons").reference("NM_000142").build(),
					Gene.builder().name("IDH1").exon("4").reference("NM_005896").build(),
					Gene.builder().name("IDH2").exon("4").reference("NM_002168").build(),
					Gene.builder().name("IKZF1").exon("All coding exons").reference("NM_006060").build(),
					Gene.builder().name("IRF4").exon("All coding exons").reference("NM_002460").build(),
					Gene.builder().name("MAX").exon("All coding exons").reference("NM_002382").build(),
					Gene.builder().name("MYD88").exon("All coding exons").reference("NM_002468").build(),
					Gene.builder().name("NFKBIA").exon("All coding exons").reference("NM_020529").build(),
					Gene.builder().name("NR3C1").exon("All coding exons").reference("NM_001024094").build(),
					Gene.builder().name("PSMB5").exon("All coding exons").reference("NM_002797").build(),
					Gene.builder().name("PSMD1").exon("All coding exons").reference("NM_002807").build(),
					Gene.builder().name("PSMG2").exon("All coding exons").reference("NM_020232").build(),
					Gene.builder().name("RB1").exon("All coding exons").reference("NM_000321").build(),
					Gene.builder().name("TENT5C").exon("All coding exons").reference("NM_017709").build(),
					Gene.builder().name("TRAF2").exon("All coding exons").reference("NM_021138").build(),
					Gene.builder().name("TRAF3").exon("All coding exons").reference("NM_003300").build(),
					Gene.builder().name("XBP1").exon("All coding exons").reference("NM_005080").build()
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo N105 = TestInfo.builder()
			.code("N105")
			.isTissueTest(true)
			.name("다발성 골수종 유전자 패널검사(Tissue)")
			.referralDefault("Multiple Myeloma")
			.region("34 genes (필수: 3 genes, 선택: 31 genes)")
			.panel("Multiple Myeloma(MM) Panel")
			.genesEssential(new Gene[] {
					Gene.builder().name("KRAS").exon("All coding exons").reference("NM_004985").build(),
					Gene.builder().name("NRAS").exon("2-3").reference("NM_002524").build(),
					Gene.builder().name("TP53").exon("All coding exons").reference("NM_000546").build()
			}).genesSelective(new Gene[] {
					Gene.builder().name("ATM").exon("All coding exons").reference("NM_000051").build(),
					Gene.builder().name("ATR").exon("All coding exons").reference("NM_001184").build(),
					Gene.builder().name("BRAF").exon("All coding exons").reference("NM_004333").build(),
					Gene.builder().name("CARD11").exon("5-9").reference("NM_032415").build(),
					Gene.builder().name("CCND1").exon("1, 5").reference("NM_053056").build(),
					Gene.builder().name("CDK4").exon("2").reference("NM_000075").build(),
					Gene.builder().name("CDKN1B").exon("All coding exons").reference("NM_004064").build(),
					Gene.builder().name("CRBN").exon("All coding exons").reference("NM_016302").build(),
					Gene.builder().name("CUL4A").exon("All coding exons").reference("NM_001008895").build(),
					Gene.builder().name("CUL4B").exon("All coding exons").reference("NM_003588").build(),
					Gene.builder().name("CXCR4").exon("All coding exons").reference("NM_003467").build(),
					Gene.builder().name("CYLD").exon("All coding exons").reference("NM_001042355").build(),
					Gene.builder().name("DIS3").exon("All coding exons").reference("NM_014953").build(),
					Gene.builder().name("EGR1").exon("All coding exons").reference("NM_001964").build(),
					Gene.builder().name("FGFR3").exon("All coding exons").reference("NM_000142").build(),
					Gene.builder().name("IDH1").exon("4").reference("NM_005896").build(),
					Gene.builder().name("IDH2").exon("4").reference("NM_002168").build(),
					Gene.builder().name("IKZF1").exon("All coding exons").reference("NM_006060").build(),
					Gene.builder().name("IRF4").exon("All coding exons").reference("NM_002460").build(),
					Gene.builder().name("MAX").exon("All coding exons").reference("NM_002382").build(),
					Gene.builder().name("MYD88").exon("All coding exons").reference("NM_002468").build(),
					Gene.builder().name("NFKBIA").exon("All coding exons").reference("NM_020529").build(),
					Gene.builder().name("NR3C1").exon("All coding exons").reference("NM_001024094").build(),
					Gene.builder().name("PSMB5").exon("All coding exons").reference("NM_002797").build(),
					Gene.builder().name("PSMD1").exon("All coding exons").reference("NM_002807").build(),
					Gene.builder().name("PSMG2").exon("All coding exons").reference("NM_020232").build(),
					Gene.builder().name("RB1").exon("All coding exons").reference("NM_000321").build(),
					Gene.builder().name("TENT5C").exon("All coding exons").reference("NM_017709").build(),
					Gene.builder().name("TRAF2").exon("All coding exons").reference("NM_021138").build(),
					Gene.builder().name("TRAF3").exon("All coding exons").reference("NM_003300").build(),
					Gene.builder().name("XBP1").exon("All coding exons").reference("NM_005080").build()
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo N157 = TestInfo.builder()
			.code("N157")
			.name("악성림프종 유전자 패널검사 / 전용")
			.referralDefault("Lymphoma")
			.region("50 genes (필수: 3 genes, 선택: 47 genes)")
			.panel("Lymphoma Panel")
			.genesEssential(new Gene[] {
					Gene.builder().name("BRAF").exon("3, 6-8, 11-15, 17-18").reference("NM_004333").build(),
					Gene.builder().name("MYD88").exon("All coding exons").reference("NM_002468").build(),
					Gene.builder().name("TP53").exon("All coding exons").reference("NM_000546").build()
			}).genesSelective(new Gene[] {
					Gene.builder().name("ALK").exon("All coding exons").reference("NM_004304").build(),
					Gene.builder().name("ATM").exon("All coding exons").reference("NM_000051").build(),
					Gene.builder().name("B2M").exon("All coding exons").reference("NM_004048").build(),
					Gene.builder().name("BCL6").exon("All coding exons").reference("NM_001706").build(),
					Gene.builder().name("BIRC3").exon("All coding exons").reference("NM_182962").build(),
					Gene.builder().name("BTK").exon("14-15").reference("NM_000061").build(),
					Gene.builder().name("CARD11").exon("3-9, 15").reference("NM_032415").build(),
					Gene.builder().name("CD79A").exon("2-5").reference("NM_001783").build(),
					Gene.builder().name("CD79B").exon("2-5").reference("NM_001039933").build(),
					Gene.builder().name("CREBBP").exon("All coding exons").reference("NM_004380").build(),
					Gene.builder().name("CXCR4").exon("All coding exons").reference("NM_003467").build(),
					Gene.builder().name("EGR2").exon("2").reference("NM_001136177").build(),
					Gene.builder().name("EP300").exon("All coding exons").reference("NM_001429").build(),
					Gene.builder().name("EZH2").exon("12, 16, 18").reference("NM_004456").build(),
					Gene.builder().name("FAS").exon("All coding exons").reference("NM_000043").build(),
					Gene.builder().name("FAT4").exon("All coding exons").reference("NM_024582").build(),
					Gene.builder().name("FBXO11").exon("All coding exons").reference("NM_001190274").build(),
					Gene.builder().name("ID3").exon("All coding exons").reference("NM_002167").build(),
					Gene.builder().name("IDH2").exon("4").reference("NM_002168").build(),
					Gene.builder().name("IKBKB").exon("All coding exons").reference("NM_001556").build(),
					Gene.builder().name("IKZF1").exon("All coding exons").reference("NM_006060").build(),
					Gene.builder().name("JAK3").exon("All coding exons").reference("NM_000215").build(),
					Gene.builder().name("KLF2").exon("All coding exons").reference("NM_016270").build(),
					Gene.builder().name("MYC").exon("All coding exons").reference("NM_002467").build(),
					Gene.builder().name("NFKBIE").exon("1").reference("NM_004556").build(),
					Gene.builder().name("NOTCH1").exon("26-28, 34").reference("NM_017617").build(),
					Gene.builder().name("NOTCH2").exon("26-27, 34").reference("NM_024408").build(),
					Gene.builder().name("PLCG1").exon("11, 15, 18-19").reference("NM_182811").build(),
					Gene.builder().name("PLCG2").exon("19-20, 24").reference("NM_002661").build(),
					Gene.builder().name("POT1").exon("All coding exons").reference("NM_015450").build(),
					Gene.builder().name("PRDM1").exon("All coding exons").reference("NM_001198").build(),
					Gene.builder().name("RHOA").exon("All coding exons").reference("NM_001664").build(),
					Gene.builder().name("RPS15").exon("4").reference("NM_001018").build(),
					Gene.builder().name("RRAGC").exon("1-2").reference("NM_022157").build(),
					Gene.builder().name("SF3B1").exon("14-19").reference("NM_012433").build(),
					Gene.builder().name("SOCS1").exon("All coding exons").reference("NM_003745").build(),
					Gene.builder().name("STAT3").exon("7, 13-14, 19-22").reference("NM_139276").build(),
					Gene.builder().name("STAT5B").exon("14-18").reference("NM_012448").build(),
					Gene.builder().name("TBL1XR1").exon("All coding exons").reference("NM_024665").build(),
					Gene.builder().name("TCF3").exon("All coding exons").reference("NM_001136139").build(),
					Gene.builder().name("TET2").exon("All coding exons").reference("NM_001127208").build(),
					Gene.builder().name("TNFAIP3").exon("All coding exons").reference("NM_006290").build(),
					Gene.builder().name("TNFRSF14").exon("All coding exons").reference("NM_003820").build(),
					Gene.builder().name("TP63").exon("All coding exons").reference("NM_003722").build(),
					Gene.builder().name("TRAF3").exon("All coding exons").reference("NM_003300").build(),
					Gene.builder().name("UBR5").exon("All coding exons").reference("NM_015902").build(),
					Gene.builder().name("XPO1").exon("15-16, 18").reference("NM_003400").build()
			}).isNationalInsuranceTest(true).build();
	public static final TestInfo ON064 = TestInfo.builder()
			.code("ON064")
			.name("Acute Myeloid Leukemia (AML) Panel")
			.referralDefault("AML")
			.region("58 genes")
			.panel("Acute Myeloid Leukemia (AML) Panel")
			.genesEssential(new Gene[] {
					Gene.builder().name("CEBPA").exon("All coding exons").reference("NM_004364").build(),
					Gene.builder().name("FLT3").exon("14-20").reference("NM_004119").build(),
					Gene.builder().name("IDH1").exon("4").reference("NM_005896").build(),
					Gene.builder().name("IDH2").exon("4").reference("NM_002168").build(),
					Gene.builder().name("JAK2").exon("12, 14").reference("NM_004972").build(),
					Gene.builder().name("KIT").exon("8-14, 17-18").reference("NM_000222").build(),
					Gene.builder().name("NPM1").exon("10-11").reference("NM_002520").build(),
					Gene.builder().name("RUNX1").exon("All coding exons").reference("NM_001754").build(),
					Gene.builder().name("TP53").exon("All coding exons").reference("NM_000546").build()
			}).genesSelective(new Gene[] {
					Gene.builder().name("ABL1").exon("Exon 4-8").reference("NM_005157").build(),
					Gene.builder().name("ANKRD26").exon("5'UTR").reference("NM_014915").build(), //
					Gene.builder().name("ASXL1").exon("12").reference("NM_015338").build(),
					Gene.builder().name("ATRX").exon("8-10, 17-31").reference("NM_000489").build(),
					Gene.builder().name("BCOR").exon("All coding exons").reference("NM_001123385").build(),
					Gene.builder().name("BCORL1").exon("All coding exons").reference("NM_021946").build(),
					Gene.builder().name("BRAF").exon("15").reference("NM_004333").build(),
					Gene.builder().name("CALR").exon("All coding exons").reference("NM_004343").build(),
					Gene.builder().name("CBL").exon("8-9").reference("NM_005188").build(),
					Gene.builder().name("CBLB").exon("9-10").reference("NM_170662").build(),
					Gene.builder().name("CSF3R").exon("14, 17").reference("NM_000760").build(),
					Gene.builder().name("DDX41").exon("All coding exons").reference("NM_016222").build(),
					Gene.builder().name("DNMT3A").exon("All coding exons").reference("NM_022552").build(),
					Gene.builder().name("ETNK1").exon("H243, N244").reference("NM_018638").build(),
					Gene.builder().name("ETV6").exon("All coding exons").reference("NM_001987").build(),
					Gene.builder().name("EZH2").exon("All coding exons").reference("NM_004456").build(),
					Gene.builder().name("GATA1").exon("All coding exons").reference("NM_002049").build(),
					Gene.builder().name("GATA2").exon("All coding exons").reference("NM_032638").build(),
					Gene.builder().name("GNB1").exon("All coding exons").reference("NM_001282539").build(),
					Gene.builder().name("HRAS").exon("2-3").reference("NM_005343").build(),
					Gene.builder().name("IKZF1").exon("All coding exons").reference("NM_006060").build(),
					Gene.builder().name("JAK3").exon("13").reference("NM_000215").build(),
					Gene.builder().name("KDM6A").exon("All coding exons").reference("NM_021140").build(),
					Gene.builder().name("KRAS").exon("2-4").reference("NM_004985").build(),
					Gene.builder().name("MLL (KMT2A)").exon("All coding exons").reference("NM_001197104").build(),
					Gene.builder().name("MPL").exon("10").reference("NM_005373").build(),
					Gene.builder().name("NF1").exon("All coding exons").reference("NM_000267").build(),
					Gene.builder().name("NOTCH1").exon("26-28, 34").reference("NM_017617").build(),
					Gene.builder().name("NRAS").exon("2-4").reference("NM_002524").build(),
					Gene.builder().name("PDGFRA").exon("12, 14, 18").reference("NM_006206").build(),
					Gene.builder().name("PHF6").exon("All coding exons").reference("NM_001015877").build(),
					Gene.builder().name("PPM1D").exon("6").reference("NM_003620").build(),
					Gene.builder().name("PRPF8").exon("All coding exons").reference("NM_006445").build(),
					Gene.builder().name("PTPN11").exon("3-4, 12-13").reference("NM_002834").build(),
					Gene.builder().name("RAD21").exon("All coding exons").reference("NM_006265").build(),
					Gene.builder().name("SAMD9").exon("Exon 3").reference("NM_017654").build(),
					Gene.builder().name("SAMD9L").exon("Exon 5").reference("NM_152703").build(),
					Gene.builder().name("SETBP1").exon("4*").reference("NM_015559").build(),
					Gene.builder().name("SF3B1").exon("6-8, 12-17").reference("NM_012433").build(),
					Gene.builder().name("SMC1A").exon("2, 11, 16-17").reference("NM_006306").build(),
					Gene.builder().name("SMC3").exon("All coding exons").reference("NM_005445").build(),
					Gene.builder().name("SRSF2").exon("1").reference("NM_003016").build(),
					Gene.builder().name("STAG1").exon("All coding exons").reference("NM_005862").build(),
					Gene.builder().name("STAG2").exon("All coding exons").reference("NM_001042749").build(),
					Gene.builder().name("STAT3").exon("20-21").reference("NM_139276").build(),
					Gene.builder().name("TET2").exon("All coding exons").reference("NM_001127208").build(),
					Gene.builder().name("U2AF1").exon("2, 6").reference("NM_006758").build(),
					Gene.builder().name("WT1").exon("2-10").reference("NM_024426").build(),
					Gene.builder().name("ZRSR2").exon("All coding exons").reference("NM_005089").build()
			}).isNationalInsuranceTest(false).i18n("ENUS").build();
	public static final TestInfo ON065 = TestInfo.builder()
			.code("ON065")
			.name("Myelodysplastic Syndromes(MDS) / Myeloproliferative Neoplasm(MPN) Panel")
			.referralDefault("MDS/MPN")
			.region("58 genes")
			.panel("Myelodysplastic syndromes (MDS) Panel")
			.genesEssential(new Gene[] {
					Gene.builder().name("ASXL1").exon("12").reference("NM_015338").build(),
					Gene.builder().name("CALR").exon("All coding exons").reference("NM_004343").build(),
					Gene.builder().name("CSF3R").exon("14, 17").reference("NM_000760").build(),
					Gene.builder().name("DNMT3A").exon("All coding exons").reference("NM_022552").build(),
					Gene.builder().name("JAK2").exon("12, 14").reference("NM_004972").build(),
					Gene.builder().name("MPL").exon("10").reference("NM_005373").build(),
					Gene.builder().name("RUNX1").exon("All coding exons").reference("NM_001754").build(),
					Gene.builder().name("SETBP1").exon("4*").reference("NM_015559").build(),
					Gene.builder().name("SF3B1").exon("6-8, 12-17").reference("NM_012433").build(),
					Gene.builder().name("SRSF2").exon("1").reference("NM_003016").build(),
					Gene.builder().name("TET2").exon("All coding exons").reference("NM_001127208").build()
			}).genesSelective(new Gene[] {
					Gene.builder().name("ABL1").exon("Exon 4-8").reference("NM_005157").build(),
					Gene.builder().name("ANKRD26").exon("5'UTR").reference("NM_014915").build(),
					Gene.builder().name("ATRX").exon("8-10, 17-31").reference("NM_000489").build(),
					Gene.builder().name("BCOR").exon("All coding exons").reference("NM_001123385").build(),
					Gene.builder().name("BCORL1").exon("All coding exons").reference("NM_021946").build(),
					Gene.builder().name("BRAF").exon("15").reference("NM_004333").build(),
					Gene.builder().name("CBL").exon("8-9").reference("NM_005188").build(),
					Gene.builder().name("CBLB").exon("9-10").reference("NM_170662").build(),
					Gene.builder().name("CEBPA").exon("All coding exons").reference("NM_004364").build(),
					Gene.builder().name("DDX41").exon("All coding exons").reference("NM_016222").build(),
					Gene.builder().name("ETNK1").exon("H243, N244").reference("NM_018638").build(),
					Gene.builder().name("ETV6").exon("All coding exons").reference("NM_001987").build(),
					Gene.builder().name("EZH2").exon("All coding exons").reference("NM_004456").build(),
					Gene.builder().name("FLT3").exon("14-20").reference("NM_004119").build(),
					Gene.builder().name("GATA1").exon("All coding exons").reference("NM_002049").build(),
					Gene.builder().name("GATA2").exon("All coding exons").reference("NM_032638").build(),
					Gene.builder().name("GNB1").exon("All coding exons").reference("NM_001282539").build(),
					Gene.builder().name("HRAS").exon("2-3").reference("NM_005343").build(),
					Gene.builder().name("IDH1").exon("4").reference("NM_005896").build(),
					Gene.builder().name("IDH2").exon("4").reference("NM_002168").build(),
					Gene.builder().name("IKZF1").exon("All coding exons").reference("NM_006060").build(),
					Gene.builder().name("JAK3").exon("13").reference("NM_000215").build(),
					Gene.builder().name("KDM6A").exon("All coding exons").reference("NM_021140").build(),
					Gene.builder().name("KIT").exon("8-14, 17-18").reference("NM_000222").build(),
					Gene.builder().name("KRAS").exon("2-4").reference("NM_004985").build(),
					Gene.builder().name("MLL (KMT2A)").exon("All coding exons").reference("NM_001197104").build(),
					Gene.builder().name("NF1").exon("All coding exons").reference("NM_000267").build(),
					Gene.builder().name("NOTCH1").exon("26-28, 34").reference("NM_017617").build(),
					Gene.builder().name("NPM1").exon("10-11").reference("NM_002520").build(),
					Gene.builder().name("NRAS").exon("2-4").reference("NM_002524").build(),
					Gene.builder().name("PDGFRA").exon("12, 14, 18").reference("NM_006206").build(),
					Gene.builder().name("PHF6").exon("All coding exons").reference("NM_001015877").build(),
					Gene.builder().name("PPM1D").exon("6").reference("NM_003620").build(),
					Gene.builder().name("PRPF8").exon("All coding exons").reference("NM_006445").build(),
					Gene.builder().name("PTPN11").exon("3-4, 12-13").reference("NM_002834").build(),
					Gene.builder().name("RAD21").exon("All coding exons").reference("NM_006265").build(),
					Gene.builder().name("SAMD9").exon("Exon 3").reference("NM_017654").build(),
					Gene.builder().name("SAMD9L").exon("Exon 5").reference("NM_152703").build(),
					Gene.builder().name("SMC1A").exon("2, 11, 16-17").reference("NM_006306").build(),
					Gene.builder().name("SMC3").exon("All coding exons").reference("NM_005445").build(),
					Gene.builder().name("STAG1").exon("All coding exons").reference("NM_005862").build(),
					Gene.builder().name("STAG2").exon("All coding exons").reference("NM_001042749").build(),
					Gene.builder().name("STAT3").exon("20-21").reference("NM_139276").build(),
					Gene.builder().name("TP53").exon("All coding exons").reference("NM_000546").build(),
					Gene.builder().name("U2AF1").exon("2, 6").reference("NM_006758").build(),
					Gene.builder().name("WT1").exon("2-10").reference("NM_024426").build(),
					Gene.builder().name("ZRSR2").exon("All coding exons").reference("NM_005089").build()
			}).isNationalInsuranceTest(false).i18n("ENUS").build();
	public static final TestInfo ON082 = TestInfo.builder()
			.code("ON082")
			.name("Acute Lymphoblastic Leukemia (ALL) Panel")
			.referralDefault("ALL")
			.region("54 genes")
			.panel("Acute Lymphoblastic Leukemia (ALL) Panel")
			.genesEssential(new Gene[] {
					Gene.builder().name("IKZF1").exon("All coding exons").reference("NM_006060").build(),
					Gene.builder().name("JAK2").exon("16, 20-21, 24").reference("NM_004972").build(),
					Gene.builder().name("NRAS").exon("2-3").reference("NM_002524").build(),
					Gene.builder().name("RB1").exon("4, 9, 13, 20").reference("NM_000321").build(),
					Gene.builder().name("TP53").exon("All coding exons").reference("NM_000546").build()
			}).genesSelective(new Gene[] {
					Gene.builder().name("ABL1").exon("4-10").reference("NM_005157").build(),
					Gene.builder().name("BRAF").exon("11,15").reference("NM_004333").build(),
					Gene.builder().name("BTG1").exon("2").reference("NM_001731").build(),
					Gene.builder().name("CDKN2A").exon("All coding exons").reference("NM_000077").build(),
					Gene.builder().name("CDKN2B").exon("All coding exons").reference("NM_004936").build(),
					Gene.builder().name("CREBBP").exon("4, 6, 14, 17-19,\n21, 26-28, 31").reference("NM_004380").build(),
					Gene.builder().name("DNM2").exon("All coding exons").reference("NM_001005360").build(),
					Gene.builder().name("DNMT3A").exon("All coding exons").reference("NM_022552").build(),
					Gene.builder().name("EED").exon("All coding exons").reference("NM_003797").build(),
					Gene.builder().name("EP300").exon("30").reference("NM_001429").build(),
					Gene.builder().name("ERG").exon("All coding exons").reference("NM_182918").build(),
					Gene.builder().name("ETV6").exon("All coding exons").reference("NM_001987").build(),
					Gene.builder().name("EZH2").exon("All coding exons").reference("NM_004456").build(),
					Gene.builder().name("FBXW7").exon("All coding exons").reference("NM_033632").build(),
					Gene.builder().name("FLT3").exon("5, 9, 13-16, 19-21, plus\nexons 13-15 (FLT3-ITD)").reference("NM_004119").build(),
					Gene.builder().name("GATA3").exon("4-6").reference("NM_002051").build(),
					Gene.builder().name("IDH1").exon("4, 7").reference("NM_005896").build(),
					Gene.builder().name("IDH2").exon("All coding exons").reference("NM_002168").build(),
					Gene.builder().name("IL7R").exon("3, 5-6").reference("NM_002185").build(),
					Gene.builder().name("JAK1").exon("10, 13, 14-23").reference("NM_002227").build(),
					Gene.builder().name("JAK3").exon("2, 4, 10-13, 18-19").reference("NM_000215").build(),
					Gene.builder().name("KDM6A").exon("15, 25").reference("NM_021140").build(),
					Gene.builder().name("KMT2A").exon("All coding exons").reference("NM_001197104").build(),
					Gene.builder().name("KMT2D").exon("All coding exons").reference("NM_003482").build(),
					Gene.builder().name("KRAS").exon("2-4").reference("NM_004985").build(),
					Gene.builder().name("LEF1").exon("3-4").reference("NM_016269").build(),
					Gene.builder().name("LMO1").exon("All coding exons").reference("NM_002315").build(),
					Gene.builder().name("MAPK1").exon("4").reference("NM_002745").build(),
					Gene.builder().name("NF1").exon("9-10, 12, 18-19, 21, 23,\n25, 28-29, 31, 33-34, 36\n-38, 41, 44, 49, 52").reference("NM_000267").build(),
					Gene.builder().name("NOTCH1").exon("All coding exons").reference("NM_017617").build(),
					Gene.builder().name("NSD2").exon("18").reference("NM_001042424").build(),
					Gene.builder().name("NT5C2").exon("All coding exons").reference("NM_001134373").build(),
					Gene.builder().name("NUDT15").exon("1-3").reference("NM_018283").build(),
					Gene.builder().name("PAX5").exon("2-5, 7, 9").reference("NM_016734").build(),
					Gene.builder().name("PDGFRB").exon("12, 14, 18").reference("NM_002609").build(),
					Gene.builder().name("PHF6").exon("All coding exons").reference("NM_001015877").build(),
					Gene.builder().name("PTEN").exon("2, 5, 7").reference("NM_000314").build(),
					Gene.builder().name("PTPN11").exon("3, 8, 13").reference("NM_002834").build(),
					Gene.builder().name("RUNX1").exon("All coding exons").reference("NM_001754").build(),
					Gene.builder().name("SETD2").exon("All coding exons").reference("NM_014159").build(),
					Gene.builder().name("SH2B3").exon("All coding exons").reference("NM_005475").build(),
					Gene.builder().name("STAG2").exon("All coding exons").reference("NM_001042749").build(),
					Gene.builder().name("STAT3").exon("All coding exons").reference("NM_139276").build(),
					Gene.builder().name("STAT5B").exon("All coding exons").reference("NM_012448").build(),
					Gene.builder().name("SUZ12").exon("All coding exons").reference("NM_015355").build(),
					Gene.builder().name("TBL1XR1").exon("5-7, 9").reference("NM_024665").build(),
					Gene.builder().name("TCF3").exon("All coding exons").reference("NM_001136139").build(),
					Gene.builder().name("TPMT").exon("All coding exons").reference("NM_000367").build(),
					Gene.builder().name("WT1").exon("All coding exons").reference("NM_024426").build()
			}).isNationalInsuranceTest(false).i18n("ENUS").build();
	public static final TestInfo ON083 = TestInfo.builder()
			.code("ON083")
			.name("Lymphoma Panel")
			.referralDefault("Lymphoma")
			.region("66 genes")
			.panel("Lymphoma Panel")
			.genesEssential(new Gene[] {
					Gene.builder().name("BRAF").exon("3, 6-8, 11-15, 17-18").reference("NM_004333").build(),
					Gene.builder().name("MYD88").exon("All coding exons").reference("NM_002468").build(),
					Gene.builder().name("TP53").exon("All coding exons").reference("NM_000546").build()
			}).genesSelective(new Gene[] {
					Gene.builder().name("ALK").exon("All coding exons").reference("NM_004304").build(),
					Gene.builder().name("ATM").exon("All coding exons").reference("NM_000051").build(),
					Gene.builder().name("B2M").exon("All coding exons").reference("NM_004048").build(),
					Gene.builder().name("BCL10").exon("All coding exons").reference("NM_003921").build(),
					Gene.builder().name("BCL2").exon("2").reference("NM_000633").build(),
					Gene.builder().name("BCL6").exon("All coding exons").reference("NM_001706").build(),
					Gene.builder().name("BIRC3").exon("All coding exons").reference("NM_182962").build(),
					Gene.builder().name("BTG2").exon("All coding exons").reference("NM_006763").build(),
					Gene.builder().name("BTK").exon("14-15").reference("NM_000061").build(),
					Gene.builder().name("CARD11").exon("3-9, 15").reference("NM_032415").build(),
					Gene.builder().name("CCND3").exon("5").reference("NM_001760").build(),
					Gene.builder().name("CD79A").exon("2-5").reference("NM_001783").build(),
					Gene.builder().name("CD79B").exon("2-5").reference("NM_001039933").build(),
					Gene.builder().name("CD83").exon("All coding exons").reference("NM_004233").build(),
					Gene.builder().name("CDKN2A").exon("All coding exons").reference("NM_000077").build(),
					Gene.builder().name("CREBBP").exon("All coding exons").reference("NM_004380").build(),
					Gene.builder().name("CXCR4").exon("All coding exons").reference("NM_003467").build(),
					Gene.builder().name("DDX3X").exon("All coding exons").reference("NM_001356").build(),
					Gene.builder().name("EGR2").exon("2").reference("NM_001136177").build(),
					Gene.builder().name("EP300").exon("All coding exons").reference("NM_001429").build(),
					Gene.builder().name("ETV6").exon("5-8").reference("NM_001987").build(),
					Gene.builder().name("EZH2").exon("12, 16, 18").reference("NM_004456").build(),
					Gene.builder().name("FAS").exon("All coding exons").reference("NM_000043").build(),
					Gene.builder().name("FAT4").exon("All coding exons").reference("NM_024582").build(),
					Gene.builder().name("FBXO11").exon("All coding exons").reference("NM_001190274").build(),
					Gene.builder().name("H1-4").exon("All coding exons").reference("NM_005321").build(),
					Gene.builder().name("ID3").exon("All coding exons").reference("NM_002167").build(),
					Gene.builder().name("IDH2").exon("4").reference("NM_002168").build(),
					Gene.builder().name("IKBKB").exon("All coding exons").reference("NM_001556").build(),
					Gene.builder().name("IKZF1").exon("All coding exons").reference("NM_006060").build(),
					Gene.builder().name("JAK3").exon("All coding exons").reference("NM_000215").build(),
					Gene.builder().name("KLF2").exon("All coding exons").reference("NM_016270").build(),
					Gene.builder().name("KLHL6").exon("All coding exons").reference("NM_130446").build(),
					Gene.builder().name("KMT2D").exon("All coding exons").reference("NM_003482").build(),
					Gene.builder().name("MEF2B").exon("2, 3").reference("NM_001145785").build(),
					Gene.builder().name("MYC").exon("All coding exons").reference("NM_002467").build(),
					Gene.builder().name("NFKBIA").exon("All coding exons").reference("NM_020529").build(),
					Gene.builder().name("NFKBIE").exon("1").reference("NM_004556").build(),
					Gene.builder().name("NOTCH1").exon("26-28, 34").reference("NM_017617").build(),
					Gene.builder().name("NOTCH2").exon("26-27, 34").reference("NM_024408").build(),
					Gene.builder().name("PIM1").exon("1-4").reference("NM_002648").build(),
					Gene.builder().name("PLCG1").exon("11, 15, 18-19").reference("NM_182811").build(),
					Gene.builder().name("PLCG2").exon("19-20, 24").reference("NM_002661").build(),
					Gene.builder().name("POT1").exon("All coding exons").reference("NM_015450").build(),
					Gene.builder().name("PRDM1").exon("All coding exons").reference("NM_001198").build(),
					Gene.builder().name("RHOA").exon("All coding exons").reference("NM_001664").build(),
					Gene.builder().name("RPS15").exon("4").reference("NM_001018").build(),
					Gene.builder().name("RRAGC").exon("1-2").reference("NM_022157").build(),
					Gene.builder().name("SF3B1").exon("14-19").reference("NM_012433").build(),
					Gene.builder().name("SGK1").exon("All coding exons").reference("NM_005627").build(),
					Gene.builder().name("SOCS1").exon("All coding exons").reference("NM_003745").build(),
					Gene.builder().name("SPEN").exon("All coding exons").reference("NM_015001").build(),
					Gene.builder().name("STAT3").exon("7, 13-14, 19-22").reference("NM_139276").build(),
					Gene.builder().name("STAT5B").exon("14-18").reference("NM_012448").build(),
					Gene.builder().name("TBL1XR1").exon("All coding exons").reference("NM_024665").build(),
					Gene.builder().name("TCF3").exon("All coding exons").reference("NM_001136139").build(),
					Gene.builder().name("TET2").exon("All coding exons").reference("NM_001127208").build(),
					Gene.builder().name("TNFAIP3").exon("All coding exons").reference("NM_006290").build(),
					Gene.builder().name("TNFRSF14").exon("All coding exons").reference("NM_003820").build(),
					Gene.builder().name("TP63").exon("All coding exons").reference("NM_003722").build(),
					Gene.builder().name("TRAF3").exon("All coding exons").reference("NM_003300").build(),
					Gene.builder().name("UBR5").exon("All coding exons").reference("NM_015902").build(),
					Gene.builder().name("XPO1").exon("15-16, 18").reference("NM_003400").build()
			}).isNationalInsuranceTest(false).i18n("ENUS").build();
	public static final TestInfo ON104 = TestInfo.builder()
			.code("ON104")
			.name("Multiple Myeloma (MM) Panel")
			.referralDefault("Multiple Myeloma")
			.region("34 genes")
			.panel("Multiple Myeloma(MM) Panel")
			.genesEssential(new Gene[] {
					Gene.builder().name("KRAS").exon("All coding exons").reference("NM_004985").build(),
					Gene.builder().name("NRAS").exon("2-3").reference("NM_002524").build(),
					Gene.builder().name("TP53").exon("All coding exons").reference("NM_000546").build()
			}).genesSelective(new Gene[] {
					Gene.builder().name("ATM").exon("All coding exons").reference("NM_000051").build(),
					Gene.builder().name("ATR").exon("All coding exons").reference("NM_001184").build(),
					Gene.builder().name("BRAF").exon("All coding exons").reference("NM_004333").build(),
					Gene.builder().name("CARD11").exon("5-9").reference("NM_032415").build(),
					Gene.builder().name("CCND1").exon("1, 5").reference("NM_053056").build(),
					Gene.builder().name("CDK4").exon("2").reference("NM_000075").build(),
					Gene.builder().name("CDKN1B").exon("All coding exons").reference("NM_004064").build(),
					Gene.builder().name("CRBN").exon("All coding exons").reference("NM_016302").build(),
					Gene.builder().name("CUL4A").exon("All coding exons").reference("NM_001008895").build(),
					Gene.builder().name("CUL4B").exon("All coding exons").reference("NM_003588").build(),
					Gene.builder().name("CXCR4").exon("All coding exons").reference("NM_003467").build(),
					Gene.builder().name("CYLD").exon("All coding exons").reference("NM_001042355").build(),
					Gene.builder().name("DIS3").exon("All coding exons").reference("NM_014953").build(),
					Gene.builder().name("EGR1").exon("All coding exons").reference("NM_001964").build(),
					Gene.builder().name("FGFR3").exon("All coding exons").reference("NM_000142").build(),
					Gene.builder().name("IDH1").exon("4").reference("NM_005896").build(),
					Gene.builder().name("IDH2").exon("4").reference("NM_002168").build(),
					Gene.builder().name("IKZF1").exon("All coding exons").reference("NM_006060").build(),
					Gene.builder().name("IRF4").exon("All coding exons").reference("NM_002460").build(),
					Gene.builder().name("MAX").exon("All coding exons").reference("NM_002382").build(),
					Gene.builder().name("MYD88").exon("All coding exons").reference("NM_002468").build(),
					Gene.builder().name("NFKBIA").exon("All coding exons").reference("NM_020529").build(),
					Gene.builder().name("NR3C1").exon("All coding exons").reference("NM_001024094").build(),
					Gene.builder().name("PSMB5").exon("All coding exons").reference("NM_002797").build(),
					Gene.builder().name("PSMD1").exon("All coding exons").reference("NM_002807").build(),
					Gene.builder().name("PSMG2").exon("All coding exons").reference("NM_020232").build(),
					Gene.builder().name("RB1").exon("All coding exons").reference("NM_000321").build(),
					Gene.builder().name("TENT5C").exon("All coding exons").reference("NM_017709").build(),
					Gene.builder().name("TRAF2").exon("All coding exons").reference("NM_021138").build(),
					Gene.builder().name("TRAF3").exon("All coding exons").reference("NM_003300").build(),
					Gene.builder().name("XBP1").exon("All coding exons").reference("NM_005080").build(),
			}).isNationalInsuranceTest(false).i18n("ENUS").build();
	public static final TestInfo[] TESTS = new TestInfo[] {
			N064, N065,
			N082, N083,
			N093, N094,
			N104, N105,// S051
			N157,
			ON064, ON065,
			ON082, ON083,
			ON104
	};
}