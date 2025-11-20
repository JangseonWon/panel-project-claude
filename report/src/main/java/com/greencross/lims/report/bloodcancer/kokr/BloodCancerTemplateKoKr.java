package com.greencross.lims.report.bloodcancer.kokr;

import com.gcgenome.lims.test.bloodcancer.TestInfo;
import com.greencross.lims.report.bloodcancer.BloodCancerDto;
import com.greencross.lims.report.bloodcancer.BloodCancerResource;
import com.greencross.lims.report.bloodcancer.BloodCancerTemplate;
import com.greencross.lims.report.builder.LogoType;
import com.greencross.lims.report.builder.Sex;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Stream;

@Data
@Accessors(fluent = true)
public class BloodCancerTemplateKoKr implements BloodCancerTemplate {
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
	private final String lblDetails						= "상세 결과";
	private final String lblTestInfo					= "검사 정보";
	private final String lblTestMethod					= "1. 검사 방법";
	private final String lblTestQc						= "2. QC 정보";
	private final String lblTestLimitation				= "3. 검사의 한계";
	private final String lblTestVariantCategorizations	= "4. 변이의 해석 및 분류";
	private final String lblTestGeneInfo				= "5. 유전자 정보";
	private final String[] commonLimitations = {
			"본 검사는 염기서열분석법으로 시행되었으며, SNP와 Small indel 을 검출할 수 있고 Copy number variation (CNV) 및 gene rearrangement와 같은 구조적 변이는 검출할 수 없습니다.",
			"SNV 및 small indel 변이의 검출 한계는 약 5%입니다.",
			"해당 검사에서 발견된 변이는 Sanger sequencing, ddPCR 등 다른 검사법을 이용하여 재확인을 시행하지 않습니다.",
			"본 검사로 Germline 변이와 Somatic 변이를 감별할 수 없으며 Variant allele frequency가 50% 혹은 100%에 가까운 경우 Germline variant 의 가능성을 배제할 수 없습니다.",
			"본 검사에서 발견된 변이는 2017 JMD guideline (J Mol Diagn 2017;19:313-327)에 따라 4 단계로 분류하며(Tier 1~4), Tier 4 변이는 보고하지 않습니다."
	};
	private static final String AML_MDS_MPN_MSG = "MLL-PTD 변이 및 17p 영역의 LOH/Deletion 검출 민감도는 tumor burden에 따라 달라질 수 있으며, 검출 정확도가 제한적일 수 있으므로 추가적인 확진 검사가 권고됩니다.";
	private static final String ALL_LYMPHOMA_MM_MSG = "17p 영역의 LOH/Deletion 검출 민감도는 tumor burden에 따라 달라질 수 있으며, 검출 정확도가 제한적일 수 있으므로 추가적인 확진 검사가 권고됩니다.";
	private final String[] lblTestLimitations;

	private final String lblTestVariantCategorizationDetails = "변이의 근거 수준 및 임상적 의의에 따라 4단계로 분류하며, Tier 4는 보고하지 않습니다.\n";
	@Override
	public String lblTierSignificanceCategory(BloodCancerDto.Tier tier) {
		switch(tier) {
			case Tier1: return "임상적 의의가 강한 변이\n(Strong clinical significance)";
			case Tier2: return "임상적 의의가 있을 수 있는 변이\n(Potential clinical significance)";
			case Tier3: return "임상적 의의가 불분명한 변이\n(Unknown clinical significance)";
			case Tier4: return "양성 혹은 양성일 가능성이 높은 변이\n(Benign or likely benign)";
		}
		return null;
	}
	@Override
	public String lblTierSignificanceDetails(BloodCancerDto.Tier tier) {
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
	public String lblEvidenceLevelDetails(BloodCancerDto.EvidenceLevel evidenceLevel) {
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
	private final String lblGeneEssential = "필수 유전자 목록";
	private final String lblGeneSelective = "선택 유전자 목록";

	private final BloodCancerResource resource;
	private final LogoType logoType;
	private final TestInfo testInfo;
	public BloodCancerTemplateKoKr(BloodCancerResource resource, TestInfo testInfo, LogoType logoType) {
		this.resource = resource;
		this.testInfo = testInfo;
		this.logoType = logoType;
		String additionalMsg = switch (testInfo.referralDefault()) {
			case "AML", "MDS/MPN" -> AML_MDS_MPN_MSG;
			case "ALL", "Lymphoma", "Multiple Myeloma" -> ALL_LYMPHOMA_MM_MSG;
			default -> null;
		};
		this.lblTestLimitations = Stream.concat(Arrays.stream(commonLimitations), additionalMsg == null ? Stream.empty() : Stream.of(additionalMsg)).toArray(String[]::new);
	}
	public final BloodCancerResource resource() {
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
}
