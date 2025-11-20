package com.greencross.lims.report.dgs.enus;

import com.gcgenome.lims.test.dgs.TestInfo;
import com.greencross.lims.report.builder.Sex;
import com.greencross.lims.report.dgs.DgsResource;
import com.greencross.lims.report.dgs.DgsTemplate;
import com.greencross.lims.report.enus.RevisionableEnUs;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Accessors(fluent = true)
public final class DgsTemplateEnUs implements DgsTemplate, RevisionableEnUs {
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
	private final String templateEmptyIncidentalFinding	= "No (Likely) Pathogenic Variant was identified in the %d genes recommended by ACMG";
	private final DgsResource resource;
	private final TestInfo testInfo;
	public DgsTemplateEnUs(DgsResource resource, TestInfo testInfo) {
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
	public final String sex(Sex sex) {
		if(sex == null) return "-";
		switch(sex) {
			case M: return "Male";
			case F: return "Female";
			default: return "-";
		}
	}
}
