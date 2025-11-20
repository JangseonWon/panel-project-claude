package com.greencross.lims.report.genomescreen.enus;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.greencross.lims.report.genomescreen.GenomeScreenTemplateN089;
import com.gcgenome.lims.test.genomescreen.TestInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.awt.*;

// 심장돌연사 지놈 스크린
@EqualsAndHashCode(callSuper = true)
@Getter
@Accessors(fluent = true)
public class GenomeScreenTemplateN089EnUs extends GenomeScreenTemplateEnUs<GenomeScreenResourceN089EnUs> implements GenomeScreenTemplateN089<GenomeScreenResourceN089EnUs> {
	private final GenomeScreenResourceN089EnUs resource;
	private final String lblSummaryTestIntroTitleInfo = "This is a brief description of Sudden Cardiac Arrest genome screening.";
	private final String lblSummaryInfo = "This analysis, being the latest gene analysis technique, analyses genes that can cause heart disease such as cardiac sudden death and reviews the results of existing research papers to provide personalized information to help people manage their health. Even if any pathogenic variant(PV) associated with hereditary heart disease is found, individuals may have no symptoms(reduced penetrance). However, such individual may have higher risk of heart disease compared to the general population, measures to reduce the heart disease risk and regular thorough examinations for early detection are recommended.";
	private final String lblGuideTestTitle = "What is Sudden Cardiac Arrest Genome Screen?";
	private final TextBlock[] guideTest;
	private final String lblGuideDiseaseTitle = "Hereditary heart diseases have these characteristics.";
	private final TextBlock[] guideDisease;
	private final String lblGeneListInfo = "Sudden Cardiac Arrest Genome Screen tests following diseases and genes according to the recommendation of the American College of Medical Genetics and Genomics (ACMG).";
	private final TextBlock[] limitations;
	private final TextBlock[] references;
	public GenomeScreenTemplateN089EnUs(GenomeScreenResourceN089EnUs resource, TestInfo testInfo) {
		super(testInfo);
		this.resource = resource;
		TextStyle textStyle = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8).paragraph(true);
		guideTest = new TextBlock[] {
				new TextBlock(textStyle.clone().fonts(resource.fontHeader(), resource.fontDefault()).color(Color.BLACK), "Sudden Cardiac Arrest"),
				new TextBlock(textStyle, " is death from an unexpected cardiac arrest, whether or not a heart disease has been diagnosed. Sudden cardiac death is " +
						"defined as death within one hour of acute cardiac arrest. The number of acute cardiac arrest cases is over 20,000 a year, resulting in " +
						"significant socio-economic losses, many of which are caused by hereditary heart diseases. Sudden Cardiac Arrest Genome Screen is a test that " +
						"can be expected to prevent, diagnose early, and improve the treatment effects regarding hereditary heart disease by examining 40 genes known to " +
						"increase the risk of developing sudden cardiac arrest or sudden cardiac death with NGS test.")
		};
		guideDisease = new TextBlock[] {
				new TextBlock(textStyle, "Even if pathogenic variant(PV) is found in genes related to hereditary heart disease, disease does not occur 100%(Reduced Penetrance). The time of onset and clinical patterns of diseases vary widely from person to person."),
				new TextBlock(textStyle, "The time of onset of hereditary heart disease may occur immediately after birth if symptoms are severe, but it usually starts in adolescence or early adulthood, and sometimes in late adulthood."),
				new TextBlock(textStyle, "Hereditary cardiomyopathy and arrhythmia related genes can usually be associated with the clinical patterns of various cardiomyopathy and arrhythmia in a single gene."),
				new TextBlock(textStyle, "Even if genetic testing related to hereditary heart disease does not identify any disease-related PV, there is still a possibility of heart disease occurrence due to non-hereditary causes such as environmental effects and lifestyle.")
		};
		limitations = new TextBlock[] {
				new TextBlock(textStyle, "Genetic variation is divided into five categories, pathogenic variant (PV), likely pathogenic variant (LPV), variant of unknown significance (VUS), likely benign variant (LBV), and benign variant (BV), according to 2015 ACMG/AMP."),
				new TextBlock(textStyle, "The disease relevance of sequence variation according to 2015 ACMG/AMP guidelines is analyzed by a specialist in the department of laboratory medicine, combining various evidence such as allele frequency in population databases, frequency, function analysis and computer prediction, and papers and mutation databases. However, the interpretation of the variation could be changed as additional evidence builds up after the results are reported."),
				new TextBlock(textStyle, "In this test, it is a rule to report mainly pathogenic variant and likely pathogenic variant, which have high or very high disease relevance, and not to report variant of unknown significance , likely benign variant, and benign variant. But in some genes (RYR2, DSP, MYH7, TNNI3, TPM1, MYL3, MYL2, PRKAG2, ACTC1, MYH7, APOB, PCSK9) it is a rule to report only well-known PVs."),
				new TextBlock(textStyle, "The genes included in the test include the entire exon, but in some areas sequencing may not be sufficiently covered. In addition, if a highly homologous sequence exists, the sequencing of the base may not be accurate, and variations in large deletions or duplications or non-protein-coding sequence areas may be difficult to detect.")
		};
		TextStyle referenceStyle = new TextStyle().color(resource.colorText()).fonts(resource.fontScientific(), resource.fontText(), resource.fontDefault()).fontSize(8).paragraph(true);
		references = new TextBlock[] {
				new TextBlock(referenceStyle, "GeneReviews(https://www.ncbi.nlm.hih.gov)"),
				new TextBlock(referenceStyle, "Online Medelian Inheritance in Man(https://www.omim.org/)"),
				new TextBlock(referenceStyle, "The Human Gene Mutation Database(http://www.hgmd.cf.ac.uk)"),
				new TextBlock(referenceStyle, "Richards. S. et al., Standards and guidelines for the interpretation of sequence variants: a joint consensus recommendation of the American College of Medical Genetics and Genomics and the Association for Molecular Pathology. Genet Med 2015;17"),
				new TextBlock(referenceStyle, "Kalia SS. et al., Recommendations for reporting of secondary findings in clinical exome and genome sequencing, 2016 update (ACMG SF v2.0): a policy statement of the American College of Medical Genetics and Genomics., Genet Med 2017 Feb; 19(2): 249-255"),
				new TextBlock(referenceStyle, "gnomAD(http://gnomad.broadinstitute.org/)")
		};
	}
	private String diseaseName(DiseaseN089 disease) {
		switch (disease) {
			case 흉부대동맥류와_박리증: 	return "Thoracic aortic aneurysms and dissections";
			case 유전성_부정맥:			return "Hereditary arrhythmia";
			case 유전성_심근병증:			return "Hereditary cardiomyopathy";
			case 고콜레스테롤혈증_및_혈전증:return "Hypercholesterolemia and Thrombophilia";
			case 동맥비틀림증후군:		return "Arterial tortuosity syndrome";
			default:					return "";
		}
	}
	private String diseaseInfo(DiseaseN089 disease) {
		switch (disease) {
			case 흉부대동맥류와_박리증: 	return "Vascular diseases such as arterial dilation and rupture caused by connective tissue abnormality.";
			case 유전성_부정맥:			return "A inherited disease related with disturbance in the electrical impulses to the heart which results in an irregular rhythm or rate of heart beat, and maybe cause sudden cardiac arrest.";
			case 유전성_심근병증:			return "A group of diseases that can cause the thickness changes or contraction weakness of myocardium, and may cause heart failure.";
			case 고콜레스테롤혈증_및_혈전증:return "Causes hypercoagulation conditions that form hypercholesterolemia and thrombosis, occurrence of complications of early strokes, myocardial infarction, and pulmonary embolism.";
			case 동맥비틀림증후군:		return "Connective tissue disorder that is characterized by lengthening and distortion of arteries. It can cause arterial rupture and other various complications such as joint abnormalities, scoliosis and hernias.";
			default:					return "";
		}
	}
	private String diseaseSubName(DiseaseSubN089 diseaseSub) {
		switch (diseaseSub) {
			case 엘러스_단로스_증후군:				return "Vascular Ehlers-Danlos Syndrome";
			case 마르판_증후군: 					return "Marfan Syndrome";
			case 로이_디에츠_증후군: 				return "Loeys-Dietz Syndrome";
			case 가족성_흉부대동맥류와_박리증:		return "Familial thoracic aortic aneurysms and dissections";
			case 카테콜아민성_다형성_심실성_빈맥: 	return "Catecholaminergic Polymorphic Ventricular Tachycardia";
			case 심장_긴간격_증후군: 				return "Long QT Syndrome";
			case 브루가다_증후군:				 	return "Brugada Syndrome";
			case 비후성_심근병증: 				return "Hypertrophic cardiomyopathy";
			case 확장성_심근병증: 				return "Dilated cardiomyopathy";
			case 파브리병:						return "Fabry Disease";
			case 부정맥_유발성_우심실_심근병증:		return "Arrhythmogenic right ventricular cardiomyopathy";
			case 애머리_드라이푸스_증후군:			return "Emery-dreifuss syndrome";
			case 가족성_고콜레스테롤혈증:			return "Familial hypercholesterolemia";
			case 고호모시스테인혈전증:				return "Hyperhomocysteinemia";
			case 동맥비틀림증후군:				return "Arterial tortuosity syndrome";
			default:							return "";
		}
	}
	@Override
	public String diseaseName(Disease disease) {
		if(disease instanceof DiseaseN089) return diseaseName((DiseaseN089) disease);
		return null;
	}

	@Override
	public String diseaseInfo(Disease disease) {
		if(disease instanceof DiseaseN089) return diseaseInfo((DiseaseN089) disease);
		return null;
	}

	@Override
	public String diseaseSubName(DiseaseSub diseaseSub) {
		if(diseaseSub instanceof DiseaseSubN089) return diseaseSubName((DiseaseSubN089) diseaseSub);
		return null;
	}
	@Override
	public String diseaseSubInfo(DiseaseSub diseaseSub) {
		if(diseaseSub instanceof DiseaseSubN089) switch ((DiseaseSubN089) diseaseSub) {
			case 엘러스_단로스_증후군: 			return "Ehlers-Danlos syndrome (EDS) is a hereditary connective tissue disease. It is characterized by a defect in collagen, the main structural protein found in skin and connective tissues. Collagen plays an essential role in holding together, strengthening and providing flexibility to cells and tissues in the body. If there are defects in collagen, joints become abnormally soft, loose and dislocate easily. In addition, the skin becomes thin and saggy and the connection to vessels and other tissues in the body gets weaker. Essentially, the EDS is classified into six major subtypes based on the clinical biochemical hereditary characteristics. Among them, an angiogenic EDS due to a pathogenic variant of the COL3A1 gene may cause serious complications because even a small external injury can lead to serious bruise on the skin and rupture of vessels.";
			case 마르판_증후군:					return "Marfan syndrome is a hereditary disease that affects connective tissues. Connective tissues are substances between cells that provide the shape to the tissues and empower them and are distributed all over the body. Therefore, Marfan syndrome may affect various organs of the patient’s body, mostly in the heart, blood vessels, skeleton and eyes. The main symptoms include excessive growth of long bones in arms and legs, rachiocampsis, depression or protrusion of chest bone, dislocation of crystalline lens of the eyes, myopia, aortectasia and degeneration of aorta, aortic regurgitation, mitral valve prolapse and mitral regurgitation. Marfan syndrome follows the autosomal dominant inherited fashion. The defect in or duplication of fibrillin-1 (FBN1) is linked to diseases related to Marfan syndrome. Marfan syndrome affects men and women in equal proportion and occurs all around the world with no difference in races, which is estimated to occur in 1 out of 5,000~10,000 people in the general population. It is not easy to define the incidence of Marfan syndrome in the general population, because Marfan syndrome with mild symptoms is difficult to diagnose.";
			case 로이_디에츠_증후군:				return "Loeys-Dietz syndrome (LDS) shows ocular hypertelorism, staphyloschisis and arterial tortuosity as well as similar symptoms as Marfan syndrome (aortectasia and aortic dissection, abnormally long limbs and joint hypermotility). It is caused by a variant of the TGFBR1 or TGFBR2 gene and follows the autosomal dominant inherited fashion. Because LDS patients have a high risk of sudden death due to aortic dissection, they have to take echocardiography every year to check the condition of the aorta. If the aorta is dilated or degenerated or aortic aneurysm occurs, a surgical procedure may ultimately be required.";
			case 가족성_흉부대동맥류와_박리증:		return "Thoracic aortic aneurysms and dissection (TAAD) is the hereditary disease that causes dilation or dissection of thoracic aorta. It may also become a cause of sudden death. This disease occurs at the relatively early age, and relatives may have abnormal findings in the thoracic aorta. It is estimated that about 20% of overall TAAD is caused by familial aortic aneurysms and dissection. According to the study, 20% of the patients with this disease die a sudden death. The TAAD is similar to Marfan syndrome. However, it does not have the distinctive skeletal muscle findings, merely causing dilation or dissection of thoracic aorta.";
			case 카테콜아민성_다형성_심실성_빈맥:	return "Catecholaminergic polymorphic ventricular tachycardia (CPVT) is characterized by exercise or emotional stress-induced intermittent syncope in a person who has a normal heart structure. If the person resists arrhythmia, only mild symptoms such as dizziness may occur. If arrhythmia is resolved, the person may recover spontaneously. However, in some cases, ventricular tachycardia may cause ventricular fibrillation, resulting in sudden death. The average age when symptoms occur is 7~9, but it is sometimes reported to occur in the 40s. About 30% of people affected have experienced at least one cardiac arrest and about 80% have experienced syncope at least once. Therefore, untreated CPVT can be fatal.";
			case 심장_긴간격_증후군:				return "Long QT syndrome (LQTS) is a serious disease that may cause faint or a sudden cardiac death. It can be divided into congenital LQTS due to an abnormality in genes and acquired LQTS due to an electrolyte imbalance and drugs. After a heartbeat, the heart’s electric system recharges itself for the next heartbeat, but a person with LQTS needs more time than normal people, which causes very rapid and abnormal arrhythmia called torsade de Points. The heart cannot send enough blood and the brain cannot receive sufficient oxygen, which leads to deficiency in oxygen, causing syncope or even death.";
			case 브루가다_증후군:					return "According to the occurrence of sudden cardiac death, it is reported that about 5% of patients have psychogenic sudden cardiac death without being accompanied by organic cardiopathies. These cases are classified into the idiopathic ventricular fibrillation category. In the idiopathic ventricular fibrillation group, there have been reported cases that show right bundle branch block in ECG findings and at the same time rise in the ST node in chest lead (V1-3) and display the characteristic clinical catamnesis of psychogenic sudden death caused by ventricular fibrillation. Patients in this case are diagnosed as having Brugada syndrome. Reported first to the academic world in 1992, Brugada syndrome (BS) has accumulated a lot of data about its diagnosis, treatment and prognosis because of its dramatic clinical picture (sudden cardiac death), characteristic changes in ECG, genetic linkage and unique electrophysiological mechanism, despite of its short history. It is recognized as an important cause of sudden cardiac death, especially for younger adults in Southeast Asia and Japan. It is also known to be the most common cause of sudden cardiac death for younger adult males who do not have any underlying diseases In Thailand and Laos.";
			case 비후성_심근병증:					return "Hypertrophic cardiomyopathy (HCM) is a cardiopathy with a thickening of the left ventricular wall without other symptoms that may cause left ventricular hypertrophy such as aortic stenosis and high blood pressure. It accompanies various types of left ventricular hypertrophy and occlusion in the outflow tract of the left ventricle. The HCM was initially considered a very rare disease, but currently it is one of the most common diseases with 1 in 500 births subsequently succumbing to sudden death at a young age. It is found to follow the autosomal dominant inherited pattern. It is estimated that there are about 100,000 patients with this disease in Korea. Excessive activities may cause dizziness and difficulty in breathing and high risk of sudden death. The result of a study conducted in 2006 showed that the average prevalence of HCM from 1998 to 2006 was 0.07/100,000 and the estimated point prevalence of 2006 was 0.51/100,000 in Korea.";
			case 확장성_심근병증:					return "Dilated cardiomyopathy (DCM) is a syndrome that accompanies dilation of left ventricle or right ventricle and systolic dysfunction. It is also named as congestive cardiomyopathy. In most cases ventricle dilation occurs first then the symptoms of heart failure develop later, but some patients only have systolic dysfunction or no symptoms at all. The natural progress of the DCM is not clear, but the reported prognosis is poor – it is known that once the symptoms occur, the disease gradually progresses, causing half of patients to die within 5 years. However, it is reported that early diagnosis and adequate treatment increase the long-term survival rate of patients. A screening test for family is very important because 9-48% of total DCM show familial occurrence. The result of a study conducted in 2010 showed that the average prevalence of DCM from 1998 to 2006 was 0.18/100,000 and the estimated point prevalence in 2006 was 1.39/100,000 in Korea.";
			case 파브리병:						return "Fabry disease is one of the lysosomal storage disorders caused by a deficiency of alpha-galactosidase A, which is an enzyme also called ceramidetrihexosidase. Angiokeratomas with the skin swells like a wart, stomachache and visual disturbances occur for Fabry disease. In the later stage of the disease, gradual changes in the vascular system such as formation of infection or thrombus progress as patients get older and develop proteinuria, hematuria and adiposuria. Therefore, most patients have kidney failure and high blood pressure in their 30s~40s, which becomes the most common cause of death. Cardiovascular diseases such as congestive heart failure, arrhythmia, angina pectoris, left ventricular hypertrophy, valvular heart disease, and high blood pressure may also be involved. Fabry disease is inherited as an X-linked recessive character. It mostly occurs in males, while symptoms are relatively mild in females. Symptoms start mainly in childhood or adolescence and progress slowly throughout adulthood.";
			case 부정맥_유발성_우심실_심근병증:		return "Arrhythmogenic right ventricular cardiomyopathy (ARVC) is autosomal dominant disease characterized by progressive fibro-fatty replacement of the myocardium and ventricular arrhythmia. It causes ventricular tachycardia and sudden death in young people and athletes. Basically, the ARVC causes abnormality in right ventricle and at the same time affects the left ventricle. The manifestation of the disease may vary considerably among family members. People who show manifestations of the disease may not satisfy the clinical diagnosis criteria. The average age of diagnosis is 31. Although the prevalence is not exactly known, it is estimated to be 1 in 1000~1,250 people.";
			case 애머리_드라이푸스_증후군:			return "Emery-Dreifuss Syndrome is the rare disease in which the arms, legs, neck, spine and heart muscles are weakened. Emery-Dreifuss Syndrome is characterized by the presence of joint contractures, muscle weakness and cardiac involvement mainly cardiac conduction defects. Cardiac involvement vary in age, but usually arises after the second decade of life. Cardiomyopathy, palpitations, fatigue, poor exercise tolerance and cardiac muscle involvement can occur. In some patients, also arrhythmia or atrioventricular block can occur.";
			case 가족성_고콜레스테롤혈증:			return "Hypercholesterolemia is an autosomal dominant hereditary disease and is characterized by blood low-density lipoprotein cholesterol (LDL-C) increase, normal triglyceride, tendinous xanthoma, premature coronary sclerosis, etc. The omozygous type has abnormality in both sides of genes with the LDL-C value of 500–900 mg/dL and the total cholesterol (TC) value of 600 mg/dL or higher, and the heterozygous type has abnormality in one side of genes with the LDL-C value of 150–420 mg/dL and the TC value of 230–500 mg/dL. This disease causes severe coronary sclerosis. Therefore, it is important to find and treat this disease in the early stage.";
			case 고호모시스테인혈전증:				return "Hyperhomocysteinemia shows an abnormally elevated level of homocysteine. Patients with increased homocysteine level can cause thrombosis without symptoms of normal homocysteinemia. This disease is known as a risk factor for cardiovascular disease, cerebrovascular disease and peripheral vascular disease. In the case of severe hyperhomocysteinemia patients, it may develop into homocystinuria. Children may experience decreased intelligence, convulsions and skeletal deformities, and may appear in the form of coronary artery disease, cerebral infarction, peripheral arteriosclerosis, deep vein thrombosis, and pulmonary embolism. The disease is inherited as autosomal recessive.";
			case 동맥비틀림증후군:				return "Arterial tortuosity syndrome is an abnormal connective tissue that maintains the shape of the body, causing abnormality in various organs such as skin, joints, and blood vessels. In particular, it is characterized by abnormal dilation of blood vessels, tortuosity, stenosis, and ischemic vessels. Blood loss and vascular stenosis due to dissecting aneurysms can cause complications in various organs by limiting blood supply. The SLC2A10 gene is known to be involved and is inherited as autosomal recessive.";
		}
		return null;
	}
	@Override
	public String diseaseSubReference(DiseaseSub diseaseSub) {
		if(diseaseSub instanceof DiseaseSubN089) switch ((DiseaseSubN089) diseaseSub) {
			case 엘러스_단로스_증후군: 			return "03. Prevention and Treatment page10~11";
			case 마르판_증후군:					return "03. Prevention and Treatment page12~13";
			case 로이_디에츠_증후군:				return "03. Prevention and Treatment page16~17";
			case 가족성_흉부대동맥류와_박리증:		return "03. Prevention and Treatment page14~15";
			case 카테콜아민성_다형성_심실성_빈맥:	return "03. Prevention and Treatment page28~29";
			case 심장_긴간격_증후군:				return "03. Prevention and Treatment page30~31";
			case 브루가다_증후군:					return "03. Prevention and Treatment page32~33";
			case 비후성_심근병증:					return "03. Prevention and Treatment page18~19";
			case 확장성_심근병증:					return "03. Prevention and Treatment page20~21";
			case 파브리병:						return "03. Prevention and Treatment page22~23";
			case 부정맥_유발성_우심실_심근병증:		return "03. Prevention and Treatment page24~25";
			case 애머리_드라이푸스_증후군:			return "03. Prevention and Treatment page26~27";
			case 가족성_고콜레스테롤혈증:			return "03. Prevention and Treatment page34~35";
			case 고호모시스테인혈전증:				return "03. Prevention and Treatment page36~37";
			case 동맥비틀림증후군:				return "03. Prevention and Treatment page38~39";
		}
		return null;
	}
	@Override
	public String clinicalReference(Gene gene) {
		if(gene instanceof GeneN089) switch((GeneN089) gene) {
			case ACTA2:
			case APOB:
			case DSC2:
			case MYBPC3:
			case PCSK9:
			case PRKAG2:
			case RYR2:
			case SMAD3:
			case TNNI3:
			case TNNT2:
				return "MedlinePlus";
			case ACTC1:
			case DSG2:
			case MYH11:
			case MYL2:
			case MYL3:
			case PKP2:
			case TMEM43:
			case TPM1:
				return "GeneCards";
			default:		return null;
		}
		return null;
	}
	@Override
	public String clinicalMeaning(Gene gene) {
		if(gene instanceof GeneN089) switch((GeneN089)gene) {
			case ACTA2:		return "More than 30 ACTA2 pathogenic variants have been identified in people with familial thoracic aortic aneurysm and dissection (familial TAAD). This disorder involves problems with the aorta, which is the large blood vessel that distributes blood from the heart to the rest of the body. The aorta can weaken and stretch, causing a bulge in the blood vessel wall (an aneurysm). Stretching of the aorta may also lead to a sudden tearing of the layers in the aorta wall (aortic dissection). Aortic aneurysm and dissection can cause life-threatening internal bleeding. ACTA2 pathogenic variants that are associated with familial TAAD change single protein building blocks (amino acids) in the smooth muscle α-2 actin protein.";
			case ACTC1:		return "Actins are highly conserved proteins that are involved in various types of cell motility. Polymerization of globular actin (G-actin) leads to a structural filament (F-actin) in the form of a two-stranded helix. Each actin can bind to four others. The protein encoded by this gene belongs to the actin family which is comprised of three main groups of actin isoforms, alpha, beta, and gamma. The alpha actins are found in muscle tissues and are a major constituent of the contractile apparatus. Defects in this gene have been associated with idiopathic dilated cardiomyopathy(IDC) and familial hypertrophic cardiomyopathy(FHC).";
			case APOB:		return "More than 100 pathogenic variants in the APOB gene are known to cause familial hypercholesterolemia. This condition is characterized by very high levels of cholesterol in the blood and an increased risk of developing heart disease. Each pathogenic variant that causes this condition changes a single protein building block (amino acid) in a critical region of apolipoprotein B-100(Apolipoprotein B-48 is normal).";
			case DSC2:		return "At least one pathogenic variant in the DSC2 gene has been found to cause a form of keratoderma with woolly hair classified as type III. It is characterized by thick, calloused skin on the palms of the hands and soles of the feet (palmoplantar keratoderma); coarse, dry, fine, and tightly curled hair; and a potentially life-threatening form of heart disease called arrhythmogenic right ventricular cardiomyopathy(ARVC).";
			case DSG2:		return "DSG2 gene encodes a member of the desmoglein family and cadherin cell adhesion molecule superfamily of proteins. Desmogleins are calcium-binding transmembrane glycoprotein components of desmosomes, cell-cell junctions between epithelial, myocardial, and other cell types. Pathogenic variants in this gene have been associated with arrhythmogenic right ventricular dysplasia.";
			case MYBPC3:	return "Pathogenic variants in the MYBPC3 gene are a common cause of familial hypertrophic cardiomyopathy, accounting for up to 30 percent of all cases. This condition is characterized by thickening (hypertrophy) of the cardiac muscle. Although some people with familial hypertrophic cardiomyopathy have no obvious health effects, all affected individuals have an increased risk of heart failure and sudden death. MYBPC3 pathogenic variants that cause familial hypertrophic cardiomyopathy lead to an abnormally short or otherwise altered cardiac MyBP-C protein. It is unknown how these changes cause hypertrophy of the heart muscle.";
			case TNNT2:		return "Pathogenic variants in the TNNT2 gene can cause familial hypertrophic cardiomyopathy, a condition characterized by thickening (hypertrophy) of the cardiac muscle. TNNT2 pathogenic variants are found in approximately 5 percent of individuals with this condition. Although some people with hypertrophic cardiomyopathy have no obvious health effects, all affected individuals have an increased risk of heart failure and sudden death.";
			case MYL2:		return "MYL2 gene encodes the regulatory light chain associated with cardiac myosin beta (or slow) heavy chain. Ca+ triggers the phosphorylation of regulatory light chain that in turn triggers contraction. Pathogenic variants in this gene are associated with mid-left ventricular chamber type hypertrophic cardiomyopathy.";
			case TPM1: 		return "TPM1 gene is a member of the tropomyosin family of highly conserved, widely distributed actin-binding proteins involved in the contractile system of striated and smooth muscles and the cytoskeleton of non-muscle cells. Tropomyosin is composed of two alpha-helical chains arranged as a coiled-coil. It is polymerized end to end along the two grooves of actin filaments and provides stability to the filaments. Pathogenic variants in this gene are associated with type 3 familial hypertrophic cardiomyopathy.";
			case MYH11:		return "Thoracic aortic aneurysms leading to acute aortic dissections (TAAD) can be inherited in isolation or in association with genetic syndromes, such as Marfan syndrome and Loeys-Dietz syndrome. When TAAD occurs in the absence of syndromic features, it is inherited in an autosomal dominant manner with decreased penetrance and variable expression, the disease is referred to as familial TAAD. Familial TAAD exhibits significant clinical and genetic heterogeneity. Pathogenic variants in MYH11 have been described in individuals with TAAD with patent ductus arteriosus(PDA).";
			case MYL3:		return "MYL3 encodes myosin light chain 3, an alkali light chain also referred to in the literature as both the ventricular isoform and the slow skeletal muscle isoform. Pathogenic variants in MYL3 have been identified as a cause of mid-left ventricular chamber type hypertrophic cardiomyopathy.";
			case PCSK9:		return "More than 50 PCSK9 pathogenic variants have been found that cause familial hypercholesterolemia. Most of these pathogenic variants change single protein building blocks (amino acids) in the PCSK9 protein. Pathogenic variants responsible for familial hypercholesterolemia are \"gain-of-function\" variants because they appear to enhance the activity of the PCSK9 protein.";
			case PKP2:		return "Most people with familial hypercholesterolemia inherit one altered copy of the PCSK9 gene from an affected parent and one normal copy of the gene from the other parent. These cases are associated with an increased risk of early heart disease, typically beginning in a person's forties or fifties. Rarely, a person with familial hypercholesterolemia is born with two copies of the PCSK9 pathogenic variants. This situation occurs when the person has two affected parents, each of whom passes on one altered copy of the gene. The presence of two PCSK9 pathogenic variants results in a more severe form of hypercholesterolemia that usually appears in childhood.";
			case PRKAG2:	return "At least seven pathogenic variants that cause Wolff-Parkinson-White syndrome have been identified in the PRKAG2 gene. Some people with these pathogenic variants also have features of hypertrophic cardiomyopathy, a form of heart disease that enlarges and weakens the heart (cardiac) muscle. These pathogenic variants alter the activity of AMP-activated protein kinase in the heart, disrupting the enzyme's ability to respond to changes in cellular energy demands.";
			case RYR2:		return "More than 200 pathogenic variants in the RYR2 gene have been found to cause catecholaminergic polymorphic ventricular tachycardia (CPVT), a heart condition characterized by an abnormal heart rhythm (arrhythmia) during exercise or emotional stress, which can be fatal. Almost all of the RYR2 pathogenic variants involved in CPVT change single protein building blocks (amino acids) in the ryanodine receptor 2 protein. These pathogenic variants alter the structure and function of the RYR2 channel.";
			case SMAD3:		return "At least 35 pathogenic variants in the SMAD3 gene have been found to cause Loeys-Dietz syndrome type III. This disorder affects connective tissue, which gives structure and support to blood vessels, the skeleton, and many other parts of the body. Loeys-Dietz syndrome type III is characterized by abnormal blood vessels, skeletal and joint deformities, and skin abnormalities. Some of the pathogenic variants that cause this disorder insert or delete small amounts of genetic material in the SMAD3 gene, while other pathogenic variants result in a change to single protein building blocks (amino acids) in the SMAD3 protein.";
			case TMEM43:	return "TMEM43 gene belongs to the TMEM43 family. Defects in this gene are the cause of familial arrhythmogenic right ventricular dysplasia type 5 (ARVD5), also known as arrhythmogenic right ventricular cardiomyopathy type 5 (ARVC5). Arrhythmogenic right ventricular dysplasia is an inherited disorder, often involving both ventricles, and is characterized by ventricular tachycardia, heart failure, sudden cardiac death, and fibrofatty replacement of cardiomyocytes.";
			case TNNI3:		return "Pathogenic variants in the TNNI3 gene can cause familial hypertrophic cardiomyopathy, a condition characterized by thickening (hypertrophy) of the cardiac muscle. TNNI3 pathogenic variants are found in less than 5 percent of people with this condition. Although some people with hypertrophic cardiomyopathy have no obvious health effects, all affected individuals have an increased risk of heart failure and sudden death.";
			default:		return null;
		}
		return null;
	}
}
