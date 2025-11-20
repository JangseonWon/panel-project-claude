package com.greencross.lims.report.wes.enus;

import com.gcgenome.lims.test.wes.TestInfo;
import com.greencross.lims.report.builder.Sex;
import com.greencross.lims.report.enus.RevisionableEnUs;
import com.greencross.lims.report.wes.WesResource;
import com.greencross.lims.report.wes.WesTemplate;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Accessors(fluent = true)
public final class WesTemplateEnUs implements WesTemplate, RevisionableEnUs {
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
	private final String lblReceiptReportDate			= "";
	private final String lblNoteAdditionalGene			= "* Genes reported as additional genes are reported only for PV/LPV";
	private final WesResource resource;
	private final TestInfo testInfo;
	public WesTemplateEnUs(WesResource resource, TestInfo testInfo) {
		this.resource = resource;
		this.testInfo = testInfo;
	}
	public WesResource resource() {
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
