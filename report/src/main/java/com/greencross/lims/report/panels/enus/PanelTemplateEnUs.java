package com.greencross.lims.report.panels.enus;

import com.gcgenome.lims.test.panel.TestInfo;
import com.greencross.lims.report.builder.LogoType;
import com.greencross.lims.report.builder.Sex;
import com.greencross.lims.report.panels.PanelResource;
import com.greencross.lims.report.panels.PanelTemplate;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Accessors(fluent = true)
public final class PanelTemplateEnUs implements PanelTemplate {
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

	private final PanelResource resource;
	private final TestInfo testInfo;
	private final LogoType logoType;
	public PanelTemplateEnUs(PanelResource resource, TestInfo testInfo, LogoType logoType) {
		this.logoType = logoType;
		this.resource = resource;
		this.testInfo = testInfo;
	}
	public PanelResource resource() {
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
	public LogoType logoType() {
		return this.logoType;
	}

	@Override
	public String lblLimitations() {
		return "This test is performed by NGS technique. The genes included in the test include the entire exon, " +
				"but in some areas sequencing may not be sufficiently covered. In addition, if a highly homologous " +
				"sequence exists, the sequencing of the base may not be accurate, and exonic deletion/duplication, " +
				"regulatory or deep intronic region, repeat expansion, imprinting defect etc. may be difficult to " +
				"detect. Genetic variation is divided into five categories, pathogenic variant (PV), likely pathogenic " +
				"variant (LPV), variant of unknown significance (VUS), likely benign variant (LBV), and benign variant " +
				"(BV), according to 2015 ACMG/AMP (Genet Med 2015;17:405-24). Likely benign variant (LBV) and Benign " +
				"variant (BV) are not reported. However, the interpretation of the variation could be changed as " +
				"additional evidence builds up after the results are reported.";
	}
}
