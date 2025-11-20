package com.greencross.lims.report.genomescreen.enus;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.greencross.lims.report.genomescreen.GenomeScreenTemplateN090;
import com.gcgenome.lims.test.genomescreen.TestInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.awt.*;

// 심장돌연사 지놈 스크린
@EqualsAndHashCode(callSuper = true)
@Getter
@Accessors(fluent = true)
public class GenomeScreenTemplateN090EnUs extends GenomeScreenTemplateEnUs<GenomeScreenResourceN090EnUs> implements GenomeScreenTemplateN090<GenomeScreenResourceN090EnUs> {
	private final GenomeScreenResourceN090EnUs resource;
	private final String lblSummaryTestIntroTitleInfo = "This is a brief description of cancer genome screening.";
	private final String lblSummaryInfo = "This analysis, being the latest gene analysis technique, analyses genes that can cause cancer and reviews the results of existing " +
			"research papers to provide personalized information to help people manage their health. Even if any pathogenic variant(PV) " +
			"associated with hereditary cancer is found, individuals may have no symptoms(reduced penetrance). However, such individual " +
			"may have higher risk of cancer compared to the general population, measures to reduce the cancer risk and regular thorough " +
			"examinations for early detection are recommended.";
	private final String lblGuideTestTitle = "What is Cancer Genome Screen?";
	private final TextBlock[] guideTest;
	private final String lblGuideDiseaseTitle = "Hereditary cancer diseases have these characteristics.";
	private final TextBlock[] guideDisease;
	private final String lblGeneListInfo = "Cancer genome screen tests following diseases and genes according to the recommendation of the American College of Medical Genetics and Genomics (ACMG).";
	private final TextBlock[] limitations;
	private final TextBlock[] references;
	public GenomeScreenTemplateN090EnUs(GenomeScreenResourceN090EnUs resource, TestInfo testInfo) {
		super(testInfo);
		this.resource = resource;
		TextStyle textStyle = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8).paragraph(true);
		guideTest = new TextBlock[] {
				new TextBlock(textStyle.clone().fonts(resource.fontHeader(), resource.fontDefault()).color(Color.BLACK), "Hereditary cancer"),
				new TextBlock(textStyle, " refers to a cancer caused by an abnormality in the gene associated with a tumor occurrence, the oncogene or " +
						"the tumor suppressor gene. About 5~10% of all cancers are known to be hereditary cancers. " +
						"Early diagnosis through genetic testing is important in hereditary cancers because they can occur earlier than non-hereditary " +
						"cancers and can lead to cancer in many organs. " +
						"Cancer Genome Screen is a test that can be expected to prevent, diagnose early, and improve the treatment effects regarding " +
						"hereditary cancer by examining 35 genes known to increase the risk of developing various cancers, including breast cancer, ovarian " +
						"cancer, colon cancer, prostate cancer, pancreatic cancer, and thyroid cancer, with NGS test.")
		};
		guideDisease = new TextBlock[] {
				new TextBlock(textStyle, "Hereditary cancer is caused by a pathogenic variant (PV) of a gene known to cause certain cancers. Hereditary cancers may" +
						"have different genes for different types of cancers, and abnormalities in more than one gene can cause various cancers."),
				new TextBlock(textStyle, "Even if pathogenic variant(PV) is found in genes related to hereditary cancer, cancer does not occur 100% (Reduced " +
						"Penetrance). However, it is important to be aware of and prevent it because the risk of cancer is very high compared to the " +
						"general population. In particular, regular thorough examinations for early detection are recommended by identifying types and " +
						"risks of cancer with high incidence."),
				new TextBlock(textStyle, "Hereditary cancers account for 5-10% of all cancers, and if more than one family member is diagnosed with cancer, the risk of " +
						"developing cancer at a young age or simultaneously in multiple organs increases."),
				new TextBlock(textStyle, "Even if genetic testing related to hereditary cancer does not identify any disease-related PV, there is still a possibility of cancer " +
						"occurrence due to non-hereditary causes such as environmental effects and lifestyle.")
		};
		limitations = new TextBlock[] {
				new TextBlock(textStyle, "Genetic variation is divided into five categories, pathogenic variant (PV), likely pathogenic variant (LPV), variant of unknown significance (VUS), likely benign variant (LBV), and benign variant (BV), according to 2015 ACMG/AMP."),
				new TextBlock(textStyle, "The disease relevance of sequence variation according to 2015 ACMG/AMP guidelines is analyzed by a specialist in the department of laboratory medicine, combining various evidence such as allele frequency in population databases, frequency, function analysis and computer prediction, and papers and mutation databases. However, the interpretation of the variation could be changed as additional evidence builds up after the results are reported."),
				new TextBlock(textStyle, "In this test, it is a rule to report mainly pathogenic variant and likely pathogenic variant, which have high or very high disease relevance, and not to report variant of unknown significance, likely benign variant, and benign variant. But in some genes (RET, SDHAF2), it is a rule to report only well-known PVs."),
				new TextBlock(textStyle, "Among the genes included in the test, the MUTYH gene is inherited as an autosomal recessive gene. For autosomal recessive genes, two PVs must exist to increase the possibility of cancer. Therefore, for MUTYH, it is a rule to report only when two PVs are present."),
				new TextBlock(textStyle, "The genes included in the test include the entire exon, but in some areas sequencing may not be sufficiently covered. In addition, if a highly homologous sequence exists, the sequencing of the base may not be accurate, and variations in large deletions or duplications or non-protein-coding sequence areas may be difficult to detect.")
		};
		TextStyle referenceStyle = new TextStyle().color(resource.colorText()).fonts(resource.fontScientific(), resource.fontText(), resource.fontDefault()).fontSize(8).paragraph(true);
		references = new TextBlock[] {
				new TextBlock(referenceStyle, "Breast Cancer Information Core(http://research.nhgri.nih.gov/bic/)"),
				new TextBlock(referenceStyle, "The Human Gene Mutation Database(http://www.hgmd.cf.ac.uk)"),
				new TextBlock(referenceStyle, "GeneReviews(https://www.ncbi.nlm.hih.gov)")
		};
	}
	private String diseaseName(DiseaseN090 disease) {
		switch (disease) {
			case 유전성_유방암_난소암_증후군:	return "Hereditary Breast and Ovarian Cancer";
			case 유방암_감수성:				return "Breast Cancer, Susceptibility";
			case 난소암_감수성:				return "Ovarian Cancer, Susceptibility";
			case 전립선암_감수성:				return "Prostate Cancer, Susceptibility";
			case 리_프라우메니_증후군:			return "Li-fraumeni syndrome";
			case 유전성_미만성_위암:			return "Herediatry Diffuse Gastric Cancer";
			case 포이츠_제거스_증후군:			return "Peutz Jeghers syndrome";
			case 린치_증후군:				return "Lynch syndrome";
			case 용종증_증후군:				return "Polyposis syndrome";
			case 폰히펠_린다우_증후군:			return "Von hippel-lindau syndrome";
			case 다발성_내분비선종증:			return "Multiple endocrine neoplasia";
			case PTEN_과오종_증후군:			return "PTEN hamartoma tumor syndrome";
			case 망막모세포종:				return "Retinoblastoma";
			case 유전성_부신경절종_갈색세포종:	return "Hereditary paraganglioma pheochromocytoma syndrome";
			case 결절성_경화증:				return "Tuberous sclerosis complex";
			case WT1_연관_윌름스_종양:			return "WT1-related wilms tumor";
			case 제2형_신경섬유종증:			return "Neurofibromatosis type 2";
			default:						return "";
		}
	}
	private String diseaseInfo(DiseaseN090 disease) {
		switch (disease) {
			case 유전성_유방암_난소암_증후군:	return "Hereditary diseases that cause breast and ovarian cancer due to the abnormalities in the BRCA1 and BRCA2 gene.";
			case 유방암_감수성:				return "Genes related to breast cancer other than BRCA1 and BRCA2(When pathogenic variants are found in males, it is recommended to genetic tests on female immediate family members.)";
			case 난소암_감수성:				return "Genes related to ovarian cancer other than BRCA1 and BRCA2(When pathogenic variants are found in males, it is recommended to genetic tests on female immediate family members.)";
			case 전립선암_감수성:				return "Genes related to prostate cancer other than BRCA1 and BRCA2(When pathogenic variants are found in females, please refer to the interpretation of results for breast cancer susceptibility or Lynch syndrome.)";
			case 리_프라우메니_증후군:			return "Familial cancer diseases that are inherited as autosomal dominant due to the abnormalities in TP53.";
			case 유전성_미만성_위암:			return "Autosomal hereditary disease in which gastric cancer is produced under the gastric mucosa due to an abnormality of the CDH1 gene.";
			case 포이츠_제거스_증후군:			return "Hereditary diseases that show multiple hamartomatous polyposis in the digestive tract and melanin pigmentation on the skin mucosa.";
			case 린치_증후군:				return "Diseases that show a risk to several cancers due to genetic abnormalities of the DNA repairing system.";
			case 용종증_증후군:				return "Hereditary diseases associated with developing multiple polyps in the stomach, colon, and rectum.";
			case 폰히펠_린다우_증후군:			return "Hereditary diseases that cause malignant and benign tumors, especially in the central nervous system and kidneys.";
			case 다발성_내분비선종증:			return "Hereditary diseases of in the endocrine system, such as thyroid glands, parathyroid glands, intestinal and pancreatic neuroendocrine system, anterior pituitary, and skin.";
			case PTEN_과오종_증후군:			return "Hamartoma of various organ associated with PTEN gene abnormalities.";
			case 망막모세포종:				return "Diseases in which primary malignant tumors occur in the optic nerve cells of the retina, mostly in infants and babies.";
			case 유전성_부신경절종_갈색세포종:	return "Endocrine diseases that indicate pheochromocytoma in the adrenal gland, paraganglioma and neuroendocrine tumor.";
			case 결절성_경화증:				return "Hereditary diseases in which tumors in the central nervous system and various body parts, associated with mental retardation, epilepsy, and skin lesions, appear.";
			case WT1_연관_윌름스_종양:		return "Diseases that can accompany congenital abnormality along with malignant tumors in the kidneys.";
			case 제2형_신경섬유종증:			return "Diseases in which a benign tumor occurs in the acoustic nerve, a type of brain nerve.";
			default:						return "";
		}
	}
	private String diseaseSubName(DiseaseSubN090 diseaseSub) {
		switch (diseaseSub) {
			case 유전성_유방암_난소암_증후군:			return "Breast cancer, Ovarian cancer, Prostate cancer";
			case 유방암_감수성:						return "Breast cancer";
			case 난소암_감수성:						return "Ovarian cancer";
			case 전립선암_감수성:						return "Prostate cancer";
			case 리_프라우메니_증후군:					return "Breast cancer, Brain tumor, leukemia,\nAdrenocortical carcinoma etc.";
			case 위암:								return "Gastric Cancer";
			case 포이츠_제거스_증후군:					return "Colorectal cancer, Gastric cancer";
			case 린치_증후군:						return "Colorectal cancer, Endometrial cancer, Gastric cancer, Ovarian cancer etc.";
			case 가족성_선종성_용종증:					return "Familial adenomatous polyposis";
			case MUTYH_연관_용종증:					return "MUTYH-associated polyposis";
			case 연소성_용종증_증후군:					return "Juvenile polyposis syndrome";
			case 폰히펠_린다우_증후군:					return "CNS hemangioblastoma,\nRetinal hemangioblastoma, Pheochromocytoma";
			case 제1형_다발성_내분비선종증:			return "Multiple Endocrine Neoplasia Type 1";
			case 제2형_다발성_내분비선종증:			return "Multiple Endocrine Neoplasia Type 2";
			case PTEN_과오종_증후군:					return "Breast cancer, Thyroid cancer";
			case 망막모세포종:						return "Retinoblastoma";
			case 유전성_부신경절종_갈색세포종:			return "Paraganglioma, Pheochromocytoma";
			case 결절성_경화증:						return "Retinal tumor, Brain tumor, Lung lymphoma etc.";
			case WT1_연관_윌름스_종양:				return "Renal cell carcinoma";
			case 제2형_신경섬유종증:					return "Acoustic Neuroma";
			default:								return "";
		}
	}
	@Override
	public String diseaseName(Disease disease) {
		if(disease instanceof DiseaseN090) return diseaseName((DiseaseN090) disease);
		return null;
	}

	@Override
	public String diseaseInfo(Disease disease) {
		if(disease instanceof DiseaseN090) return diseaseInfo((DiseaseN090) disease);
		return null;
	}

	@Override
	public String diseaseSubName(DiseaseSub diseaseSub) {
		if(diseaseSub instanceof DiseaseSubN090) return diseaseSubName((DiseaseSubN090) diseaseSub);
		return null;
	}
	@Override
	public String diseaseSubInfo(DiseaseSub diseaseSub) {
		if(diseaseSub instanceof DiseaseSubN090) switch ((DiseaseSubN090) diseaseSub) {
			case 유전성_유방암_난소암_증후군:			return "For breast cancer, 5% to 20% of cases are caused by a genetic variation which one has since birth. The major cause of most hereditary breast cancer sare pathogenic variants of the BRCA1 and BRCA2 genes";
			case 유방암_감수성:						return "About 5-20% of breast cancers are caused by genetic abnormalities that have been inherited from Parents. Most of hereditary breast cancers are caused by pathogenic mutations in the BRCA1 and BRCA2 genes, but the incidence rate of breast cancer can increase due to mutations in other genes as well. This is called breast cancer susceptibility gene.";
			case 난소암_감수성:						return "Most of hereditary Ovarian cancers are caused by pathogenic mutations in the BRCA1 and BRCA2 genes. However the incidence of breast cancer can also be increased by mutations in other genes, which are called Ovarian cancer susceptibility genes.";
			case 전립선암_감수성:						return "About 9% of prostate cancer cases are caused by heredity. Hereditary prostate cancer is mainly caused by pathogenic mutations in DNA repair genes, which are called prostate cancer susceptibility genes. Hereditary prostate cancer occurs relatively early compared to common prostate cancer, especially before the age of 55.";
			case 리_프라우메니_증후군:					return "When one is born with a pathogenic variant of the TP53 gene, it causes Li-Fraumeni syndrome, This is characterized by sarcoma, brain tumor, leukemia and adrenocortical carcinoma which develop in childhood and breast cancer which develops before menopause.";
			case 위암:								return "Diffuse gastric cancer is a differentiated adenocarcinoma that infiltrates into the stomach wall without forming a distinct mass. It is characterized by asymptomatic and rapid progression, and is often detected in stages 3 and 4. It is also easily metastasized to other sites such as blood vessels and lymph nodes. Hereditary diffuse gastric cancer is an autosomal dominant inherited disease caused by pathogenic variants in the CDH1 gene. The average age of onset of hereditary diffuse gastric cancer (HDGC) is 38 years.";
			case 포이츠_제거스_증후군:					return "Peutz Jeghers syndrome is inherited in an autosomal dominant fashion. This disease forms gastrointestinal hamartoma and is characterized by pigmentation in the skin’s mucous membranes. Gastrointestinal hamartoma is most common in the small intestine (78%), but also occurs in the stomach (38%) and large intestine (20-40%). Although rare, hamartoma also occurs in the nasal cavity, bronchus and urinary system. Even though it is not common for gastrointestinal hamartoma to develop into a malignant tumor, but it is known to have adenomatous changes and progresses to glandular cancer, and there are cases reported that families with this disease have many cases of colorectal cancer. Other than colorectal cancer, gastric cancer, breast cancer, cervical cancer, ovarian cancer, testicular cancer, and pancreatic cancer also occur commonly. Lentigos spread on lips and dark brown spots appear on the palms of the hands and soles of the feet.";
			case 린치_증후군:						return "Hereditary non-polyposis colorectal cancer accounts for 5-10% of total colorectal cancers and causes other cancers in various organs. When there is a family history of colorectal cancer, or when colorectal cancer develops under the age of 50, this disease can be suspected. The cause is a pathogenic variant in the MLH1, MSH2 and MSH6 genes, and it is inherited in an autosomal dominant fashion. When there is a corresponding genetic abnormality, the risk of colorectal cancer is 50-80% and that of endometrial cancer is 25-60%. Also, the risk of gastric cancer (6-13%) and ovarian cancer (4-12%) increases. Therefore, regular check-ups should be conducted for the large intestine, stomach, female reproductive system and urinary system.";
			case 가족성_선종성_용종증:					return "Familial adenomatous polyposis (FAP) is a disease that causes the occurrence of hundreds to tens of thousands of adenomas in the large intestine and rectum, and about one out of approximately every 7,000 people contracts the disease. The cause is a pathogenic variant of the APC gene, and it is inherited in an autosomal dominant fashion. Unless the occurrence area is removed, polyps develop as malignant tumors, and when a person who has this variant enters their 40s, almost 100% of colorectal cancer occurs in the polyps. Not only colorectal cancer, but also other cancers such as the small intestine cancer, gastric cancer and duodenal cancer may occur. If a pathogenic variant is found, prophylactic proctocolectomy should be conducted.";
			case MUTYH_연관_용종증:					return "MUTYH-associated polyposis is the most important disease that requires differential diagnosis to familial adenomatous polyposis. This disease is accompanied by 20 to several hundred adenomas and has a highly increased risk of large intestine cancer. The risks of duodenal cancer, ovarian cancer, bladder cancer, skin cancer, breast cancer and endometrial cancer are also increased, although these cancers occur in relatively later age than polyps. The average age of diagnosis is 45. There are no standard guidelines for follow-up and prevention for patients with MUTYH-associated polyposis. In Europe, prevention and treatment guidelines similar to those for attenuated familial adenomatous polyposis are applied.";
			case 연소성_용종증_증후군:					return "Juvenile Polyposis Syndrome (JPS) is the growth of polyps on the inner walls of the stomach. The polyps may occur from the stomach to the rectum, but rarely in the small intestine. It is reported mainly before and after 20s. Various sizes and shapes of polyps are found from several to more than 100.";
			case 폰히펠_린다우_증후군:					return "Von Hippel-Lindau syndrome (VHL) is a disease characterized by malignant and benign tumors that occur in various organs, especially the central nervous system and kidney. Angiomas of the retina, hemangiomas of the central nervous system, renal and pancreatic cysts and carcinomas, adrenal pheochromocytoma, cystadenoma papillare of the epididymis and liver and spleen and lung cysts may occur, and more than half of patients die of hemangiomas of the central nervous system. It is inherited in an autosomal dominant fashion with the incidence of 1 in about 36,000. The mean life span is about 54, because the frequency of tumors in various organs increases as patients age. In most cases, causes of death are central nerve blood vessel blastoma and renal cell carcinoma.";
			case 제1형_다발성_내분비선종증:			return "Multiple endocrine neoplasia type 1 is a tumor syndrome that develops tumors in parathyroid glands, pancreatic nerve endocrine system, anterior pituitary and skin. The most common endocrine tumor is the parathyroid gland tumor that causes hyperparathyroidism and hypercalcemia. Moreover, gastrinoma, insulinoma, prolactinoma and carcinoid tumors also may occur. Cutaneous neoplasm is common but easy to miss. Tumors that occur in skin are mutipleangiofibroma, collagenoma and lipoma. Finding these benign tumors is important in diagnosis because they are helpful as a marker to predict multiple endocrine neoplasia type 1.";
			case 제2형_다발성_내분비선종증:			return "Multiple endocrine neoplasia is a tumor that is developed in various endocrine glands such as thyroid, parathyroid glands and adrenal glands, and type 2 can be classified into MEN2A, MEN2B and familial medullary thyroid cancer based on clinical feature. All three subtypes have a risk of medullary thyroid cancer. About 20% of medullary thyroid cancer is hereditary cancer. The cause is a pathogenic variant of the RET gene, and it is inherited in an autosomal dominant fashion. Medullary thyroid cancer is just 1-3% of all thyroid cancers, but it is known that lymphatic metastasis and hematogenous spread are common during diagnosis, and the prognosis is worse. If one has a pathogenic variant of the RET gene, medullary thyroid cancer occurs in almost 100% of cases.";
			case PTEN_과오종_증후군:					return "PTEN hamartoma tumor syndrome is characterized by malformation such as benign tumors (hamartoma) and an increase in the risk of cancer (especially breast cancer and thyroid cancer). Symptoms vary according to detailed diseases and patients. This syndrome may occur in every age. All patients have skin lesions, and lesions may occur in various other body parts. The breast (70%), thyroid (68%) and urinary system (60%) are commonly invaded, but lesions also occur in the eyes, nerve system, respiratory system, cardiovascular system and, although rare, skeleton system. There have been many malignant changes in a lot of lesions. Malignant degeneration can occur in skin diseases, but mainly occurs in the breasts and thyroid. Breast disease shows malignant degeneration in 36%, and the thyroid shows various changes from enlarged thyroid, which is simply a sudden growth of the thyroid, to adenoma and cancer. In rare cases, cancers occur in the endometrium and cervix. Alimentary tract lesions are reported in 50-70% of all patients. They occur more in the large intestine than small intestine, especially in the sigmoid colon or rectum. Adenomas account for about 25% of large intestine polyps that may cause symptoms like bleeding and recurrent diarrhea, but it is rarely expressed in cancer, and it is not clear whether the malignant degeneration is caused by the PTEN hamartoma tumor syndrome.";
			case 망막모세포종:						return "Retinoblastoma is a childhood eye tumor that occurs in the retina. Although this tumor mainly occurs before 2 years of age, it may occur at any age. The most common clinical aspects are leukokoria (white reflection from the pupil). In the early stage, leukokoria can be only seen from a certain angle and under certain light or by the flash of a camera. There may be other symptoms such as redness in irises, hypopyon, hyphema, macrophthalmia, orbital cellulitis and exophthalmos. Retinoblastoma may be classified according to various aspects, but clinically, it is commonly divided into hereditary and non-hereditary. Whether it is hereditary is an important factor to find retinoblastoma early and treat it properly, because it is crucial in prediction of the same tumor occurring in the family as well as in treatment and follow-up of patients.";
			case 유전성_부신경절종_갈색세포종:			return "Hereditary non-polyposis colorectal cancer accounts for 5-10% of all colorectal cancers and causes other cancers in various organs. When there is a family history of colorectal cancer, or when colorectal cancer develops under the age of 50, this disease can be doubted. The cause is a pathogenic variant in the MLH1, MSH2 and MSH6 genes, and it is inherited in an autosomal dominant fashion. When there is a corresponding genetic abnormality, the risk of colorectal cancer is 50-80% and that of endometrial cancer is 25-60%. Also, the risk of gastric cancer (6-13%) and ovarian cancer (4-12%) increases. Therefore, regular check-ups should be conducted for the large intestine, stomach, female reproductive system and urinary system.";
			case 결절성_경화증:						return "Tuberous sclerosis complex is a congenital disorder characterized by symptoms such as mental retardation, epilepsy and skin lesions. It is a rare disease that occurs in 1 in 6,000-9,000 people. This disease requires long-term observation because it may invade various body parts such as the central nervous system, heart, kidney, eyes, skin and teeth and cause multiple symptoms and tumors. This is a difficult disease that requires mobilization of experts in various areas because of its complex related symptoms.";
			case WT1_연관_윌름스_종양:				return "Wilms tumor, which occurs in the kidney, is the most common tumor in children. The causes of Wilms tumor vary genetically. It has been reported that pathogenic variants of the WT1 gene occur in 10~15% of Wilms tumor cases. About 50 cases of Wilms tumor are reported every year in Korea. Among them, about 80% occur at the age of 5 or less and most commonly at 2-4 years. The symptoms of Wilms tumor are gastrointestinal disturbance such as abdominal pain, vomiting and nausea, and hematuria is often seen. Wilms tumor mostly occurs in children, but when it occurs in adults, the prognosis is often poor. Compared to neuroblastoma that often occur in the abdominal cavity, patients with Wilms tumor have a slightly higher age of onset and less systematic symptoms. Patients may complain of pain when it spreads to bones.";
			case 제2형_신경섬유종증:					return "Neurofibromatosis type II is a rare disease that forms a benign tumor (bilateral acoustic neuroma) in the auditory nerve, which is a cranial nerve, from the inner ear. In addition, the possibility of other tumors, such as neuroma, meningioma and neuroglioma, increases in patients with neurofibromatosis type II. The symptoms usually appear in childhood, adolescence or early adulthood. If benign tumors occur in the auditory nerve, impairments in balance and walking, vertigo, throbbing headache, tingling, pain and tinnitus (ringing in the ear) occur, and the patient will gradually lose hearing. These symptoms vary according to the size and location of the acoustic neuroma. Patients may also experience facial spasm, overall muscle weakness and difficulty in swallowing food and talking. Pain and partial paralysis may also occur. About 85% of patients have cataracts. Unlike neurofibromatosis type I, skin lesions in cafe-au-lait color are not common. If there is a genetic abnormality, bilateral acoustic neuroma occurs in about 75%, accompanied by other brain tumors in 50%. Other benign tumors can also occur in the central nervous system.";
			default:								return "";
		}
		return null;
	}
	@Override
	public String diseaseSubReference(DiseaseSub diseaseSub) {
		if(diseaseSub instanceof DiseaseSubN090) switch ((DiseaseSubN090) diseaseSub) {
			case 유전성_유방암_난소암_증후군:		return "03. Prevention and Treatment page12~13, 17~18";
			case 유방암_감수성:					return "03. Prevention and Treatment page14, 17~18";
			case 난소암_감수성:					return "03. Prevention and Treatment page15, 17~18";
			case 전립선암_감수성:					return "03. Prevention and Treatment page16~18";
			case 리_프라우메니_증후군:				return "03. Prevention and Treatment page19~21";
			case 위암:							return "03. Prevention and Treatment page22~23";
			case 포이츠_제거스_증후군:				return "03. Prevention and Treatment page24~25";
			case 린치_증후군:					return "03. Prevention and Treatment page26~29";
			case 가족성_선종성_용종증:				return "03. Prevention and Treatment page30~31";
			case MUTYH_연관_용종증:				return "03. Prevention and Treatment page32~33";
			case 연소성_용종증_증후군:				return "03. Prevention and Treatment page34~35";
			case 폰히펠_린다우_증후군:				return "03. Prevention and Treatment page36~37";
			case 제1형_다발성_내분비선종증:		return "03. Prevention and Treatment page38~39";
			case 제2형_다발성_내분비선종증:		return "03. Prevention and Treatment page40~42";
			case PTEN_과오종_증후군:				return "03. Prevention and Treatment page43~45";
			case 망막모세포종:					return "03. Prevention and Treatment page46~47";
			case 유전성_부신경절종_갈색세포종:		return "03. Prevention and Treatment page48~49";
			case 결절성_경화증:					return "03. Prevention and Treatment page50~51";
			case WT1_연관_윌름스_종양:			return "03. Prevention and Treatment page52~53";
			case 제2형_신경섬유종증:				return "03. Prevention and Treatment page54~55";
		}
		return null;
	}
	@Override
	public String clinicalReference(Gene gene) {
		if(gene instanceof GeneN090) switch((GeneN090) gene) {
			case BMPR1A:
			case MUTYH:
			case MSH6:
			case PMS2:
			case SDHC:
			case WT1:		return "MedlinePlus";
			case PALB2:		return "Clinical Cancer Research. 2008;14(18):5931–7";
			case SDHAF2:	return "UniProt";
			default:		return null;
		}
		return null;
	}
	@Override
	public String clinicalMeaning(Gene gene) {
		if(gene instanceof GeneN090) switch((GeneN090)gene) {
			case BMPR1A:	return "More than 60 pathogenic variants in the BMPR1A gene have been found to cause juvenile polyposis syndrome. Most BMPR1A " +
					"pathogenic variants result in the production of an abnormally short, nonfunctional protein. As a result, the BMPR1A protein " +
					"cannot bind to ligands in the TGF-β pathway. This disruption in binding interferes with the activation of the SMAD protein " +
					"complex. This inactive complex is not transported to the nucleus, where it is needed to regulate cell growth and the activity of " +
					"certain genes. Unregulated cell growth can lead to polyp formation in people with juvenile polyposis syndrome.";
			case MSH6:		return "Pathogenic variants in the MSH6 gene have been reported in about 13 percent of families with Lynch syndrome that have an " +
					"identified pathogenic variant. Lynch syndrome increases the risk of many types of cancer, particularly colorectal cancer. People " +
					"with Lynch syndrome also have an increased risk of cancers of the endometrium (lining of the uterus), ovaries, stomach, small " +
					"intestine, gallbladder ducts, upper urinary tract, and brain. By age 75, the risk of developing one of these cancers is 60 percent " +
					"for women and 40 percent for men with an MSH6 pathogenic variant. Endometrial cancer is especially common in women with " +
					"Lynch syndrome caused by MSH6 pathogenic variants.";
			case MUTYH:		return "Pathogenic variants in the MUTYH gene cause an autosomal recessive form of familial adenomatous polyposis (also called MYH associated " +
					"polyposis). Pathogenic variants in this gene affect the ability of cells to correct errors made during DNA replication. In " +
					"individuals who have autosomal recessive familial adenomatous polyposis, both copies of the MUTYH gene in each cell are " +
					"mutated. Most pathogenic variants in this gene result in the production of a nonfunctional or low-functioning MYH glycosylase. " +
					"When base excision repair in the cell is impaired, pathogenic variants in other genes build up, leading to cell overgrowth and " +
					"possibly tumor formation.";
			case PALB2:		return "Pathogenic variants in the PALB2 gene are associated with an increased risk of developing breast cancer of magnitude similar to " +
					"that associated with BRCA2 pathogenic variants and PALB2-deficient cells are sensitive to PARP inhibitors.";
			case PMS2:		return "Pathogenic variants in the PMS2 gene have been reported in about 6 percent of families with Lynch syndrome that have an " +
					"identified pathogenic variant. Lynch syndrome increases the risk of many types of cancer, particularly colorectal cancer. People " +
					"with Lynch syndrome also have an increased risk of cancers of the endometrium (lining of the uterus), ovaries, stomach, small " +
					"intestine, liver, gallbladder ducts, upper urinary tract, and brain. By age 75, the risk of developing one of these cancers is 30 " +
					"percent for women and 25 percent for men with a PMS2 pathogenic variant. These athogenic variants lead to a form of Lynch " +
					"syndrome with a lower risk of cancer development compared to other causes of this condition. Additionally, in people with a " +
					"PMS2 pathogenic variant, cancer tends to occur at a later age compared to others with Lynch syndrome.";
			case SDHAF2:	return "Pathogenic variants in the SDHAF2 gene cause hereditary paraganglioma, a neuroendocrine tumor. Paraganglioma is a neural " +
					"crest tumor usually derived from the chemoreceptor tissue of a paraganglion, and may develop at various body sites, including " +
					"the head, neck, thorax and abdomen.";
			case SDHC:		return "More than 30 pathogenic variants in the SDHC gene have been found to increase the risk of hereditary paraganglioma-pheochromocytoma " +
					"type 3. People with this condition have paragangliomas, pheochromocytomas, or both. An inherited SDHC " +
					"pathogenic variant predisposes an individual to the condition, and a somatic pathogenic variant that deletes the normal copy of " +
					"the SDHC gene is needed to cause hereditary paraganglioma-pheochromocytoma type 3.";
			case WT1:		return "At least 80 pathogenic variants in the WT1 gene have been found to cause Denys-Drash syndrome, a condition that affects " +
					"development of the kidneys and genitalia and most often affects males. These pathogenic variants are germline, which means " +
					"they are present in cells throughout the body. The pathogenic variants that cause Denys-Drash syndrome almost always occur in " +
					"areas of the gene known as exon 8 and exon 9. Most of these pathogenic variants result in changes in single protein building " +
					"blocks (amino acids) in the WT1 protein. The most common pathogenic variant that causes Denys-Drash syndrome (found in " +
					"about 40 percent of cases) replaces the amino acid arginine with the amino acid tryptophan at protein position 394 (written " +
					"p.Arg394Trp or p.R394W).";
			default:		return null;
		}
		return null;
	}
}
