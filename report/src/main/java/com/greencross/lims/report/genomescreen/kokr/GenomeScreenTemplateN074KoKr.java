package com.greencross.lims.report.genomescreen.kokr;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.greencross.lims.report.genomescreen.GenomeScreenTemplateN074;
import com.gcgenome.lims.test.genomescreen.TestInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.awt.*;

// 암 지놈 스크린
@EqualsAndHashCode(callSuper = true)
@Getter
@Accessors(fluent = true)
public class GenomeScreenTemplateN074KoKr extends GenomeScreenTemplateKoKr<GenomeScreenResourceN090KoKr> implements GenomeScreenTemplateN074<GenomeScreenResourceN090KoKr> {
	private final GenomeScreenResourceN090KoKr resource;
	private final String lblSummaryTestIntroTitleInfo = "암 지놈 스크린 검사에 대한 간단한 설명입니다.";
	private final String lblSummaryInfo = "본 검사는 최신 유전자 분석기법으로 유전성 암을 발병시킬 수 있는 유전자를 검사하며 기존 연구논문 결과를 분석하여 " +
										  "건강 관리에 도움이 될 수 있는 개인 맞춤형 정보를 제공합니다. 유전성 암과 관련된 병원성 변이가 발견된 경우 " +
										  "개인에 따라 증상이 없을 수 있으나(reduced penetrance) 일반 인구에 비해 암 발생 위험도가 매우 높기 때문에 " +
										  "암 발생 위험도를 낮추기 위한 조치 및 발견을 위한 주기적인 정밀 검사 등이 권장됩니다.";
	private final String lblGuideTestTitle = "암 지놈 스크린 검사란?";
	private final TextBlock[] guideTest;
	private final String lblGuideDiseaseTitle = "유전성 암 질환은 이러한 특징이 있습니다.";
	private final TextBlock[] guideDisease;
	private final String lblGeneListInfo = "암 지놈 스크린 검사는 미국의학 유전학회(ACMG)의 권고에 따라 다음과 같은 질환 및 유전자들을 검사합니다.";
	private final TextBlock[] limitations;
	private final TextBlock[] references;
	public GenomeScreenTemplateN074KoKr(GenomeScreenResourceN090KoKr resource, TestInfo testInfo) {
		super(testInfo);
		this.resource = resource;
		TextStyle textStyle = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(10).paragraph(true);
		guideTest = new TextBlock[] {
				new TextBlock(textStyle.clone().fonts(resource.fontHeader(), resource.fontDefault()).color(Color.BLACK), "유전성 암(Hereditary Cancer)"),
				new TextBlock(textStyle, "이란 종양 발생과 관련된 유전자, 즉 종양 유전자(Oncogene) 또는 종양 억제유전자(Tumor Suppressor Gene) 의 이상으로 인해 발병하는 암 질환을 뜻하며, " +
												"전체 암의 5~10% 정도가 유전성 암에 해당하는 것으로 알려져 있습니다.\n" +
												"유전성 암은 비유전성으로 발생하는 암에 비해 조기에 발병하고 여러 장기에서 암이 발생할 수 있기 때문에 유전자 검사를 통한 조기 진단이 중요합니다.\n" +
												"암 지놈 스크린 검사(Cancer Genome Screen)는 유방암, 난소암, 대장암, 전립선암, 췌장암, 갑상선암 등 다양한 암의 발생 위험을 높이는 것으로 알려진 " +
												 "35개의 유전자를 차세대 염기서열분석(Next Generation Sequencing; NGS) 검사법으로 한 번에 검사하여 유전성 암 질환의 예방 및 조기 진단, 치료 " +
												 "효과의 향상을 기대할 수 있는 검사입니다.")
		};
		guideDisease = new TextBlock[] {
				new TextBlock(textStyle, "유전성 암은 특정 암을 유발시키는 것으로 알려진 유전자의 병원성 변이(Pathogenic variant)가 원인이 됩니다. 유전성 암은 암의 종류마다 관련된 유전자가 다를 수 있고, 하나의 유전자 이상이 다양한 암을 일으킬 수 있습니다."),
				new TextBlock(textStyle, "유전성 암 관련 유전자에서 병원성 변이가 발견되더라도 100% 질환이 발병하는 것은 아닙니다(Reduced Penetrance). 그러나, 일반 인구에 비해 암 발생 위험도가 매우 높기 때문에 이러한 사실을 알고 미리 예방하는 것이 중요합니다. 특히 발생률이 높은 암의 종류와 위험도를 미리 파악하여 조기 발견을 위한 주기적인 정밀 검사 등이 권장됩니다."),
				new TextBlock(textStyle, "유전성 암은 전체 암 발생 중 5~10%에 해당하며 2명 이상의 가족이 암 진단을 받았을 경우 젊은 나이에 암이 발병하거나 여러 장기에서 암이 동시에 발병할 위험도가 증가하는 것으로 알려져 있습니다."),
				new TextBlock(textStyle, "유전성 암과 관련된 유전자 검사에서 질환과 관련된 병원성 변이가 발견되지 않았더라도 환경적 영향 및 생활습관 등 비유전성 원인으로 인한 암의 발병 가능성은 여전히 존재합니다.")
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
				new TextBlock(textStyle, "단, 일부 유전자(RET, SDHAF2)는 잘 알려진 병원성 변이에 대해서만 보고하는 것을 원칙으로 합니다."),
				new TextBlock(textStyle, "검사에 포함된 유전자 중 MUTYH는 상염색체 열성으로 유전됩니다. 상염색체 열성의 경우 두 개의 병원성 변이가 존재해야 암 발생 가능성이 높아집니다. 따라서, MUTYH 유전자의 경우 두 개의 병원성 변이가 존재할 때만 보고하는 것을 원칙으로 합니다."),
				new TextBlock(textStyle, "검사에 포함된 유전자는 전체 엑손을 포함하나, 일부 영역에서는 염기서열 해독이 충분하지 않을 수 있습니다. " +
												 "또한, 상동성이 높은 염기서열이 존재하는 경우 염기서열 해독이 정확하지 않을 수 있으며, 큰 " +
												 "결실 또는 중복, 단백질을 만들지 않는 서열 부위에 존재하는 변이는 검출이 어려울 수 있습니다.")
		};
		TextStyle referenceStyle = new TextStyle().color(resource.colorText()).fonts(resource.fontScientific(), resource.fontText(), resource.fontDefault()).fontSize(8).paragraph(true);
		references = new TextBlock[] {
				new TextBlock(referenceStyle, "Breast Cancer Information Core(https://research.nhgri.nih.gov/bic/)"),
				new TextBlock(referenceStyle, "The Human Gene Mutation Database(http://www.hgmd.cf.ac.uk)"),
				new TextBlock(referenceStyle, "Gene Reviews(https://www.ncbi.nlm.nih.gov/books/NBK1116/)")
		};
	}
	private String diseaseName(DiseaseN074 disease) {
		switch (disease) {
			case 유전성_유방암_난소암_증후군:	return "유전성 유방암-난소암 증후군";
			case 리_프라우메니_증후군:			return "리-프라우메니증후군";
			case 포이츠_제거스_증후군:			return "포이츠-제거스증후군";
			case 린치_증후군:				return "린치증후군";
			case 용종증_증후군:				return "용종증 증후군";
			case 폰히펠_린다우_증후군:			return "폰히펠-린다우 증후군";
			case 다발성_내분비선종증:			return "다발성 내분비선종증";
			case PTEN_과오종_증후군:			return "PTEN 과오종 증후군";
			case 망막모세포종:				return "망막모세포종";
			case 유전성_부신경절종_갈색세포종:	return "유전성 부신경절종-갈색세포종";
			case 결절성_경화증:				return "결절성 결화증";
			case WT1_연관_윌름스_종양:		return "WT1 연관 윌름스 종양";
			case 제2형_신경섬유종증:			return "제2형 신경섬유종증";
			default:						return "";
		}
	}
	private String diseaseInfo(DiseaseN074 disease) {
		switch (disease) {
			case 유전성_유방암_난소암_증후군:	return "BRCA1, BRCA2 유전자의 이상으로 인해 유방암 및 난소암(남성의 경우 전립선암, 남성 유방암)이 발생하는 유전성 질환";
			case 리_프라우메니_증후군:			return "종양억제유전자인 TP53의 이상으로 인해 상염색체 우성으로 유전되는 가족성 암 질환";
			case 포이츠_제거스_증후군:			return "소화관에 발생하는 다발성의 과오종성 용종증과 피부점막의 멜라닌 색소침착을 보이는 유전되는 질환";
			case 린치_증후군:				return "손상된 DNA 복구에 관여하는 유전자의 이상으로 인해 여러 암에 대한 위험도를 보이는 질환";
			case 용종증_증후군:				return "위, 대장 및 직장에 수 백개에서 수 만개까지의 선종이 발생하는 유전성 질환";
			case 폰히펠_린다우_증후군:			return "여러 장기 중 특히 중추신경계 및 신장에 악성 및 양성 종양이 발생하는 유전성 질환";
			case 다발성_내분비선종증:			return "갑상선, 부갑상선, 장췌장 신경내분비계, 뇌하수체 전엽 등 내분비계 및 피부에 종양이 발생하는 유전성 질환";
			case PTEN_과오종_증후군:			return "PTEN 유전자의 변이로 인해 과오종으로 분류할 수 있는 특징적인 피부병변 이외에 각종 장기의 이상이 동반되는 질환";
			case 망막모세포종:				return "주로 영유아기에 망막의 시신경세포에서 원발성 악성 종양이 발생하는 질환";
			case 유전성_부신경절종_갈색세포종:	return "부신에서의 갈색세포종과 부신경절종 및 신경내분비 종양을 나타내는 내분비 질환";
			case 결절성_경화증:				return "정신지연, 간질, 피부병변을 비롯한 중추신경계 및 다양한 신체 부위의 종양이 나타나는 유전성 질환";
			case WT1_연관_윌름스_종양:		return "소아에서 가장 흔한 복부 종양으로서 신장에 생기는 악성 종양과 함께 선천성 기형이 동반될 수 있는 질환";
			case 제2형_신경섬유종증:			return "내이(안쪽 귀)로부터 뇌신경의 일종인 청신경에 양성 종양이 생기는 질환";
			default:						return "";
		}
	}
	private String diseaseSubName(DiseaseSubN074 diseaseSub) {
		switch (diseaseSub) {
			case 유전성_유방암_난소암_증후군:			return "유방암, 난소암, 전립선암";
			case 리_프라우메니_증후군:					return "유방암, 뇌종양, 백혈병, 부신피질암 등";
			case 포이츠_제거스_증후군:					return "대장암, 위암";
			case 린치_증후군:						return "대장암, 자궁내막암, 위암, 난소암 등";
			case 가족성_선종성_용종증:					return "가족성 선종성 용종증";
			case MUTYH_연관_용종증:					return "MUTYH 연관 용종증";
			case 연소성_용종증_증후군:					return "연소성 용종증 증후군";
			case 폰히펠_린다우_증후군:					return "중추신경계 혈관모세포종, 망막 혈관모세포종, 갈색세포종";
			case 제1형_다발성_내분비선종증:			return "제1형 다발성 내분비선종증";
			case 제2형_다발성_내분비선종증:			return "제2형 다발성 내분비선종증";
			case PTEN_과오종_증후군:					return "유방암, 갑상선암";
			case 망막모세포종:						return "망막모세포종";
			case 유전성_부신경절종_갈색세포종:			return "부신경절종, 갈색세포종";
			case 결절성_경화증:						return "망막종양, 뇌종양, 폐의 림프종 등";
			case WT1_연관_윌름스_종양:				return "신세포암";
			case 제2형_신경섬유종증:					return "청신경초종";
			default:								return "";
		}
	}
	@Override
	public String diseaseName(Disease disease) {
		if(disease instanceof DiseaseN074) return diseaseName((DiseaseN074) disease);
		return null;
	}

