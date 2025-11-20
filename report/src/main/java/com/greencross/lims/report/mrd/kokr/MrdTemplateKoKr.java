package com.greencross.lims.report.mrd.kokr;

import com.gcgenome.lims.test.mrd.TestInfo;
import com.greencross.lims.report.builder.LogoType;
import com.greencross.lims.report.builder.Sex;
import com.greencross.lims.report.mrd.MrdDto;
import com.greencross.lims.report.mrd.MrdResource;
import com.greencross.lims.report.mrd.MrdTemplate;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Accessors(fluent = true)
public class MrdTemplateKoKr implements MrdTemplate {
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

	private final String lblSummary						= "결과 요약";
	private final String lblResultDetected				= "MRD\n검출 여부";

	private final String lblDetails						= "상세 결과";
	private final String tmplInterpretationPositive		= "%g 클론 추적 관찰 결과 미세잔존질환이 확인되었습니다.";
	private final String tmplInterpretationNegative		= "%g 클론 추적 관찰 결과 미세잔존질환이 확인되지 않았습니다.";
	private final String tmplInterpretationNA			= "See Interpretation";
	private final String[] lblComments;
	private final String lblTestInfo					= "검사 정보";
	private final String lblTestMethod					= "1. 검사 방법";
	private final String lblTestQc						= "2. QC 정보";
	private final String lblTestLimitation				= "3. 검사의 한계";
	private final String[] lblQcComments;
	private final String[] lblLimitations;
	private final MrdResource resource;
	private final LogoType logoType;
	private final TestInfo testInfo;
	public MrdTemplateKoKr(MrdResource resource, TestInfo testInfo, LogoType logoType) {
		this.resource = resource;
		this.testInfo = testInfo;
		this.logoType = logoType;
		lblComments	= new String[] {
				"* 검체당 " + testInfo.cell() + " 100개의 DNA(LymphoQuant Internal Control, LQIC)를 혼합하여 측정된 근사치입니다.",
				"** 전체 " + testInfo.cell() + " 중 클론성 세포가 차지하는 비율을 환산한 근사치입니다.",
				"*** 전체 유핵 세포 수는 Input DNA로부터 도출된 값입니다. (Estimated DNA mass per 1 cell = 6.5pg). "
		};
		lblQcComments = new String[] {
				"* Estimated DNA mass per 1 cell = 6.5pg",
				"** 검체당 " + testInfo.cell() + " 100개의 DNA (LymphoQuant internal control, LQIC)를 혼합하여 측정된 근사치입니다."
		};
		lblLimitations = new String[]{
			"검체 내 전체 read depth가 190,000 이상인 경우 클론성 염기서열은 10⁻⁴ 의 분석 민감도를 가지며, read depth가 190,000 이상이 아닌 경우에도 95% 신뢰구간 내에 발견된 클론성 염기서열은 보고될 수 있습니다.",
			"PCR 증폭은 " + testInfo.cell() + "의 DNA양에 영향을 받으며 primer결합 부위 변이가 있는 경우 위음성을 보일 가능성이 있습니다.",
			"본 검사 결과는 임상, 조직학 및 면역 표현형 데이터의 맥락에서 해석되어야 합니다.",
			"본 검사는 clonal cell populations 의 100 %를 식별하지 않습니다."
		};
	}
	public final MrdResource resource() {
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
	public final LogoType logoType() {
		return this.logoType;
	}

	@Override
	public String tmplInterpretation(MrdDto.Result result) {
		return switch (result) {
			case DETECTED		-> tmplInterpretationPositive;
			case NOT_DETECTED	-> tmplInterpretationNegative;
			case NA				-> tmplInterpretationNA;
			default				-> "";
		};
	}
}
