package com.greencross.lims.report.panel.enus;

import com.gcgenome.lims.test.panel.TestInfo;
import com.greencross.lims.report.builder.LogoType;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class HereditaryTemplateEnUsON001 extends HereditaryTemplateEnUs {
	private final HereditaryResourceEnUsON001 resource;
	public HereditaryTemplateEnUsON001(HereditaryResourceEnUsON001 resource, TestInfo testInfo, LogoType logoType) {
		super(testInfo, logoType);
		this.resource = resource;
	}
	private final String lblCategory = "Breast & Ovarian";
	private final String lblTitle = testInfo().name();
	@Override
	public HereditaryResourceEnUs resource() {
		return resource;
	}

	@Override
	CancerType[] cancerTypes() {
		return CancerTypeON001.values();
	}
	@Getter
	@Accessors(fluent = true)
	private enum CancerTypeON001 implements CancerType {
		BREAST("Hereditary Breast and Ovarian Cancer","For breast cancer, 5% to 20% of cases are caused by a genetic variation that one has since birth. The major cause of most hereditary breast cancers is pathogenic variants of the BRCA1 and BRCA2 genes. When there is a pathogenic variant of these genes, a maximum of 80% of those women have breast cancer and a maximum of 40% of those women have ovarian cancer. However not all hereditary breast and ovarian cancer have BRCA1 or BRCA2 pathogenic variants. The incidence rate of breast cancer and ovarian cancer can increase due to pathogenic variants in other genes as well such as tumor suppressor genes and DNA repair genes.",
				"Hereditary breast cancers are usually autosomal dominant and associated with pathogenic variants of the BRCA1 and BRCA2 genes. BRCA1 and BRCA2, identified in 1994 and 1995, respectively, are considered to be one of the most important factors for the pathogenesis of breast cancers because the incidence risk of breast cancer is significantly increased with pathogenic variants of BRCA1 and BRCA2. BRCA1 and BRCA2 are located on the long arm of chromosome 17 and chromosome 13, respectively. In case of DNA damage, these genes, reacting with other proteins, play a role in repairing the damaged double strand DNA. If BRCA1 or BRCA2 is damaged, the DNA repair process is not properly performed, and therefore, the risk of cancer increases. Pathogenic variants of the BRCA genes are autosomal dominantly inherited and the same genetic disorders have a 50% probability of occurring in the patient’s offsprings and other family members. However, not all hereditary breast cancer have BRCA1 or BRCA2 pathogenic variants. Some tumor suppressor genes and DNA repair genes are also associated with increased risk of breast cancer. Pathogenic variants in the ATM, CDH1, CHEK2, NBN, NF1 and PALB2 genes increase breast cancer risk. Depending on the positive genetic results, breast cancer screening through MRI mammography is recommended for those in 30 to 40 years of age. Therefore, when a pathogenic variant is found in a patient, the patient's offsprings and family members in close relationships are recommended to have genetic testing.");
		private final String title;
		private final String information;
		private final String feature;
		private Gene[] genes = GeneON001.values();
		CancerTypeON001(String title, String information, String feature) {
			this.title = title;
			this.information = information;
			this.feature = feature;
		}
	}
	@Getter
	@Accessors(fluent = true)
	enum GeneON001 implements Gene {
		ATM("ATM", "Breast cancer, Ovarian cancer"),
		BARD1("BARD1", "Breast cancer"),
		BRCA1("BRCA1", "Breast cancer, Ovarian cancer"),
		BRCA2("BRCA2", "Breast cancer, Ovarian cancer"),
		BRIP1("BRIP1", "Ovarian cancer"),
		CDH1("CDH1", "Breast cancer"),
		CHEK2("CHEK2", "Breast cancer"),
		NBN("NBN", "Breast cancer"),
		NF1("NF1", "Breast cancer"),
		PALB2("PALB2", "Breast cancer"),
		PPM1D("PPM1D", "Breast cancer, Ovarian cancer"),
		PTEN("PTEN", "Breast cancer"),
		RAD51C("RAD51C", "Ovarian cancer"),
		STK11("STK11", "Breast cancer, Ovarian cancer"),
		TP53("TP53", "Breast cancer");
		private final String symbol;
		private final String disease;
		GeneON001(String name, String disease) {
			this.symbol = name;
			this.disease = disease;
		}
	}
	private final String[] references = new String[] {
			"Breast Cancer Information Core (http://research.nhgri.nih.gov/bic/)",
			"The Human Gene Mutation Database (http://www.hgmd.cf.ac.uk)",
			"GeneReviews (https://www.ncbi.nlm.nih.gov/books/NBK1116/)"
	};
}