	@Override
	public String diseaseInfo(Disease disease) {
		if(disease instanceof DiseaseN074) return diseaseInfo((DiseaseN074) disease);
		return null;
	}

	@Override
	public String diseaseSubName(DiseaseSub diseaseSub) {
		if(diseaseSub instanceof DiseaseSubN074) return diseaseSubName((DiseaseSubN074) diseaseSub);
		return null;
	}
	@Override
	public String diseaseSubInfo(DiseaseSub diseaseSub) {
		if(diseaseSub instanceof DiseaseSubN074) switch ((DiseaseSubN074) diseaseSub) {
			case 유전성_유방암_난소암_증후군:			return "유방암의 5-20%정도는 부모로부터 물려받아 태어날 때부터 가지고 있는 유전자 이상에 의해 발생합니다. 유전성 유방암의 대부분은 BRCA1과 BRCA2유전자의 병원성 변이가 주요 원인입니다. 이 유전자에 변이가 있는 여성의 경우 최대 80% 에서 유방암이 발생하고, 최대 40%에서 난소암이 발생하게 됩니다.";
			case 리_프라우메니_증후군:					return "TP53 유전자 병원성 변이가 있는 경우, 리-프라우메니 증후군 발생 위험이 높아집니다. 리-프라우메니 증후군은 유년기 발생하는 육종, 뇌종양, 백혈병, 부신피질암과 폐경 이전에 발생하는 유방암을 특징으로 합니다.";
			case 포이츠_제거스_증후군:					return "포이츠-제거스증후군은 상염색체 우성으로 유전되는 질환으로 위장관에 과오종을 형성하고 피부점막부에 색소 침착이 특징적인 질환 입니다. 과오종은 소장에서 가장 흔하며 (78%), 위(38%) 와 대장(20-40%)에도 발생합니다. 드물게 비강, 기관지, 요로계 등에서도 과오종이 유발됩니다. 과오종 자체가 악성종양으로 발전하는 경우는 흔하지 않으나, 드물게 선종성 변화 및 선암으로의 진행이 있는 것으로 알려져 있고 이 질환을 가진 가족에서 대장암이 다발하는 증례들이 보고되어 있습니다. 또한, 대장암 외에도 위암, 유방암, 자궁경부암, 난소암, 고환암 및 췌장암 등이 호발 됩니다. 입술에 작은 흑점이 퍼지고 손바닥과 발바닥에도 흑갈색 반점이 나타납니다.";
			case 린치_증후군:						return "유전성 비용종성 대장암은 전체 대장암의 5-10%를 차지하며, 대장암 이외에도 다양한 장기의 암을 발생시키는 질환입니다. 대장직장암의 가족력이 있거나 50세 이전에 젊은 나이에 발생한 경우 의심해 볼 수 있습니다. MLH1, MSH2, MSH6 등의 유전자의 병원성 변이가 원인이며, 상염색체 우성으로 유전됩니다. 해당 유전자의 이상을 가진 경우 대장암의 발생 확률은 50-80%이며 25-60%에서 자궁내막암이 발생합니다. 그 외에도 위암 (6-13%), 난소암 (4-12%) 발생 위험이 증가합니다. 따라서 대장, 위, 여성 생식기, 비뇨기계에 대한 정기적인 검사를 시행해야 합니다.";
			case 가족성_선종성_용종증:					return "가족성 선종성 용종증은 대장 및 직장에 수백 개에서 수만 개까지의 선종이 발생하는 질환으로 약 7,000명 중 한 명의 빈도로 발생합니다. APC 유전자의 병원성 변이가 원인이며 상염색체 우성으로 유전됩니다. 발생 부분을 제거하지 않으면 용종들이 악성 종양으로 발전하여 40대에 이르면 용종에서 거의 100% 대장암이 발생합니다. 대장암 이외에도 소장암, 위암, 십이지장암 등 다양한 장기에 암이 발생할 수 있습니다. 병원성 변이가 확인된 경우 대장에 대한 예방적 절제술이 시행되어야 합니다.";
			case MUTYH_연관_용종증:					return "가족성 선종성 용종증과의 감별진단으로 가장 중요한 병이 MUTYH 연관 용종증입니다. 보통 20개에서 수백 개의 샘종이 발견되며 대장암 위험이 매우 증가하는 질환입니다. 십이지장암 및 난소암, 방광암, 피부암, 유방암, 자궁내막암 발생 위험도 증가하며, 이들암은 용종보다 비교적 늦은 나이에 발생합니다. 평균 진단 나이는 45세입니다. MUTYH 연관 용종증환자에서의 추적 관찰 및 예방 목적의 표준 가이드라인이 없지만, 유럽에서는 약화형 가족성 선종성 용종증 환자와 유사한 예방 및 치료 가이드라인을 적용하고 있습니다.";
			case 연소성_용종증_증후군:					return "연소성용종증증후군(Juvenile polyposis syndrome, JPS)은 위장 내벽에 용종이 자라는 것으로 용종은 위에서 직장까지 발생하나 소장에서는 흔치 않습니다. 주로 20대 전후에 보고되며 다양한 크기와 형태의 용종이 수 개에서 백 개 이상 발견되기도 합니다.";
			case 폰히펠_린다우_증후군:					return "폰 히펠-린다우 증후군(VHL)은 여러 장기 중 특히 중추신경계 및 신장에 발생하는 악성 및 양성 종양을 특징으로 하는 질환입니다. 망막의 혈관종증과 중추신경계의 혈관모세포종, 신장과 췌장의 낭종 및 암종, 부신의 갈색세포종, 부고환의 유두상 낭선종, 간과 비장 및 폐의 낭종 등이 발생할 수 있으며 중추신경계의 혈관모세포종으로 환자의 절반 이상이 사망합니다. 상염색체 우성으로 유전되며, 약 3만 6,000명 당 1명의 발생빈도를 보입니다. 연령이 증가함에 따라 여러 장기에 종양 발생의 빈도가 증가하기 때문에, 평균 수명은 약 54세 이며, 사인의 대부분은 중추신경혈관아세포종과 신세포암입니다.";
			case 제1형_다발성_내분비선종증:			return "제1형 다발성 내분비선종증은 부갑상선, 췌장 신경내분비계, 뇌하수체 전엽 그리고 피부에 종양이 발생하는 종양 증후군입니다. 가장 흔한 내분비 종양은 부갑상선항진증과 고칼슘혈증을 일으키는 부갑상선 종양입니다. 그리고 가스트린종, 인슐린종, 프로락틴샘종, 카르시노이드종양도 생길 수 있습니다. 피부 종양은 흔하지만 쉽게 놓칠 수 있습니다. 피부에 생기는 종양으로는 다발성 혈관섬유종, 아교질(阿膠質)종 그리고 지방종이 있습니다. 이러한 양성 종양을 발견하는 것은 제1형 다발성 내분비선종증이 있음을 예측할 수 있는 표지자로 도움이 되기 때문에 진단에 중요합니다.";
			case 제2형_다발성_내분비선종증:			return "다발성 내분비성 종양은 갑상선, 부갑상선, 부신 등의 체내 여러 내분비선에서 발생하는 종양으로, 제2형의 경우 임상양상에 따라 MEN2A와 MEN2B 및 가족성 갑상선 수질암으로 분류할 수 있으며, 3가지 유형 모두에서 갑상선 수질암의 발생 위험이 있다는 공통점이 있습니다. 갑상선 수질암의 약 20%가 유전성으로 발생합니다. RET 유전자의 병원성 변이가 원인이며, 상염색체 우성으로 유전됩니다. 갑상선 수질암은 전체 갑상선암의 1-3%에 불과하나, 진단 당시에 림프성과 혈행성 전이가 흔하고 예후가 더 나쁜 것으로 알려져 있습니다. RET 유전자의 병원성 변이가 있는 경우 갑상선 수질암이 거의 100%에서 나타납니다.";
			case PTEN_과오종_증후군:					return "PTEN 과오종 증후군은 양성 종양 같은 기형(과오종)이 생기고 암(특히 유방암, 갑상선암)발생 위험이 증가하는 특징을 가집니다. 증상은 세부 질환 및 환자마다 매우 다양하고, 모든 연령에서 발생이 가능합니다. 모든 환자에서 피부병변이 일어나며, 그 외 병변이 나타나는 신체부위는 매우 다양합니다. 그 중 유방(70%), 갑상선(68%), 비뇨기계(60%)가 흔히 침범되며 그 외 눈, 신경계, 호흡기계, 심혈관계, 드물지만 골격계에서도 발현되는 것으로 알려져 있습니다. 많은 병변에서 악성 변화가 보고되고 있으며, 피부질환도 악성화의 가능성은 있으나 주로 유방과 갑상선에서 악성화가 일어나는 것으로 알려져 있습니다. 유방질환은 36% 에서 악성변화를 보이고 갑상선은 단순히 갑상선이 커지는 갑상선 종대에서 선종, 암까지 다양한 변화를 보입니다. 또한 드물게는 자궁내막과 자궁경부에서도 암이 발생하기도 합니다. 소화관 병변은 전체 환자의 50~70% 정도로 보고되고 있는데, 소장보다는 대장에서 보다 많이 발생하고 특히 구불결장이나 직장에서 발생하는 경우가 많이 알려져 있습니다. 출혈, 반복되는 설사 등의 증상을 일으킬 수 있는 대장 용종 중에서 선종(adenoma)은 25% 정도를 차지하는데 암으로 발현되는 경우는 드물고, 악성변화가 PTEN 과오종 증후군에 의한 것인지도 확실하게 증명되지는 않은 상태입니다.";
			case 망막모세포종:						return "망막모세포종은 망막에서 발생하는 소아기 안구 종양이며, 이 종양은 주로 2세 이전에 발생하지만 어느 나이에서나 발생할 수 있습니다. 가장 흔한 임상양상은 백색동공 (동공에서의 흰색 반사)과 사시입니다. 백색 동공은 초기에는 특정한 각도와 빛 아래에서만 보이며 사진기의 플래쉬로 인해 보일 수 있습니다. 홍채 홍조, 앞방고름, 앞방출혈, 큰안구증, 안와 봉와직염, 안구 돌출증과 같은 다른 징후들도 보일 수 있습니다. 망막모세포종은 여러 관점에서 종류를 나눌 수 있지만 임상적으로는 흔히 유전성과 비유전성으로 구분됩니다. 유전 여부는 환자의 치료와 추적 관찰에 중요할 뿐만 아니라 환자의 가족에서 같은 종양이 생길 가능성을 예측할 수 있어 망막모세포종을 조기에 발견하고 적절한 치료를 하는 데 중요한 요소입니다.";
			case 유전성_부신경절종_갈색세포종:			return "두저개에서 골반저까지 어느 곳의 부신경절로부터 발생할 수 있는 부신경절종과 부신에서 발생하는 갈색세포종을 포함하는 신경내분비 질환입니다. 전체 부신경절종/갈색세포종 환자의 30%정도가 유전성이며 유병율은 갈색세포종의 경우 50만 명 당 1명, 부신경절종의 경우 100만 명 당 1명 정도로 발생합니다. 증상으로는 종괴에서 분비되는 카테콜아민과 관련되어 나타나는 혈압상승, 두통, 간헐적인 발한, 빈맥 또는 불안감 등이 있습니다. 카테콜아민을 분비하지 않는 부신경절종은 주로 두경부에 위치하며, 커다란 종괴로 나타나는 경우가 있어 청력저하, 박동성 이명, 기침, 애성, 삼킴곤란 등의 증상을 보일 수 있습니다.";
			case 결절성_경화증:						return "결절성 경화증은 정신지연, 간질, 피부 병변 등의 증상이 특징적으로 나타나고 6,000-9,000명 당 1명의 빈도로 드물게 발생하는 선천성 질환입니다. 중추신경계 및 심장, 신장, 눈, 피부, 치아 등 다양한 신체 부위를 침범하여 여러 가지 증상을 일으키며, 종양을 일으킬 수 있기 때문에 장기적인 관찰이 필요한 병입니다. 관련 증상 역시 워낙 복잡하다보니 다양한 분야의 전문가들이 총 동원되어야하는 어려운 질환입니다.";
			case WT1_연관_윌름스_종양:				return "윌름스 종양(Wilms tumor)은 신장에 생기는 종양으로 소아에서 가장 흔한 종양입니다. 윌름스 종양의 원인은 유전적으로 다양하며 WT1 유전자의 병원성 변이는 윌름스 종양의 10~15%에서 나타난 다고 보고되고 있고, 우리나라에서는 1년에 50명 정도의 윌름스 종양이 보고되고 있습니다. 이 중 약 80%가 5세 미만에서 발생하고 호발연령은 2-4세로 나타나고 있습니다. 윌름스 종양의 증상으로는 복부동통이나 구토, 구역 등의 위장 장애가 있으며 종종 혈뇨도 보입니다. 대부분 소아에서 발병하지만, 간혹 성인에서 발병하는 경우에는 예후가 불량한 것으로 알려져 있습니다. 복강 내에 흔히 발생하는 종양인 신경 모세포종과 비교하면 윌름스 종양 환자에서 발생연령이 조금 높고 전신 증상은 적은 편입니다. 뼈에 전이된 경우에는 고통을 호소할 수 있습니다.";
			case 제2형_신경섬유종증:					return "제2형 신경섬유종증(Neurofibromatosis type II)은 내이(안쪽 귀)로부터 뇌신경의 일종인 청신경에 양성 종양(양측성 청신경초종)이 생기는 희귀 질환입니다. 또한 제2형 신경섬유종증을 가진 환자들에서는 신경에 다른 종양의 가능성이 증가하는 것으로 알려져 있으며, 신경초종,수막종,신경교종이 이에 속합니다. 증상은 주로 아동기, 청소년기나 성인 초기에 나타납니다. 청신경에 양성종양이 생기면서 균형 감각과 걸음걸이에 장애가 생길 수 있고 어지러움증, 추통, 저림, 통증, 이명(귀가 울리는 현상)이 생기며 점차적으로 청력을 잃게 됩니다. 이러한 증상은 청신경초종의 크기와 위치에 따라 다르게 나타납니다. 이 외에도 얼굴에 경련이 일어나고, 전반적으로 근육이 허약해지며, 음식을 삼키고 말하는데 어려움을 겪을 수 있습니다. 또한 통증이나 부분마비가 발생하기도 합니다. 환자의 약 85% 에서는 백내장이 나타납니다. 신경섬유종증 제1형과는 달리 커피색 반점(cafe-au-lait)과 같은 피부의 병변은 흔하지 않습니다. 유전적으로 이상이 있는 경우 약 75%에서 양측성 청신경초종이 발생하며 50%에서 다른 뇌종양이 동반됩니다. 또한 다른 양성 종양들이 중추신경계에 생길 수 있습니다.";
			default:								return "";
		}
		return null;
	}
	@Override
	public String diseaseSubReference(DiseaseSub diseaseSub) {
		if(diseaseSub instanceof DiseaseSubN074) switch ((DiseaseSubN074) diseaseSub) {
			case 유전성_유방암_난소암_증후군:		return "03. 예방 및 치료 page12~13, 17~18 참조";
			case 리_프라우메니_증후군:				return "03. 예방 및 치료 page19~21 참조";
			case 포이츠_제거스_증후군:				return "03. 예방 및 치료 page22~23 참조";
			case 린치_증후군:					return "03. 예방 및 치료 page24~27 참조";
			case 가족성_선종성_용종증:				return "03. 예방 및 치료 page28~29 참조";
			case MUTYH_연관_용종증:				return "03. 예방 및 치료 page30~31 참조";
			case 연소성_용종증_증후군:				return "03. 예방 및 치료 page32~33 참조";
			case 폰히펠_린다우_증후군:				return "03. 예방 및 치료 page34~35 참조";
			case 제1형_다발성_내분비선종증:		return "03. 예방 및 치료 page36~37 참조";
			case 제2형_다발성_내분비선종증:		return "03. 예방 및 치료 page38~40 참조";
			case PTEN_과오종_증후군:				return "03. 예방 및 치료 page41~43 참조";
			case 망막모세포종:					return "03. 예방 및 치료 page44~45 참조";
			case 유전성_부신경절종_갈색세포종:		return "03. 예방 및 치료 page46~47 참조";
			case 결절성_경화증:					return "03. 예방 및 치료 page48~49 참조";
			case WT1_연관_윌름스_종양:			return "03. 예방 및 치료 page50~51 참조";
			case 제2형_신경섬유종증:				return "03. 예방 및 치료 page52~53 참조";
			default:							return "";
		}
		return null;
	}
	@Override
	public String clinicalMeaning(Gene gene) {
		if(gene instanceof GeneN074) switch((GeneN074)gene) {
			case BMPR1A:	return "65명의 연소성 용종증 증후군 환자(JPS) 중 30명(46%)의 환자에서 점 돌연변이(point mutation)가 발견되었으며 그 중 BMPR1A 유전자에서 13개의 변이가 발견됨(5 nonsense, 2 frameshift, 4 missense, 2 splice site 변이). MLPA 분석 결과 JPS 환자 중 14%에서 유전자 결실이 발견되었음(SMAD4 유전자 6명, BMPR1A 유전자 3명). BMPR1A 유전자 변이를 가지고 있는 13명의 환자 중 1명(8%)에서 위 용종증 증상을 보임.";
			case MSH6:		return "대장암 환자 유전력이 있는 129 가족군에서 MLH1, MSH2, MSH6, PMS2 유전자 변이를 확인한 결과 69개의 서로 다른 변이가 확인되었으며, 이 중 MSH6 유전자의 변이는 12%에 해당한다.";
			case MUTYH:		return "가족성선종성용종증 유전력이 있으나 APC 유전자 변이가 나타나지 않은 가족에서 MUTYH 유전자 변이를 확인한 결과 가족성선종성용종증이 나타나는 3명의 형제자매에서 missense 변이 p.Tyr165Cys, p.Gly382Asp coumpound heterozygote를 확인함.";
			case PMS2:		return "린치증후군 환자의 62%(heterozygous: 55명, homozygous: 6명)에서 PMS2 유전자의 선천적 변이가 발견됨. PMS2 변이를 가진 경우 일반 인구집단에 비해 대장암 발병률은 5.2배, 자궁내막암 발병률은 7.5배 높게 나타났으며, 북아메리카 인구집단에서 70세까지 린치증후군 관련 암 위험도는 25-32%(95% CI)로 나타남.";
			case SDHAF2:	return "산발적 환자에서 유의미한 SDHAF2 유전자의 결손은 발견되지 않았으며, 7개의 단일염기다형성이 발견됨. 그 중 하나의 변이(c.139A>G, p.Met47Val)만 아미노산의 변화를 야기하지만 in-silico분석과 종간 보존 정도를 분석한 결과 비 병원성 변이로 확인됨. 3명의 딸이 부신경절종 질환을 보이는 스페인 가족에서 SDHAF2 유전자의 선천적 변이(c.232G>A, p.Gly78Arg)가 발견되었으며 동일한 변이가 독일 가족에서도 발견됨.";
			case SDHC:		return "전체 환자 중 SDH 유전자의 변이가 발견된 환자는 총 242명으로 SDHD 유전자 130명, SDHB 유전자 96명, SDHC 유전자 16명으로 나타남. Direct sequencing 결과 점 돌연변이가 발견된 환자는 220명(SDHC 유전자 14명, 5.8%), QMPSF 및 MLPA 분석 결과 유전자 결실이 나타난 환자는 22명(SDHC 유전자 2명, 0.8%) 발견됨. SDHC 유전자 변이를 가진 환자 16명 14명(87.5%)에서 경두부 부신경절종이 발견되었고, 2명(12.5%)에서 흉복부 및 골반 부신경절종이 발견됨.";
			case WT1:		return "31명의 환자 중 25명(81%)에서 WT1 유전자의 이상을 발견함. 25명에서 얻은 35개의 윌름스 종양에서 31개의 homozygous한 WT1 유전자 변이와 IGF2 유전자의 한쪽 부모의 이염색체성 (uniparental disomy)이 나타났고 4개의 종양에서 11p13 유전자 결실이 나타남. 윌름스 종양에 대한 침투도는 아버지로부터 WT1 유전자 돌연변이를 받은 경우 100%, 모계에선 67%로 나타남.";
			default:		return null;
		}
		return null;
	}
}
