package com.greencross.lims.report.genomescreen.kokr;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.greencross.lims.report.genomescreen.GenomeScreenTemplateN101;
import com.gcgenome.lims.test.genomescreen.TestInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.awt.*;

// 캔서 진 스크린
@EqualsAndHashCode(callSuper = true)
@Getter
@Accessors(fluent = true)
public class GenomeScreenTemplateN101KoKr extends GenomeScreenTemplateKoKr<GenomeScreenResourceN101KoKr> implements GenomeScreenTemplateN101<GenomeScreenResourceN101KoKr> {
	private final GenomeScreenResourceN101KoKr resource;
	private final String lblSummaryTestIntroTitleInfo = "캔서 진 스크린 검사에 대한 간단한 설명입니다.";
	private final String lblSummaryInfo = "본 검사는 최신 유전자 분석기법으로 유전성 암을 발병시킬 수 있는 유전자를 검사하며 기존 연구논문 결과를 분석하여 " +
										  "건강 관리에 도움이 될 수 있는 개인 맞춤형 정보를 제공합니다. 유전성 암과 관련된 병원성 변이가 발견된 경우 " +
										  "개인에 따라 증상이 없을 수 있으나(reduced penetrance) 일반 인구에 비해 암 발생 위험도가 매우 높기 때문에 " +
										  "암 발생 위험도를 낮추기 위한 조치 및 발견을 위한 주기적인 정밀 검사 등이 권장됩니다.";
	private final String lblGuideTestTitle = "캔서 진 스크린 검사란?";
	private final TextBlock[] guideTest;
	private final String lblGuideDiseaseTitle = "유전성 암 질환은 이러한 특징이 있습니다.";
	private final TextBlock[] guideDisease;
	private final String lblGeneListInfo = "캔서 진 스크린 검사는 미국의학 유전학회(ACMG)의 권고에 따라 다음과 같은 질환 및 유전자들을 검사합니다.";
	private final TextBlock[] limitations;
	private final TextBlock[] references;
	public GenomeScreenTemplateN101KoKr(GenomeScreenResourceN101KoKr resource, TestInfo testInfo) {
		super(testInfo);
		this.resource = resource;
		TextStyle textStyle = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8).paragraph(true);
		guideTest = new TextBlock[] {
				new TextBlock(textStyle.clone().fonts(resource.fontHeader(), resource.fontDefault()).color(Color.BLACK), "유전성 암(Hereditary Cancer)"),
				new TextBlock(textStyle, "이란 종양 발생과 관련된 유전자, 즉 종양 유전자(Oncogene) 또는 종양 억제유전자(Tumor Suppressor Gene)의 이상으로 인해 발병하는 암 질환을 뜻하며, " +
												"전체 암의 5~10% 정도가 유전성 암에 해당하는 것으로 알려져 있습니다.\n" +
												"유전성 암은 비유전성으로 발생하는 암에 비해 조기에 발병하고 여러 장기에서 암이 발생할 수 있기 때문에 유전자 검사를 통한 조기 진단이 중요합니다.\n" +
												"캔서 진 스크린 검사(Cancer Gene Screen)는 유방암, 난소암 등 다양한 암의 발생 위험을 높이는 것으로 알려진 " +
												 "3개의 유전자를 차세대 염기서열분석(Next Generation Sequencing; NGS) 검사법으로 한 번에 검사하여 유전성 암 질환의 예방 및 조기 진단, 치료 " +
												 "효과의 향상을 기대할 수 있는 검사입니다.")
		};
		guideDisease = new TextBlock[] {
				new TextBlock(textStyle, "유전성 암은 특정 암을 유발시키는 것으로 알려진 유전자의 병원성 변이(Pathogenic variant)가 원인이 됩니다. 유전성 암은 암의 종류마다 관련된 유전자가 다를 수 있고, 하나의 유전자 이상이 다양한 암을 일으킬 수 있습니다."),
				new TextBlock(textStyle, "유전성 암 관련 유전자에서 병원성 변이가 발견되더라도 100% 질환이 발병하는 것은 아닙니다(Reduced Penetrance). 그러나, 일반 인구에 비해 암 발생 빈도가 매우 높기 때문에 이러한 사실을 알고 미리 예방하는 것이 중요합니다. 특히 발생률이 높은 암의 종류와 위험도를 미리 파악하여 조기 발견을 위한 주기적인 정밀 검사 등이 권장됩니다."),
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
				new TextBlock(textStyle, "본 검사에서는 질병 관련성이 확실하거나 매우 높은 병원성 변이와 준병원성 변이 외에도 의미를 " +
												 "알 수 없는 변이, 준양성 변이 및 양성 변이를 모두 보고합니다."),
				new TextBlock(textStyle, "검사에 포함된 유전자는 전체 엑손을 포함하나, 일부 영역에서는 염기서열 해독이 충분하지 않을 수 있습니다. " +
												 "또한, 상동성이 높은 염기서열이 존재하는 경우 염기서열 해독이 정확하지 않을 수 있으며, 큰 " +
												 "결실 또는 중복, 단백질을 만들지 않는 서열 부위에 존재하는 변이는 검출이 어려울 수 있습니다.")
		};
		TextStyle referenceStyle = new TextStyle().color(resource.colorText()).fonts(resource.fontScientific(), resource.fontText(), resource.fontDefault()).fontSize(8).paragraph(true);
		references = new TextBlock[] {};
	}
	private String diseaseName(DiseaseN101 disease) {
		switch (disease) {
			case 유전성_유방암_난소암_증후군:	return "유전성 유방암-난소암 증후군";
			case 리_프라우메니_증후군:			return "리-프라우메니 증후군";
			default:						return "";
		}
	}
	private String diseaseInfo(DiseaseN101 disease) {
		switch (disease) {
			case 유전성_유방암_난소암_증후군:	return "BRCA1, BRCA2 유전자의 이상으로 인해 유방암 및 난소암이 발생하는 유전성 질환";
			case 리_프라우메니_증후군:			return "종양억제유전자인 TP53의 이상으로 인해 상염색체 우성으로 유전되는 가족성 암 질환";
			default:						return "";
		}
	}
	private String diseaseSubName(DiseaseSubN101 diseaseSub) {
		switch (diseaseSub) {
			case 유전성_유방암_난소암_증후군:			return "유방암, 난소암";
			case 리_프라우메니_증후군:					return "유방암, 뇌종양, 백혈병, 부신피질암 등";
			default:								return "";
		}
	}
	@Override
	public String diseaseName(Disease disease) {
		if(disease instanceof DiseaseN101) return diseaseName((DiseaseN101) disease);
		return null;
	}

	@Override
	public String diseaseInfo(Disease disease) {
		if(disease instanceof DiseaseN101) return diseaseInfo((DiseaseN101) disease);
		return null;
	}

	@Override
	public String diseaseSubName(DiseaseSub diseaseSub) {
		if(diseaseSub instanceof DiseaseSubN101) return diseaseSubName((DiseaseSubN101) diseaseSub);
		return null;
	}
	@Override
	public String diseaseSubInfo(DiseaseSub diseaseSub) {
		if(diseaseSub instanceof DiseaseSubN101) switch ((DiseaseSubN101) diseaseSub) {
			case 유전성_유방암_난소암_증후군:	return "유방암의 5-20%정도는 부모로부터 물려받아 태어날 때부터 가지고 있는 유전자 이상에 의해 발생합니다. 유전성 유방암의 대부분은 BRCA1과 BRCA2유전자의 병원성 변이가 주요 원인입니다. 이 유전자에 변이가 있는 여성의 경우 최대 80% 에서 유방암이 발생하고, 최대 40%에서 난소암이 발생하게 됩니다.";
			case 리_프라우메니_증후군:			return "TP53 유전자 병원성 변이가 있는 경우, 리-프라우메니 증후군 발생 위험이 높아집니다. 리-프라우메니 증후군은 유년기 발생하는 육종, 뇌종양, 백혈병, 부신피질암과 폐경 이전에 발생하는 유방암을 특징으로 합니다.";
			default:						return "";
		}
		return null;
	}
	@Override
	public String diseaseSubReference(DiseaseSub diseaseSub) {
		if(diseaseSub instanceof DiseaseSubN101) switch ((DiseaseSubN101) diseaseSub) {
			case 유전성_유방암_난소암_증후군:	return "03. 예방 및 치료 page10~13 참조";
			case 리_프라우메니_증후군:			return "03. 예방 및 치료 page14~15 참조";
			default:						return "";
		}
		return null;
	}
	@Override
	public String clinicalMeaning(Gene gene) {
		return null;
	}
}
