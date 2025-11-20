package com.greencross.lims.report.genomescreen.kokr;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.greencross.lims.report.genomescreen.GenomeScreenTemplateN087;
import com.greencross.lims.report.genomescreen.GenomeScreenWithRiskScreenDto;
import com.greencross.lims.report.genomescreen.RiskScreenTemplate;
import com.gcgenome.lims.test.genomescreen.TestInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.awt.*;
import java.util.List;

// 뇌졸중 지놈 스크린
@EqualsAndHashCode(callSuper = true)
@Getter
@Accessors(fluent = true)
public class GenomeScreenTemplateN087KoKr extends GenomeScreenTemplateKoKr<GenomeScreenResourceN087KoKr> implements GenomeScreenTemplateN087<GenomeScreenResourceN087KoKr>, RiskScreenTemplateKoKr<GenomeScreenResourceN087KoKr> {
	private final GenomeScreenResourceN087KoKr resource;
	private final String lblSummaryTestIntroTitleInfo = "뇌졸중 지놈 스크린 검사에 대한 간단한 설명입니다.";
	private final String lblSummaryInfo = "본 검사는 최신 유전자 분석기법으로 유전성 뇌졸중을 발병시킬 수 있는 유전자를 검사하며 기존 연구논문 결과를 분석하여 " +
										 "건강 관리에 도움이 될 수 있는 개인 맞춤형 정보를 제공합니다. 유전성 뇌졸중과 관련된 병원성 변이가 발견된 경우 " +
										 "개인에 따라 증상이 없을 수 있으나(reduced penetrance) 일반 인구에 비해 발생 위험도가 매우 높기 때문에 뇌졸중 발생 " +
										 "위험도를 낮추기 위한 조치 및 발견을 위한 주기적인 정밀 검사 등이 권장됩니다.";
	private final String lblGuideTestTitle = "뇌졸중 지놈 스크린 검사란?";
	private final TextBlock[] guideTest;
	private final String lblGuideDiseaseTitle = "유전성 뇌졸중 질환은 이러한 특징이 있습니다.";
	private final TextBlock[] guideDisease;
	private final String lblGeneListInfo = "뇌졸중 지놈 스크린 검사는 미국의학 유전학회(ACMG)의 권고에 따라 다음과 같은 질환 및 유전자들을 검사합니다.";
	private final TextBlock[] limitations;
	private final TextBlock[] references;
	private final TextBlock[] referencesRiskScreen;
	public GenomeScreenTemplateN087KoKr(GenomeScreenResourceN087KoKr resource, TestInfo testInfo) {
		super(testInfo);
		this.resource = resource;
		TextStyle textStyle = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8).paragraph(true);
		guideTest = new TextBlock[] {
				new TextBlock(textStyle.clone().fonts(resource.fontHeader(), resource.fontDefault()).color(Color.BLACK), "뇌졸중(Stroke)"),
				new TextBlock(textStyle, "이란 뇌혈관이 막히거나(뇌경색) 뇌혈관이 터져서(뇌출혈) 발생하는 신경학적 결손 증상을 " +
												 "말하며, 우리나라에선 흔히 '중풍'이라고도 합니다. " +
												 "뇌졸중의 주요 위험요인으로는 고혈압, 당뇨, 고지혈증, 흡연, 음주, 혈액응고질환, 관상동맥 질환, 치주질환 " +
												 "등이 알려져 있으며, 여기에 다양한 유전적 요인도 관여하게 됩니다. " +
												 "뇌졸중의 일부는 유전자 이상에 의해 발생할 수 있는데, 뇌졸중을 일으키는 대표적인 유전질환으로 모야모야" +
												 "병(Moyamoya Disease)과 카다실(CADASIL; Cerebral Autosomal Dominant Arteriopathy with Subcortical " +
												 "Infarcts and Leukoencephalopathy)을 들 수 있습니다. " +
												 "고혈압, 당뇨, 고지혈증, 흡연, 음주 등 뇌졸중의 위험인자가 없는 젊은 환자에서 뇌경색 또는 뇌출혈 증상이 " +
												 "있을 경우 유전성 뇌졸중 질환을 의심할 필요가 있습니다.\n" +
												 "뇌졸중 지놈 스크린 검사는 유전성 뇌졸중 발병의 위험성을 높이는 다양한 유전성 뇌졸중 질환과 관련된 34개" +
												 "의 유전자를 차세대 염기서열분석(Next Generation Sequencing; NGS) 검사법으로 한 번에 검사하여 유전성 뇌" +
												 "졸중 질환을 미리 진단하고 이를 통해 질환의 예방 및 조기진단, 치료 효과의 향상을 기대할 수 있는 검사입" +
												 "니다.")
		};
		guideDisease = new TextBlock[] {
				new TextBlock(textStyle, "유전성 뇌졸중 질환 관련 유전자에서 병원성 변이(Pathogenic variant)가 발견되더라도 100% 질환이 발" +
												 "병하는 것은 아니며, 질환의 발병 시기 및 임상 양상은 개개인에 따라 매우 다양하게 나타납니다."),
				new TextBlock(textStyle, "유전성 뇌졸중은 전체 뇌졸중의 일부분을 차지하며, 유전성 뇌졸중의 발병 기전 및 임상 양상은 관련 유" +
												 "전자에 따라 다르게 나타날 수 있습니다."),
				new TextBlock(textStyle, "본 검사에 포함된 유전성 뇌졸중은 뇌졸중 뿐 아니라 그 외 다양한 임상 양상 및 질환과도 연관되어 나" +
												 "타날 수 있습니다."),
				new TextBlock(textStyle, "유전성 뇌졸중 질환과 관련된 유전자 검사에서 질환과 관련된 병원성 변이가 발견되지 않았더라도 환경" +
												 "적 영향 및 생활습관 등 비유전성 원인으로 인한 뇌졸중의 발병 가능성은 여전히 존재합니다.")
		};
		limitations = new TextBlock[] {
				new TextBlock(textStyle, "유전자 변이는 표준 해석 지침인 2015 ACMG/AMP 가이드라인에 따라 병원성 변이(Pathogenic Variant), " +
												 "준병원성 변이(Likely Pathogenic Variant), 의미를 알 수 없는 변이(Variant of Uncertain " +
												 "Significance), 준양성 변이(Likely Benign Variant), 양성 변이(Benign Variant)의 다섯 가지 카테고리" +
												 "로 분류됩니다."),
				new TextBlock(textStyle, "2015 ACMG/AMP 가이드라인에 따른 염기서열 변이의 질병 관련성은 인구집단에서의 변이 빈도, 기능분석, " +
												 "컴퓨터 분석을 이용한 병인성 예측자료, 논문 및 병인성 변이 데이터베이스 등 현재까지 알려진 다양한 " +
												 "근거를 종합하여 해석(Evidence-based Interpretation) 하며, 진단검사의학과 전문의가 담당하고 있습니" +
												 "다. 단, 결과보고 이후에도 추가 근거가 축적됨에 따라 변이의 해석은 달라질 수 있습니다."),
				new TextBlock(textStyle, "본 검사에서는 질병 관련성이 확실하거나 매우 높은 병원성 변이와 준병원성 변이를 주로 보고하며, 의" +
												 "미를 알 수 없는 변이, 준양성 변이 및 양성 변이는 보고하지 않는 것을 원칙으로 합니다."),
				new TextBlock(textStyle, "검사에 포함된 유전자는 전체 엑손을 포함하나, 일부 영역에서는 염기서열 해독이 충분하지 않을 수 있" +
												 "습니다. 또한, 상동성이 높은 염기서열이 존재하는 경우 염기서열 해독이 정확하지 않을 수 있으며, 큰 " +
												 "결실 또는 중복, 단백질을 만들지 않는 서열 부위에 존재하는 변이는 검출이 어려울 수 있습니다.")
		};
		TextStyle referenceStyle = new TextStyle().color(resource.colorText()).fonts(resource.fontScientific(), resource.fontText(), resource.fontDefault()).fontSize(8).paragraph(true);
		references = new TextBlock[] {
				new TextBlock(referenceStyle, "The Human Gene Mutation Database(http://www.hgmd.cf.ac.uk)"),
				new TextBlock(referenceStyle, "Gene Reviews(https://www.ncbi.nlm.nih.gov/books/NBK1116/)")
		};
		referencesRiskScreen = new TextBlock[] {
				new TextBlock(referenceStyle, "Development. 2014;141(2):307-17."),
				new TextBlock(referenceStyle, "Eur Neurol. 2014;71(5-6):217-22."),
				new TextBlock(referenceStyle, "Exp Ther Med. 2013;5(3):853-859."),
				new TextBlock(referenceStyle, "J Stroke Cerebrovasc Dis. 2013;22(5):608-14."),
				new TextBlock(referenceStyle, "J Stroke Cerebrovasc Dis. 2017;26(11):2482-2493."),
				new TextBlock(referenceStyle, "J Stroke Cerebrovasc Dis. 2018:27(8):2259-2270."),
				new TextBlock(referenceStyle, "Lancet. 2016;388(10046):761-75.")
		};
	}

	private String diseaseName(DiseaseN087 disease) {
		switch (disease) {
			case 대동맥과_소동맥_폐핵으로_인한_허혈성_뇌졸중:return "대동맥과 소동맥 폐핵으로 인한 허혈성 뇌졸중";
			case 소동맥_폐색으로_인한_허혈성_뇌졸중:		return "소동맥 폐색으로 인한 허혈성 뇌졸중";
			case 심장_색전성_뇌졸중:						return "심장 색전성 뇌졸중";
			case 기타_원인으로_인한_허혈성_뇌졸중:			return "기타 원인으로 인한 허혈성 뇌졸중";
			case 뇌혈관질환_편두통:						return "뇌혈관질환, 편두통";
			case 모야모야병:								return "모야모야병";
			case 뇌내출혈:								return "뇌내출혈";
			case 고콜레스테롤혈증:						return "고콜레스테롤혈증";
			case 혈전증:									return "혈전증";
			default:									return "";
		}
	}
	private String diseaseInfo(DiseaseN087 disease) {
		switch (disease) {
			case 대동맥과_소동맥_폐핵으로_인한_허혈성_뇌졸중:	return "유전적 이상으로 인해 뇌 깊숙이 퍼져있는 작은 동맥들이 막히면서 발생하는 허혈성 뇌졸중";
			case 소동맥_폐색으로_인한_허혈성_뇌졸중:			return "유전적 이상으로 인해 뇌에 분지된 크고 작은 동맥들이 막히면서 발생하는 허혈성 뇌졸중";
			case 심장_색전성_뇌졸중:						return "심장유전자 이상으로 심부정맥의 발병 가능성이 증가하고 이로 인해 심장 내에 색전증의 형성 가능성이 증가";
			case 기타_원인으로_인한_허혈성_뇌졸중:			return "유전적 이상으로 인해 발생한 결합조직 이상 혹은 염증 등이 뇌혈관에 영향을 미치면서 발생하는 허혈성 뇌졸중";
			case 뇌혈관질환_편두통:						return "유전적 이상으로 나타나는 높은 빈도의 가족력을 가진 편두통";
			case 모야모야병:								return "유전자 원인에 의해 전대뇌동맥과 중대동맥 시작 부분에 협착이나 폐색을 보이는 만성 진행성 뇌혈관 질환";
			case 뇌내출혈:								return "유전적 이상으로 인해 뇌에 분지된 혈관의 출혈이 원인이 되어 발생하는 뇌혈관 장애";
			case 고콜레스테롤혈증:							return "유전자 이상으로 인한 콜레스테롤 대사의 이상으로 인해 혈중 콜레스테롤 농도가 올라가는 이상지질혈증";
			case 혈전증:									return "유전자 이상으로 인한 혈액 응고인자 이상으로 혈액 응고능력이 항진되어 혈전을 만들기 쉽게 된 상태";
			default:									return "";
		}
	}
	private String diseaseSubName(DiseaseSubN087 diseaseSub) {
		switch (diseaseSub) {
			case 베타_지중해혈증:							return "베타 지중해혈증";
			case 고호모시스테인혈증:						return "고호모시스테인혈증";
			case 파브리병:								return "파브리병";
			case 탄력섬유거짓황색종:						return "탄력섬유거짓황색종";
			case 카다실:									return "카다실";
			case 카라실:									return "카라실";
			case 백색질_형성장애_동반_망막_혈관병증:			return "백색질 형성장애 동반 망막 혈관병증";
			case 심장_긴간격_증후군:						return "심장 긴간격 증후군";
			case 혈관성_엘러스_단로스_증후군:				return "혈관성 엘러스-단로스 증후군";
			case 마르판_증후군:							return "마르판 증후군";
			case 결절성_다발동맥염:						return "결절성 다발동맥염";
			case 동맥_비틀림_증후군:						return "동맥 비틀림 증후군";
			case 가족성_편마비_편두통:						return "가족성 편마비 편두통";
			case 모야모야병:								return "모야모야병";
			case 아밀로이드_뇌_혈관병증:					return "아밀로이드 뇌 혈관병증";
			case 뇌_소혈관질환:							return "뇌 소혈관질환";
			case 뇌구멍증:								return "뇌구멍증";
			case 가족성_고콜레스테롤혈증:					return "가족성 고콜레스테롤혈증";
			case FactorVLeiden혈전증:					return "Factor V Leiden 혈전증";
			case 프로트롬빈_관련_혈전증:					return "프로트롬빈 관련 혈전증";
			case 항트롬빈_결핍_혈전증:						return "항트롬빈 결핍 혈전증";
			case 단백질C_결핍_혈전증:						return "단백질 C 결핍 혈전증";
			case 단백질S_결핍_혈전증:						return "단백질 S 결핍 혈전증";
			default:									return "";
		}
	}
	@Override
	public String diseaseSubInfo(DiseaseSub diseaseSub) {
		if(diseaseSub instanceof DiseaseSubN087) switch ((DiseaseSubN087) diseaseSub) {
			case 베타_지중해혈증:					return "베타 지중해혈증(Beta thalassemia)은 헤모글로빈 subunit beta의 합성이 감소하는 것을 특징으로 하는 혈액질환입니다. 헤모글로빈 수치 감소로 인해 성숙한 적혈구가 부족해져 빈혈 증상이 나타납니다. 베타 지중해혈증 환자는 창백함, 피로감 등의 증상이 나타날 수 있습니다. 해당 질환은 HBB 유전자의 변이에 의해서 나타나는 질환으로 대부분 상염색체 열성으로 유전되지만 아주 드물게는 상염색체 우성으로 나타나기도 합니다.";
			case 고호모시스테인혈증:				return "고호모시스테인혈증은 혈액 속의 호모시스테인 농도가 비정상적으로 증가되어 있는 상태를 말합니다. 고호모시스테인 혈전증 환자의 경우 일반적인 고호모시스테인혈증의 증상없이 혈전증이 나타나기도 합니다. 해당 질환은 심혈관질환, 뇌혈관 질환 및 말초혈관질환의 위험인자로 알려져 있습니다. 심각한 고호모시스텐인혈증 환자의 경우에는 호모시스틴뇨증으로 발전되기도 합니다. 어린아이들의 경우에는 지능저하, 경련, 골격변형 등이 나타날 수 있으며 관상동맥질환, 뇌경색, 말초동맥경화증, 심부정맥혈전증 및 폐색전증의 형태로 나타날 수 있습니다. 해당 질환은 상염색체 열성으로 유전됩니다.";
			case 파브리병:						return "파브리병(Fabry disease)은 알파-갈락토시다제 A(alpha-galactosidase A), 다른 말로 세라마이드트라이헥소사이(ceramidetrihexosidase)라 불리는 효소의 결핍으로 발생하는 리소좀 저장 질환(Lysosomal storage disorders) 중 하나입니다. 파브리병(Fabry disease)은 팔과 다리의 심각한 통증과 함께 피부가 사마귀처럼 솟아오르는 혈관각화종(Angiokeratomas), 비정상적인 땀의 배출 그리고 시력장애가 나타납니다. 질병의 후기에, 신부전, 부정맥 및 진행성의 신경학적 이상 등의 심각한 합병증을 일으킬 수 있습니다. 파브리병은 X염색체 연관 열성 형질로 유전되며 주로 남성에게 나타나고, 여성의 경우 증상이 비교적 후기에 보다 경미하게 나타납니다. 증상은 주로 아동기 또는 청소년기에 시작되며, 성인기 동안에 서서히 진행됩니다.";
			case 탄력섬유거짓황색종:				return "탄력섬유거짓황색종은 피부, 눈, 심장혈관 등의 탄성 조직에 나타나는 유전질환입니다. 피부의 변화가 가장 두드러지게 나타나지만 개인 차가 나타납니다. 일반적으로 목 부근의 피부에서 황색종과 비슷한 형태가 확인되며, 안구에서 오렌지색 반점이 확인됩니다. 심혈관계에 나타날 경우 혈류량 감소, 협심증 등의 증상이 나타날 수 있으며, 위장 출혈이 증상으로 나타나기도 합니다. 해당 질환은 상염색체 열성 질환으로 유전됩니다.";
			case 카다실:							return "카다실(CADASIL)은 편두통과 재발하는 뇌졸중, 점차 진행하는 치매를 특징으로 하는 상염색체 우성의 유전 질환입니다. 최초에는 프랑스 등 유럽 지방에서 보고되었으나 현재 전 세계적으로 발견되고 있으며 한국에서도 카다실 환자가 보고되고 있습니다. 주로 평균 40-50세의 비교적 젊은 사람에게 나타나며 뚜렷한 가족력을 보입니다. 염색체 19번의 NOTCH3 유전자의 다양한 돌연변이에 의해 나타나고 주로 미세한 혈관벽의 손상으로 혈류 장애를 초래하여 반복적인 피질하 뇌경색과 혈관성 치매를 유발합니다. 피부조직에서 혈관 평활근에 granular osmophilic material의 축적을 확인하거나 19번 염색체에 위치한 NOTCH3 유전자의 이상을 유전자 검사로 확인함으로써 진단할 수 있습니다. 카다실은 점점 진행하는 질환으로 반복적인 뇌졸중, 치매 증세 등이 생기는데 현재까지는 확실한 치료법이 없습니다. 환자의 수명도 단축되며 평균생존나이가 남자는 64세, 여자는 69세 정도 입니다.";
			case 카라실:							return "카라실(CARASIL)은 뇌의 작은 혈관이 뇌졸중 및 기타 장애를 유발하는 유전 질환입니다. 첫 증상은 대부분 20세에서 30세 사이에 강직 증상으로 인해 걸음걸이가 달라지는 것으로 나타납니다. 23% 가량의 카라실 환자들이 40세 이전에 뇌졸증과 비슷한 증상을 경험하며, 근육 경직, 성격 변화, 치매 등의 증상이 특징적으로 나타납니다. 또한 카라실 환자들은 탈모, 요통 등을 경험할 수 있으며, 이러한 증상은 신경학적 증상 발병후 5년에서 20년동안 천천히 진행됩니다. 카라실은 HTRA1 유전자의 돌연변이에 의해 나타나며 상염색체 열성으로 유전됩니다.";
			case 백색질_형성장애_동반_망막_혈관병증:	return "백색질형성장애를 동반한 망막 혈관병(RVCL)은 미세혈관의 손실로 인한 중추신경계의 퇴행으로 점차 시각상실, 뇌졸중, 운동능력상실, 인지력 감퇴와 같은 증상을 보이는 희귀 유전질환입니다. 초기 증상은 보통 맹점(blind spot) 또는 눈의 부유물(floater)같은 안과증상을 보이며 장년기(30-40대)에 나타나기 시작합니다. RVCL은 상염색체 우성으로 유전되며, TREX1 유전자의 이상이 원인이 되어 나타납니다.";
			case 심장_긴간격_증후군:				return "QT연장증후군은 실신(faint) 및 급사(sudden cardiac death)를 초래할 수 있는 위중한 질환으로 유전자 이상으로 인한 선천성과 전해질 및 약물에 따른 후천성으로 구별할 수 있습니다. 심장박동이 있은 후 심장의 전기체계는 다음 심장박동을 위 해 스스로 재충전을 하게 되는데, 심장 긴간격 증후군이 있을 경우 정상인에 비해 더 긴 시간이 필요하며 이로 인해 torsade de points라 하는 비정상적으로 매우 빠른 부정맥(arrhythmia)이 오게 되며 심장에서는 충분한 피를 내보내지 못하게 되고, 뇌에서 충분한 산소를 공급 받지 못하면서 산소부족현상이 일어나 의식을 잃게 되거나(syncope) 사망할 수도 있습니다. 이러한 부정맥은 심장 내 혈전이 생성될 가능성을 증가시키며 이는 심장 색전성 뇌졸증의 위험성을 증가시킵니다.";
			case 혈관성_엘러스_단로스_증후군:		return "혈관성 엘러스-단로스 증후군은 유전성 결합조직 질환중의 하나로, 신체의 주요한 구조 단백질인 콜라겐에 결함이 생겨 얇고 투명한 피부와 쉽게 멍듬, 관절의 과운동성, 동맥의 확장 및 파열, 장 파열, 분만 중 자궁 파열 등의 증상이 나타나는 질환입니다. 특징적인 얼굴을 보이기도 하나 일부에서만 나타나며 관절과 피부 증상이 경미한 환자들은 질환을 조기에 발견하기 어렵고 생명을 위협할 수 있는 동맥 파열, 장 천공, 및 다른 기관들의 파열이 대개 첫 증상으로 나타나므로 질환의 조기 진단 및 예방이 매우 중요한 질환입니다.";
			case 마르판_증후군:					return "마르판 증후군(Marfan`s syndrome)은 유전성 결합조직 질환 중의 하나로 세포외 기질을 구성하는 단백질에 결함이 생겨 골격, 눈, 심장 및 혈관에 증상이 나타나는 질환입니다. 주요 증상으로는 팔과 다리의 긴뼈의 과다한 성장, 척추 만곡, 가슴뼈 의 함몰 또는 돌출, 눈의 수정체 탈구, 근시, 대동맥의 확장과 변성, 대동맥판 역류, 승모판 탈출, 승모판 역류 등이 있습니다. 마르판 증후군은 상염색체 우성으로 유전되며, fibrillin-1(FBN1) 유전자의 결함 또는 중복이 마르판 증후군과 관련된 질병과 연결되어 있습니다. 마르판 증후군의 주요 이환 증상은 심혈관계 질환과 관련되어 있으며 이는 주요한 조기 사망의 원인과 관련되므로, 조기 진단 및 모니터링과 심혈관 질환의 예방이 매우 중요한 질환입니다. 마르판 증후군은 남여 동등한 비율로 영향을 미치고, 인종적인 차이 없이 전 세계적으로 발생하며 일반 인구에서 5,000~10,000명 중 1명의 비율로 발생하는 것으로 추정됩니다. 마르판 증후군 증상이 경미한 경우에 진단하는 것이 어려워 일반 인구에서 실제 발병률을 확정하기는 어렵습니다.";
			case 결절성_다발동맥염:				return "결절다발동맥염은 작거나 중간정도 크기의 동맥에 염증이 생기는 혈관질환으로, 신체의 여러 기관에 영향을 미칩니다. 대부분의 경우가 40-50대에서 발병하며 피부, 말초신경, 위장관, 심장, 눈 및 신장의 혈관에 염증이 나타납니다. 환자에게서 나타나는 증상은 대개 혈관 염증에 의한 장기 손상에서 비롯되며 피로, 열병, 식욕저하, 체중감소, 근육 및 관절통, 발진 등 다양하게 나타납니다. 결절성 다발동맥염증은 상염색체 열성으로 유전되는 질환입니다.";
			case 동맥_비틀림_증후군:				return "동맥 비틀림 증후군은 몸의 형태를 유지하는 결합조직의 이상으로 피부, 관절, 혈관 등 다양한 장기에 이상을 야기합니다. 특히 혈관의 비정상적인 늘어남, 비틀림, 협착과 함께 허혈성 혈관이 특징적으로 나타납니다. 동맥류 또는 박리로 인한 혈액 손실, 혈관 협착으로 인한 혈액 공급 제한으로 다양한 장기의 합병증을 유발할 수 있습니다. SLC2A10 유전자가 관련된 것으로 알려져 있으며 상염색체 열성으로 유전됩니다.";
			case 가족성_편마비_편두통:				return "가족성 편마비 편두통은 아동기 또는 청소년기에 수시간에서 길게는 수일 동안 반복되는 편두통 증상과 반신불완전마비(hemiparesis)를 보이는 신경학적 질환입니다. 편두통 증상은 대뇌 피질, 뇌간에 국한되어 나타나며, 시각 장애와 감각 상실, 실어증과 같은 증상을 동반하기도 합니다. 가족성 편마비 편두통은 일반적인 편두통 발병시기보다 이른 시기(10-20대)에 나타나며 편두통의 빈도는 연령이 올라감에 따라 감소합니다. 가족성 편마비 편두통 환자의 40-50%는 눈떨림에서 운동 실조까지 다양한 소뇌 징후를 보입니다. 가족성 편마비 편두통은 ACNA1A, ATP1A2, SCN1A 유전자가 관련되어 있으며 상염색체 우성으로 유전됩니다.";
			case 모야모야병:						return "모야모야병(Moyamoya disease)은 유전성 뇌혈관질환으로, 뇌동맥조영상에서 양측 속목동맥의 끝부분과 앞대뇌동맥(anterior cerebral artery)과 중대뇌동맥(middle cerebral artery)의 시작 부분이 점진적으로 좁아지다가 막히는 소견과, 이로 인하여 좁아진 동맥부분 인접부위의 뇌기저부에서 가느다란 비정상적인 혈관들이 자라나와 모여 있는 혈관망이 나타납니다. 뇌허혈증상, 뇌경색 뇌출혈, 두통 등이 주요 증상으로 나타나며 경련, 실신, 불수의적 운동이 나타날 수 있습니다. 소아에서는 일시적인 뇌허혈증상이 흔하게 나타나며, 성인에서는 소아보다 뇌출혈로 나타나는 경우가 많습니다. 모야모야병은 상염색체 열성으로 유전되며, 우리나라와 일본 같은 극동아시아지역에 흔하며 여성 환자가 많으며 약 10-15%에서 가족력이 있습니다. 모야모야병과 3번, 6번, 8번 염색체와의 연관성이 보고된 바 있으며, 17번 염색체의 RNF213 유전자와 관련이 있습니다.";
			case 아밀로이드_뇌_혈관병증:			return "뇌 아밀로이드 혈관병증은 중년기부터 치매, 뇌졸증 및 기타 신경학적 증상이 점진적으로 나타나는 질환입니다. 신경학적 쇠퇴로 인한 징후와 증상의 정도는 개인마다 차이가 있지만 일반적으로 60대에서 치명적으로 나타나며, 대부분의 환자들이 증상이 나타난지 10년 이내에 사망합니다. 뇌 아밀로이드 혈관병증은 유전학적 원인과 증상에 따라서 여러 타입으로 분류되었으며, 이러한 타입은 질환이 처음 진단된 지역의 이름을 따라 명명되었습니다. dutch 타입은 가장 일반적인 타입으로 주로 뇌졸중이 첫 번째 증상으로 나타나며, 이 중 1/3의 환자에게서 치명적으로 나타납니다. 생존한 환자에게서는 뇌졸중의 재발과 치매가 증상으로 나타나며, 뇌졸중을 겪은 환자에서는 뇌전증 증상이 나타나기도 합니다. Flemish 와 Italiain 타입의 환자들은 반복적인 뇌졸중과 치매를 겪기 쉽습니다. Arctic 타입의 환자들은 보통 첫 번째 증상으로 심각한 치매 증상으로 이어지는 기억 상실을 겪으며, 뇌졸중 증상을 잘 나타나지 않습니다. Iowa 타입의 환자들에게서도 뇌졸중이 잘 나타나지는 않으며, 이 타입의 환자들은 기억 상실, 언어 문제, 성격 변화 등을 겪습니다. 뇌 아밀로이드 혈관병증은 APP, CST4, ITM2B 유전자의 상염색체 우성으로 유전됩니다.";
			case 뇌_소혈관질환:					return "뇌 소혈관 질환은 뇌의 혈관의 약화되는 질환이며, 최초 증상이 중년기에 뇌졸중으로 나타나기도 합니다. 일반적으로 허혈성 뇌졸중보다 출혈성 뇌졸중의 형태로 일어나며, 두가지 경우로 모두 나타날 수 있습니다. 발작과 편두통을 동반하기도하며 백색질뇌증, 악센펠트-리이거 증후군(Axenfeld-Rieger syndrome)와 같은 시각장애를 보이기도 합니다.";
			case 뇌구멍증:						return "뇌구멍증은 대뇌 반구 속에 액체가 들어있는 낭종이나 구멍이 생기는 뇌질환입니다. 공간 또는 낭은 보통 파괴성 병변의 잔유물이지만, 때때로 비정상적인 발달로 야기되기도 합니다. 뇌구멍증의 증상으로는 성장지연, 발달지연, 경직반마비, 근육긴장저하, 발작, 종종 영아연축과 대두증 또는 소두증을 포함합니다. 또한 언어발달이 결여되거나 지연되고, 간질, 수두증, 경직구축과 지적장애를 가질 수 있습니다. 뇌구멍증의 예후는 낭 또는 구멍의 범위와 위치에 따라 달라집니다. 뇌구멍증은 1형과 2형으로 나누어지며, 1형은 보통 한쪽에 나타나고 출생 시 외상 또는 태아 혈관 폐쇄와 같은 국부적인 파괴성 병변으로 인해 야기됩니다. 2형은 보통 대칭적으로 나타나고, 뇌실의 발달에서 정지되거나 주요한 결함으로 나타나며, 뇌갈림 뇌구멍증이라고도 합니다. 해당 질환은 COL4A2 유전자의 상염색체 우성으로 유전되며, 본 유전자의 변이는 출혈성 뇌졸중을 유발시키는 것으로도 알려져 있습니다.";
			case 가족성_고콜레스테롤혈증:			return "가족성 고콜레스테롤혈증(familial hypercholesterolemia, FH)은 상염색체 상호 우성 유전 질환이며, 혈중 LDL 콜레스테롤의 증가와 정상 트리글리세리드, 건 황색종, 조발성 관상동맥경화증 등을 특징으로 합니다. 호모형은 양쪽 유전자에 이상이 있는 경우로 LDL-C값이 500~900mg/dL, 총 콜레스테롤(TC)값이 600mg/dL 이상이고, 헤테로형은 어느 한쪽 유전자에 이상이 있는 경우로 LDL-C값이 150~420mg/dL, TC값이 230~500mg/dL입니다. 이 질환은 중증의 동맥경화를 일으키기 때문에 조기발견 및 조기치료가 중요합니다.";
			case FactorVLeiden혈전증:			return "Factor V Leiden 혈전증은 항응고제 반응 저하와 정맥혈전색전증의 위험도가 증가하는 유전적 혈액응고 질환입니다. 해당 질환으로 인해 생긴 심부정맥혈전증은 주로 다리에 생기지만 뇌, 눈, 간, 신장 등 신체 여러 부위에도 생길 수 있어서 다양한 합병증을 일으킬 수 있습니다. 또한 해당질환을 가진 여성의 경우 혈전증으로 인한 유산과 자간전증(preeclampsia)의 위험도가 증가할 수 있습니다. 해당 질환은 F5 유전자와 관련된 것으로 알려져 있으며, 상염색체 우성으로 유전됩니다.";
			case 프로트롬빈_관련_혈전증:			return "프로트롬빈 관련 혈전증은 유전성 혈액질환으로 비정상적인 혈액 응고로 인해 심부정맥혈전증과 같은 혈전증 발생의 위험도가 증가하는 특징이 있습니다. 혈전은 주로 다리의 심정맥에 발생하지만 혈관을 통해 혈전이 순환하여 폐색전증의 위험도 역시 증가하는 것으로 알려져 있습니다. 프로트롬빈 관련 혈전증을 가진 산모의 경우 혈전으로 인해 유산 또는 자간전증(preeclampsia)과 그로인한 태아의 성장지연, 태반의 조기박리와 같은 합병증을 유발할 수 있습니다. 그러나 대부분의프로트롬빈 관련 혈전증 환자에게서 혈전증이 발생하는 경우는 드뭅니다. F2 유전자의 20210G>A 변이가 프로트롬빈 관련 혈전증과 관련된 것으로 알려져 있습니다. 해당 질환은 상염색체 우성으로 유전됩니다.";
			case 항트롬빈_결핍_혈전증:				return "항트롬빈 결핍 혈전증은 혈액의 응고를 막는 항트롬빈 활성의 감소 또는 결핍으로 트롬빈과 Factor Xa의 불활성화가 제대로 일어나지 않아 혈전이 생기는 유전 질환입니다. 항트롬빈 결핍증은 인자자체가 생성되지 않는 양적인 결함(1형)과 기능이상을 동반하는 질적인 결함(2형)으로 구분됩니다. 항트롬빈 결핍 혈전증 환자는 일반적으로 팔, 다리, 폐 등의 정맥에서 정맥 혈전 색전증의 증상이 나타납니다. 하지 심부정맥혈전이 가장 흔히 발생되는 부위이지만 장간막정맥, 대정맥, 신장정맥 및 안저정맥등의 흔하지 않은 부위에 정맥혈전증을 형성하기도 합니다. 임신 및 출산, 경구피임약, 수술, 노화, 운동부족과 같은 요인에 의해 혈전이 유발될 수 있습니다. 임신과 에스트로겐의 사용은 중요한 위험 요소이므로 항트롬빈이 결핍된 여성은 남성보다 이른 시기에 혈전증이 발생하는 경향이 있습니다. 유전성 항트롬빈 결핍 혈전증 환자의 절반 정도는 보통 청소년기 이후 한번 이상의 혈전 증상을 보인다고 합니다.";
			case 단백질C_결핍_혈전증:				return "C 단백 결핍은 반복적인 정맥 혈전증이 특징적인 질환입니다. 하지만 성인에서는 많은 경우 증상이 없을 수도 있습니다. 증상이 있는 경우에는 반복적인 정맥혈전증 및 정맥염이 나타나며 폐색전증을 동반하기도 합니다. 이러한 증상들은 가족력을 보일 수 있으며 심근경색, 뇌경색 등으로 이어지기도 합니다. C 단백은 비타민 K 의존 단백의 일종으로 체내에서는 트롬빈-트롬보모둘린(thrombomodulin) 복합체에 의하여 활성화 되는데 S 단백을 보조인자로 하여 혈액응고인자 V와 VIII을 억제하며, 플라스미노겐 활성제 억제인자(plasminogen activator inhibitor; PAI) 의 생성과 분비를 억제함으로써 섬유소용해능력을 증가시키는 역할을 합니다. C 단백이 이러한 천연 항응고제 역할을 하기 때문에, C 단백의 유전적인 결핍증은 과도한 혈액응고 기전의 활성화를 일으키고 혈전증을 유발하게 됩니다.";
			case 단백질S_결핍_혈전증:				return "비타민 K 의존성 당단백의 일종인 S 단백은 C 단백 체계의 보조인자입니다. S 단백이 존재할 때 활성화된 C 단백은 Va인자와 VIIIa인자를 불활성화 시킴으로써 트롬빈(thrombin)의 생성을 줄이게 됩니다. 또한 S 단백은 C 단백의 섬유소 분해의 보조인자 역할을 하면서 직접적으로 다른 응고 인자와 반응하여 프로트롬빈 활성화를 억제합니다. 이러한 역할을 하는 S 단백의 결핍은 결국 혈전증의 원인이 됩니다. 1984년에 S 단백의 유전적 결핍증이 처음으로 보고되었으며 항트롬빈 III 나 C 단백의 결핍증에서와 같이 주로 정맥의 혈전증을 가져오나 드물게 뇌졸중 등 동맥 혈전증도 유발합니다. 항응고제의 복용, 간질환, 신증후군, 임신 등에서 S 단백이 감소하는 것으로 알려져 있습니다.";
			default:							return null;
		}
		return null;
	}
	@Override
	public String diseaseSubReference(DiseaseSub diseaseSub) {
		if(diseaseSub instanceof DiseaseSubN087) switch ((DiseaseSubN087) diseaseSub) {
			case 베타_지중해혈증:					return "03.예방 및 치료 page1~2 참조";
			case 고호모시스테인혈증:				return "03.예방 및 치료 page3~4 참조";
			case 파브리병:						return "03.예방 및 치료 page5~6 참조";
			case 탄력섬유거짓황색종:				return "03.예방 및 치료 page7~8 참조";
			case 카다실:							return "03.예방 및 치료 page9~10 참조";
			case 카라실:							return "03.예방 및 치료 page11~12 참조";
			case 백색질_형성장애_동반_망막_혈관병증:	return "03.예방 및 치료 page13~14 참조";
			case 심장_긴간격_증후군:				return "03.예방 및 치료 page15~17 참조";
			case 혈관성_엘러스_단로스_증후군:		return "03.예방 및 치료 page18~20 참조";
			case 마르판_증후군:					return "03.예방 및 치료 page21~22 참조";
			case 결절성_다발동맥염:				return "03.예방 및 치료 page23~25 참조";
			case 동맥_비틀림_증후군:				return "03.예방 및 치료 page26~28 참조";
			case 가족성_편마비_편두통:				return "03.예방 및 치료 page29~30 참조";
			case 모야모야병:						return "03.예방 및 치료 page31~33 참조";
			case 아밀로이드_뇌_혈관병증:			return "03.예방 및 치료 page34~36 참조";
			case 뇌_소혈관질환:					return "03.예방 및 치료 page37~38 참조";
			case 뇌구멍증:						return "03.예방 및 치료 page39~40 참조";
			case 가족성_고콜레스테롤혈증:			return "03.예방 및 치료 page41~44 참조";
			case FactorVLeiden혈전증:			return "03.예방 및 치료 page45~46 참조";
			case 프로트롬빈_관련_혈전증:			return "03.예방 및 치료 page47~49 참조";
			case 항트롬빈_결핍_혈전증:				return "03.예방 및 치료 page50~52 참조";
			case 단백질C_결핍_혈전증:				return "03.예방 및 치료 page53~54 참조";
			case 단백질S_결핍_혈전증:				return "03.예방 및 치료 page55~56 참조";
			default:							return null;
		}
		return null;
	}
	private String clinicalMeaning(GeneN087 gene) {
		switch(gene) {
			case ABCC6:		return "뇌졸중 가족력이 있는 가족 구성원 19명 중에 11명에서 알려진 ABCC6 pathogenic 변이인 p.Arg1314Gln이 확인됨. 뇌졸중 환자에게서 ABCC6 유전자 변이가 4.9배 빈번하게 나타남을 확인함(p=0.036, 95% CI = 1.11-21.33)";
			case ACTA2:		return "ACTA2 유전자 변이를 가진 20가족 중 ACTA2 변이 보유자에서 조기 허혈성 뇌졸중이 나타났으며, 이 중 4가족에서는 20세 미만에서 발생하는 매우 조기 발병 뇌졸중이 나타남. ACTA2 유전자 변이를 가지지 않은 가족 구성원과 비교하여도 ACTA2 유전자 변이를 가진 가족 구성원에서 뇌졸중 발병이 통계적으로 더 높게 나타남. (P=1.7X10^-12)";
			case ADA2:		return "9명의 뇌졸중 환자에서 ADA2 유전자에서 유전적으로 유전되는 돌연변이를 보였으며, 건강한 대조군인 가족들에서는 드물거나 나타나지 않음. 6명의 환자는 8종의 ADA2 유전자 변이를 이형접합체로 가지고 있었으며, 다발성 결절염 또는 소 혈관염을 가진 3 명의 환자는 p.Gly47Arg 동형접합체를 가진 것을 확인함. 환자들에서 ADA2 및 ADA2 특이 적 효소 활성 수준이 현저하게 감소됨을 확인함.";
			case APP:		return "뇌졸중 가족력이 있는 이탈리아인 가족 중 상염색체 우성으로 유전되는 인지장애와 다양한 뇌졸중 증상을 겪은 사람들에게서 APP 유전자의 A713T 변이를 확인함. 신경병리학적 검사를 진행한 결과 환자들에게서 심각한 아밀로이드 뇌 혈관병증과 뇌경색이 확인됨. 이러한 검사 결과를 통해 APP 유전자의 A713T 변이와 아밀로이드 뇌 혈관병증과의 연관성이 확인됨";
			case ATP1A2:	return "허혈성 뇌졸중 증상을 경험한 편마비 편두통 환자에게서 자기공명혈관조영술(Magnetic resonance angiography)을 통해 확인한 결과 좌측 중앙의 뇌동맥이 확장된 것을 확인하였으며, ATP1A2 (c.2273 G>C) 변이를 확인함.";
			case CBS:		return "연구대상자의 CBS 유전자 다형성 genotype을 취합한 자료 분석 결과 (CC vs TT : OR=1.79, 95 % CI=1.14–2.82, P=0.012; TC vs. TT: OR=1.56, 95 % CI=1.01–2.40, P=0.044; TC+CC vs. TT: OR=1.57, 95 % CI=1.02–2.41, P=0.039 )로 CBS유전자의 833 위치의 C genotype의 빈도가 건강한 대조군 보다 뇌졸중 환자에서 더 높은 것으로 보아 뇌졸중과의 연관성이 확인됨.";
			case COL4A1:	return "181명의 뇌출혈 환자의 COL4A1 유전자 다형성 genotype을 분석하고 3개월, 6개월동안 추적 관찰한 결과, 12명이 중도 탈락하였고, (rs532625 T vs A: OR = 1.648, 95 % CI = 1.004-2.704, p = 0.047; TT + AT vs AA: OR = 2.819, 95 % CI = 1.013-7.845, p = 0.026)으로 COL4A1 유전자 rs532625 위치의 AA genotype이 뇌출혈 환자의 사망 및 장애와의 연관성이 확인됨.";
			case COL4A2:	return "COL4A2 유전자 다형성 genotype을 취합한 자료 분석 결과 (rs9515201 허혈성 뇌졸중 C vs A OR=1.17, 95% CI=1.11–1.24, P=6.62 x 10-8; rs4771674 출혈성 뇌졸중 G vs A OR=1.28, 95% CI=1.13–1.44, P=5.76 x 10-5)으로 COL4A2 유전자의 다형성과 뇌졸중과의 연관성이 확인됨.";
			case CST3:		return "뇌 조직의 면역 조직 화학 분석을 통한 부검에서 뇌출혈 증상을 확인한 환자 17명과 5명의 뇌출혈을 경험한 환자들의 Alu I 제한효소 처리를 통해 Alu I 제한효소 부위의 변이를 확인한 결과 68번 아미노산인 leucine이 합성하는 코돈에 돌연변이가 생겨 Alu I 제한효소 부위가 제거되는 변이가 공통적으로 나타나는 것을 확인하였으며, 해당 변이는 유전성 cystatin C 아밀로이드 혈관증 환자에게서 나타남";
			case GUCY1A1:	return "모야모야병의 다른 유전 요인이 배제된 모야모야병 환자 96명의 GUCY1A1 유전자 염기서열을 분석한 결과 c.334_335delGA(p.Glu112fs) 변이와 c.1550G>A(p.Cys517Tyr). 변이가 확인됨.";
			case ITM2B:		return "뇌졸중을 유발할 수 있는 뇌 아밀로이드 혈관병 환자의 ITM2B(BRI) 유전자의 염기서열을 XbaI 제한 효소를 통한 염기서열 분석 결과 ITM2B 유전자의795-796insTTTAATTTGT 변이를 확인함.";
			case PCSK9:		return "PCSK9 유전자 rs505151 위치 다형성 genotype을 취합한 자료 분석 결과 (AA vs GG: OR=3.56, 95% CI=0.96-13.20, P=0.06; AA vs AG: OR=1.19, 95% CI=0.86-1.66, P=0.30; AA + AG vs GG: OR=1.61, 95% CI=0.04-67.16, P=0.06; AA vs AG + GG: OR=1.29, 95% CI=0.94-1.79, P=0.12; A vs G: OR=1.36, 95% CI=1.01-1.85, P=0.05)으로 PCSK9 유전자의 rs505151 위치의 G genotype의 빈도가 건강한 대조군 보다 허혈성 뇌졸중 환자에서 더 높은 것으로 보아 허혈성 뇌졸중 과의 연관성이 확인됨.";
			case SCN1A:		return "2명 이상의 가족성 편마비 편두통을 진단받은 가족 구성원을 포함한 20 가족의 염기서열을 분석한 결과 11 가족에서는 가족성 편마비 편두통과 관련 있다고 알려진 CACNA1A, ATP1A2 유전자의 변이가 확인되지 않았으며, SCN1A 유전자의 Gln1489Lys 변이가 3 가족 중 가족성 편마비 편두통 환자에서 확인됨.";
			case SLC2A10:	return "16명의 환자의 컴퓨터 보조 단층 촬영(computer assisted tomography) 또는 MRI 혈관 조영술(MRI angiography)를 확인한 결과 동맥 비틀림 증후군인 것이 확인되었으며, 환자들에서 9개의 novel한 SLC2A10 유전자 변이가 확인되었음. 2명의 환자는 뇌졸중 증상을 경험함.";
			case TREX1:		return "3명의 환자들과 15명의 가족들에게서 TREX1 유전자 p.V235Gfs*6 변이를 확인하였으며, 환자들을 허혈성 뇌졸중 증상 혹은 뇌출혈에 대한 경험이 있음.";
			default: 		return null;
		}
	}
	private final String lblSummaryTestIntroTitleInfoRiskScreen = "뇌졸중 위험도 검사에 대한 간단한 설명입니다.";
	private final String lblSummaryInfoRiskScreen = "본 검사는 최신 유전자 분석기법으로 뇌졸중 위험도를 상승시킬 수 있는 유전요인을 검사하며, 기존 연구논문 결과를 분석하여 " +
													"건강 관리에 도움이 될 수 있는 개인 맞춤형 정보를 제공합니다. 하지만, 질병의 진단과는 무관하므로 진단 및 치료 결정을 위해서는 " +
													"반드시 의사와의 상담이 필요합니다.";
	private final String lblGraphDiseaseInfo	= "질환정보";
	private final String lblGraphSummary		= "종합결과";
	private final String lblGraphRisk			= "위험도";
	private final String lblGraphGene			= "검사 유전자";
	private String diseaseRiskScreenName(DiseaseRiskScreenN087 disease) {
		switch (disease) {
			case 허혈성_뇌졸중:	return "허혈성 뇌졸중";
			case 모야모야병:		return "모야모야병";
			case 카다실:			return "카다실";
			default:			return "";
		}
	}
	private final String diseaseInfo1 = "뇌졸중은 혈관이 막혀서 뇌의 일부가 손상되는 허혈성 뇌졸중(뇌경색)과 " +
												"뇌혈관이 터져서 생기는 출혈성 뇌졸중(뇌출혈)으로 나뉩니다. 일반적으로 " +
												"뇌경색이 뇌출혈보다 흔하며 국내 뇌졸중의 약 82%가 허혈성" +
												"으로 나타납니다";
	private final String diseaseInfo2 = "뇌혈관질환의 일종으로 전세계에서 우리나라와 일본에서 가장 높은 빈도로 " +
												"나타납니다. 양측 뇌혈관의 일정한 부위가 내벽이 두꺼워지면서 " +
												"막히고, 주위에 비정상적인 혈관이 생기는 질환으로 소아에서는 활발한 " +
												"뇌 활동으로 많은 피가 필요한 반면 공급되는 피는 적기 때문에 주로 " +
												"허혈성 증상이 나타나고, 성인에서는 주로 얇은 혈관이 터지는 뇌출혈 " +
												"증상으로 나타나게 됩니다.";
	private final String diseaseInfo3 = "주로 미세한 혈관벽의 손상으로 혈류장애를 일으키는 뇌혈관 질환의 " +
												"하나입니다. 주로 평균 40-50세의 비교적 젊은 사람에게 나타나며 " +
												"뇌졸중, 치매 전조를 동반한 편두통 등을 유발합니다. 혈압, 당뇨, 흡연 등 " +
												"뇌졸중의 위험인자가 없는 젊은 환자에서 뇌경색 증상이 나타날 때 " +
												"카다실(Cerebral autosomal dominant arteriopathy with subcortical " +
												"infarcts and leukoencephalopathy, CADASIL)을 의심할 필요가 " +
												"있습니다.";
	@Override
	public String diseaseRiskScreenInfo(RiskScreenTemplate.DiseaseRiskScreen disease) {
		if(disease instanceof DiseaseRiskScreenN087) switch ((DiseaseRiskScreenN087)disease) {
			case 허혈성_뇌졸중:	return diseaseInfo1;
			case 모야모야병:		return diseaseInfo2;
			case 카다실:			return diseaseInfo3;
			default:			return "";
		}
		return null;
	}
	private final String apoeInfo = "APOE 유전자는 아포지단백 중 하나인 Apolipoprotein E를 합성하는 " +
											"유전자로 혈중 지질 농도조절과 신경계에서의 콜레스테롤 전달 등에 " +
											"관여하는 것으로 알려져 있습니다. 메타분석을 통해 증가형 APOE " +
											"유전자형과 허혈성 뇌졸중 사이의 연관성이 보고되었습니다.";
	private final String mthfrInfo = "MTHFR 유전자는 호모시스테인을 메티오닌으로 전환시키는 MTHFR 효소를 " +
											 "합성하는 유전자입니다. 증가형 MTHFR 유전자형은 효소 활성을 저하시켜, " +
											 "혈중 호모시스테인 농도를 증가시킴으로써 혈관벽을 손상시키고 " +
											 "혈전 형성을 촉진시켜 뇌졸중 위험도를 상승시킵니다.";
	private final String rnf213Info = "RNF213 유전자의 정확한 기능은 알려져 있지 않지만, 본 유전자에 의해 " +
											 "합성되는 단백질이 혈관 발달에 관여한다는 연구 결과가 있습니다. " +
											 "모야모야병 환자에서 증가형 RNF213 유전자형이 흔히 나타납니다.";
	private final String notch3Info = "NOTCH3 유전자는 혈관 발생에 주요 역할을 하는 NOTCH 신호 전달 경로와 " +
											 "관련되어 있습니다. 증가형 NOTCH3 유전자형으로 인한 비정상적인 NOTCH " +
											 "신호는 카다실과 연관이 있는 혈관 평활근 세포의 퇴행을 유발할 수 있습니다.";
	@Override
	public String geneRiskScreenInfo(RiskScreenTemplate.GeneRiskScreen gene) {
		if(gene instanceof GeneRiskScreenN087) switch((GeneRiskScreenN087)gene) {
			case APOE:	return apoeInfo;
			case MTHFR:	return mthfrInfo;
			case RNF213:return rnf213Info;
			case NOTCH3:return notch3Info;
			default:	return null;
		}
		return null;
	}

	@Override
	public String diseaseName(Disease disease) {
		if(disease instanceof DiseaseN087) return diseaseName((DiseaseN087) disease);
		return null;
	}

	@Override
	public String diseaseInfo(Disease disease) {
		if(disease instanceof DiseaseN087) return diseaseInfo((DiseaseN087) disease);
		return null;
	}
	@Override
	public String diseaseSubName(DiseaseSub diseaseSub) {
		if(diseaseSub instanceof DiseaseSubN087) return diseaseSubName((DiseaseSubN087) diseaseSub);
		return null;
	}
	@Override
	public String clinicalMeaning(Gene gene) {
		if(gene instanceof GeneN087) return clinicalMeaning((GeneN087) gene);
		return null;
	}
	@Override
	public String clinicalMeaning(RiskScreenTemplate.GeneRiskScreen gene) {
		if(gene instanceof GeneRiskScreenN087) switch((GeneRiskScreenN087)gene) {
			case RNF213:	return "RNF213 유전자 다형성 genotype을 취합한 자료 분석 결과 (모야모야병 GA + AA vs GG: OR = 96.52, 95% CI = 67.01-139.02, P=0.0001; AA vs GA + GG: OR = 13.99, 95% CI = 6.44-30.42; AA vs GG: OR = 30.25, 95% CI = 13.96-65.57; A vs G: OR = 54.25, 95% CI = 39.79-73.95; GA vs GG: OR = 91.14, 95% CI = 63.22-131.37; 두개강 내 동맥협착증 GA + AA vs GG: OR = 13.92, 95% CI = 5.39-35.95, P=0.96, A vs G: OR = 13.00, 95% CI = 5.09-33.18))로 RNF213유전자의 14429 위치의 A genotype의 빈도가 건강한 대조군 보다 모야모야병 환자 및 두개강 내 동맥협착증 환자에서 더 높은 것으로 보아 모야모야병 환자 및 두개강 내 동맥협착증과의 연관성이 확인됨.";
			case NOTCH3:	return "151명의 환자 중 6명의 환자에서 NOTCH3 유전자 exon11의 1630 위치의 변이가 확인되었으며, 소혈관 질환 관련 증상이 신경영상학적으로 관찰되는 환자에서 카다실의 prevalence는 36.0%로 나타남(95% CI = 8.0-64.8).";
			default:		return null;
		}
		return null;
	}

	@Override
	public String diseaseRiskScreenName(RiskScreenTemplate.DiseaseRiskScreen disease) {
		if(disease instanceof DiseaseRiskScreenN087) return diseaseRiskScreenName((DiseaseRiskScreenN087) disease);
		return null;
	}
	@Override
	public String diseaseSubRiskScreenName(RiskScreenTemplate.DiseaseSubRiskScreen diseaseSub) {
		if(diseaseSub instanceof DiseaseRiskScreenN087) return diseaseRiskScreenName((DiseaseRiskScreenN087) diseaseSub);
		return null;
	}
	@Override
	public String summaryPositive(DiseaseRiskScreen disease) {
		if(DiseaseRiskScreenN087.허혈성_뇌졸중 == disease)	return "허혈성 뇌졸중의 위험도를 증가시키는 유전요인이 검출되었습니다.";
		else if(DiseaseRiskScreenN087.모야모야병 == disease)	return "모야모야병의 위험도를 증가시키는 유전요인이 검출되어 모야모야병의 진단 또는 합병증 예방을 위한 관리가 권장됩니다.";
		else if(DiseaseRiskScreenN087.카다실 == disease)		return "카다실(CADASIL)의 위험도를 증가시키는 유전요인이 검출되어 이에 따른 영상학적 진단 또는 합병증 예방을 위한 관리가 권장됩니다.";
		return null;
	}
	@Override
	public String summaryNegative(DiseaseRiskScreen disease){
		if(DiseaseRiskScreenN087.허혈성_뇌졸중 == disease)	return "허혈성 뇌졸중의 위험도를 증가시키는 유전요인이 검출되지 않았습니다.";
		else if(DiseaseRiskScreenN087.모야모야병 == disease)	return "모야모야병의 위험도를 증가시키는 유전요인이 검출되지 않았습니다.";
		else if(DiseaseRiskScreenN087.카다실 == disease)		return "카다실(CADASIL)의 위험도를 증가시키는 유전요인이 검출되지 않았습니다.";
		return null;
	}
	@Override
	public List<String> interpretation(DiseaseRiskScreen disease, GeneRiskScreen gene, Snv snv, String genotype) {
		if(DiseaseRiskScreenN087.허혈성_뇌졸중 == disease) {
			if(GeneRiskScreenN087.APOE == gene) {
				if("e2e2".equalsIgnoreCase(genotype))		return List.of(허혈성_뇌졸중_APOE_E2E2);
				else if("e2e3".equalsIgnoreCase(genotype))	return List.of(허혈성_뇌졸중_APOE_E2E3);
				else if("e2e4".equalsIgnoreCase(genotype))	return List.of(허혈성_뇌졸중_APOE_E2E4);
				else if("e3e3".equalsIgnoreCase(genotype))	return List.of(허혈성_뇌졸중_APOE_E3E3);
				else if("e3e4".equalsIgnoreCase(genotype))	return List.of(허혈성_뇌졸중_APOE_E3E4);
				else if("e4e4".equalsIgnoreCase(genotype))	return List.of(허혈성_뇌졸중_APOE_E4E4);
			} else if(GeneRiskScreenN087.MTHFR == gene) {
				if(SnvRiskScreenN087.MTHFR_c677 == snv) {
					if ("CC".equalsIgnoreCase(genotype)) return List.of(허혈성_뇌졸중_MTHFR_c677_CC);
					else if ("CT".equalsIgnoreCase(genotype)
					 	 || "TC".equalsIgnoreCase(genotype)) return List.of(허혈성_뇌졸중_MTHFR_c677_CT);
					else if ("TT".equalsIgnoreCase(genotype)) return List.of(허혈성_뇌졸중_MTHFR_c677_TT);
				} else if(SnvRiskScreenN087.MTHFR_c1298 == snv) {
					if ("AA".equalsIgnoreCase(genotype)) return List.of(허혈성_뇌졸중_MTHFR_c1298_AA);
					else if ("AC".equalsIgnoreCase(genotype)
						 || "CA".equalsIgnoreCase(genotype)) return List.of(허혈성_뇌졸중_MTHFR_c1298_AC);
					else if ("CC".equalsIgnoreCase(genotype)) return List.of(허혈성_뇌졸중_MTHFR_c1298_CC);
				}
			}
		} else if(DiseaseRiskScreenN087.모야모야병 == disease) {
			if(GeneRiskScreenN087.RNF213 == gene) {
				if("GG".equalsIgnoreCase(genotype))			return List.of(모야모야병_RNF213_c14429_GG);
				else if("GA".equalsIgnoreCase(genotype)
					|| "AG".equalsIgnoreCase(genotype))		return List.of(모야모야병_RNF213_c14429_GA);
				else if("AA".equalsIgnoreCase(genotype))	return List.of(모야모야병_RNF213_c14429_AA);
			}
		} else if(DiseaseRiskScreenN087.카다실 == disease) {
			if(GeneRiskScreenN087.NOTCH3 == gene) {
				if("CC".equalsIgnoreCase(genotype))			return List.of(카다실_NOTCH3_c1630_CC);
				else if("CT".equalsIgnoreCase(genotype)
					 || "TC".equalsIgnoreCase(genotype))	return List.of(카다실_NOTCH3_c1630_CT);
				else if("TT".equalsIgnoreCase(genotype))	return List.of(카다실_NOTCH3_c1630_TT);
			}
		}
		return null;
	}
	private final String 허혈성_뇌졸중_APOE_E2E2 = "검사한 APOE 유전자 영역에서 허혈성 뇌졸중 위험도 증가와 관련된 유전자형이 검출되지 않았습니다.";
	private final String 허혈성_뇌졸중_APOE_E2E3 = "검사한 APOE 유전자 영역에서 허혈성 뇌졸중 위험도 증가와 관련된 유전자형이 검출되지 않았습니다.";
	private final String 허혈성_뇌졸중_APOE_E2E4 = "APOE 유전자형이 e2e4인 경우 표준 유전형(e3e3)에 비해 허혈성 뇌졸중 위험도를 약 2배 증가시킵니다.";
	private final String 허혈성_뇌졸중_APOE_E3E3 = "검사한 APOE 유전자 영역에서 허혈성 뇌졸중 위험도 증가와 관련된 유전자형이 검출되지 않았습니다.";
	private final String 허혈성_뇌졸중_APOE_E3E4 = "APOE 유전자형이 e3e4인 경우 표준 유전형(e3e3)에 비해 허혈성 뇌졸중 위험도를 약 1.9배 증가시킵니다.";
	private final String 허혈성_뇌졸중_APOE_E4E4 = "APOE 유전자형이 e4e4인 경우 표준 유전형(e3e3)에 비해 허혈성 뇌졸중 위험도를 약 3.4배 증가시킵니다.";
	private final String 허혈성_뇌졸중_MTHFR_c677_CC = "검사한 MTHFR 유전자의 677 영역은 CC 유전자형으로 허혈성 뇌졸중 위험도 증가와 관련된 유전자형이 검출되지 않았습니다.";
	private final String 허혈성_뇌졸중_MTHFR_c677_CT = "검사한 MTHFR 유전자의 677 영역은 CT 유전자형으로 허혈성 뇌졸중 위험도 증가와 관련된 유전자형이 검출되지 않았습니다.";
	private final String 허혈성_뇌졸중_MTHFR_c677_TT = "MTHFR 유전자의 677 영역에서 TT 유전자형이 검출된 경우 표준 유전형에 비해 허혈성 뇌졸중 위험도를 약 1.4배 증가시킵니다.";
	private final String 허혈성_뇌졸중_MTHFR_c1298_AA = "검사한 MTHFR 유전자의 1298 영역은 AA 유전자형으로 허혈성 뇌졸중 위험도 증가와 관련된 유전자형이 검출되지 않았습니다.";
	private final String 허혈성_뇌졸중_MTHFR_c1298_AC = "검사한 MTHFR 유전자의 1298 영역은 AC 유전자형으로 허혈성 뇌졸중 위험도 증가와 관련된 유전자형이 검출되지 않았습니다.";
	private final String 허혈성_뇌졸중_MTHFR_c1298_CC = "MTHFR 1298CC 유전자형이 검출된 경우 표준 유전형에 비해 허혈성 뇌졸중 위험도를 약 1.5배 증가시킵니다.";
	private final String[] 모야모야병_RNF213_c14429_GG = new String[] {"검사한 RNF213 유전자에서 모야모야병 위험도 증가와 관련된 유전자형이 검출되지 않았습니다."};
	private final String[] 모야모야병_RNF213_c14429_GA = new String[] {"RNF213 유전자형이 GA인 경우 모야모야병 위험도가 일반 인구에 비해 약 96배 증가합니다.", "본 유전자형을 가진 경우에도 실제 모야모야병 발생 확률은 150명 중의 1명 빈도입니다."};
	private final String[] 모야모야병_RNF213_c14429_AA = new String[] {"RNF213 유전자형이 AA인 경우 모야모야병 위험도가 일반 인구에 비해 약 96배 증가합니다.", "본 유전자형을 가진 경우에도 실제 모야모야병 발생 확률은 150명 중의 1명 빈도입니다."};
	private final String[] 카다실_NOTCH3_c1630_CC = new String[] {"검사한 NOTCH3 유전자 위치에서 카다실 위험도 증가와 관련된 유전자형이 검출되지 않았습니다."};
	private final String[] 카다실_NOTCH3_c1630_CT = new String[] {"NOTCH3 유전자형이 CT인 경우 카다실의 위험도가 표준 유전형에 비해 약 100배 이상 증가합니다.", "본 유전자형을 가진 경우 거의 대부분 카다실이 발생합니다."};
	private final String[] 카다실_NOTCH3_c1630_TT = new String[] {"NOTCH3 유전자형이 TT인 경우 카다실의 위험도가 표준 유전형에 비해 약 100배 이상 증가합니다.", "본 유전자형을 가진 경우 거의 대부분 카다실이 발생합니다."};
	@Override
	public List<String> recommendation(DiseaseRiskScreen disease, GeneRiskScreen gene, Snv snv, String genotype) {
		if(DiseaseRiskScreenN087.허혈성_뇌졸중 == disease) {
			if(GeneRiskScreenN087.APOE == gene) {
				if("e2e2".equalsIgnoreCase(genotype))		return null;
				else if("e2e3".equalsIgnoreCase(genotype))	return null;
				else if("e2e4".equalsIgnoreCase(genotype))	return List.of(허혈성_뇌졸중_RISK_HIGH);
				else if("e3e3".equalsIgnoreCase(genotype))	return null;
				else if("e3e4".equalsIgnoreCase(genotype))	return List.of(허혈성_뇌졸중_RISK_HIGH);
				else if("e4e4".equalsIgnoreCase(genotype))	return List.of(허혈성_뇌졸중_RISK_HIGH);
			} else if(GeneRiskScreenN087.MTHFR == gene) {
				if(SnvRiskScreenN087.MTHFR_c677 == snv) {
					if ("CC".equalsIgnoreCase(genotype))		return null;
					else if ("CT".equalsIgnoreCase(genotype)
						 || "TC".equalsIgnoreCase(genotype))	return null;
					else if ("TT".equalsIgnoreCase(genotype))	return List.of(허혈성_뇌졸중_RISK_HIGH);
				} else if(SnvRiskScreenN087.MTHFR_c1298 == snv) {
					if ("AA".equalsIgnoreCase(genotype))		return null;
					else if ("AC".equalsIgnoreCase(genotype)
						 || "CA".equalsIgnoreCase(genotype))	return null;
					else if ("CC".equalsIgnoreCase(genotype))	return List.of(허혈성_뇌졸중_RISK_HIGH);
				}
			}
		} else if(DiseaseRiskScreenN087.모야모야병 == disease) {
			if(GeneRiskScreenN087.RNF213 == gene) {
				if("GG".equalsIgnoreCase(genotype))			return List.of(모야모야병_ALL, 모야모야병_LOW_RISK);
				else if("GA".equalsIgnoreCase(genotype)
					 || "AG".equalsIgnoreCase(genotype))	return List.of(모야모야병_RISK_HIGH);
				else if("AA".equalsIgnoreCase(genotype))	return List.of(모야모야병_RISK_HIGH);
			}
		} else if(DiseaseRiskScreenN087.카다실 == disease) {
			if(GeneRiskScreenN087.NOTCH3 == gene) {
				if("CC".equalsIgnoreCase(genotype))			return List.of(카다실_LOW_RISK);
				else if("CT".equalsIgnoreCase(genotype)
					 || "TC".equalsIgnoreCase(genotype))	return List.of(카다실_RISK_HIGH);
				else if("TT".equalsIgnoreCase(genotype))	return List.of(카다실_RISK_HIGH);
			}
		}
		return null;
	}
	public List<String> recommendationNegative(DiseaseRiskScreen disease) {
		if(DiseaseRiskScreenN087.허혈성_뇌졸중 == disease) return List.of(허혈성_뇌졸중_RISK_LOW);
		else return List.of();
	}
	private final String 허혈성_뇌졸중_RISK_HIGH = "허혈성 뇌졸중은 유전적 요인 뿐만 아니라 다양한 환경적인 요인이 관여합니다. 유전적 위험 요인을 가지고 있다면, 허혈성 뇌졸중 위험도를 낮추기 위해 허혈성 뇌졸중과 관련된 다른 위험 요소(고혈압, 당뇨, 고지혈증, 고호모시스테인혈증 등)에 대해 적극적 관리가 권장됩니다.";
	private final String 허혈성_뇌졸중_RISK_LOW = "유전적 위험요인은 발견되지 않았으나, 생활 습관에 의한 뇌졸중 위험을 예방하기 위한 관리가 필요합니다.";
	private final String 모야모야병_ALL = "본 검사는 모야모야병의 발병 위험도를 예측하는 검사로, 진단적인 검사는 아닙니다.";
	private final String 모야모야병_LOW_RISK = "모야모야병은 RNF213 유전자와 관련이 있는 것으로 알려져 있으나 가족력이나 관련 유전형과 관련 없이 나타나는 경우도 있습니다. 정확한 진단을 위해서는 의료진과의 상담이 권장됩니다.";
	private final String[] 모야모야병_RISK_HIGH = new String[] {
			"모야모야병의 진단은 CT, MRI, 혈관조영술, MR을 이용한 혈관 조영술, SPECT등으로 이루어지며, 진단을 위한 의료진과의 상담이 권장됩니다.",
			"다른 뇌졸중 위험인자 (고혈압, 당뇨, 고지혈증 등)을 가지고 있다면, 이에 대해 적극적 관리가 필요합니다.",
			"현재 증상이 없더라도 뇌혈관 상태에 대한 주기적인 모니터링이 필요할 수 있습니다."
	};
	private final String 카다실_LOW_RISK = "카다실은 NOTCH3 유전자의 다양한 변이에 의해 발병하는 질환입니다. 본 검사는 NOTCH3 유전자의 다른 위치에서 발견되는 변이를 확인하지는 못하므로, 관련된 유전형이 확인되지 않았으나, 카다실이 의심되는 경우 Genome screen을 통한 NOTCH3 유전자의 다른 변이의 확인이 권장됩니다.";
	private final String[] 카다실_RISK_HIGH = new String[] {
			"카다실은 뇌 영상학적 소견에서 기저핵, 백질, 및 뇌교 부위에 다양한 크기와 모양의 다발성 뇌경색이 보일 수 있습니다. 개인에 따라 발병 시기, 증상에 차이가 있으므로 진단 및 치료, 합병증 예방을 위해 의료진과의 상담이 필요합니다.",
			"다른 뇌졸중 위험인자 (고혈압, 당뇨, 고지혈증 등)을 가지고 있다면, 이에 대해 적극적 관리가 필요합니다.",
			"현재 증상이 없더라도 뇌혈관 상태에 대한 주기적인 모니터링이 필요할 수 있습니다."
	};
	private String lblLifestyleTitle = "뇌졸중 관련 다른 위험 인자";
	private String lblLifestyleInfo = "뇌졸중의 위험도를 높이는 유전형 이외의 다른 위험인자 및 위험도 증가 정도입니다.";
	private String lblLifestyleInfo2 = "해당되는 항목에 체크하여 생활습관에 의한 위험도를 알아보세요.\n(모야모야병과 카다실은 뇌졸중 증상이 특징적으로 나타나는 질환입니다.)";
	private String lblLifestyleResultTitle = "뇌졸중";
	private String lblLifestyle = "생활습관";
	public String lblLifestyleResultHeader(GenomeScreenWithRiskScreenDto dto) {
		return dto.patientName() + " 님의\n뇌졸중 리스크스크린 결과";
	}
	public String toString(RiskFactor factor) {
		if(factor instanceof RiskFactorN087) switch((RiskFactorN087)factor) {
			case 고혈압:				return "고혈압";
			case 흡연:				return "흡연";
			case 비만:				return "비만";
			case 당뇨:				return "당뇨";
			case 음주:				return "음주(30회 이상 / 1달)";
			case 스트레스:			return "스트레스";
			case 심혈관계_유질환자:	return "심혈관계 유질환자";
			default:				return null;
		}
		return null;
	}
	private String lblGuidePreventTitle = "뇌졸중 예방 수칙";
	private String lblGuideDietInfo = "포화지방산과 콜레스테롤의 과잉 섭취, 칼로리 섭취와 소모의 불균형, 당질의 과잉 섭취 및 섬유소 섭취 부족, 그리고 알코올의 과잉 섭취 등이 혈청 지질에 영향을 미치므로 혈중 콜레스테롤이나 포화 지방산 농도를 감소시키고 당질 제공량은 총 열량의 60% 이하로 제한합니다.";
	private String lblGuideReferenceTitle = "참고 논문";
}
