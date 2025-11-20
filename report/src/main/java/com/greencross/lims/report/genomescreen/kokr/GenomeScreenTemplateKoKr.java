package com.greencross.lims.report.genomescreen.kokr;

import com.greencross.lims.report.builder.Sex;
import com.greencross.lims.report.genomescreen.GenomeScreenDto;
import com.greencross.lims.report.genomescreen.GenomeScreenTemplate;
import com.greencross.lims.report.genomescreen.RiskScreenTemplate;
import com.gcgenome.lims.test.genomescreen.TestInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Accessors(fluent = true)
public abstract class GenomeScreenTemplateKoKr<R extends GenomeScreenResourceKoKr> implements GenomeScreenTemplate<R> {
	protected static final DateTimeFormatter DTF 		= DateTimeFormatter.ofPattern("yyyy-MM-dd");
	protected final String lblMedicalInstitution		= "의뢰기관";
	protected final String lblMedicalRecordNumber		= "등록번호";
	protected final String lblRequestNumber				= "접수번호";
	protected final String lblPatientName				= "성명";
	protected final String lblAgeSex					= "나이/성별";
	protected final String lblSpecimenTypeDate			= "검체종류/채취일";
	protected final String lblPatientInfo				= "임상정보/기타";
	protected final String lblReceiptReportDate			= "접수일/보고일";

	private final TestInfo testInfo;
	public GenomeScreenTemplateKoKr(TestInfo testInfo) {
		this.testInfo = testInfo;
	}

	@Override
	public final String date(LocalDate date) {
		if(date == null) return null;
		return DTF.format(date);
	}
	@Override
	public final String date(LocalDateTime date) {
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

	private final String lblPatientCode					= "주민번호";
	private final String lblSpecimenType				= "검체종류";
	private final String lblWardDepartment				= "병동/진료과";
	private final String lblCollectionDate				= "검체채취일";
	private final String lblPhysician					= "주치의";
	@Override
	public String fmtMyResult(GenomeScreenDto dto) {
		return dto.patientName() + "님의 검사 결과는 다음과 같습니다.";
	}

	private final String lblSummaryTestIntroTitle		= "검사설명";
	private final String lblSummaryTitle				= "검사결과 요약";
	private final String[] lblVariantTable				= new String[] {"유전자", "DNA 변이", "아미노산 변이", "접합자", "변이의 분류"};
	private final String lblPositiveDiseaseInfoTitle	= "연관 질환 정보";
	private final String lblPositiveDisease				= "연관 질환";
	private final String lblPositiveDiseaseInfo			= "검사 설명서";
	private final String lblPositiveInterpretationTitle	= "수검자에서 발견된 변이의 해석";
	private final String lblDetailsTitle				= "검사 결과 상세 및 연관 질환";
	private final String lblDetailsDiseaseName			= "주요 대상 질환";
	private final String lblDetailsGene					= "유전자";
	private final String lblDetailsPathogenicVariant	= "병원성 변이";
	private final String lblDetailsPositive				= "발견";
	private final String lblDetailsNegative				= "미발견";
	private final String lblTestInfoTitle				= "검사정보";
	private final String lblTestInfoSpecimen			= "검체(Specimen)";
	private final String testInfoSpecimen				= "말초 혈액의 백혈구(Peripheral blood leukocytes)";
	private final String lblTestInfoMethod				= "검사방법(Method)";
	private final String testInfoMethod					= "차세대염기서열분석법(Next-Generation Sequencing; NGS)";
	private final String lblTestInfoNgs					= "차세대염기서열분석법";
	private final String testInfoNgs					= "유전체를 많은 조각으로 분해하여 각 조각을 동시에 읽어낸 뒤, 얻어진 데이터를 생물정보학적 기법을 이용하여 조합함으로써 방대한 유전체 정보를 빠르게 해독하는 기법입니다.";
	private final String lblSangerIgnored				= "Confirmatory Sanger sequencing은 자체 설정한 생물정보학적 분석 기준에 따라 생략할 수 있습니다.";
	private final String lblReferenceTranscriptTitle	= "유전자 별 표준 전사체(Reference Transcript)";
	private final String lblGeneListTitle				= "질환별 연관 유전자";
	private final String lblLimitationTitle				= "검사 결과의 보고 및 한계";
	private final String lblReferenceTitle				= "참고 문헌";
	private final String lblClinicalMeanings			= "검사 유전자별 임상적 의미";
	private final String lblClinicalMeaningInfo			= "검사 결과가 양성인 사람에서도 질병의 발병 시기와 증상은 다양하며 질병이 반드시 발생한다는 것을 의미하지 않습니다. " +
														 "또한 검사 결과가 음성인 사람에서도 검사하지 않은 다른 유전자 또는 기타 요인에 의해 질병이 발생할 수 있습니다.";
	@Override
	public String lblClinicalMeanings(Tier tier) {
		switch(tier) {
			case Tier1:	return "임상적 의미가 높은 유전자";
			case Tier2:	return "임상적 의미가 일부 증명된 유전자";
			case Tier3:	return "임상적 의미가 낮은 유전자\n본 유전자는 건강에 관련된 행위가 유용하다는 객관적 타당성이 아직 부족합니다.";
		}
		return null;
	}
	public String lblClinicalMeanings(RiskScreenTemplate.Tier tier) {
		switch(tier) {
			case Tier1:	return "임상적 의미가 높은 유전자";
			case Tier2:	return "임상적 의미가 일부 증명된 유전자";
			case Tier3:	return "임상적 의미가 낮은 유전자\n본 유전자는 건강에 관련된 행위가 유용하다는 객관적 타당성이 아직 부족합니다.";
		}
		return null;
	}
}