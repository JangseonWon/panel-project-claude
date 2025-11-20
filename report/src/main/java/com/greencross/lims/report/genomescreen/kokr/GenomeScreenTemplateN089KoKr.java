package com.greencross.lims.report.genomescreen.kokr;

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
public class GenomeScreenTemplateN089KoKr extends GenomeScreenTemplateKoKr<GenomeScreenResourceN089KoKr> implements GenomeScreenTemplateN089<GenomeScreenResourceN089KoKr> {
	private final GenomeScreenResourceN089KoKr resource;
	private final String lblSummaryTestIntroTitleInfo = "심장 돌연사 지놈 스크린 검사에 대한 간단한 설명입니다.";
	private final String lblSummaryInfo = "본 검사는 최신 유전자 분석기법으로 돌연사와 같은 심장질환을 발병시킬 수 있는 유전자를 검사하며 기존 연구논문 결과를 분석하여 " +
										  "건강 관리에 도움이 될 수 있는 개인 맞춤형 정보를 제공합니다. 유전성 심장질환과 관련된 병원성 변이가 발견된 경우 " +
										  "개인에 따라 증상이 없을 수 있으며(reduced penetrance) 정밀 검사(심전도, 심장초음파 검사 등)가 필요할 수 있습니다.";
	private final String lblGuideTestTitle = "심장 돌연사 지놈 스크린 검사란?";
	private final TextBlock[] guideTest;
	private final String lblGuideDiseaseTitle = "유전성 심장 질환은 이러한 특징이 있습니다.";
	private final TextBlock[] guideDisease;
	private final String lblGeneListInfo = "심장 돌연사 지놈 스크린 검사는 미국의학 유전학회(ACMG)의 권고에 따라 다음과 같은 질환 및 유전자들을 검사합니다.";
	private final TextBlock[] limitations;
	private final TextBlock[] references;
	public GenomeScreenTemplateN089KoKr(GenomeScreenResourceN089KoKr resource, TestInfo testInfo) {
		super(testInfo);
		this.resource = resource;
		TextStyle textStyle = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8).paragraph(true);
		guideTest = new TextBlock[] {
				new TextBlock(textStyle.clone().fonts(resource.fontHeader(), resource.fontDefault()).color(Color.BLACK), "심장 돌연사(Sudden Cardiac Arrest)"),
				new TextBlock(textStyle, "란 심장 질환의 진단 유무와 관계 없이 예기치 않게 심박동이 정지하는 것을 뜻하며 급성 심정지 후 1시간 이내에 사망하는 것을 급성 심장 돌연사라고 합니다.\n" +
												 "우리나라에서 급성 심정지의 발생 건 수는 한 해 2만 명이 넘으며, 이로 인해 상당한 사회 경제학적 손실이 야기되고 있는데, 이 중 상당 부분이 유전성 심장 질환으로 인해 발생합니다.\n" +
												 "심장 돌연사 지놈 스크린 검사는 급성 심정지 및 급성 심장 돌연사의 위험성을 높이는 다양한 유전성 심장 질환과 관련된 40개의 유전자를 차세대 염기서열분석(Next Generation Sequencing; NGS)" +
												 "검사법으로 한 번에 검사하여 유전성 심장질환을 미리 진단하고 이를 통해 질환의 예방 및 조기진단, 치료 효과의 향상을 기대할 수 있는 검사입니다.")
		};
		guideDisease = new TextBlock[] {
				new TextBlock(textStyle, "유전성 심장 질환 관련 유전자에서 병원성 변이(pathogenic variant)가 발견되더라도 100% 질환이 발" +
												 "병하는 것은 아니며, 질환의 발병 시기 및 임상 양상은 개개인에 따라 매우 다양하게 나타납니다."),
				new TextBlock(textStyle, "유전성 심장 질환의 발병 시기는 증상이 심한 경우 출생 직후에 발병하는 경우도 있으나 보통 청소년기나 이른 성인기에 시작하며 간혹 늦은 성인기에 발병하는 경우도 있습니다."),
				new TextBlock(textStyle, "유전성 심근병증(Hereditary Cardiomyopathy) 및 유전성 부정맥(Hereditary Arrhythmia) 관련 유전자들은 대개 하나의 유전자가 다양한 심근병증 및 부정맥의 임상 양상과 연관되어 나타날 수 있습니다."),
				new TextBlock(textStyle, "유전성 심장 질환과 관련된 유전자 검사에서 질환과 관련된 병원성 변이가 발견되지 않았더라도 환경" +
												 "적 영향 및 생활습관 등 비유전성 원인으로 인한 심장 질환의 발병 가능성은 여전히 존재합니다.")
		};
		limitations = new TextBlock[] {
				new TextBlock(textStyle, "유전자 변이는 표준 해석 지침인 2015 ACMG/AMP 가이드라인에 따라 병원성 변이(Pathogenic Variant), " +
												 "준병원성 변이(Likely Pathogenic Variant), 의미를 알 수 없는 변이(Variant of Uncertain " +
												 "Significance), 준양성 변이(Likely Benign Variant), 양성 변이(Benign Variant)의 다섯 가지 카테고리로 " +
												 "분류됩니다."),
				new TextBlock(textStyle, "2015 ACMG/AMP 가이드라인에 따른 염기서열 변이의 질병 관련성은 인구집단에서의 변이 빈도, 기능분석, " +
												 "컴퓨터 분석을 이용한 병인성 예측자료, 논문 및 병인성 변이 데이터베이스 등 현재까지 알려진 다양한 " +
												 "근거를 종합하여 해석(Evidence-based Interpretation) 하며, 진단검사의학과 전문의가 담당하고 있습니다. " +
												 "단, 결과보고 이후에도 추가 근거가 축적됨에 따라 변이의 해석은 달라질 수 있습니다."),
				new TextBlock(textStyle, "본 검사에서는 질병 관련성이 확실하거나 매우 높은 병원성 변이와 준병원성 변이를 주로 보고하며, 의미를 " +
												 "알 수 없는 변이, 준양성 변이 및 양성 변이는 보고하지 않는 것을 원칙으로 합니다."),
				new TextBlock(textStyle, "단, 일부 유전자(RYR2, DSP, MYH7, TNNI3, TPM1, MYL3, MYL2, PRKAG2, ACTC1, MYH7, APOB, PCSK9)는 " +
												 "잘 알려진 병원성 변이에 대해서만 보고하는 것을 원칙으로 합니다."),
				new TextBlock(textStyle, "검사에 포함된 유전자 중 일부는 상염색체 열성으로 유전됩니다. 상염색체 열성의 경우 두 개의 병원성 변이가 존재해야 " +
						"심장 돌연사 발생 가능성이 높아집니다. 따라서, 일부 유전자의 경우 두 개의 병원성 변이가 존재할 때만 보고하는 것을 원칙으로 합니다."),
				new TextBlock(textStyle, "검사에 포함된 유전자는 전체 엑손을 포함하나, 일부 영역에서는 염기서열 해독이 충분하지 않을 수 있습니다. " +
												 "또한, 상동성이 높은 염기서열이 존재하는 경우 염기서열 해독이 정확하지 않을 수 있으며, 큰 " +
												 "결실 또는 중복, 단백질을 만들지 않는 서열 부위에 존재하는 변이는 검출이 어려울 수 있습니다.")
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
			case 흉부대동맥류와_박리증: 	return "흉부대동맥류와 박리증";
			case 유전성_부정맥:			return "유전성 부정맥";
			case 유전성_심근병증:			return "유전성 심근병증";
			case 고콜레스테롤혈증_및_혈전증:return "고콜레스테롤혈증 및 혈전증";
			case 동맥비틀림증후군:			return "동맥비틀림증후군";
			default:					return "";
		}
	}
	private String diseaseInfo(DiseaseN089 disease) {
		switch (disease) {
			case 흉부대동맥류와_박리증: 	return "파열 등 혈관이상을 통해 돌연사를 일으킬 수 있는 질환";
			case 유전성_부정맥:			return "심장 구조는 정상이나, 심실세동과 같은 치명적인 부정맥을 유발하여 심장 돌연사를 일으킬 수 있는 질환";
			case 유전성_심근병증:			return "심장 근육이 두꺼워지거나 늘어나면서 심부전 및 심실성부정맥 등이 발생하는 질환";
			case 고콜레스테롤혈증_및_혈전증:return "고콜레스테롤혈증이나 혈전을 형성하는 과응고 상태를 유발하여 뇌졸중, 심근경색, 폐동맥 색전증 등 합병증을 일으킬 수 있는 질환";
			case 동맥비틀림증후군:			return "결합조직의 이상으로 혈관이 비정상적으로 늘어나거나 뒤틀려 동맥 파열 등 합병증을 일으키고 관절이상, 척추측만증, 탈장 등 다양한 이상을 일으킬 수 있는 질환";
			default:					return "";
		}
	}
	private String diseaseSubName(DiseaseSubN089 diseaseSub) {
		switch (diseaseSub) {
			case 엘러스_단로스_증후군:				return "엘러스-단로스 증후군";
			case 마르판_증후군: 					return "마르판 증후군";
			case 로이_디에츠_증후군: 				return "로이-디에츠 증후군";
			case 가족성_흉부대동맥류와_박리증:		return "가족성 흉부대동맥류와 박리증";
			case 카테콜아민성_다형성_심실성_빈맥: 	return "카테콜아민성 다형성 심실성 빈맥";
			case 심장_긴간격_증후군: 				return "심장 긴간격 증후군";
			case 브루가다_증후군:				 	return "브루가다 증후군";
			case 비후성_심근병증: 					return "비후성 심근병증";
			case 확장성_심근병증: 					return "확장성 심근병증";
			case 파브리병:						return "파브리병";
			case 부정맥_유발성_우심실_심근병증:		return "부정맥 유발성 우심실 심근병증";
			case 애머리_드라이푸스_증후군:			return "애머리-드라이푸스 증후군";
			case 가족성_고콜레스테롤혈증:			return "가족성 고콜레스테롤혈증";
			case 고호모시스테인혈전증:				return "고호모시스테인혈전증";
			case 동맥비틀림증후군:					return "동맥비틀림증후군";
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
			case 엘러스_단로스_증후군: 			return "엘러스-단로스 증후군(Ehlers-Danlos syndrome, EDS)은 유전성 결합조직 질환입니다. 신체의 주요한 구조 단백질인 콜라겐에 결함이 생기는 것이 특징입니다. 콜라겐은 신체의 세포와 조직을 서로 붙잡아 주고, 강하게 하며 유연성을 주는 데 필수적인 역할을 합니다. 콜라겐에 결함이 발생하면 관절이 비정상적으로 부드럽고 느슨해지고, 쉽게 탈골됩니다. 그리고 피부는 얇고 늘어지며, 혈관 및 다른 신체의 조직과의 결합도 약해집니다. 본래, 엘러스-단로스 증후군은 임상적 생화학적 유전성특성을 바탕으로 여섯 가지 주요 특수형으로 분류합니다. 그 중에서 COL3A1 유전자의 병원성 변이에 의한 혈관형 엘러스-단로스 증후군은 작은 외상에도 피부에 심각한 멍이 나타나고 혈관의 파열이 쉽게 일어나 심각한 합병증이 발생할 수 있습니다.";
			case 마르판_증후군:					return "마르판 증후군(Marfan syndrome)은 결합조직에 영향을 미치는 유전질환 입니다. 결합 조직은 세포간의 물질로서 조직에 형태와 힘을 주는 기능을 하며 신체 전반에 분포하고 있으므로 마르판 증후군 환자에서는 여러 기관에 영향을 미칠 수 있으며 주로 심장 및 혈관, 골격, 눈에 영향을 받게 됩니다. 주요 증상으로는 팔과 다리의 긴 뼈의 과다한 성장, 척추 만곡, 가슴뼈의 함몰 또는 돌출, 눈의 수정체 탈구, 근시, 대동맥의 확장과 변성, 대동맥판 역류, 승모판 탈출, 승모판 역류 등이 있습니다. 마르판 증후군은 상염색체 우성으로 유전되며, fibrillin-1(FBN1) 유전자의 결함 또는 중복이 마르판 증후군과 관련된 질병과 연결되어 있습니다. 마르판 증후군은 남여 동등한 비율로 영향을 미치고, 인종적인 차이 없이 전 세계적으로 발생하며 일반 인구에서 5,000~10,000명 중 1명의 비율로 발생하는 것으로 추정됩니다. 마르판 증후군 증상이 경미한 경우에 진단하는 것이 어려워 일반 인구에서 실제 발병률을 확정하기는 어렵습니다.";
			case 로이_디에츠_증후군:				return "로이-디에츠 증후군(Loeys-Dietz syndrome, LDS)은 마르판 증후군과 유사한 증상(대동맥의 팽창 및 박리, 비정상적으로 긴 사지, 관절 과운동성)을 보이면서 양안간 격리증, 목젖갈림증, 동맥만곡증 등의 증상이 나타납니다. TGFBR1 이나 TGFBR2 유전자의 변이로 유발되며 상염색체 우성으로 유전됩니다. 특히 대동맥 박리로 인한 돌연사의 위험성이 크므로 환자는 대동맥의 상태를 확인하기 위해 매년 심초음파 검사를 해야 합니다. 만약, 대동맥이 확장, 박리되거나 대동맥류를 가지면 결국에는 대동맥의 외과적 시술이 필요하게 될 수 있습니다.";
			case 가족성_흉부대동맥류와_박리증:		return "가족성 흉부대동맥류와 박리증(TAAD)은 흉부 대동맥의 확장과 박리를 일으키는 유전성 질환으로 돌연사의 원인이 되기도 합니다. 비교적 어린 나이에 발생하며, 친척에서도 흉부 대동맥에 이상소견이 보여질 수 있습니다. 전체 흉부 대동맥류와 대동맥 박리의 20% 정도가 가족성 대동맥류와 박리증이 원인인 것으로 추측됩니다. 연구에 따르면 질환을 가진 환자의 20%에서 돌연사를 보이는 것으로 알려지고 있습니다. 가족성 흉부대동맥류와 박리증은 마르판 증후군과 유사하나 특징적인 골격근소견이 없으며, 다만 흉부 대동맥의 확장과 박리를 일으킵니다.";
			case 카테콜아민성_다형성_심실성_빈맥:	return "카테콜아민성 다형성 심실성 빈맥 (Catecholaminergic polymorphic ventricular tachycardia, CPVT)은 정상 심장 구조를 가진 사람에서 간헐적으로 일어나는 운동 또는 감정 유발성 실신이 특징적입니다. 부정맥에 잘 견디는 경우 어지럼증과 같은 가벼운 증상만 나타날 수도 있습니다. 부정맥이 스스로 종료되면 자발적인 회복이 되지만, 경우에 따라서는 심실성 빈맥이 심실 세동을 야기하여 돌연사 할 수도 있습니다. 증상 발생의 평균 나이는 7살~9살 사이이지만, 40대와 같이 늦은 나이에서 보고되기도 합니다. 이환된 사람의 약 30%가 적어도 한번의 심정지를 경험하였고, 80% 정도가 한 번 이상의 실신 경험을 갖고 있으므로, 치료받지 않는 CPVT가 매우 치명적입니다.";
			case 심장_긴간격_증후군:				return "심장 긴간격증후군(Long QT syndrome, LQTS)은 실신(faint) 및 급사(sudden cardiac death)를 초래할 수 있는 위중한 질환으로 유전자 이상으로 인한 선천성, 전해질 및 약물에 따른 후천성으로 구별할 수 있습니다. 심장박동이 있은 후 심장의 전기체계는 다음 심장박동을 위해 스스로 재충전을 하게 되는데, 심장 긴간격 증후군이 있을 경우 정상인에 비해 더 긴 시간이 필요하며 이로 인해 torsade de points라 하는 비정상적으로 매우 빠른 부정맥(arrhythmia)이 오게 되며 심장에서는 충분한 피를 내보내지 못하게 되고, 뇌에서 충분한 산소를 공급 받지 못하면서 산소부족현상이 일어나 의식을 잃게 되거나(syncope) 사망할 수도 있습니다.";
			case 브루가다_증후군:					return "돌연성 심장사의 발생을 보면 약 5% 정도의 환자들에서는 기질적인 심질환의 동반 없이 심인성 급사를 경험하는 것으로 보고되고 있으며 이러한 경우를 일반적으로 특발성 심실세동(idiopathic ventricular fibrillation)범주로 구분하여 분류합니다. 이러한 특발성 심실세동군 중에서도 심전도 소견 상 우각차단 양상과 동시에 우 흉부 유도상(V1-3) ST 절의 상승 소견을 보이면서 수면 중 심실세동에 의한 심인성 급사의 특징적인 임상 경과를 보이는 증례들이 보고되면서 이러한 경우의 환자들을 브루가다 증후군이라 명명합니다. 브루가다 증후군 (Brugada syndrome, BS)은 1992년 처음 학계에 보고된 질환으로서 그 역사는 짧으나 이의 극적인 임상상( 급사), 특징적인 심전도 변화, 유전적인 연관성 및 독특한 전기생리학적 기전으로 인하여 병의 진단, 치료, 예후 등에 대한 많은 자료가 축적된 질환입니다. 유전에 의한 급사, 특히 동남아시아와 일본에서 젊은 성인 급사의 중요한 원인질환으로 인정되고 있으며, 태국, 라오스에서는 기저 심장질환 없는 젊은 성인 남성 급사의 가장 흔한 원인으로 알려져 있습니다.";
			case 비후성_심근병증:					return "비후성 심근병증(Hypertrophic cardiomyopathy, HCM)은 좌심실 비후를 유발할 만한 대동맥판 협착증이나 고혈압과 같은 다른 증세 없이 좌심실 벽이 두꺼워지는 심장 질환입니다. 다양한 형태의 좌심실 비후 소견과 좌심실 유출로의 폐색이 관찰됩니다. 처음에는 매우 드문 질환으로 생각했지만 현재는 500명 출생 당 1명으로 빈도가 높고 특히 젊은 나이에 급사를 일으키는 가장 흔한 질환으로 상염색체 우성유전을 하는 것으로 밝혀졌습니다. 국내에서 약 10만 명이 이 병을 앓는 것으로 추정됩니다, 활동을 많이 하면 어지럽고 숨쉬기 힘들고, 돌연사의 위험성이 매우 높습니다. 2006년에 시행한 연구에 의하면, 1998년부터 2006년까지 한국에서 비후성 심근병증의 평균 유병률은 0.07/100,000 이었고, 2006년 예측 시점 유병률은 0.51/100,000이었습니다.";
			case 확장성_심근병증:					return "확장성 심근병증(Dilated cardiomyopathy, DCM)은 좌심실, 우심실 또는 양심실의 확장과 수축기능장애가 동반된 증후군으로 울혈성 심장근육병증(congestive cardiomyopathy) 으로 명명되기도 합니다. 대개는 심실확장이 선행되고 심부전의 증상이 나중에 발현되지만, 일부 환자에서 심실확장 없이 수축기능 장애만 보이기도 하고, 증상이 없는 경우도 있습니다. 확장성 심근병증의 자연경과는 확실하지 않으나 보고 예후는 극히 나빠 일단 증상이 생기면 점점 진행하여 환자의 절반이 5년 이내 사망하는 것으로 알려져 있으나 초기에 진단하고 적절한 치료를 받을 경우 환자의 장기 생존율은 상승하는 것으로 보고 되고 있습니다. 전체 확장성 심근병증의 9-48%에서 가족적 발생을 보이기 때문에 이들 가족들에 대한 선별검사가 매우 중요합니다. 2010년 국내 연구에 의하면, 1998년부터 2006년까지 한국에서 확장성 심근병증의 평균 유병률은 0.18/100,000이었고, 2006년 예측시점 유병률은 1.39/100,000 이었습니다.";
			case 파브리병:						return "파브리병(Fabry disease)은 알파-갈락토시다제 A(alpha-galactosidase A), 다른 말로 세라마이드트라이헥소사이드 (ceramidetrihexosidase)라 불리는 효소의 결핍으로 발생하는 라이소좀 축적 질환(Lysosomal storage disorders) 중 하나입니다. 파브리병의 증상들은 주로 유년기 또는 청소년기에 시작되지만 20∼30세가 될 때 까지도 명확하게 나타나지 않을 수 있으며, 성인기 동안 서서히 진행됩니다. 질병의 초기에는 붉거나 짙은 푸른색의 피부 발진이 나타나며(혈관각화종), 땀의 생산량이 감소되며 따뜻한 온도에서 불편감을 느끼고, 손과 발에 타는 듯한 통증을 심하게 호소하기도 합니다. 파브리병의 증상들은 나이가 들수록 심해질 수 있으며, 단백뇨, 혈뇨, 지방뇨 등이 발전되어 대부분의 환자가 30∼40대에 신부전, 고혈압을 가지게 되며, 울혈성 심부전, 부정맥, 협심증, 좌심실 비대, 심장판막질환, 고혈압 등의 심혈관 질환이 나타납니다. 파브리병은 X염색체 연관 열성 형질로 유전되며 주로 남성에게 나타나고, 여성의 경우 증상이 비교적 경미하게 나타납니다.";
			case 부정맥_유발성_우심실_심근병증:		return "부정맥 유발성 우심실 심근병증(Arrhythmogenic right ventricular cardiomyopathy, ARVC)은 상염색체 우성질환으로, 진행하는 심근의 섬유지방화 및 심실 부정맥이 특징적이며, 젊은 연령 및 운동선수에서 심실빈맥과 돌연사를 야기합니다. 기본적으로, ARVC는 우심실에 이상을 초래하며, 이와 동시에 좌심실에도 영향을 줄 수 있습니다. 질병의 발현은 가족 내에서도 상당히 다양하며, 질병을 발현하는 사람이 임상 진단기준을 충족시키지 않을 수도 있습니다. 평균 진단나이는 31 세이며, 유병률은 정확히 알려지지 않았으나, 1,000명~1,250명 당 1명씩 발생하는 것으로 예측됩니다.";
			case 애머리_드라이푸스_증후군:			return "에머리-드라이푸스 증후군(Emery-Dreifuss Syndrome)은 팔, 다리, 목, 척추, 심장 근육이 약해지는 희귀 질환입니다. 근육약화와 관절의 증상과 같이 심장 증상이 두드러지게 나타나며, 주로 심장 근육의 전도 장애 형태로 나타납니다. 심장 증상의 경우 발병연령은 다양하지만 대부분 20세 이후에 발생합니다. 심근증, 잠재적인 심계항진, 피로, 운동내성 약화, 심장 펌프 능력 저하가 증상으로 나타날 수 있으며, 일부 환자들에서는 부정맥 혹은 방실차단 등이 발생할 수 있습니다.";
			case 가족성_고콜레스테롤혈증:			return "고콜레스테롤 혈증(Hypercholesterolemia)은 상염색체 상호 우성 유전 질환이며, 혈중 저밀도 지방단백질 콜레스테롤(LDL-C) 증가와 정상 트리글리세리드, 건황색종, 조발성 관상동맥경화증 등을 특징으로 합니다. 호모형은 양쪽 유전자에 이상이 있는 경우로 LDL-C값이 500~900mg/dL, 총 콜레스테롤(TC)값이 600mg/ dL 이상이고, 헤테로형은 어느 한쪽 유전자에 이상이 있는 경우로 LDL-C값이 150~420mg/dL, TC값이 230~500mg/dL입니다. 이 질환은 중증의 동맥경화를 일으키기 때문에 조기발견 및 조기치료가 중요합니다.";
			case 고호모시스테인혈전증:				return "고호모시스테인혈증은 혈액 속의 호모시스테인 농도가 비정상적으로 증가되어 있는 상태를 말합니다. 고호모시스테인혈전증 환자의 경우 일반적인 고호모시스테인혈증의 증상 없이 혈전증이 나타나기도 합니다. 해당 질환은 심혈관 질환, 뇌혈관질환 및 말초혈관질환의 위험인자로 알려져 있습니다. 심각한 고호모시스텐인혈증 환자의 경우에는 호모시스틴뇨증으로 발전되기도 합니다. 어린아이들의 경우에는 지능저하, 경련, 골격변형 등이 나타날 수 있으며 관상동맥질환, 뇌경색, 말초동맥경화증, 심부정맥혈전증 및 폐색전증의 형태로 나타날 수 있습니다. 해당 질환은 상염색체 열성으로 유전됩니다.";
			case 동맥비틀림증후군:				return "동맥 비틀림 증후군은 몸의 형태를 유지하는 결합조직의 이상으로 피부, 관절, 혈관 등 다양한 장기에 이상을 야기합니다. 특히 혈관의 비정상적인 늘어남, 비틀림, 협착과 함께 허혈성 혈관이 특징적으로 나타납니다. 동맥류 또는 박리로 인한 혈액 손실, 혈관 협착으로 인한 혈액 공급 제한으로 다양한 장기의 합병증을 유발할 수 있습니다. SLC2A10 유전자가 관련된 것으로 알려져 있으며 상염색체 열성으로 유전됩니다.";
		}
		return null;
	}
	@Override
	public String diseaseSubReference(DiseaseSub diseaseSub) {
		if(diseaseSub instanceof DiseaseSubN089) switch ((DiseaseSubN089) diseaseSub) {
			case 엘러스_단로스_증후군: 			return "03.예방 및 치료 page10~11 참조";
			case 마르판_증후군:					return "03.예방 및 치료 page12~13 참조";
			case 로이_디에츠_증후군:				return "03.예방 및 치료 page16~17 참조";
			case 가족성_흉부대동맥류와_박리증:		return "03.예방 및 치료 page14~15 참조";
			case 카테콜아민성_다형성_심실성_빈맥:	return "03.예방 및 치료 page28~29 참조";
			case 심장_긴간격_증후군:				return "03.예방 및 치료 page30~31 참조";
			case 브루가다_증후군:					return "03.예방 및 치료 page32~33 참조";
			case 비후성_심근병증:					return "03.예방 및 치료 page18~19 참조";
			case 확장성_심근병증:					return "03.예방 및 치료 page20~21 참조";
			case 파브리병:						return "03.예방 및 치료 page22~23 참조";
			case 부정맥_유발성_우심실_심근병증:		return "03.예방 및 치료 page24~25 참조";
			case 애머리_드라이푸스_증후군:			return "03.예방 및 치료 page26~27 참조";
			case 가족성_고콜레스테롤혈증:			return "03.예방 및 치료 page34~35 참조";
			case 고호모시스테인혈전증:				return "03.예방 및 치료 page36~37 참조";
			case 동맥비틀림증후군:				return "03.예방 및 치료 page38~39 참조";
		}
		return null;
	}
	@Override
	public String clinicalMeaning(Gene gene) {
		if(gene instanceof GeneN089) switch((GeneN089)gene) {
			case ACTA2:		return "TAAD 환자로부터 ACTA2 유전자에 이전에 보고된 바 없는 변이 3개(R39C, M49V, G304R)를 발견함. 그 중 2개의 변이는 각각 대동맥류 또는 대동맥 박리를 보인 가족에서 확인되었고 나머지 하나의 변이는 TAAD 환자에서 proband로 발견됨. 3가지 변이 모두 단백질 3차원 구조 분석결과 actin filaments의 형성에 영향을 줄 것으로 예측되며 R39C변이의 경우 같은 위치의 변이 R39H가 TAAD와의 연관성이 알려져있음.";
			case ACTC1:		return "DCM과 연관이 있는 것으로 알려진 유전자는 60개 이상으로 ACTC1 유전자를 포함한 Sarcomere 유전자가 가장 많은 수를 차지하였고, 병원성 변이는 400개 이상인 것으로 확인됨. DCM 환자의 60%가 DCM 유전자 중 1개 이상의 유전자에 변이를 가지고 있는 것으로 확인됨. DCM 관련 유전자 중 변이가 가장 높은 빈도로 발견된 유전자는 TTN 유전자로 25-30%, ACTC1 유전자는 5-10%의 빈도를 보임.";
			case APOB:		return "정상 대조군과 비교하여 FH 환자 141명으로부터 1536개의 SNP를 확인하였고, 그중 FH 위험도를 높이는 11개의 SNP와 FH 위험도를 낮추는 3개의 SNP를 확인함. FH 위험도를 높이는 SNP는 APOB 유전자에서 7개로 가장 많이 발견되었고 LDLR, PCSK9에서 각각 1, 3개가 발견됨. 그 중 APOB 유전자의 SNP rs12720762는 가장 높은 FH 위험도(OR 14.78, p<0.001)를 보였음.";
			case DSC2:		return "ARVD 환자 439명 중 276명(63%)에서 mutation을 발견하였고 그 중 5명(1%)에서 DSC2 유전자 변이가 발견되었으며, 268명(61%)의 환자가 심실부정맥(ventricular arrhythmias)을 보였다. 562명의 가족 중 409명(73%)에서 mutation을 발견하였고, 그 중 4명(0.7%)이 DSC2 유전자 변이를 가지고 있었음. 또한 PKP2, DSP, DSG2, DSC2, JUP, PLN, TMEM43 유전자에 변이를 가지고 있는 가족의 경우(154/385, 40%) 그렇지 않은 경우(28/152, 18%)에 비해 ARVD 진단을 받은 비율이 더 높았음.";
			case DSG2:		return "ARVD 환자 439명 중 276명(63%)에서 mutation을 발견하였고 그 중 17명(4%)에서 DSG2 유전자 변이가 발견되었으며, 268명(61%)의 환자가 심실부정맥(ventricular arrhythmias)을 보였다. 562명의 가족 중 409명(73%)에서 mutation을 발견하였고, 그 중 13명(2%)이 DSG2 유전자 변이를 가지고 있었음. 또한 PKP2, DSP, DSG2, DSC2, JUP, PLN, TMEM43 유전자에 변이를 가지고 있는 가족의 경우(154/385, 40%) 그렇지 않은 경우(28/152, 18%)에 비해 ARVD 진단을 받은 비율이 더 높았음.";
			case MYBPC3:
			case TNNT2:
			case MYL2:
			case TPM1: 		return "200명의 HCM 환자군에서 98개의 변이를 확인하였고 MYH7, MYBPC3, TNNT2, TNNI3, MYL2, MYL3, TPM1, ACTC1 유전자 변이의 빈도는 각각 26.0, 18.0, 4.0, 3.5, 1.0, 1.5, 1.5, 1.5 % 로 나타남. HCM 환자 중 83명(41.5%)에서 1개, 19명(9.5%)에서 2개 이상의 변이가 발견되었으며, 변이의 개수에 따라 그룹을 나누었을 때 변이가 많을수록 maximal wall thickness (MWT)가 증가함(19.7 ± 5.1; 20.5 ± 4.8; 23.6 ± 5.7 mm)";
			case MYH11:		return "두 가족 구성원에 대한 MYH11 유전자 염기서열 분석 결과, 대조군에서 발견되지 않은 새로운 변이 2개(c.232A>G, c.3766-68delAAG)를 발견함. c.232A>G 변이의 경우 heterozygous missense 변이로 p. K78E 아미노산 치환을 야기하며 해당 치환의 구조적, 기능적 영향을 예측하는 프로그램(PolyPhen2, SIFT) 결과 probably damaging / damaging effect 로 확인되었으며, 유전자 분리모형분석(Segregation analysis) 결과 가족 구성원 중 대부분의 TAAD환자(8명)에서 발견됨. c.3766-68delAAG 변이의 경우 heterozygous in-frame deletion 변이로 유전자 분리모형분석 결과 TAAD proband 환자와 PDA 환자 2명에서 발견됨.";
			case MYL3:		return "가족 구성원 중 3명의 형제자매에서 가족성심근비대증이 있는 구성원의 MYL3 유전자를 sequencing 한 결과 환자들에서 p.Glu143Lys 동형접합이 확인되었으며, 하나의 p.Glu143Lys 대립 유전자를 가진 가족들에게서도 심장 증상이 확인되었으나, 해당 변이는 150명의 대조군에서는 확인되지 않음.";
			case PCSK9:		return "정상 대조군과 비교하여 FH 환자 141명으로부터 1536개의 SNP를 확인하였고, 그중 FH 위험도를 높이는 11개의 SNP와 FH 위험도를 낮추는 3개의 SNP를 확인함. FH 위험도를 높이는 SNP는 APOB 유전자에서 7개로 가장 많이 발견되었고 LDLR, PCSK9에서 각각 1, 3개가 발견됨.";
			case PKP2:		return "ARVD 환자 439명 중 276명(63%)에서 mutation을 발견하였고 그 중 202명(46%)에서 PKP2 유전자 변이가 발견되었으며, 268명(61%)의 환자가 심실부정맥(ventricular arrhythmias)을 보였다. 562명의 가족 중 409명(73%)에서 mutation을 발견하였고, 그 중 342명(61%)이 PKP2 유전자 변이를 가지고 있었음. 또한 PKP2, DSP, DSG2, DSC2, JUP, PLN, TMEM43 유전자에 변이를 가지고 있는 가족의 경우(154/385, 40%) 그렇지 않은 경우(28/152, 18%)에 비해 ARVD 진단을 받은 비율이 더 높았음.";
			case PRKAG2:	return "심전도 결함 증상으로 인해 수행한 LMNA 유전자 검사 결과 변이가 발견되지 않았고, PRKAG2 유전자 검사 결과 c.1732T>C (p.Ser548Pro) 변이가 발견되었으며 해당변이는 200개의 정상 염색체에선 발견되지 않았음. 증상을 보이지 않는 환자의 형제에서도 발견되지 않았음.";
			case RYR2:		return "RYR2 유전자의 돌연변이를 가진 Proband의 87%, relative의 36%에서 돌연심장사(SCD) 또는 실신 증상을 보임. 증상의 발병시기는 Proband의 경우 평균 16세, relative의 경우 평균 45세로 발병함. ICD를 이식한 28명의 환자 중 8명에서 follow-up기간(65개월)동안 적절한 ICD 치료를 경험하였고 2명이 Electrical storm을 경험함.";
			case SMAD3:		return "264명의 환자 및 가족 중 233명이 TAD환자로 확인되었고 34명의 환자에서 유전자 변이가 발견됨. SMAD3 유전자에 변이는 9명에서 나타났으며 FBN1, TGFBR1, TGFBR2, TGFB2, COL3A1, ACTA2 유전자에서 각각 12, 1, 2, 3, 3, 4명이 발견되었음. 변이가 발견된 34명의 환자 중 29명이 TAD 환자였으며 7가지 SMAD3 유전자의 변이(c.401-6G>A, c.546delT, c.584_585insTC, c.715G>A, c.859C>T, c.887 T>C, c.1155-2A>G)를 확인함. SMAD3 변이를 가진 환자 9명 중 8명이 TAD 환자였고 그 중 6명이 TAD 가족력을 가지고 있었음.";
			case TMEM43:	return "ARVC가 의심되는 환자195명 중 28명에서 데스모좀 단백질의 변이를 보임. 그 중 6명이 TMEM43 유전자의 c.1073C>T (p.S358L) 변이를 가지고 있었고 5개의 TMEM43 유전자의 변이를 발견함. 6개의 TMEM43 유전자 변이 중 5개 변이가 2개이상의 in-silico program에서 pathogenic 변이로 확인됨.";
			case TNNI3:		return "제한성 심근병증(Restrictive cardiomyopathy; RCM) 및 비대성 심근병증(hypertrophic cardiomyopathy; HCM) 환자가 있는 대가족의 TNNI3 유전자를 확인한 결과 c.87A>G 변이가 확인 되었으며, 가족과 관련 없는 6명의 심근병증 환자에서도 TNNI3 missense 변이가 확인됨.";
			default:		return null;
		}
		return null;
	}
}
