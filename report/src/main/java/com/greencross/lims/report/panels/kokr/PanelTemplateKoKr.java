package com.greencross.lims.report.panels.kokr;

import com.gcgenome.lims.test.panel.TestInfo;
import com.greencross.lims.report.builder.LogoType;
import com.greencross.lims.report.builder.Sex;
import com.greencross.lims.report.panels.PanelResource;
import com.greencross.lims.report.panels.PanelTemplate;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Accessors(fluent = true)
public final class PanelTemplateKoKr implements PanelTemplate {
	private static final DateTimeFormatter DTF 			= DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private final String lblMedicalInstitution			= "의뢰기관";
	private final String lblMedicalRecordNumber			= "등록번호";
	private final String lblRequestNumber				= "접수번호";
	private final String lblPatientName					= "성명";
	private final String lblPatientCode					= "주민번호";
	private final String lblAgeSex						= "나이/성별";
	private final String lblSpecimenType				= "검체종류";
	private final String lblWardDepartment				= "병동/진료과";
	private final String lblCollectionDate				= "검체채취일";
	private final String lblPatientInfo					= "임상정보/기타";
	private final String lblPhysician					= "주치의";
	private final String lblReceiptReportDate			= "접수일/보고일";
	private final String lblNoteAdditionalGene			= "* Additional gene 으로 보고되는 유전자의 경우 PV/LPV에 대해서만 보고됩니다.";

	private final PanelResource resource;
	private final TestInfo testInfo;
	private final LogoType logoType;
	public PanelTemplateKoKr(PanelResource resource, TestInfo testInfo, LogoType logoType) {
		this.logoType = logoType;
		this.resource = resource;
		this.testInfo = testInfo;
	}
	public final PanelResource resource() {
		return resource;
	}

	@Override
	public String date(LocalDate date) {
		if(date == null) return null;
		return DTF.format(date);
	}
	@Override
	public String date(LocalDateTime date) {
		if(date == null) return null;
		return DTF.format(date);
	}
	@Override
	public final String sex(Sex sex) {
		if(sex == null) return "-";
		switch(sex) {
			case M: return "남";
			case F: return "여";
			default: return "-";
		}
	}
	@Override
	public String lblLimitations() {
		String limitationV1 = "본 검사는 염기서열분석법으로 시행되었으며, 검사에 포함된 유전자들의 coding exon과 인접 intron 영역을 분석하고, single exon deletion/duplication, deep intronic mutation, repeat expansion, imprinting defect, genomic rearrangement를 포함한 copy number variant 검출은 제한적입니다. Target region이 capture되지 않았을 가능성도 있으며, homologous region이 존재하는 유전자 혹은 exon의 경우 변이 검출의 정확도가 떨어질 수 있습니다. Confirmatory Sanger sequencing은 자체 설정한 생물정보학적 분석 기준에 따라 생략할 수 있습니다. 검사에서 발견된 변이는 2015 ACMG/AMP guideline (Genet Med 2015;17:405-24)에 따라 \"pathogenic\", \"likely pathogenic\", \"uncertain significance\", \"likely benign\", \"benign\" 의 다섯가지 카테고리로 분류되며, \"likely benign\"과 \"benign\"에 해당하는 변이는 보고하지 않습니다. 관련 문헌과 데이터베이스의 추가적인 연구 결과에 따라 해당 변이의 분류가 변경될 수 있습니다.";
		String limitationV2 = "본 검사는 염기서열분석법으로 시행되었으며, 검사에 포함된 유전자들의 coding exon과 인접 intron 영역을 분석합니다. 그러나, single exon deletion/duplication, deep intronic mutation, repeat expansion, imprinting defect, genomic rearrangement, low-level mosaicism 및 염색체 레벨의 copy number variation 검출은 제한적입니다. 또한 Target region이 capture되지 않았을 가능성도 있으며, homologous region이 존재하는 유전자 혹은 exon의 경우 변이 검출의 정확도가 떨어질 수 있습니다. Confirmatory Sanger sequencing은 자체 설정한 생물정보학적 분석 기준에 따라 생략할 수 있습니다. 검사에서 발견된 변이는 2015 ACMG/AMP guideline (Genet Med 2015;17:405-24)에 따라 \"pathogenic\", \"likely pathogenic\", \"uncertain significance\", \"likely benign\", \"benign\"의 다섯가지 카테고리로 분류되며, \"likely benign\"과 \"benign\"에 해당하는 변이는 보고하지 않습니다. 관련 문헌과 데이터베이스의 추가적인 연구 결과에 따라 해당 변이의 분류가 변경될 수 있습니다.";
		String limitationV3 = "본 검사는 2개 이상의 exon에 걸쳐서 발생한 large deletion/duplication은 대부분 검출 가능하나, single exon deletion/duplication, deep intronic mutation, repeat expansion, imprinting defect, genomic rearrangement, low-level mosaicism 및 염색체 레벨의 copy number variation 검출은 제한적입니다. Target region이 capture되지 않았을 가능성도 있으며, homologous region, GC-rich region, low coverage region이 존재하는 유전자 혹은 exon의 경우 변이 검출 정확도가 떨어질 수 있습니다.";
		switch (testInfo.code()){
			case "G2200201", "G2200101" : return limitationV1;
			case "N077", "N176": return limitationV2;
			default: return limitationV3;
		}
	}
	public LogoType logoType() {
		return this.logoType;
	}
}
