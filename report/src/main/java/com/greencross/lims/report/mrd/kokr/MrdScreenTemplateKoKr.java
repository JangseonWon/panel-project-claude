package com.greencross.lims.report.mrd.kokr;

import com.gcgenome.lims.test.mrd.TestInfo;
import com.greencross.lims.report.builder.LogoType;
import com.greencross.lims.report.builder.Sex;
import com.greencross.lims.report.mrd.MrdScreenResource;
import com.greencross.lims.report.mrd.MrdScreenTemplate;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Data
@Accessors(fluent = true)
public class MrdScreenTemplateKoKr implements MrdScreenTemplate {
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
	private final String lblTotalClone					= "검출된 클론";

	private final String lblDetails						= "상세 결과";
	private final String tmplCountClonesByGene			= "검출된 %g 클론";
	private final String tmplInterpretationPositive		= "미세잔존질환(MRD) 추적에 적합한 %g 클론이 %d개 검출되었습니다.";
	private final String tmplInterpretationNegative		= "미세잔존질환(MRD) 추적에 적합한 %g 클론이 검출되지 않았습니다.";
	private final String[] lblComments;
	private final String lblTestInfo					= "검사 정보";
	private final String lblTestMethod					= "1. 검사 방법";
	private final String lblTestQc						= "2. QC 정보";
	private final String lblTestLimitation				= "3. 검사의 한계";
	private final String[] lblQcComments;
	private final String[] lblLimitations;
	private final MrdScreenResource resource;
	private final LogoType logoType;
	private final TestInfo testInfo;
	private final String lblLimitationIgh = "IGH 클론성 염기서열은 검체 내 전체 read depth가 20,000 이상이면서 전체 read depth 중 최소 ≥ 2.5% 이상, 3순위 read depth의 2배 이상인 dominant sequence로 판정합니다.";
	private final String lblLimitationIgk = "IGK 클론성 염기서열은 검체 내 전체 read depth가 20,000 이상이면서 전체 read depth 중 최소 ≥ 5% 이상, 3순위 read depth의 2배 이상인 dominant sequence로 판정합니다.";
	private final String lblLimitationTrb = "TRB 클론성 염기서열은 검체 내 전체 read depth가 20,000 이상이면서 전체 read depth 중 최소 ≥ 2.5% 이상, 3순위 read depth의 2배 이상인 dominant sequence로 판정합니다.";
	private final String lblLimitationTrg = "TRG 클론성 염기서열은 검체 내 전체 read depth가 20,000 이상이면서 전체 read depth 중 최소 ≥ 2.5% 이상, 3순위 read depth의 2배 이상인 dominant sequence로 판정합니다.";
	private final String lblLimitationEtc1 = "Clonal process를 통해서 두 개 또는 그 이상의 clone 이 존재할 수 있습니다. 예를 들어 다발성 림프증식성 장애 (multiple lymphoproliferative disorders)나 a dominant population with a small sub-clonal population 인 경우 가능합니다. 이러한 사례는 임상적 배경과 같이 해석되어야 합니다.";
	private final String lblLimitationEtc2 = "본 검사 결과는 임상, 조직학 및 면역 표현형 데이터의 맥락에서 해석되어야 합니다.";
	private final String lblLimitationEtc3 = "본 검사는 clonal cell populations 의 100%를 식별하지 않습니다.";
	public MrdScreenTemplateKoKr(MrdScreenResource resource, TestInfo testInfo, LogoType logoType) {
		this.resource = resource;
		this.testInfo = testInfo;
		this.logoType = logoType;
		lblComments	= new String[] {
			"* 검체당 " + testInfo.cell() + " 100개의 DNA (LymphoQuant Internal Control, LQIC)를 혼합하여 측정된 근사치입니다.",
			"** 전체 " + testInfo.cell() + " 중 클론성 세포가 차지하는 비율을 환산한 근사치입니다."
		};
		lblQcComments = new String[] {
			"* Estimated DNA mass per 1 cell = 6.5pg",
			"** 검체당 " + testInfo.cell() + " 100개의 DNA (LymphoQuant internal control, LQIC)를 혼합하여 측정된 근사치입니다."
		};
		String lblLimitationPcr = "PCR 증폭은 " + testInfo.cell() + "의 DNA양에 영향을 받으며 primer결합 부위 변이가 있는 경우 위음성을 보일 가능성이 있습니다.";
		List<String> limitations = new LinkedList<>();
		if(Arrays.stream(testInfo.genes()).anyMatch("IGH"::equalsIgnoreCase)) limitations.add(lblLimitationIgh);
		if(Arrays.stream(testInfo.genes()).anyMatch("IGK"::equalsIgnoreCase)) limitations.add(lblLimitationIgk);
		if(Arrays.stream(testInfo.genes()).anyMatch("TRB"::equalsIgnoreCase)) limitations.add(lblLimitationTrb);
		if(Arrays.stream(testInfo.genes()).anyMatch("TRG"::equalsIgnoreCase)) limitations.add(lblLimitationTrg);
		limitations.add(lblLimitationPcr);
		limitations.add(lblLimitationEtc1);
		limitations.add(lblLimitationEtc2);
		limitations.add(lblLimitationEtc3);
		lblLimitations = limitations.toArray(new String[0]);
	}
	public final MrdScreenResource resource() {
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
