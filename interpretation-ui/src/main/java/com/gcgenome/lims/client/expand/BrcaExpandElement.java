package com.gcgenome.lims.client.expand;

import com.gcgenome.lims.api.InterpretationApi;
import com.gcgenome.lims.client.interpretation.Gene;
import com.gcgenome.lims.client.interpretation.SnvPanelTestTableElement;
import com.gcgenome.lims.dto.interpretation.PanelTest;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;
import org.jboss.elemento.HTMLContainerBuilder;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static org.jboss.elemento.Elements.div;

public class BrcaExpandElement extends SingleExpandElement {
	public static BrcaExpandElement build(String id, long sample, String service) {
		return new BrcaExpandElement(div(), id, sample, service);
	}
	private BrcaExpandElement(HTMLContainerBuilder<HTMLDivElement> e, String id, long sample, String service) {
		super(e, id, sample, service, Arrays.asList("P", "LP", "PV", "LPV", "VUS"), SnvPanelTestTableElement::build);
	}
	@Override
	public void auto() {
		result = elemReport.get();
		if(btnAddendum.value()) result.addendum(elemAddendum.get());
		else result.addendum(null);
		extra.values().stream().filter(m -> m.has("mim.inheritance")).forEach(m -> m.set("mim.disease", m.get("mim.disease") + "%" + m.get("mim.inheritance")));
		Map<String, String> map = extra.values().stream().collect(Collectors.toMap(m->(String)m.get("gene.refgene"), m->(String)m.get("mim.disease"), (p1, p2)->p1));
		dialog.callback(param-> InterpretationApi.auto(sample, service, param.previous(result))
				.then(result->{
					update((PanelTest) result, proven);
					DomGlobal.alert("생성되었습니다. 저장하세요.");
					return null;
				})).build(result, map, GENES);
	}

