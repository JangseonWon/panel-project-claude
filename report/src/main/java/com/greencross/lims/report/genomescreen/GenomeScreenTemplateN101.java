package com.greencross.lims.report.genomescreen;

import lombok.Getter;
import lombok.experimental.Accessors;

import static com.greencross.lims.report.genomescreen.GenomeScreenTemplate.Tier.Tier1;
import static com.greencross.lims.report.genomescreen.GenomeScreenTemplateN101.GeneN101.*;

public interface GenomeScreenTemplateN101<R extends GenomeScreenResource> extends GenomeScreenTemplate<R> {
	@Getter
	@Accessors(fluent = true)
	enum DiseaseN101 implements Disease {
		유전성_유방암_난소암_증후군(DiseaseSubN101.유전성_유방암_난소암_증후군),
		리_프라우메니_증후군(DiseaseSubN101.리_프라우메니_증후군);
		private DiseaseSub[] subs;
		DiseaseN101(DiseaseSubN101... diseaseSubs) {
			this.subs = diseaseSubs;
		}
	}
	@Override
	default String diseaseNameScientific(Disease disease) {
		if(disease instanceof DiseaseN101) switch ((DiseaseN101)disease) {
			case 유전성_유방암_난소암_증후군:	return "Hereditary Breast and Ovarian Cancer";
			case 리_프라우메니_증후군: 		return "Li–fraumeni syndrome";
			default:						return null;
		}
		return null;
	}
	@Getter
	@Accessors(fluent = true)
	enum DiseaseSubN101 implements DiseaseSub {
		유전성_유방암_난소암_증후군(BRCA1, BRCA2),
		리_프라우메니_증후군(TP53);
		private GeneN101[] genes;
		DiseaseSubN101(GeneN101... genes) {
			this.genes = genes;
		}
	}
	@Getter
	@Accessors(fluent = true)
	enum GeneN101 implements Gene {
		BRCA1(Tier1, "NM_007294.3"),
		BRCA2(Tier1, "NM_000059.3"),
		TP53(Tier1, "NM_000546.5");
		private Tier tier;
		private String transcript;
		GeneN101(Tier tier, String transcript) {
			this.tier = tier;
			this.transcript = transcript;
		}
	}
	@Override
	default String diseaseSubNameScientific(DiseaseSub diseaseSub) {
		if(diseaseSub instanceof DiseaseSubN101) switch ((DiseaseSubN101)diseaseSub) {
			case 유전성_유방암_난소암_증후군: 					return "Breast cancer, Ovarian cancer";
			case 리_프라우메니_증후군:	return "Breast cancer, Brain tumor, Leukemia,\nAdrenocortical carcinoma etc.";
			default:							return null;
		}
		return null;
	}
	@Override
	default DiseaseN101[] diseases() {
		return DiseaseN101.values();
	}
	@Override
	default String clinicalReference(Gene gene) {
		return null;
	}
}
