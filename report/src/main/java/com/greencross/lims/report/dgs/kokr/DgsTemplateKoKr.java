package com.greencross.lims.report.dgs.kokr;

import com.gcgenome.lims.test.dgs.TestInfo;
import com.greencross.lims.report.builder.Sex;
import com.greencross.lims.report.dgs.DgsResource;
import com.greencross.lims.report.dgs.DgsTemplate;
import com.greencross.lims.report.kokr.RevisionableKoKr;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Accessors(fluent = true)
public final class DgsTemplateKoKr implements DgsTemplate, RevisionableKoKr {
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
	private final String templateEmptyIncidentalFinding	= "ACMG에서 권고한 %d개 유전자에서 (Likely) Pathogenic Variant는 발견되지 않았습니다.";
	private final DgsResource resource;
	private final TestInfo testInfo;
	public DgsTemplateKoKr(DgsResource resource, TestInfo testInfo) {
		this.resource = resource;
		this.testInfo = testInfo;
	}
	public DgsResource resource() {
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
	public String sex(Sex sex) {
		if(sex == null) return "-";
		switch(sex) {
			case M: return "남";
			case F: return "여";
			default: return "-";
		}
	}
}
