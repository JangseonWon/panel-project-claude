package com.greencross.lims.report.sanger.enus;

import com.gcgenome.lims.test.sanger.TestInfo;
import com.greencross.lims.report.builder.Sex;
import com.greencross.lims.report.sanger.SangerResource;
import com.greencross.lims.report.sanger.SangerTemplate;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Accessors(fluent = true)
public final class SangerTemplateEnUs implements SangerTemplate {
	private static final DateTimeFormatter DTF 			= DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private final String lblMedicalInstitution			= "Institution";
	private final String lblMedicalRecordNumber			= "Medical Record No.";
	private final String lblRequestNumber				= "Sample ID";
	private final String lblPatientName					= "Name";
	private final String lblPatientCode					= "";
	private final String lblAgeSex						= "Sex/Age";
	private final String lblSpecimenType				= "";
	private final String lblWardDepartment				= "";
	private final String lblCollectionDate				= "";
	private final String lblPatientInfo					= "";
	private final String lblPhysician					= "Ordering physician";
	private final String lblReceiptReportDate			= "접수일/보고일";

	private final SangerResource resource;
	private final TestInfo testInfo;
	public SangerTemplateEnUs(SangerResource resource, TestInfo testInfo) {
		this.resource = resource;
		this.testInfo = testInfo;
	}
	public SangerResource resource() {
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
			case M: return "Male";
			case F: return "Female";
			default: return "-";
		}
	}
}
