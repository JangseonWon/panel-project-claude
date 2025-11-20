package com.greencross.lims.variant.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;

import java.util.Map;

@UtilityClass
public class MapToIndexQuery {
	public IndexQuery map(Map<String, String> map, ObjectMapper om) {
		map.remove("sample");
		map.remove("genotype");
		map.remove("depth");
		map.remove("vaf");
		map.remove("filter");
		map.remove("insilico_sanger");
		map.remove("qual");
		map.remove("cnv_check");
		{
			map.remove("WES300_DB".toLowerCase());
			map.remove("Cardio466_freq".toLowerCase());
			map.remove("WES_ALSS321".toLowerCase());
			map.remove("DES_ALS158".toLowerCase());
			map.remove("DESrare809".toLowerCase());
			map.remove("WES300_hom".toLowerCase());
			map.remove("WES300_het".toLowerCase());
			map.remove("WES_ALS321_hom".toLowerCase());
			map.remove("WES_ALSS321_het".toLowerCase());
			map.remove("DES_ALS_158_hom".toLowerCase());
			map.remove("DES_ALS_158_het".toLowerCase());
			map.remove("DES_rare_809_hom".toLowerCase());
			map.remove("DES_rare_809_het".toLowerCase());
			map.remove("HGMD_DISEASE".toLowerCase());
			map.remove("HGMD_HGVSc".toLowerCase());
			map.remove("HGMD_HGVSp".toLowerCase());
			map.remove("HGMD_exon".toLowerCase());
			map.remove("HGMD&Clinvar".toLowerCase());
			map.remove("New_Effect".toLowerCase());
			map.remove("n_mis|exp_mis".toLowerCase());
			map.remove("n_lof|exp_lof".toLowerCase());
			map.remove("mis_z".toLowerCase());
			map.remove("lof_z".toLowerCase());
			map.remove("pLI".toLowerCase());
			map.remove("popfreqmax_gnomad&exac&1000g".toLowerCase());
			map.remove("popfreqmax_gnomad&krgdb".toLowerCase());
			map.remove("spliceai_dp_ag|dp_al|dp_dg|dp_dl|ds_ag|ds_al|ds_dg|ds_dl|symbol".toLowerCase());
			map.remove("PV_Count_HGMD_ClinVar".toLowerCase());
			map.remove("MAX_Freq_HGMD_ClinVar".toLowerCase());
			map.remove("MIN_Freq_HGMD_ClinVar".toLowerCase());
			map.remove("97.5pecentile_HGMD_ClinVar".toLowerCase());
			map.remove("ReviewStatus".toLowerCase());
			map.remove("InhouseDB_class".toLowerCase());
			map.remove("InhouseDB_comment".toLowerCase());
			map.remove("InhouseDB_Result".toLowerCase());
			map.remove("PopFreq_Diff_High".toLowerCase());
			map.remove("OR_Alt".toLowerCase());
			map.remove("CI_95_low".toLowerCase());
			map.remove("CI_95_high".toLowerCase());
			map.remove("P.value".toLowerCase());
			map.remove("");
		}
		try {
			String s = om.writeValueAsString(map);
			String id = IdGenerator.snvId(map);
			return new IndexQueryBuilder().withId(id).withSource(s).build();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public IndexQuery map(String id, Map<String, String> map, ObjectMapper om) {
		{
			map.remove("");
			map.remove("WES300_DB".toLowerCase());
			map.remove("Cardio466_freq".toLowerCase());
			map.remove("WES_ALSS321".toLowerCase());
			map.remove("DES_ALS158".toLowerCase());
			map.remove("DESrare809".toLowerCase());
			map.remove("WES300_hom".toLowerCase());
			map.remove("WES300_het".toLowerCase());
			map.remove("WES_ALS321_hom".toLowerCase());
			map.remove("WES_ALSS321_het".toLowerCase());
			map.remove("DES_ALS_158_hom".toLowerCase());
			map.remove("DES_ALS_158_het".toLowerCase());
			map.remove("DES_rare_809_hom".toLowerCase());
			map.remove("DES_rare_809_het".toLowerCase());
			map.remove("HGMD_DISEASE".toLowerCase());
			map.remove("HGMD_HGVSc".toLowerCase());
			map.remove("HGMD_HGVSp".toLowerCase());
			map.remove("HGMD_exon".toLowerCase());
			map.remove("HGMD&Clinvar".toLowerCase());
			map.remove("New_Effect".toLowerCase());
			map.remove("n_mis|exp_mis".toLowerCase());
			map.remove("n_lof|exp_lof".toLowerCase());
			map.remove("mis_z".toLowerCase());
			map.remove("lof_z".toLowerCase());
			map.remove("pLI".toLowerCase());
			map.remove("popfreqmax_gnomad&exac&1000g".toLowerCase());
			map.remove("popfreqmax_gnomad&krgdb".toLowerCase());
			map.remove("spliceai_dp_ag|dp_al|dp_dg|dp_dl|ds_ag|ds_al|ds_dg|ds_dl|symbol".toLowerCase());
			map.remove("PV_Count_HGMD_ClinVar".toLowerCase());
			map.remove("MAX_Freq_HGMD_ClinVar".toLowerCase());
			map.remove("MIN_Freq_HGMD_ClinVar".toLowerCase());
			map.remove("97.5pecentile_HGMD_ClinVar".toLowerCase());
			map.remove("ReviewStatus".toLowerCase());
			map.remove("InhouseDB_class".toLowerCase());
			map.remove("InhouseDB_comment".toLowerCase());
			map.remove("InhouseDB_Result".toLowerCase());
			map.remove("PopFreq_Diff_High".toLowerCase());
			map.remove("OR_Alt".toLowerCase());
			map.remove("CI_95_low".toLowerCase());
			map.remove("CI_95_high".toLowerCase());
			map.remove("P.value".toLowerCase());
		}
		try {
			return new IndexQueryBuilder().withId(id).withSource(om.writeValueAsString(map)).build();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
