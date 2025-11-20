package com.greencross.lims.report.genomescreen.kokr;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.greencross.lims.report.genomescreen.GenomeScreenTemplateN088;
import com.greencross.lims.report.genomescreen.GenomeScreenWithRiskScreenDto;
import com.gcgenome.lims.test.genomescreen.TestInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.awt.*;
import java.util.List;

// 고지혈증 지놈 스크린
@EqualsAndHashCode(callSuper = true)
@Getter
@Accessors(fluent = true)
public class GenomeScreenTemplateN088KoKr extends GenomeScreenTemplateKoKr<GenomeScreenResourceN088KoKr> implements GenomeScreenTemplateN088<GenomeScreenResourceN088KoKr>, RiskScreenTemplateKoKr<GenomeScreenResourceN088KoKr> {
	private final GenomeScreenResourceN088KoKr resource;
	private final String lblSummaryTestIntroTitleInfo = "고지혈증 지놈 스크린 검사에 대한 간단한 설명입니다.";
	private final String lblSummaryInfo = "본 검사는 최신 유전자 분석기법으로 유전성 고지혈증을 발병시킬 수 있는 유전자를 검사하며 기존 연구논문 결과를 분석하여 " +
										 "건강 관리에 도움이 될 수 있는 개인 맞춤형 정보를 제공합니다. 유전성 고지혈증과 관련된 병원성 변이가 발견된 경우 " +
										 "개인에 따라 증상이 없을 수 있으며(reduced penetrance) 정밀 검사가 필요할 수 있습니다.";
	private final String lblGuideTestTitle = "고지혈증 지놈 스크린 검사란?";
	private final TextBlock[] guideTest;
	private final String lblGuideDiseaseTitle = "유전성 고지혈증 질환은 이러한 특징이 있습니다.";
	private final TextBlock[] guideDisease;
	private final String lblGeneListInfo = "고지혈증 지놈 스크린 검사는 미국의학 유전학회(ACMG)의 권고에 따라 다음과 같은 질환 및 유전자들을 검사합니다.";
	private final TextBlock[] limitations;
	private final TextBlock[] references;
	private final TextBlock[] referencesRiskScreen;
	public GenomeScreenTemplateN088KoKr(GenomeScreenResourceN088KoKr resource, TestInfo testInfo) {
		super(testInfo);
		this.resource = resource;
		TextStyle textStyle = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8).paragraph(true);
		guideTest = new TextBlock[] {
				new TextBlock(textStyle.clone().fonts(resource.fontHeader(), resource.fontDefault()).color(Color.BLACK), "고지혈증(Hyperlipidemia) 또는 이상지질혈증(Dyslipidemia)"),
				new TextBlock(textStyle, "이란 혈액 중에 지질 성분의 수치가 정상범위를 벗어난 것으로 관상동맥질환, 뇌혈관질환과 같은 합병증의 위험도를 증가시킵니다. 고지혈증 또는 이상지질혈증의 " +
												"주요 위험요인으로는 비만, 당뇨, 음주, 흡연 등이 알려져 있으며, 여기에 유전적 요인도 관여하게 됩니다. " +
												"가족성 고콜레스테롤혈증, 가족성 고중성지방혈증 등 유전적 요인에 의한 고지혈증 또는 이상지질혈증의 경우 황색종(Xanthoma),황색판종(Xanthelasma), " +
												"간 비대, 신장 비대와 같은 증상이 나타날 수 있습니다.\n" +
												"고지혈증 또는 이상지질혈증은 동맥경화와 같이 심혈관계 질환 뿐만 아니라 다양한 순환계 질환과 연관성을 보이기 때문에 단독평가 보다는 총체적인 평가가 " +
												"필요합니다. " +
												"고지혈증 지놈 스크린 검사는 고지혈증 또는 이상지질혈증의 위험성을 높이는 31개의 유전자를 차세대염기서열분석 (Next-Generation Sequencing; NGS) " +
												"검사법으로 한 번에 검사하여 유전성 고지혈증 또는 유전성 이상지질혈증을 미리 진단하고 이를 통해 질환의 예방 및 조기 진단, 치료 효과의 향상을 기대할 수 " +
												"있는 검사입니다.")
		};
		guideDisease = new TextBlock[] {
				new TextBlock(textStyle, "유전성 고지혈증 또는 이상지질혈증 관련 유전자에서 병원성 변이(Pathogenic variant)가 발견되더라도 100% 질환이 발병하는 것은 아니며, " +
												"질환의 발병 시기 및 임상 양상은 개개인에 따라 매우 다양하게 나타납니다."),
				new TextBlock(textStyle, "유전성 고지혈증 또는 유전성 이상지질혈증은 전체 고지혈증 또는 이상지질혈증 중의 일부분을 차지하며, 유전성 고지혈증 또는 이상지질혈증의 발병 " +
												"기전 및 임상 양상은 관련 유전자에 따라 다르게 나타날 수 있습니다."),
				new TextBlock(textStyle, "본 검사에 포함된 유전성 고지혈증 또는 유전성 이상지질혈증은 고지혈증이나 이상지질혈증 뿐 아니라 " +
												"그 외 다양한 임상 양상 및 질환과도 연관되어 나타날 수 있습니다."),
				new TextBlock(textStyle, "유전성 고지혈증 또는 유전성 이상지질혈증과 관련된 유전자 검사에서 질환과 관련된 병원성 변이" +
												"(Pathogenic variant)가 발견되지 않았더라도 환경적 영향 및 생활습관 등 비유전성 원인으로 인한 고지혈증 " +
												"또는 이상지질혈증의 발병 가능성은 여전히 존재합니다.")
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
				new TextBlock(textStyle, "검사에 포함된 유전자는 전체 엑손을 포함하나, 일부 영역에서는 염기서열 해독이 충분하지 않을 수 있습니다. " +
												"또한, 상동성이 높은 염기서열이 존재하는 경우 염기서열 해독이 정확하지 않을 수 있으며, 큰 " +
												"결실 또는 중복, 단백질을 만들지 않는 서열 부위에 존재하는 변이는 검출이 어려울 수 있습니다.")
		};
		TextStyle referenceStyle = new TextStyle().color(resource.colorText()).fonts(resource.fontScientific(), resource.fontText(), resource.fontDefault()).fontSize(8).paragraph(true);
		references = new TextBlock[] {
				new TextBlock(referenceStyle, "Genetic causes of monogenic heterozygous familial hypercholesterolemia: a HuGE prevalence review"),
				new TextBlock(referenceStyle, "Identification of people with heterozygous familial hypercholesterolemia. Current opinion in lipidology. 2012"),
				new TextBlock(referenceStyle, "Clinical Chemistry and Laboratory Medicine. 2008 Starr B, et al"),
				new TextBlock(referenceStyle, "Maio A., Dowd F.J. Hypertriglyceridemia. The Comprehensive Pharmacology. 2010"),
				new TextBlock(referenceStyle, "한국지질동맥경화학회, 이상지질혈증 치료지침(4판), 2018")
		};
		referencesRiskScreen = new TextBlock[] {
				new TextBlock(referenceStyle, "Atherosclerosis. 1995;113(2):157-66."),
				new TextBlock(referenceStyle, "Atherosclerosis. 1997; 129: 231-239."),
				new TextBlock(referenceStyle, "Atherosclerosis. 2010;211(1):28-9."),
				new TextBlock(referenceStyle, "Front Genet. 2018;9:112."),
				new TextBlock(referenceStyle, "Diabetes Care. 2017;40(4):529-537."),
				new TextBlock(referenceStyle, "Lipids Health Dis. 2007;6:7."),
				new TextBlock(referenceStyle, "J Nutr. 2004;134(10):2517-22."),
				new TextBlock(referenceStyle, "Hum Mol Genet. 2008;17(18):2894-9."),
				new TextBlock(referenceStyle, "J Am Coll Cardiol. 2014;64(3):267-77."),
				new TextBlock(referenceStyle, "J Lipids. 2015;2015:742408."),
				new TextBlock(referenceStyle, "Circ Cardiovasc Qual Outcomes. 2011;4(3):337-45."),
				new TextBlock(referenceStyle, "Genet Test Mol Biomarkers. 2016;20(12):758-765."),
				new TextBlock(referenceStyle, "J Lipid Res. 1994;35(11):1965-75."),
				new TextBlock(referenceStyle, "PLoS One. 2014;9(10):e110258."),
				new TextBlock(referenceStyle, "Sci Rep. 2015;5:8243.")
		};
	}
	private String diseaseName(DiseaseN088 disease) {
		switch (disease) {
			case 고콜레스테롤혈증:		return "고콜레스테롤혈증";
			case 약물적합도:			return "약물적합도";
			case 지질단백결핍:		return "지질단백결핍";
			case 복합형_이상지질혈증:	return "복합형 이상지질혈증";
			case 고중성지방혈증:		return "고중성지방혈증";
			default:				return "";
		}
	}
	private String diseaseInfo(DiseaseN088 disease) {
		switch (disease) {
			case 고콜레스테롤혈증:		return "콜레스테롤 대사의 이상으로 인해 혈중 콜레스테롤 농도가 올라가는 이상지질혈증";
			case 약물적합도:			return "고지혈증 치료 약물의 대사작용 관련";
			case 지질단백결핍:		return "에너지 대사를 위해 콜레스테롤과 지질을 운반하는 지질단백질 형성의 이상으로 지질단백질이 결핍되는 이상지질혈증";
			case 복합형_이상지질혈증:	return "지질단백을 형성하는 과정의 이상으로 혈중 콜레스테롤과 중성지방 농도가 함께 증가하는 이상지질혈증";
			case 고중성지방혈증:		return "중성지방의 물질대사 이상으로 중성지방의 합성이 증가하거나 혈장에서 제거되지 못하여 혈중 중성지방 농도가 증가하는 이상지질혈증";
			default:				return "";
		}
	}
	private String diseaseSubName(DiseaseSubN088 diseaseSub) {
		switch (diseaseSub) {
			case 가족성_고콜레스테롤혈증:			return "가족성 고콜레스테롤혈증";
			case 뇌건황색종증:					return "뇌건황색종증";
			case 시토스테롤혈증:					return "시토스테롤혈증";
			case 고알파지질단백혈증:				return "고알파지질단백혈증";
			case 스타틴_부작용:					return "스타틴 부작용";
			case 저베타지질단백혈증:				return "저베타지질단백혈증";
			case 저알파지질단백혈증:				return "저알파지질단백혈증";
			case 이상베타지질단백혈증:				return "이상베타지질단백혈증";
			case 복합_고지질혈증:					return "복합고지질혈증";
			case 가족성_지질단백_지질분해효소_결핍증:	return "가족성 지질단백 지질분해효소 결핍증";
			case 고중성지방혈증:					return "고중성지방혈증";
			case 알스트롬_증후군:					return "알스트롬 증후군";
			default:							return "";
		}
	}
	@Override
	public String diseaseSubInfo(DiseaseSub diseaseSub) {
		if(diseaseSub instanceof DiseaseSubN088) switch ((DiseaseSubN088) diseaseSub) {
			case 가족성_고콜레스테롤혈증:			return "가족성 고콜레스테롤혈증(familial hypercholesterolemia, FH)은 상염색체 우성 유전 질환이며, 혈중 LDL-콜레스테롤 수치 증가와 정상 중성지방 수치, 건 황색종, 조발성 관상동맥경화증 등이 특징입니다. 동형접합에서 20대에 관상동맥질환 및 대동맥판상부협착증을 일으킬 수 있습니다. 동협접합은 이형접합과 달리 보통 4~8배 이상 LDL 콜레스테롤 농도가 증가됩니다.";
			case 뇌건황색종증:					return "뇌건황색종증은 담즙합성의 장애로 인해 지질이 축적되는 질환으로 상염색체 열성으로 유전되는 질환입니다. 스테롤 27-하이드록시레이즈(sterol 27-hydroxylase)라는 효소의 결핍으로 인해 콜레스테롤과 콜레스탄올이 사실상 모든 조직에 축적될 수 있는 질환입니다. 지질이 축적되면서 뇌, 힘줄, 피부, 폐, 뼈 등에 황색종, 결절 등이 형성되고, 특히 소아 때 백내장과 결절성 황색종이 나타나면서 사춘기 이후 진행성 소뇌 운동실조가 동반되는 것이 특징입니다. 이 질환은 서서히 진행하며, 치료를 통해 그 진행양상을 바꿀 수 있습니다. 그러나, 치료를 하지 않을 경우 기대여명은 대개 50-60세 정도이지만, 치료를 할 경우 정상적인 수명도 기대할 수 있습니다.";
			case 시토스테롤혈증:					return "시토스테롤혈증은 음식으로 섭취한 식물성 스테롤이 과도하게 흡수돼 축적되는 질환입니다. 올리브유를 비롯한 식물성 유지류, 견과류 등에 풍부한 식물성 스테롤은 정상인에서는 많이 먹어도 거의 흡수되지 않고 배설되며, 오히려 콜레스테롤 흡수를 억제해 고지혈증에 좋은 음식으로 알려져 있습니다. 그러나 시토스테롤혈증 환자에서는 이러한 음식에 포함돼 있는 식물성 스테롤이 모두 흡수되어 혈관에 쌓이게 되고 동맥경화증을 유발할 수 있습니다. 일부 환자에서는 고지혈증으로 나타나기도 하지만 일부에서는 콜레스테롤 수치는 거의 정상인데 피부에 황색종이 나타나다가 협심증과 같은 심혈관 질환의 증상이 갑자기 나타나기도 합니다. 해외에서는 젊은 연령에 심근경색으로 급사한 환자에서 사망 후 시토스테롤혈증으로 진단된 경우도 있습니다.";
			case 고알파지질단백혈증:				return "고알파지질단백혈증은 일반적으로 혈중 고밀도지질단백질(High density lipoprotein: HDL)값의 상승, 총 콜레스테롤 값의 가벼운 상승을 나타내지만, 중성지방 값은 정상으로 나타납니다. 알코올, 에스트로겐, 운동부하 등의 유인 없이 혈중 HDL 콜레스테롤이 70mg/dL 이상을 나타내며, 환자의 HDL 콜레스테롤은 지질조성, 아포단백질 구조상 이상이 나타나지 않는 것이 특징입니다.";
			case 스타틴_부작용:					return "약물에 대한 반응도는 약물대사에 관여하는 여러 유전자의 변이에 의해 달라질 수 있습니다. SCLO1B1 유전자는 스타틴(statin) 계열 약제를 간세포로 운반하는 운반체(OATP1B1)를 암호화하고 있는 유전자로 혈중 스타틴(statin) 농도 조절에 연관된 유전자 입니다. SCLO1B1 유전자의 유전자형에 대한 개인적 차이는 운반체의 활성도에 대한 차이를 가져올 수 있습니다. 운반체의 활성도가 감소하면 스타틴(statin) 계열 약제의 혈중 농도가 상승하기 때문에 약물민감성이 증가할 가능성이 높고 그에 따른 약물 부작용에 대한 위험도 또한 증가할 수 있습니다.";
			case 저베타지질단백혈증:				return "저베타지단백혈증(hypobetalipoproteinemia, HBL)은 저밀도지질단백질(LDL)의 농도가 정상대조군의 5% 미만인 질환으로, 지방을 흡수하고 전달하지 못해서 혈중에 저밀도지질단백질의 농도가 매우 낮아져 있는 질환입니다. 이 질환은 동맥경화를 일으키는 저밀도지질단백질 수치가 낮아 죽상경화증에 의한 심혈관계 질환 발생은 낮습니다. 그러나 일부 환자에서는 운동계 혹은 신경계의 경한 장애와 각종 암, 폐 질환 및 위, 장관계의 질환 위험이 증가할 수 있습니다. 저베타지단백혈증 중 가족력이 있는 경우에 가족성 저베타지단백혈증이라 하고, 이 중에서 이형접합체인 경우 대부분 건강하나 동형접합체의 경우, 무베타지단백혈증과 구분할 수 없을 정도의 심각한 임상양상을 보이기도 합니다.";
			case 저알파지질단백혈증:				return "알파지질단백질이라고도 불리는 고밀도지질단백질(HDL)은 혈액 속에 필요 이상의 지질과 콜레스테롤이 간으로 흡수되어 다른 조직으로 분배되거나 몸에서 제거되도록 돕는 역할을 합니다. 좋은 콜레스테롤로 알려져 있는 고밀도지질단백질의 결핍 (HDL-콜레스테롤 <20 mg/dL)은 50세 이전 심혈관 질환에 대한 위험도를 증가시키며, 가족성 관상동맥 심장질환의 20-30%가 저알파지질단백혈증 때문인 것으로 알려져있습니다.";
			case 이상베타지질단백혈증:				return "이상베타지단백혈증은 콜레스테롤과 중성지방이 함께 혈중에 증가하는 상염색체 열성(드물게 상염색체 우성) 유전성 질환으로 말초혈관질환, 관상동맥질환의 위험이 증가합니다. 이상베타지단백혈증은 APOE 유전자의 병원성 변이로 인하여 혈액속의 지단백이 간세포의 지단백 수용체에 결합하지 못해 혈장으로부터 제거가 되지않아 발생합니다. 증상은 대부분 20세 이전에는 나타나지 않고 성인이 되어서야 나타납니다. 환자의 반 수 이상에서 황색종(피부에 지방이 축적되는 것)이 눈꺼풀, 손바닥, 발바닥, 무릎이나 팔꿈치의 인대에 나타납니다. 하지의 말초혈관질환과 관상동맥질환 등이 흔하게 발생할 수 있습니다.";
			case 복합_고지질혈증:					return "복합고지질혈증은 혈중 콜레스테롤과 중성지방의 농도가 증가하는 질환으로 고지혈증 중에서 가장 흔한 유형입니다. 혈중 초저밀도지질단백질(VLDL) 및/ 또는 저밀도지질단백질(LDL) 수치가 높게 나타나며 가족성 고콜레스테롤혈증과 다르게 환자의 10-20% 정도만 아동기 고지혈증을 보이며, 황색종(Xanthomas) 증상이 나타나는 경우는 드뭅니다. 조기에 발병하는 관상동맥질환의 약 10%가 복합고지질혈증에 의해 일어난다는 연구결과가 있습니다.";
			case 가족성_지질단백_지질분해효소_결핍증:	return "가족성 지질단백 지질분해효소 결핍증은 지질의 대사에 중요한 역할을 하는 지단백질지방분해효소(lipoprotein lipase)의 결핍으로 중성지방이 체내에 축적되는 질환입니다. 지단백질지방분해효소의 결핍 혹은 조효소인 Apo C-II의 결핍으로 발생하는 유전적 질환으로, 중성지방을 체내에서 효과적으로 제거하는 효소의 역할이 저하되어 결과적으로 중성지방이 체내에 쌓이게 되는 질환입니다. 특징적으로 중성지방이 독보적으로 높게 측정이 되며, 혈액이 우유를 섞어 놓은 양상(milky or lipemic)을 보입니다. 가족성 지질단백 지질분해효소 결핍증은 상염색체 열성으로 유전되며, 지단백질지방분해효소(lipoprotein lipase) 유전자나 Apo C-II 유전자의 결함으로 발생하며, 그 외에도 최근 여러 유전자가 발견되고 있습니다.";
			case 고중성지방혈증:					return "고중성지방혈증은 상염색체 우성으로 유전되는 질환으로 혈중 콜레스테롤 수치는 증가하지 않지만 중성지방이 증가하는 질환입니다. 보통 성인이 될 때까지 중성지방 상승은 뚜렷하지 않습니다. 원인은 여러가지로 정확한 기전은 알려지지 않으며, 질환으로 나타나기 위해서는 이차적 요인을 필요로 하는 것으로 보고 있습니다. 고중성지방혈증이 있는 환자는 특징적으로 중성지방치가 200~500mg/dL 으로 증가하지만 LDL-콜레스테롤 수치는 정상입니다. 비만, 당뇨병, 갑상선기능저하증, 에스트로겐 치료, 알코올 등과 같은 이차적 요인에 의해 중성지방 수치가 더 상승할 수 있습니다. 중성지방이 1000mg/dL 이상 증가하면 췌장염 위험이 증가합니다. 일부 환자에서 관상동맥질환 위험이 증가할 수 있습니다.";
			case 알스트롬_증후군:					return "알스트롬 증후군은 ALMS1이라는 유전자의 병원성 변이에 의해 유발되는 매우 드문 상염색체 열성유전 질환으로 1959년 스웨덴의 Carl-Henry Alstrom에 의해 처음 명명되었습니다. 알스트롬 증후군은 시력과 청력의 장애, 유년기의 비만, 당뇨 그리고 서서히 진행되는 신장 기능 장애가 특징이며, 개인에 따라 특징적인 증상들이 다양하게 나타납니다. 간혹 알스트롬 증후군과 관련되어 추가적인 증상으로 심근의 질환(Cardiomyoathy: 심근증), 피부의 이상, 그리고 다른 기관들에 영향을 미치는 질환이 나타날 수 있으며, 지능은 질환에 의하여 영향을 받지는 않습니다. 알스트롬 증후군은 남성과 여성에 있어서 동일한 비율로 영향을 받습니다. 의료문헌에는 100개 미만의 사례가 보고되어 있으나, 연구가들은 전 세계적으로 약 170명의 사례가 있다고 보고하고 있습니다. 알스트롬 증후군의 몇몇 사례들은 진단을 받지 못하는 경우가 있어, 실제로 어느 정도의 빈도를 보여주는지 정확히 알기는 어렵습니다.";
			default:							return null;
		}
		return null;
	}
	@Override
	public String diseaseSubReference(DiseaseSub diseaseSub) {
		if(diseaseSub instanceof DiseaseSubN088) switch ((DiseaseSubN088) diseaseSub) {
			case 가족성_고콜레스테롤혈증:			return "03.예방 및 치료 1~3 참조";
			case 뇌건황색종증:					return "03.예방 및 치료 4~6 참조";
			case 시토스테롤혈증:					return "03.예방 및 치료 7~9 참조";
			case 고알파지질단백혈증:				return "03.예방 및 치료 10~12 참조";
			case 스타틴_부작용:					return "03.예방 및 치료 13~14 참조";
			case 저베타지질단백혈증:				return "03.예방 및 치료 15~17 참조";
			case 저알파지질단백혈증:				return "03.예방 및 치료 18~20 참조";
			case 이상베타지질단백혈증:				return "03.예방 및 치료 21~22 참조";
			case 복합_고지질혈증:					return "03.예방 및 치료 23~24 참조";
			case 가족성_지질단백_지질분해효소_결핍증:	return "03.예방 및 치료 25~26 참조";
			case 고중성지방혈증:					return "03.예방 및 치료 27~29 참조";
			case 알스트롬_증후군:					return "03.예방 및 치료 30~32 참조";
			default:							return null;
		}
		return null;
	}
	public String clinicalMeaning(GeneN088 gene) {
		switch(gene) {
			case ABCA1:		return "연구대상자의 ABCA1 유전자 다형성 genotype을 취합한 자료 분석 결과 (rs2230806 A allele carrier HDL-C: SMD=0.18, 95% CI=0.13-0.24, P<0.001; rs2230808 A allele carrier TC: SMD=0.15, 95% CI=0.08-0.22, P<0.001; rs2066714 G allele carrier TC: SMD=0.13, 95% CI=0.04-0.21, P<0.01; HDL-C: SMD=0.10, 95% CI=0.02-0.18, P=0.02)로 각 allele carrier의 혈중 지방 농도가 대조군에 비해 더 높은 것으로 보아 고지혈증과의 연관성이 확인됨.";
			case ABCG5:		return "ABCG5 유전자의 다형성 중 i7892T>C 다형성과 HDL-c 농도 사이의 연관성을 확인함. ABCG5 유전자의 i7892T>C 다형성 그룹에서 p < 0.05일 때, TT VS TC + CC로 분석하였을 때 HDL-c 농도가 TT에서 더 높은 유의미한 결과를 보임 (TT ; 46.1 ± 13.7 , TC + CC ; 44.0 ± 11.7, P value ; 0.013)";
			case ABCG8:		return "ABCG8 유전자의 다형성 중 251A>G 다형성과 HDL-c 농도 사이의 연관성을 확인함. ABCG8 유전자의 251A>G 다형성 그룹에서 p < 0.05일 때, TT VS TC + CC로 분석하였을 때 HDL-c 농도가 GG에서 더 높은 유의미한 결과를 보임 (AA ; 1.19 ± 0.28 mmol/L, AG ; 1.19 ± 0.26 mmol/L, GG ; 1.60 ± 0.47 mmol/L, P value ; 0.020)";
			case ALMS1:		return "대표적인 증상 중 하나로 고지혈증이 나타나는 알스트롬 증후군이 있는 환자들의 염기 서열을 분석했을 때 ALMS1 유전자에서 10535ins, 8383C>T, 10609G>A, 10775delC, 11449C>T의 homozygous 변이와 8395insA의 heterozygote compound 변이를 확인함.";
			case ANGPTL3:	return "ANGPTL3 유전자의 다형성과 LDL-c, HDL-c 농도 사이의 연관성을 확인함. ANGPTL3 유전자의 다형성을 보유하지 않을 경우 p < 0.05일 때 (LDL-c ; 2.8 ± 0.9 (1.0–7.3) mmol/L, HDL-c ; 1.6 ± 0.4 (0.8–2.8), p <0.001), ANGPTL3 유전자의 S17X 다형성이 이형접합일 경우 (LDL-c ; 2.5 ± 0.8 (0.5–4.4) mmol/L, HDL-c ; 1.3 ± 0.4 (0.6-2.0) mmol/L)), ANGPTL3 유전자의 다형성이 이형접합 + 동형접합일 경우 (LDL-c ; 1.1 ± 0.4 (0.5–1.4) mmol/L, HDL-c ; 0.6 ± 0.2 (0.3-1.2) mmol/L, p <0.001)로 다형성이 이형접합 + 동형접합일 때 농도가 낮아지는 유의미한 결과를 보임. ";
			case APOA1:		return "메타분석을 통해 마오난족에서는 APOA1 유전자의 rs964184 다형성과 HDL-c 농도 사이의 연관성의 영향을 확인하였고, 한족에서는 APOA1 유전자의 rs964184 다형성과 TG 농도 사이의 연관성을 확인함. 마오난족의 APOA1 유전자의 rs964184 다형성과 HDL-c 농도 연관성 비교에서는 p < 0.05일 때, 유전형이 CG+GG일때 농도가 더 낮아지는 유의미한 결과를 보임 (CC ; 1.65 ± 0.02 mmol/L, CG+GG ; 1.55 ± 0.03, p valuel ; 0.003). 한족의 APOA1 유전자의 rs964184 다형성과 TG 농도 연관성 비교에서는 p < 0.05일 때, 유전형이 CG+GG일때 농도가 더 높아지는 유의미한 결과를 보임 (CC ; 1.04 (0.80) mmol/L, CG+GG ; 1.23 (1.16), p valuel ; 0.028).";
			case APOA2:		return "고콜레스테롤혈증이 있는 환자들의 염기 서열을 분석했을 때 APOA2 유전자에서 -265T>C의 변이를 확인함.";
			case APOA5:		return "4가지 SNP(rs662799, rs3135506, rs2075291, and rs2266788) 모두 중성지방농도 수준과 유의 한 연관성을 확인함. rs662799-C, rs3135506-C, rs2075291-T 및 rs2266788-C 대립 유전자형을 가진 그룹과 해당 유전형을 가지지 않은 그룹을 비교했을 때 rs662799-C, rs3135506-C, rs2075291-T 및 rs2266788-C 유전자형을 가진 그룹이 각각 16.0 %, 15.1 %, 17.1 % 및 12.4 % 더 높은 중성지방농도 수준을 보임. 이러한 결과는 백인과 동아시아인 등에서 보고 된 기존 연구결과와 일치함. 이는 민족성과 무관함을 나타냄.";
			case APOB:		return "정상 대조군과 비교하여 FH 환자 141명으로부터 1536개의 SNP를 확인하였고, 그중 FH 위험도를 높이는 11개의 SNP와 FH 위험도를 낮추는 3개의 SNP를 확인함. FH 위험도를 높이는 SNP는 APOB 유전자에서 7개로 가장 많이 발견되었고 LDLR, PCSK9에서 각각 1, 3개가 발견됨. 그 중 APOB 유전자의 SNP rs12720762는 가장 높은 FH 위험도(OR 14.78, p<0.001)를 보였음.";
			case APOC2:		return "APOC2 유전자의 c.86A > CC 다형성과 중성지방 농도 사이의 연관성을 확인함. c.86A > CC 다형성을 갖는 환자의 TG (mmol/L)는 1차 ; 12.41 (mmol/L), 2차 ; 4.5 (mmol/L) 였고, 아버지는 1.3 (mmol/L), 어머니는 0.83 (mmol/L), 여동생은 0.87 (mmol/L), 남동생은 0.51 (mmol/L), 아들은 1.18 (mmol/L)로 환자를 제외한 평균 TG 농도는 0.94 (mmol/L) 였음. TG의 Reference 범주는 0.6-1.7에 해당하였고, APOC2 유전자의 c.86A > CC 다형성을 갖는 환자의 수치가 유의미하게 높은 것이 확인됨";
			case APOC3:		return "APOC3 유전자의 rs5128 다형성과 TG, TC, LDL-c 농도 사이의 연관성을 확인함. rs5128 다형성의 Dominant model(CC vs. CG + GG)로 TG와의 연관성을 보았고 p < 0.05일 때 SMD: 0.33, 95% CI: 0.23-0.44, P < 0.00001, TC는 SMD: 0.15, 95% CI: 0.09-0.22, P < 0.00001, LDL-c는 SMD: 0.11, 95% CI: 0.04-0.17, P = 0.001로 유의미한 결과를 얻음";
			case CETP:		return "한국인 서울시 그룹과 분당구 그룹에서 CETP 유전자의 다형성과 HDL-c 농도 사이의 연관성의 영향을 확인함. 특히, CETP 유전자의 2개의 다형성(rs6499861, rs6499863)에서 높은 연관성이 확인됨. 메타 분석에서 CETP 유전자의 rs6499861 변이의 HDL-c 농도는 -2.50mg/dL였고, rs6499863 변이의 HDL-c 농도는 -2.30mg/dL로 CETP 유전자의 다형성이 HDL-c 농도의 감소에 영향을 준다는 것을 확인함.";
			case CREB3L3:	return "고지혈증 환자 413명과 건강한 대조군 324명의 APOC2, GPIHBP1, LMF1, CREB3L3, ZHX3 유전자 염기서열을 분석한 결과 고지혈증 환자에서 47개의 희귀 변이, 건강한 대조군에서 16개의 희귀 변이가 확인되었음(OR = 2.3, p=0.005) post hoc 분석 결과 CREB3L3에서 희귀 변이에 대한 개별 유전자의 부담이 두드러지게 나타남.";
			case CYP27A1:	return "고지혈증의 증상이 나타나는 뇌건황색종증 있는 환자들의 염기 서열을 분석했을 때 CYP27A1 유전자에서 Arg362Cys, Arg446Cys의 변이를 확인함.";
			case CYP7A1:	return "중성지방 시험에 포함된 51명의 환자들의 CYP7A1 유전자의 A-278C 다형성과 중성지방 농도 사이의 연관성을 확인함. CYP7A1 유전자의 A-278C 다형성 유전형이 CC인 환자의 중성지방 농도가 높은 결과를 보임 (AA ; -0.28 ± 0.19 mmol/L, AC ; -0.16 ± 0.12 mmol/L, CC ; 1.79 ± 1.61 mmol/L)";
			case GPD1:		return "고중성지방혈증이 있는 환자들의 염기 서열을 분석했을 때 GPD1 유전자에서 3번 인트론의 c.361-1G>C 의 변이를 확인함.";
			case GPIHBP1:	return "고중성지방혈증 환자들의 염기 서열을 분석했을 때 GPIHBP1 유전자에서 c.331A>C 변이와 c.413_429del의 변이를 확인함.";
			case LCAT:		return "98명의 대상자 중 28명(29%)에서 LCAT 유전자의 다형성과 HDL-c 농도 사이의 연관성을 확인함. 앞선 28명의 임상시험 대상자와 대조군의 평균 HDL cholesterol (mg/dl)와 Triglycerides (mg/dl)을 비교하였을 때, 28명의 임상시험 대상자의 HDL cholesterol (mg/dl)는 59% 감소되었고, Triglycerides (mg/dl)는 37% 증가된 농도가 확인됨. ";
			case LDLRAP1:	return "고콜레스테롤혈증이 있는 환자들의 염기 서열을 분석했을 때 LDLRAP1 유전자에서 c.863C>T (p.Ser288Leu)의 변이를 확인함.";
			case LIPA:		return "고콜레스테롤혈증이 있는 환자들의 염기 서열을 분석했을 때 LIPA 유전자에서 836번째 염기서열인 G가 T로 치환되면서 Gly이 종결코돈으로 치환되는 G245*의 변이를 확인함.";
			case LIPC:		return "LIPC 유전자의 250G>A 다형성과 LDL 중성지방 농도 사이의 연관성을 확인함. LIPC 유전자 다형성 유전형이 GG 일 때 LDL 중성지방 농도는 0.35 ± 0.15, GA 일 때 0.41 ± 0.21, AA 일 때 0.43 ± 0.18로 AA 일 때 LDL 중성지방 농도가 가장 높은 유의미한 결과를 보임.";
			case LMF1:		return "고중성지방혈증이 있는 환자의 염기 서열을 분석했을 때 LMF1 유전자에서 1317번째 염기서열인 C가 T로 치환되면서 Tyr이 종결코돈으로 치환되는 Y439X의 변이를 확인함.";
			case LPL:		return "코호트 연구를 통해 LPL 유전자의 다형성과 TG 농도 사이의 연관성의 영향을 확인함. 고지혈증 환자 212명에서 LPL 유전자의 S447X 다형성에서 SS type 과 SX+XX type을 비교하였을 때 SX+XX type에서 TG 농도가 더 낮게 측정됨. 161명의 대조군의 SS type의 TG 농도는 92.1 ± 36.66 mg/dl 과 SX+XX type의 농도는 87.9 ± 41.30 mg/dl으로 유의미한 차이는 없었으나, 고지혈증 환자 212명의 SS type의 TG 농도는 383.8 ± 357.69 mg/dl 과 SX+XX type의 농도는 267.2 ± 65.24 mg/dl으로 유의미한 결과를 보임. LPL 유전자의 다형성이 TG 농도에 영향을 준다는 것을 확인함.";
			case MTTP:		return "저베타지질단백혈증 있는 환자들의 염기 서열을 분석했을 때 MTTP 유전자에서 c.2593G>T (p.G865X), c.2211delT, c.148-2A>G, c.307A >T의 변이를 확인함.";
			case PCSK9:		return "정상 대조군과 비교하여 FH 환자 141명으로부터 1536개의 SNP를 확인하였고, 그중 FH 위험도를 높이는 11개의 SNP와 FH 위험도를 낮추는 3개의 SNP를 확인함. FH 위험도를 높이는 SNP는 APOB 유전자에서 7개로 가장 많이 발견되었고 LDLR, PCSK9에서 각각 1, 3개가 발견됨.";
			case SAR1B:		return "저베타지질단백혈증 있는 환자들의 염기 서열을 분석했을 때 SAR1B 유전자에서 c.109G>A (p.Gly37Arg), c.409G>A, c.75_76delTG, c.537T>A, c.555_558dupTTAC, c.349-1G>C, c.536G>T, c.542T>C의 변이를 확인함.";
			case SCARB1:	return "메타-분석을 통해 무라오족에서는 SCARB1 유전자의 rs5888 다형성과 HDL-c 농도 사이의 연관성을 확인함. 무라오의 SCARB1 유전자의 rs964184 다형성과 HDL-c 농도 연관성 비교에서는 p < 0.05일 때, 유전형이 TT일 때 농도가 더 낮아지는 유의미한 결과를 보임 (CC ; 1.76 ± 0.40 mmol/L, CT ; 1.76 ± 0.40, CT ; 1.61 ± 0.47, p valuel ; 0.041).";
			case STAP1:		return "가족성 고콜레스테롤혈증과 연관된 CAD/MI 환자들의 염기 서열을 분석했을 때 STAP1 유전자에서 c.139A>G (p.Thr47Ala)의 변이를 확인함.";
			default: 		return null;
		}
	}
	private final String lblSummaryTestIntroTitleInfoRiskScreen = "고지혈증 위험도 검사에 대한 간단한 설명입니다.";
	private final String lblSummaryInfoRiskScreen = "본 검사는 최신 유전자 분석기법으로 고지혈증 위험도를 상승시킬 수 있는 유전요인을 검사하며, 기존 연구논문 결과를 분석하여 " +
													"건강 관리에 도움이 될 수 있는 개인 맞춤형 정보를 제공합니다. 하지만, 질병의 진단과는 무관하므로 진단 및 치료 결정을 위해서는 " +
													"반드시 의사와의 상담이 필요합니다.";
	private final String lblGraphDiseaseInfo	= "질환정보";
	private final String lblGraphSummary		= "종합결과";
	private final String lblGraphDisease		= "상세항목";
	private final String lblGraphRisk			= "위험도";
	private final String lblGraphGene			= "검사 유전자";
	private String diseaseRiskScreenName(DiseaseRiskScreenN088 disease) {
		switch (disease) {
			case 고지혈증: 		return "고지혈증(이상지질혈증)";
			case 치료제부작용:	return "치료제 부작용";
			default:			return "";
		}
	}
	private String diseaseSubRiskScreenName(DiseaseSubRiskScreenN088 diseaseSub) {
		switch (diseaseSub) {
			case LDL콜레스테롤: 				return "LDL 콜레스테롤";
			case 중성지방:					return "중성지방";
			case 스타틴_약물_복용시_부작용:		return "치료제 부작용";
			default:						return "";
		}
	}

	private final String apoeInfo = "APOE 유전자는 중성지방을 포함하는 혈장의 지단백들이 세포로 흡수될 " +
									"수 있게 돕는 역할을 합니다. 증가형 APOE 유전자형으로 인해 혈중 " +
									"지단백이 흡수되지 못하면 혈중 LDL콜레스테롤 혹은 중성지방이 높아지게 " +
									"됩니다.";
	private final String apoa5Info = "APOA5 유전자는 중성지방을 포함하는 혈장의 지단백들이 세포로 흡수" +
									 "될 수 있게 돕는 역할을 합니다. 증가형 APOA5 유전자형으로 " +
									 "인해 혈중 지단백이 흡수되지 못하면 혈중 중성지방이 높아지게 됩니다.";
	private final String coq2Info = "항고지혈증제인 스타틴 약물은 coenzyme Q10 합성을 방해하여 근육병증과 " +
									"같은 부작용을 유발하는 것으로 알려져 있습니다. COQ2 유전자는 " +
									"coenzyme Q10 합성에 관여하는 유전자로 증가형 COQ2 유전자는 스타틴 " +
									"약물의 부작용 위험도를 높일 수 있습니다.";
	public String geneRiskScreenInfo(GeneRiskScreen gene) {
		if(gene instanceof GeneRiskScreenN088) switch((GeneRiskScreenN088)gene) {
			case APOE:	return apoeInfo;
			case APOA5:	return apoa5Info;
			case COQ2:	return coq2Info;
			default:	return null;
		}
		return null;
	}

	@Override
	public String clinicalMeaning(GeneRiskScreen gene) {
		return null;
	}

	@Override
	public String diseaseName(Disease disease) {
		if(disease instanceof DiseaseN088) return diseaseName((DiseaseN088) disease);
		return null;
	}
	@Override
	public String diseaseInfo(Disease disease) {
		if(disease instanceof DiseaseN088) return diseaseInfo((DiseaseN088) disease);
		return null;
	}
	@Override
	public String diseaseSubName(DiseaseSub diseaseSub) {
		if(diseaseSub instanceof DiseaseSubN088) return diseaseSubName((DiseaseSubN088) diseaseSub);
		return null;
	}
	@Override
	public String clinicalMeaning(Gene gene) {
		if(gene instanceof GeneN088) return clinicalMeaning((GeneN088) gene);
		return null;
	}
	@Override
	public String diseaseRiskScreenName(DiseaseRiskScreen disease) {
		if(disease instanceof DiseaseRiskScreenN088) return diseaseRiskScreenName((DiseaseRiskScreenN088) disease);
		return null;
	}
	@Override
	public String diseaseSubRiskScreenName(DiseaseSubRiskScreen diseaseSub) {
		if(diseaseSub instanceof DiseaseSubRiskScreenN088) return diseaseSubRiskScreenName((DiseaseSubRiskScreenN088) diseaseSub);
		return null;
	}
	@Override
	public String summaryPositive(DiseaseRiskScreen disease) {
		if(DiseaseRiskScreenN088.고지혈증 == disease)		return "고지혈증의 위험도를 증가시키는 유전요인이 검출되어 고지혈증의 적극적 (약물) 치료 혹은 예방을 위한 관리가 권장됩니다.";
		else if(DiseaseRiskScreenN088.치료제부작용 == disease)return "고지혈증 치료제 중 하나인 스타틴 복용 시 부작용의 위험도를 증가시키는 유전 요인이 검출되어 현재 스타틴을 복용하고 있다면 부작용 발생에 관한 주의가 필요합니다.";
		return null;
	}
	@Override
	public String summaryNegative(DiseaseRiskScreen disease){
		if(DiseaseRiskScreenN088.고지혈증 == disease)		return "고지혈증의 위험도를 증가시키는 유전요인이 검출되지 않았습니다.";
		else if(DiseaseRiskScreenN088.치료제부작용 == disease)return "고지혈증 치료제 부작용과 관련된 유전요인이 검출되지 않았습니다.";
		return null;
	}
	@Override
	public List<String> interpretation(DiseaseRiskScreen disease, GeneRiskScreen gene, Snv snv, String genotype) {
		if(DiseaseRiskScreenN088.고지혈증 == disease) {
			if(GeneRiskScreenN088.APOE == gene) {
				if(SnvRiskScreenN088.APOE_ == snv) {
					if("e2e2".equalsIgnoreCase(genotype))		return List.of(고지혈증_APOE_E2E2);
					else if("e2e3".equalsIgnoreCase(genotype))	return List.of(고지혈증_APOE_E2E3);
					else if("e2e4".equalsIgnoreCase(genotype))	return List.of(고지혈증_APOE_E2E4);
					else if("e3e3".equalsIgnoreCase(genotype))	return List.of(고지혈증_APOE_E3E3);
					else if("e3e4".equalsIgnoreCase(genotype))	return List.of(고지혈증_APOE_E3E4);
					else if("e4e4".equalsIgnoreCase(genotype))	return List.of(고지혈증_APOE_E4E4);
				}
			} else if(GeneRiskScreenN088.APOA5 == gene) {
				if(SnvRiskScreenN088.APOA5_553 == snv) {
					if("GG".equalsIgnoreCase(genotype))			return List.of(고지혈증_APOA5_c553_GG);
					else if("GT".equalsIgnoreCase(genotype))	return List.of(고지혈증_APOA5_c553_GT);
					else if("TT".equalsIgnoreCase(genotype))	return List.of(고지혈증_APOA5_c553_TT);
				} else if(SnvRiskScreenN088.APOA5_56 == snv) {
					if("CC".equalsIgnoreCase(genotype))			return List.of(고지혈증_APOA5_c56_CC);
					else if("CG".equalsIgnoreCase(genotype))	return List.of(고지혈증_APOA5_c56_CG);
					else if("GG".equalsIgnoreCase(genotype))	return List.of(고지혈증_APOA5_c56_GG);
				}
			}
		} else if(DiseaseRiskScreenN088.치료제부작용 == disease) {
			if(GeneRiskScreenN088.COQ2 == gene) {
				if(SnvRiskScreenN088.COQ2_c779_1022 == snv) {
					if("GG".equalsIgnoreCase(genotype))			return List.of(치료제부작용_COQ2_c779_1022_GG);
					else if("GC".equalsIgnoreCase(genotype))	return List.of(치료제부작용_COQ2_c779_1022_GC);
					else if("CC".equalsIgnoreCase(genotype))	return List.of(치료제부작용_COQ2_c779_1022_CC);
				}
			}
		}
		return null;
	}
	private final String[] 고지혈증_APOE_E2E2 = new String[] {"APOE 유전자형이 e2e2인 경우 중성지방 증가 위험도를 약 2.7배 증가시켜 표준 유전형에 비해 고지혈증이 나타날 확률이 증가합니다.", "APOE 유전자형이 e2e2인 경우 저지방식이에 대한 반응성이 떨어질 수 있습니다."};
	private final String[] 고지혈증_APOE_E2E3 = new String[] {"검사한 APOE 유전자 영역에서 고지혈증 위험도 증가와 관련된 유전자형이 검출되지 않았습니다.", "APOE 유전자형이 e2e3인 경우 저지방식이에 대한 반응성이 떨어질 수 있습니다."};
	private final String[] 고지혈증_APOE_E2E4 = new String[] {"검사한 APOE 유전자 영역에서 고지혈증 위험도 증가와 관련된 유전자형이 검출되지 않았습니다."};
	private final String[] 고지혈증_APOE_E3E3 = new String[] {"검사한 APOE 유전자 영역에서 고지혈증 위험도 증가와 관련된 유전자형이 검출되지 않았습니다."};
	private final String[] 고지혈증_APOE_E3E4 = new String[] {"검사한 APOE 유전자 영역에서 고지혈증 위험도 증가와 관련된 유전자형이 검출되지 않았습니다.", "APOE 유전자형이 e3e4인 경우 고지혈증 치료제의 하나인 스타틴에 대한 반응성이 떨어질 수 있습니다."};
	private final String[] 고지혈증_APOE_E4E4 = new String[] {"APOE 유전자형이 e4e4인 경우 LDL 콜레스테롤 증가 위험도를 약 2.1 배 증가시켜 표준 유전형에 비해 고지혈증이 나타날 확률이 증가합니다.", "APOE 유전자형이 e4e4인 경우 고지혈증 치료제의 하나인 스타틴에 대한 반응성이 떨어질 수 있습니다."};
	private final String 고지혈증_APOA5_c553_GG = "검사한 APOA5 유전자의 c.553 영역은 GG 유전자형으로 고지혈증 위험도 증가와 관련된 유전자형이 검출되지 않았습니다.";
	private final String 고지혈증_APOA5_c553_GT = "APOA5의 c.553GT 유전자형이 검출된 경우 중성지방 증가 위험도를 약 4.4배 증가시킵니다. 표준 유전형에 비해 고지혈증이 나타날 확률이 증가합니다.";
	private final String 고지혈증_APOA5_c553_TT = "APOA5의 c.553TT 유전자형이 검출된 경우 중성지방 증가 위험도를 약 4.4배 증가시킵니다. 표준 유전형에 비해 고지혈증이 나타날 확률이 증가합니다.";
	private final String 고지혈증_APOA5_c56_CC = "검사한 APOA5 유전자의 c.56 영역은 CC 유전자형으로 고지혈증 위험도 증가와 관련된 유전자형이 검출되지 않았습니다.";
	private final String 고지혈증_APOA5_c56_CG = "APOA5의 c.56CG 유전자형이 검출된 경우 중성지방 증가 위험도를 약 6.5배 증가시킵니다. 표준 유전형에 비해 고지혈증이 나타날 확률이 증가합니다.";
	private final String 고지혈증_APOA5_c56_GG = "APOA5의 c.56GG 유전자형이 검출된 경우 중성지방 증가 위험도를 약 6.5배 증가시킵니다. 표준 유전형에 비해 고지혈증이 나타날 확률이 증가합니다.";
	private final String[] 치료제부작용_COQ2_c779_1022_GG = new String[] {"COQ2 유전자형이 GG인 경우 고지혈증 치료제 부작용을 증가시키지 않습니다."};
	private final String[] 치료제부작용_COQ2_c779_1022_GC = new String[] {"COQ2 유전자형이 GC인 경우 고지혈증 치료제 중 하나인 스타틴 복용 시 표준 유전형에 비해 근육증상 발생 위험도를 약 2.6배 증가시킵니다.", "부작용 주의 약물 : 아토르바스타틴(atorvastatin), 로슈바스타틴(rosuvastatin)"};
	private final String[] 치료제부작용_COQ2_c779_1022_CC = new String[] {"COQ2 유전자형이 CC인 경우 고지혈증 치료제 중 하나인 스타틴 복용 시 표준 유전형에 비해 근육증상 발생 위험도를 약 2.6배 증가시킵니다.", "부작용 주의 약물 : 아토르바스타틴(atorvastatin), 로슈바스타틴(rosuvastatin)"};
	@Override
	public List<String> recommendation(DiseaseRiskScreen disease, GeneRiskScreen gene, Snv snv, String genotype) {
		if(DiseaseRiskScreenN088.고지혈증 == disease) {
			if(GeneRiskScreenN088.APOE == gene) {
				if(SnvRiskScreenN088.APOE_ == snv) {
					if("e2e2".equalsIgnoreCase(genotype))		return List.of(고지혈증_ALL, 고지혈증_중성지방_RISK_HIGH, 고지혈증_LDL_RISK_HIGH);
					else if("e2e3".equalsIgnoreCase(genotype))	return List.of(고지혈증_ALL, 고지혈증_LDL_RISK_HIGH);
					else if("e2e4".equalsIgnoreCase(genotype))	return List.of(고지혈증_ALL);
					else if("e3e3".equalsIgnoreCase(genotype))	return List.of(고지혈증_ALL);
					else if("e3e4".equalsIgnoreCase(genotype))	return List.of(고지혈증_ALL, 고지혈증_중성지방_RISK_HIGH);
					else if("e4e4".equalsIgnoreCase(genotype))	return List.of(고지혈증_ALL, 고지혈증_중성지방_RISK_HIGH);
				}
			} else if(GeneRiskScreenN088.APOA5 == gene) {
				if(SnvRiskScreenN088.APOA5_553 == snv) {
					if("GG".equalsIgnoreCase(genotype))			return List.of(고지혈증_ALL);
					else if("GT".equalsIgnoreCase(genotype))	return List.of(고지혈증_ALL, 고지혈증_중성지방_RISK_HIGH);
					else if("TT".equalsIgnoreCase(genotype))	return List.of(고지혈증_ALL, 고지혈증_중성지방_RISK_HIGH);
				} else if(SnvRiskScreenN088.APOA5_56 == snv) {
					if("CC".equalsIgnoreCase(genotype))			return List.of(고지혈증_ALL);
					else if("CG".equalsIgnoreCase(genotype))	return List.of(고지혈증_ALL, 고지혈증_중성지방_RISK_HIGH);
					else if("GG".equalsIgnoreCase(genotype))	return List.of(고지혈증_ALL, 고지혈증_중성지방_RISK_HIGH);
				}
			}
		} else if(DiseaseRiskScreenN088.치료제부작용 == disease) {
			if(GeneRiskScreenN088.COQ2 == gene) {
				if(SnvRiskScreenN088.COQ2_c779_1022 == snv) {
					if("GG".equalsIgnoreCase(genotype))			return List.of(치료제부작용_RISK_LOW);
					else if("GC".equalsIgnoreCase(genotype))	return List.of(치료제부작용_RISK_HIGH_GC);
					else if("CC".equalsIgnoreCase(genotype))	return List.of(치료제부작용_RISK_HIGH_CC);
				}
			}
		}
		return null;
	}
	private final String 고지혈증_ALL = "고지혈증은 유전적 요인 이외의 다양한 환경적 요인이 관여하므로, 고지혈증 위험과 관련된 유전형을 가지고 있지 않더라도 고지혈증이 나타날 수도 있습니다.";
	private final String 고지혈증_LDL_RISK_HIGH = "저지방식이에 대한 반응성이 떨어질 경우, 저지방식이를 통한 혈중지질농도 조절이 잘 이루어지지 않을 수 있습니다. 고지혈증이 나타날 경우, 식이요법 보다 약물을 통한 치료가 권장될 수 있으므로 의료진과의 상담이 권장됩니다.";
	private final String 고지혈증_중성지방_RISK_HIGH = "유전적 요인을 가지고 있는 사람에게 현재 고지혈증이 나타난 경우라면 식이 혹은 운동 조절만으로 고지혈증이 잘 개선되지 않을 수 있으므로, 적극적 치료와 관련하여 의료진과의 상담이 권장됩니다.";
	private final String[] 고지혈증_RISK_MID = new String[] {"검사한 APOE 유전자 영역에서 고지혈증 위험도 증가와 관련된 유전자형이 검출되지 않았습니다.", "APOE 유전자형이 e2e3인 경우 저지방식이에 대한 반응성이 떨어질 수 있습니다."};
	private final String[] 고지혈증_RISK_LOW = new String[] {"검사한 APOE 유전자 영역에서 고지혈증 위험도 증가와 관련된 유전자형이 검출되지 않았습니다."};
	private final String[] 치료제부작용_RISK_HIGH_CC = new String[] {
			"COQ2 유전자형이 CC인 경우 약제 선택 혹은 용량과 관련된 표준 가이드라인은 아직 없습니다. ",
			"스타틴과 관련된 근육증상 발생에는 COQ2 유전자형 외에 다른 유전자 이상 혹은 환경적 요인이 관여할 수 있습니다. 따라서 근육증상 발생 위험 증가와 관련된 유전자형을 가지고 있다 하더라도 스타틴 복용 시 증상이 나타나지 않을 수 있습니다.",
			"스타틴 복용 시 근육 증상을 경험하였거나 하는 경우, 용량 감소 혹은 다른 약제로의 변경 등과 관련하여 의료진과의 상담이 권장됩니다."};
	private final String[] 치료제부작용_RISK_HIGH_GC = new String[] {
			"COQ2 유전자형이 GC인 경우 약제 선택 혹은 용량과 관련된 표준 가이드라인은 아직 없습니다.",
			"스타틴과 관련된 근육증상 발생에는 COQ2 유전자형 외에 다른 유전자 이상 혹은 환경적 요인이 관여할 수 있습니다. 따라서 근육증상 발생 위험 증가와 관련된 유전자형을 가지고 있다 하더라도 스타틴 복용 시 증상이 나타나지 않을 수 있습니다.",
			"스타틴 복용 시 근육 증상을 경험하였거나 하는 경우, 용량 감소 혹은 다른 약제로의 변경 등과 관련하여 의료진과의 상담이 권장됩니다."};
	private final String[] 치료제부작용_RISK_LOW = new String[] {
			"스타틴과 관련된 근육증상 발생에는 COQ2 유전자형 외에 다른 유전자 이상 혹은 환경적 요인이 관여할 수 있습니다. 따라서 근육증상 발생 위험 증가와 관련되지 않은 COQ2 유전자형을 가지고 있다 하더라도 스타틴 복용 시 근육증상이 나타날 수 있으니 약물 복용과 관련하여 의료진과의 상담이 권장됩니다."};

	private String lblLifestyleTitle = "고지혈증에 의한 관련 질환 위험도";
	private String lblLifestyleInfo = "고지혈증이 있을 때 관련 질환 위험도가 증가하는 정도입니다.";
	private String lblLifestyleInfo2 = "고지혈증으로 인해 관련 질환 위험도가 얼마나 증가하는지 확인해 보세요.";
	private String lblLifestyleResultTitle = "고지혈증";
	private String lblLifestyle = "관련 질환";
	public String lblLifestyleResultHeader(GenomeScreenWithRiskScreenDto dto) {
		return dto.patientName() + " 님의\n고지혈증 리스크스크린 결과";
	}
	public String toString(RiskFactor factor) {
		if(factor instanceof RiskFactorN088) switch((RiskFactorN088)factor) {
			case 허혈성_뇌졸중:		return "허혈성 뇌졸중";
			case 관상동맥:			return "관상동맥";
			case 죽상경화성_심혈관질환:	return "죽상경화성 심혈관질환";
			case 암:				return "암";
			default:				return null;
		}
		return null;
	}
	private String lblGuideTermTitle = "고지혈증 관련 용어";
	private String[] lblGuideTerms = new String[] {
			"LDL 콜레스테롤: 나쁜 콜레스테롤로 분류되는 LDL 콜레스테롤은 혈관벽 안쪽에 파고들어 각종 염증 반응을 일으킨 후 덩어리처럼 뭉쳐져 혈관벽에 붙은 상태인 죽상경화반을 형성하거나 전체적으로 혈관벽을 두꺼워지게 합니다.",
			"HLD 콜레스테롤: 좋은 콜레스테롤로 분류되는 HDL 콜레스테롤은 혈관벽에 쌓여 있는 나쁜 콜레스테롤을 다시 빼내 제거하는 기능을 합니다. 하지만 HDL 콜레스테롤 수치가 낮아지거나 제 역할을 못하는 HDL 콜레스테롤이 대부분이라면 혈관에 좋은 역할을 하지 못하게 됩니다.",
			"중성지방: 중성지방은 체내에서 합성되는 지방의 한 형태로 우리 몸의 여러 곳에 존재하며 칼로리 섭취가 부족한 경우 체내에서 에너지원으로 사용되기도 합니다. 하지만 중성지방의 양이 많아질 경우 심혈관계 건강이 위협받을 수 있습니다."
	};
	private String lblGuidePreventTitle = "고지혈증 예방 수칙";
	private String lblGuideDietInfo = "포화지방산과 콜레스테롤의 과잉 섭취, 칼로리 섭취와 소모의 불균형, 당질의 과잉 섭취 및 섬유소 섭취 부족, 그리고 알코올의 과잉 섭취 등이 혈청 지질에 영향을 미치므로 혈중 콜레스테롤이나 포화 지방산 농도를 감소시키고 당질 제공량은 총 열량의 60% 이하로 제한합니다.";
	private String lblGuideReferenceTitle = "참고 논문";
}