	private static final Gene[] GENES = new Gene[] {
			Gene.builder().symbol("APC").abbr("FAP").inheritance("AD").disease("Familial adenomatosis polyposis").build(),
			Gene.builder().symbol("ATM").abbr("BCs").inheritance("AD").disease("Susceptibility to breast cancer").build(),
			Gene.builder().symbol("BARD1").abbr("BCs").inheritance("AD").disease("Susceptibility to breast cancer").build(),
			Gene.builder().symbol("BRCA1").abbr("HBOC").inheritance("AD").disease("Hereditary breast and ovarian cancer").build(),
			Gene.builder().symbol("BRCA2").abbr("HBOC").inheritance("AD").disease("Hereditary breast and ovarian cancer").build(),
			Gene.builder().symbol("BRIP1").abbr("BCs").inheritance("AD").disease("Susceptibility to breast cancer").build(),
			Gene.builder().symbol("CDH1").abbr("BCs, HDGC").inheritance("AD").disease("Susceptibility to breast cancer, Hereditary diffuse gastic cancer").build(),
			Gene.builder().symbol("CHEK2").abbr("BCs").inheritance("AD").disease("Susceptibility to breast cancer").build(),
			Gene.builder().symbol("EPCAM").abbr("HNPCC").inheritance("AD").disease("Hereditary nonpolyposis colorectal cancer").build(),
			Gene.builder().symbol("MEN1").abbr("MEN").inheritance("AD").disease("Multiple endocrine neoplasia").build(),
			Gene.builder().symbol("MLH1").abbr("HNPCC").inheritance("AD").disease("Hereditary nonpolyposis colorectal cancer").build(),
			Gene.builder().symbol("MSH2").abbr("HNPCC").inheritance("AD").disease("Hereditary nonpolyposis colorectal cancer").build(),
			Gene.builder().symbol("MSH6").abbr("HNPCC").inheritance("AD").disease("Hereditary nonpolyposis colorectal cancer").build(),
			Gene.builder().symbol("MUTYH").abbr("MCA").inheritance("AR").disease("Multiple colorectal adenoma").build(),
			Gene.builder().symbol("NBN").abbr("BCs").inheritance("AD").disease("Susceptibility to breast cancer").build(),
			Gene.builder().symbol("PALB2").abbr("BCs").inheritance("AD").disease("Susceptibility to breast cancer").build(),
			Gene.builder().symbol("PMS2").abbr("HNPCC").inheritance("AD").disease("Hereditary nonpolyposis colorectal cancer").build(),
			Gene.builder().symbol("PTEN").abbr("PHTS").inheritance("AD").disease("PTEN hamartoma tumor syndrome").build(),
			Gene.builder().symbol("RAD50").abbr("BCs").inheritance("AD").disease("Susceptibility to breast cancer").build(),
			Gene.builder().symbol("RAD51C").abbr("BOCs").inheritance("AD").disease("Susceptibility to breast-ovarian cancer").build(),
			Gene.builder().symbol("RET").abbr("MEN").inheritance("AD").disease("Multiple endocrine neoplasia").build(),
			Gene.builder().symbol("STK11").abbr("BCs, PJS").inheritance("AD").disease("Susceptibility to breast cancer, Peutz-Jeghers syndrome").build(),
			Gene.builder().symbol("TP53").abbr("LFS").inheritance("AD").disease("Li-Fraumeni syndrome").build(),
			Gene.builder().symbol("NF1").abbr("NF1").inheritance("AD").disease("Neurofibromatosis, type 1").build(),
			Gene.builder().symbol("RAD51D").abbr("BOCs").inheritance("AD").disease("Susceptibility to breast-ovarian cancer").build(),
			Gene.builder().symbol("POLD1").abbr("CRCs").inheritance("AD").disease("Susceptibility to colorectal cancer").build(),
			Gene.builder().symbol("POLE").abbr("CRCs").inheritance("AD").disease("Susceptibility to colorectal cancer").build(),
			Gene.builder().symbol("SMAD4").abbr("JPS").inheritance("AD").disease("Juvenile polyposis syndrome").build(),
			Gene.builder().symbol("AIP").abbr("PITA1").inheritance("AD").disease("Pituitary adenoma").build(),
			Gene.builder().symbol("ALK").abbr("NBLSTs").inheritance("AD").disease("Susceptibility to neuroblastoma").build(),
			Gene.builder().symbol("BAP1").abbr("TPDS").inheritance("AD").disease("Tumor predisposition syndrome").build(),
			Gene.builder().symbol("BLM").abbr("BCs, CRCs").inheritance("AD").disease("Susceptibility to breast cancer, Susceptibility to colorectal cancer").build(),
			Gene.builder().symbol("BMPR1A").abbr("JPS").inheritance("AD").disease("Juvenile polyposis syndrome").build(),
			Gene.builder().symbol("BUB1B").abbr("CRCs, MVA1").inheritance("AR").disease("Susceptibility to colorectal cancer, Mosaic variegated aneuploidy syndrome 1").build(),
			Gene.builder().symbol("CDC73").abbr("HRPT2").inheritance("AD").disease("Hyperparathyroidism-jaw tumor syndrome").build(),
			Gene.builder().symbol("CDK4").abbr("CMMs").inheritance("AD").disease("Susceptibility to cutaneous malignant melanoma").build(),
			Gene.builder().symbol("CDKN1C").abbr("BWS").inheritance("AD").disease("Beckwith-Wiedemann syndrome").build(),
			Gene.builder().symbol("CDKN2A").abbr("CMMs, HCs").inheritance("AD").disease("Susceptibility to cutaneous melanoma, Susceptibility to hereditary cancer").build(),
			Gene.builder().symbol("CEBPA").abbr("AMLs").inheritance("AD").disease("Susceptibility to acute myeloid leukemia").build(),
			Gene.builder().symbol("CEP57").abbr("MVA2").inheritance("AR").disease("Mosaic variegated aneuploidy syndrome 2").build(),
			Gene.builder().symbol("CYLD").abbr("SBS").inheritance("AD").disease("Brooke-Spiegler syndrome").build(),
			Gene.builder().symbol("DDB2").abbr("XP").inheritance("AR").disease("Xeroderma pigmentosum").build(),
			Gene.builder().symbol("DICER1").abbr("MNG").inheritance("AD").disease("Multinodular goiter").build(),
			Gene.builder().symbol("DIS3L2").abbr("PRLMNS").inheritance("AR").disease("Perlman syndrome").build(),
			Gene.builder().symbol("EGFR").abbr("NSCLCs").inheritance("AD").disease("Susceptibility to nonsmall cell lung cancer").build(),
			Gene.builder().symbol("ERCC2").abbr("XP").inheritance("AR").disease("Xeroderma pigmentosum").build(),
			Gene.builder().symbol("ERCC3").abbr("XP").inheritance("AR").disease("Xeroderma pigmentosum").build(),
			Gene.builder().symbol("ERCC4").abbr("XP").inheritance("AR").disease("Xeroderma pigmentosum").build(),
			Gene.builder().symbol("ERCC5").abbr("XP").inheritance("AR").disease("Xeroderma pigmentosum").build(),
			Gene.builder().symbol("EXT1").abbr("OSRCs").inheritance("AD").disease("Susceptibility to osteochondromas").build(),
			Gene.builder().symbol("EXT2").abbr("OSRCs").inheritance("AD").disease("Susceptibility to osteochondromas").build(),
			Gene.builder().symbol("EZH2").abbr("WVS, HCs").inheritance("AD").disease("Weaver syndrome, Susceptibility to hereditary cancer").build(),
			Gene.builder().symbol("FANCA").abbr("FANC").inheritance("AR").disease("Fanconi anemia").build(),
			Gene.builder().symbol("FANCB").abbr("FANC").inheritance("XLR").disease("Fanconi anemia").build(),
			Gene.builder().symbol("FANCC").abbr("FANC").inheritance("AR").disease("Fanconi anemia").build(),
			Gene.builder().symbol("FANCD2").abbr("FANC").inheritance("AR").disease("Fanconi anemia").build(),
			Gene.builder().symbol("FANCE").abbr("FANC").inheritance("AR").disease("Fanconi anemia").build(),
			Gene.builder().symbol("FANCF").abbr("FANC").inheritance("AR").disease("Fanconi anemia").build(),
			Gene.builder().symbol("FANCG").abbr("FANC").inheritance("AR").disease("Fanconi anemia").build(),
			Gene.builder().symbol("FANCI").abbr("FANC").inheritance("AR").disease("Fanconi anemia").build(),
			Gene.builder().symbol("FANCL").abbr("FANC").inheritance("AR").disease("Fanconi anemia").build(),
			Gene.builder().symbol("FANCM").abbr("FANC").inheritance("AR").disease("Fanconi anemia").build(),
			Gene.builder().symbol("FH").abbr("HLRCC").inheritance("AD").disease("Hereditary leiomyomatosis and renal cell cancer").build(),
			Gene.builder().symbol("FLCN").abbr("BHD").inheritance("AD").disease("Birt-Hogg-Dube syndrome").build(),
			Gene.builder().symbol("GATA2").abbr("HCs").inheritance("AD").disease("Susceptibility to hematopoietic cancers").build(),
			Gene.builder().symbol("GPC3").abbr("SGBS1, WTs").inheritance("XLR").disease("Susceptibility to Wilms tumor, Simpson-Golabi-Behmel syndrome, type 1").build(),
			Gene.builder().symbol("HNF1A").abbr("HCCs, RCCs").inheritance("AD").disease("Susceptibility to hepatocellular carcinomas, Susceptibility to renal cell carcinoma").build(),
			Gene.builder().symbol("HOXB13").abbr("PCs").inheritance("AD").disease("Susceptibility to prostate cancer").build(),
			Gene.builder().symbol("HRAS").abbr("CSTLO, HCs").inheritance("AD").disease("Costello syndrome, Susceptibility to hereditary cancer").build(),
			Gene.builder().symbol("KIT").abbr("GIST, HCs").inheritance("AD").disease("Gastrointestinal stromal tumor, Susceptibility to hereditary cancer").build(),
			Gene.builder().symbol("MAX").abbr("PHEOs").inheritance("AD").disease("Susceptibility to pheochromocytoma").build(),
			Gene.builder().symbol("MET").abbr("HCs").inheritance("AD").disease("Susceptibility to hereditary cancer").build(),
			Gene.builder().symbol("NF2").abbr("NF2").inheritance("AD").disease("Neurofibromatosis, type 2").build(),
			Gene.builder().symbol("NSD1").abbr("SOTOS").inheritance("AD").disease("Sotos syndrome").build(),
			Gene.builder().symbol("PHOX2B").abbr("NBLSTs").inheritance("AD").disease("Susceptibility to neuroblastoma").build(),
			Gene.builder().symbol("PMS1").abbr("HNPCC").inheritance("AD").disease("Hereditary nonpolyposis colorectal cancer").build(),
			Gene.builder().symbol("PPM1D").abbr("BCs").inheritance("AD").disease("Susceptibility to breast cancer").build(),
			Gene.builder().symbol("PRF1").abbr("FHL, HCs").inheritance("AR, AD").disease("Familial hemophagocytic lymphohistiocytosis, Susceptibility to hereditary cancer").build(),
			Gene.builder().symbol("PRKAR1A").abbr("CNC1").inheritance("AD").disease("Carney complex, type 1").build(),
			Gene.builder().symbol("PTCH1").abbr("BCNS").inheritance("AD").disease("Basal cell nevus syndrome").build(),
			Gene.builder().symbol("AXIN2").abbr("ODCRCS").inheritance("AD").disease("Oligodontia-colorectal cancer syndrome").build(),
			Gene.builder().symbol("CDK12").abbr("HBOC").inheritance("AD").disease("Hereditary breast and ovarian cancer").build(),
			Gene.builder().symbol("CDKN1B").abbr("MEN4").inheritance("AD").disease("Multiple endocrine neoplasia, type IV").build(),
			Gene.builder().symbol("CHEK1").abbr("HBOC").inheritance("AD").disease("Hereditary breast and ovarian cancer").build(),
			Gene.builder().symbol("CTNNA1").abbr("HDGC").inheritance("AD").disease("Hereditary diffuse gastric cancer").build(),
			Gene.builder().symbol("GREM1").abbr("HMPS").inheritance("AD").disease("Hereditary Mixed Polyposis syndrome").build(),
			Gene.builder().symbol("MRE11").abbr("BCs").inheritance("AD").disease("Susceptibility to breast cancer").build(),
			Gene.builder().symbol("MSH3").abbr("FAP").inheritance("AR").disease("Familial adenomatous polyposis").build(),
			Gene.builder().symbol("NTHL1").abbr("FAP").inheritance("AR").disease("Familial adenomatous polyposis").build(),
			Gene.builder().symbol("PPP2R2A").abbr("PCs").inheritance("AD").disease("Susceptibility to prostate cancer").build(),
			Gene.builder().symbol("RAD51B").abbr("HBOC").inheritance("AD").disease("Hereditary breast and ovarian cancer").build(),
			Gene.builder().symbol("RAD54L").abbr("HBOC").inheritance("AD").disease("Hereditary breast and ovarian cancer").build(),
			Gene.builder().symbol("RB1").abbr("RB").inheritance("AD").disease("RETINOBLASTOMA").build(),
			Gene.builder().symbol("SDHA").abbr("HPPS").inheritance("AD").disease("Hereditary paraganglioma-pheochromocytoma syndrome").build(),
			Gene.builder().symbol("SDHAF2").abbr("HPPS").inheritance("AD").disease("Hereditary paraganglioma-pheochromocytoma syndrome").build(),
			Gene.builder().symbol("SDHB").abbr("HPPS").inheritance("AD").disease("Hereditary paraganglioma-pheochromocytoma syndrome").build(),
			Gene.builder().symbol("SDHC").abbr("HPPS").inheritance("AD").disease("Hereditary paraganglioma-pheochromocytoma syndrome").build(),
			Gene.builder().symbol("SDHD").abbr("HPPS").inheritance("AD").disease("Hereditary paraganglioma-pheochromocytoma syndrome").build(),
			Gene.builder().symbol("SMARCA4").abbr("RTPS").inheritance("AD").disease("Rhabdoid tumor predisposition syndrome").build(),
			Gene.builder().symbol("SMARCB1").abbr("RTPS, SWNTS1").inheritance("AD").disease("Rhabdoid tumor predisposition syndrome, Susceptibility to Schwannomatosis").build(),
			Gene.builder().symbol("SUFU").abbr("BCNS, MDB").inheritance("AD").disease("Basal cell nevus syndrome, Medulloblastoma").build(),
			Gene.builder().symbol("TMEM127").abbr("PHEOs").inheritance("AD").disease("Susceptibility to Pheochromocytoma").build(),
			Gene.builder().symbol("TSC1").abbr("TSC").inheritance("AD").disease("Tuberous sclerosis complex").build(),
			Gene.builder().symbol("TSC2").abbr("TSC").inheritance("AD").disease("Tuberous sclerosis complex").build(),
			Gene.builder().symbol("VHL").abbr("VHL, PHEO").inheritance("AD").disease("von Hippel-Lindau syndrome, Pheochromocytoma").build(),
			Gene.builder().symbol("WT1").abbr("WT1").inheritance("AD").disease("Wilms tumor, type 1").build()
	};
}
