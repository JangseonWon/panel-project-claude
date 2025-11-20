package com.greencross.lims.report.tmp;

import com.gcgenome.lims.test.tmp.Ballondor;
import com.greencross.lims.report.builder.Sex;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Accessors(fluent = true)
public class BallondorTemplateKoKr implements BallondorTemplate {
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
	private final BallondorResource resource;
	private final Ballondor testInfo;
	private final String info;
	public BallondorTemplateKoKr(BallondorResource resource, Ballondor testInfo) {
		this.resource = resource;
		this.testInfo = testInfo;
		info = "1. 본 검사는 %g 유전자의 %p 돌연변이를 검출하여 발덴스트롬마크로글로불린혈증 환자의 미세잔존질환을 확인합니다.\n"
				.replace("%g", testInfo.gene()).replace("%p", testInfo.hgvsp()) +
				"2. 검사에 포함되지 않는 돌연변이 부위 및 검출한계 이하의 낮은 돌연변이는 검출이 불가능합니다.";
	}
	public final BallondorResource resource() {
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
