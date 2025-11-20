package com.greencross.lims.report.solidtumor2.kokr;

import com.gcgenome.lims.test.solidtumor2.TestInfo;
import com.greencross.lims.report.builder.LogoType;
import com.greencross.lims.report.builder.Sex;
import com.greencross.lims.report.solidtumor2.SolidTumorDto;
import com.greencross.lims.report.solidtumor2.SolidTumorResource;
import com.greencross.lims.report.solidtumor2.SolidTumorTemplate;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Accessors(fluent = true)
public class SolidTumorTemplateKoKr implements SolidTumorTemplate {
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
	private final String lblReferT3NextPage				= "* Tier 3 변이는 상세 변이 정보를 참조하십시오.";
	private final String lblQcSummary					= "QC 결과";
	private final String lblDetailsResult				= "결과 상세";
	private final String lblDetailsVariant				= "변이 상세";
	private final String lblTestInfo					= "검사 정보";
	private final String lblTestMethod					= "Test Method";
	private final String lblTestQc						= "QC Information";
	private final String lblTestLimitation				= "Limitations";
	private final String lblQcDetailsGuide				= "* 자세한 QC 기준은 뒷장의 검사 정보를 참조하십시오.";
	private final String lblTestVariantCategorizationDetails = "변이의 근거 수준 및 임상적 의의에 따라 4단계로 분류하며, Tier 4는 보고하지 않습니다.\n";
	private final String etc = "등";
	@Override
	public String lblCancerCategory() {
		return "암 조직 부위";
	}
	@Override
	public String lblCancerType() {
		return "암종";
	}
	@Override
	public String lblTierSignificanceCategory(SolidTumorDto.Tier tier) {
		switch(tier) {
			case Tier1: return "임상적 의의가 강한 변이\n(Strong clinical significance)";
			case Tier2: return "임상적 의의가 있을 수 있는 변이\n(Potential clinical significance)";
			case Tier3: return "임상적 의의가 불분명한 변이\n(Unknown clinical significance)";
			case Tier4: return "양성 혹은 양성일 가능성이 높은 변이\n(Benign or likely benign)";
		}
		return null;
	}
	@Override
	public String lblTierSignificanceDetails(SolidTumorDto.Tier tier) {
		switch(tier) {
			case Tier1: return "Level A 또는 Level B에 해당하는 근거를 가지는 변이";
			case Tier2: return "Level C 또는 Level D에 해당하는 근거를 가지는 변이";
			case Tier3: return "일반 인구 집단 데이터베이스 혹은 암 관련 데이터베이스에서 유의한 빈도로 관찰되지 않는 경우 " +
							   "혹은 암과의 연관성에 대해 신뢰할만한 문헌이 없는 경우";
			case Tier4: return "일반 인구 집단 데이터베이스에서 유의한 빈도로 관찰되거나, 암과의 관련성에 대해 문헌이 존재하지 않는 경우";
		}
		return null;
	}
	@Override
	public String lblEvidenceLevelDetails(SolidTumorDto.EvidenceLevel evidenceLevel) {
		switch(evidenceLevel) {
			case LevelA: return "특정 암종에서 FDA 승인된 약제에 대한 치료 반응 혹은 내성 예측과 관련된 바이오마커 혹은 전문적 가이드라인에 " +
								"약제에 대한 치료 반응 혹은 내성, 암의 진단 혹은 예후와 관련된 것으로 포함된 바이오마커";
			case LevelB: return "잘 고안된 연구에 의해 특정 암종에서 약제에 대한 치료 반응 혹은 내성, 암의 진단 혹은 예후와 관련된 것으로 " +
								"전문가 집단의 합의가 있는 바이오마커";
			case LevelC: return "다른 암종에서 FDA 승인된 약제에 대한 치료 반응 혹은 내성을 예측하는 바이오마커 혹은 임상 시험 참여가 가능한 " +
								"바이오마커, 여러 개의 소규모 연구에서 암의 진단 혹은 예후와 관련된 것으로 보고된 바이오마커";
			case LevelD: return "전임상 시험 혹은 소규모 연구 혹은 몇 개의 사례 보고가 있는 바이오마커";
		}
		return null;
	}

	private final SolidTumorResource resource;
	private final LogoType logoType;
	private final TestInfo testInfo;
	public SolidTumorTemplateKoKr(SolidTumorResource resource, TestInfo testInfo, LogoType logoType) {
		this.resource = resource;
		this.testInfo = testInfo;
		this.logoType = logoType;
	}
	public final SolidTumorResource resource() {
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
}
