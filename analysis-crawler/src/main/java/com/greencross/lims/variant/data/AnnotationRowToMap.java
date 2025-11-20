package com.greencross.lims.variant.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class AnnotationRowToMap {
	public Map<String, String> map(Map<String, Integer> header, String line) {
		Map<String, String> map = new HashMap<>();
		map.put("organism", "homo sapiens");
		map.put("reference", "hg19");
		map.put("create_at", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")));
		String[] split = line.split("\t", -1);
		for(String key: header.keySet()) {
			if(split.length <= header.get(key)) continue;
			String cellValue = cellValue(split[header.get(key)]);
			if("qual".equalsIgnoreCase(key)) {
				if("None".equalsIgnoreCase(cellValue)) cellValue = null;
			} else if("strand".equalsIgnoreCase(key)) {
				if("NA".equalsIgnoreCase(cellValue) || "NGS_dead_zone".equalsIgnoreCase(cellValue)) cellValue = null;
				else if(cellValue!=null) {
					try {
						double v = Double.parseDouble(cellValue);
						if (v == -1) cellValue = "false";
						else if (v == 1) cellValue = "true";
					} catch(Exception ignore) {}
				}
			} else if("class".equalsIgnoreCase(key)) map.put("class_order", String.valueOf(order(cellValue)));
			map.put(key.replace(" ", "_"), cellValue);
		}
		return normalize(reservedWords(map));
	}
	private int order(String clazz) {
		if(clazz == null) return 9999;
		clazz = clazz.toUpperCase();
		if(clazz.startsWith("PVS1"))			return 10;
		if(clazz.startsWith("PVS1?"))			return 15;
		if(clazz.startsWith("PV"))				return 20;
		if(clazz.startsWith("(L)PV"))			return 25;
		if(clazz.startsWith("CLINVAR(L)PV"))	return 28;
		if(clazz.startsWith("DM"))				return 30;
		if(clazz.startsWith("HGMD_DM"))			return 32;
		if(clazz.startsWith("DM?"))				return 35;
		if(clazz.startsWith("PM5"))				return 40;
		if(clazz.startsWith("VUS_PM"))			return 48;
		if(clazz.startsWith("NO_QC_PM"))		return 49;
		if(clazz.startsWith("MAF0-S"))			return 50;
		if(clazz.startsWith("MAF0"))			return 51;
		if(clazz.startsWith("VUS_MAF0"))		return 52;
		if(clazz.startsWith("VUS-S"))			return 55;
		if(clazz.startsWith("VUS"))				return 60;
		if(clazz.startsWith("PP5"))				return 70;
		if(clazz.startsWith("PP5?"))			return 75;
		if(clazz.startsWith("VUS?"))			return 85;
		if(clazz.startsWith("SPLICEAI"))		return 90;
		if(clazz.startsWith("SPLICEAI_HIGH_PRECISION"))		return 91;
		if(clazz.startsWith("SPLICEAI_RECOMMENDED"))		return 92;
		if(clazz.startsWith("SPLICEAI_HIGH_RECALL"))		return 93;
		if(clazz.startsWith("HIGH_EFFECT"))		return 100;
		return 9999;
	}
	private Map<String, String> reservedWords(Map<String, String> map) {
		// replace(map, "dbsnp147", "dbsnp");
		String dbsnp = null;
		for(String key: map.keySet()) if(key.startsWith("dbsnp")) {
			dbsnp = key;
			break;
		}
		replace(map, "vaf(%)", "vaf");
		replace(map, "quality", "qual");
		replace(map, dbsnp, "dbsnp");

		replace(map, "cadd", "cadd.result");
		replace(map, "cadd_pred", "cadd.pred");
		replace(map, "cadd_phred", "cadd.pred");
		replace(map, "cadd_raw", "cadd.raw");

		replace(map, "clinvar", "clinvar.class");
		replace(map, "clinvar_id", "clinvar.id");
		replace(map, "clinvar_clndbn", "clinvar.clndbn");
		replace(map, "clinvar_clndn", "clinvar.clndbn");
		replace(map, "clinvar_updated_class", "clinvar.updated_class");
		replace(map, "clinvar_updated_review", "clinvar.updated_review");

		replace(map, "codons", "codon");

		replace(map, "esp6500_all", "esp6500.all");
		replace(map, "exac_all", "exac.all");
		replace(map, "exac_af_eas", "exac.eas");
		replace(map, "exac_af_sas", "exac.sas");

		replace(map, "gene_full_name", "gene.full_name");
		replace(map, "gene_refgene", "gene.refgene");

		replace(map, "gnomad_exome_all", "gnomad.exome.all");
		replace(map, "gnomad_exome_eas", "gnomad.exome.eas");
		replace(map, "gnomad_exomes_af_eas", "gnomad.exome.eas");
		replace(map, "gnomad_exomes_af_afr", "gnomad.exome.afr");
		replace(map, "gnomad_exomes_af_amr", "gnomad.exome.amr");
		replace(map, "gnomad_exomes_af_asj", "gnomad.exome.asj");
		replace(map, "gnomad_exomes_af_fin", "gnomad.exome.fin");
		replace(map, "gnomad_exomes_af_nfe", "gnomad.exome.nfe");
		replace(map, "gnomad_exomes_af_oth", "gnomad.exome.oth");

		replace(map, "gnomad_exomes_af_eas_kor", "gnomad.exome.eas_kor");
		replace(map, "gnomad_exomes_af_sas", "gnomad.exome.sas");
		replace(map, "gnomad_genome_all", "gnomad.genome.all");
		replace(map, "gnomad_genome_af_eas", "gnomad.genome.eas");
		replace(map, "gnomad_genome_eas", "gnomad.genome.eas");
		replace(map, "gnomad_genome_af_afr", "gnomad.genome.afr");
		replace(map, "gnomad_genome_af_amr", "gnomad.genome.amr");
		replace(map, "gnomad_genome_af_asj", "gnomad.genome.asj");
		replace(map, "gnomad_genome_af_fin", "gnomad.genome.fin");
		replace(map, "gnomad_genome_af_nfe", "gnomad.genome.nfe");
		replace(map, "gnomad_genome_af_oth", "gnomad.genome.oth");
		replace(map, "gnomad_genomes_af_eas", "gnomad.genome.eas");
		replace(map, "gnomad_total_af", "gnomad.total");

		replace(map, "go_biological_process", "go.biological_process");
		replace(map, "go_cellular_component", "go.cellular_component");
		replace(map, "go_molecular_function", "go.molecular_function");

		replace(map, "hgmd_mut", "hgmd.mut");
		replace(map, "hgmd_pmid", "hgmd.pmid");
		replace(map, "hgmd_tag", "hgmd.tag");
		replace(map, "hgmd_web_literature", "hgmd.web.literature");
		replace(map, "hgmd_web_disease", "hgmd.web.disease");
		replace(map, "hgmd_web_tag", "hgmd.web.tag");
		replace(map, "hgmd_codon_disease", "hgmd.codon.disease");
		replace(map, "hgmd_codon_tag", "hgmd.codon.tag");
		replace(map, "hgvsc_in_hgmd", "hgmd.hgvsc");
		replace(map, "hgvsp_in_hgmd", "hgmd.hgvsp");

		replace(map, "intervar", "intervar_");
		replace(map, "intervar_bv_evidence", "intervar.evidence.bv");
		replace(map, "intervar_pv_evidence", "intervar.evidence.pv");
		replace(map, "intervar_class", "intervar.class");
		replace(map, "intervar_evidence", "intervar.evidence_");

		replace(map, "dann_score", "dann.score");
		replace(map, "fathmmw", "fathmmw.pred");
		replace(map, "fathmmw_score", "fathmmw.score");
		replace(map, "gerp++_rs", "gerp++.rs");
		replace(map, "gerp++gt2", "gerp++.gt2");
		replace(map, "gerp++_gt2", "gerp++.gt2");
		replace(map, "lrt_pred", "lrt.pred");
		replace(map, "lrt_score", "lrt.score");
		replace(map, "m-cap_pred", "m-cap.pred");
		replace(map, "m-cap_score", "m-cap.score");
		replace(map, "metalr_pred", "metalr.pred");
		replace(map, "metalr_score", "metalr.score");
		replace(map, "metasvm_pred", "metasvm.pred");
		replace(map, "metasvm_score", "metasvm.score");
		replace(map, "mutationassessor", "mutationassessor.pred");
		replace(map, "mutationassessor_pred", "mutationassessor.pred");
		replace(map, "mutationassessor_score", "mutationassessor.score");
		replace(map, "mutationtaster", "mutationtaster.pred");
		replace(map, "mutationtaster_pred", "mutationtaster.pred");
		replace(map, "mutationtaster_score", "mutationtaster.score");
		replace(map, "apogee", "apogee.pred");
		replace(map, "apogee_score", "apogee.score");

		replace(map, "ncc_class", "ncc.class");
		replace(map, "ncc_interpret", "ncc.interpret");
		replace(map, "ncc_sample", "ncc.sample");
		replace(map, "provean", "provean.pred");
		replace(map, "provean_pred", "provean.pred");
		replace(map, "provean_score", "provean.score");
		replace(map, "sift", "sift.pred");
		replace(map, "sift_pred", "sift.pred");
		replace(map, "sift_score", "sift.score");
		replace(map, "polyphen_pred", "polyphen.pred");
		replace(map, "polyphen_score", "polyphen.score");
		replace(map, "polyphen2", "polyphen2.score");
		replace(map, "polyphen2_hdiv_pred", "polyphen2.hdiv.pred");
		replace(map, "polyphen2_hdiv_score", "polyphen2.hdiv.score");
		replace(map, "polyphen2_hvar_pred", "polyphen2.hvar.pred");
		replace(map, "polyphen2_hvar_score", "polyphen2.hvar.score");

		replace(map, "mgi_mouse_gene", "mgi_mouse.gene");
		replace(map, "mgi_mouse_phenotype", "mgi_mouse.phenotype");

		replace(map, "mim_disease", "mim.disease");
		replace(map, "mim_inheritance", "mim.inheritance");
		replace(map, "mim_phenotype_id", "mim.phenotype_id");
		return map;
	}
	private static final ObjectMapper OM = new ObjectMapper();
	private Map<String, String> normalize(Map<String, String> map) {
		toLong(map, "clinvar.id");
		toLong(map, "hgmd.pmid");
		toLong(map, "pos");
		normalizeArray(map, "clinvar.clndbn");
		normalizeArray(map, "exon");
		normalizeArray(map, "exon_in_hgmd");
		normalizeArray(map, "hgvsc");
		normalizeArray(map, "hgmd.hgvsc");
		normalizeArray(map, "hgvsp");
		normalizeArray(map, "hgmd.hgvsp");
		if(map.containsKey("mim.phenotype_id")) {
			String prev =  map.get("mim.phenotype_id");
			if(prev != null) map.put("mim.phenotype_id", prev.substring(0, prev.length()-1));	// Remove last semicolon;
		}
		normalizeArray(map, "mim.phenotype_id", ";");
		normalizeArray(map, "mim.inheritance");
		normalizeArray(map, "pathway(kegg)_full", ";");
		normalizeArray(map, "so_term", "&");
		normalizeArray(map, "go.biological_process", ";");
		normalizeArray(map, "go.cellular_component", ";");
		normalizeArray(map, "go.molecular_function", ";");
		return map;
	}
	private void replace(Map<String, String> map, String key, String replace) {
		if(map.containsKey(key)) {
			map.put(replace, map.get(key));
			map.remove(key);
		}
	}
	private void normalizeArray(Map<String, String> map, String key, String tokenizer) {
		if(map.containsKey(key)) {
			String prev = map.get(key);
			if(prev == null) return;
			try {map.put(key, OM.writeValueAsString(toArray(prev, tokenizer)));} catch(Exception ignore) {}
		}
	}
	private void normalizeArray(Map<String, String> map, String key) {
		normalizeArray(map, key, "\\|");
	}
	private void toLong(Map<String, String> map, String key) {
		if(map.containsKey(key)) {
			String prev =  map.get(key);
			if(prev == null) return;
			try {map.put(key, String.valueOf((long)Double.parseDouble(prev)));} catch(Exception ignore){}
		}
	}
	private String[] toArray(String str, String token) {
		return Arrays.stream(str.split(token, -1)).map(String::trim).map(a->".".equals(a)?"":a).toArray(String[]::new);
	}
	private String cellValue(String value) {
		if(".".equals(value)) return null;
		else return value;
	}
}
