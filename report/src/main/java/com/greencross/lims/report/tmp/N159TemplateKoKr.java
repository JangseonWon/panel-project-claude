package com.greencross.lims.report.tmp;

import com.gcgenome.lims.test.tmp.N159;
import com.greencross.lims.report.builder.Sex;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Accessors(fluent = true)
public final class N159TemplateKoKr implements N159Template {
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

	private final N159Resource resource;
	private final N159 testInfo;
	public N159TemplateKoKr(N159Resource resource, N159 testInfo) {
		this.resource = resource;
		this.testInfo = testInfo;
	}
	public N159Resource resource() {
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
	public String[] limitations() {
		return new String[]{
				"해당 검사는 연구 목적의 검사로 진단용으로 사용될 수 없습니다.",
				"본 검사로 Germline 변이와 Somatic 변이를 감별할 수 없으며 Variant allele frequency가 50% 혹은 100%에 가까운 경우 Germline variant의 가능성이 있습니다.",
				"SNV 및 Small indel 변이의 검출 한계는 약 2%입니다."
		};
	}
}
