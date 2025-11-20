package com.greencross.lims.report.genomescreen.enus;

import com.greencross.lims.report.builder.Sex;
import com.greencross.lims.report.genomescreen.GenomeScreenDto;
import com.greencross.lims.report.genomescreen.GenomeScreenTemplate;
import com.gcgenome.lims.test.genomescreen.TestInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Accessors(fluent = true)
public abstract class GenomeScreenTemplateEnUs<R extends GenomeScreenResourceEnUs> implements GenomeScreenTemplate<R> {
	protected static final DateTimeFormatter DTF 		= DateTimeFormatter.ofPattern("yyyy-MM-dd");
	protected final String lblMedicalInstitution		= "Institution";
	protected final String lblMedicalRecordNumber		= "Medical record number";
	protected final String lblRequestNumber				= "Sample ID";
	protected final String lblPatientName				= "Name";
	protected final String lblAgeSex					= "Age / Sex";
	protected final String lblSpecimenType				= "Sample type";
	protected final String lblAcceptionDate				= "Accepted";
	protected final String lblPatientInfo				= "";
	protected final String lblReportDate				= "Reported";
	private final TestInfo testInfo;
	public GenomeScreenTemplateEnUs(TestInfo testInfo) {
		this.testInfo = testInfo;
	}

	@Override
	public final String date(LocalDate date) {
		if(date == null) return null;
		return DTF.format(date);
	}
	@Override
	public final String date(LocalDateTime date) {
		if(date == null) return null;
		return DTF.format(date);
	}
	@Override
	public final String sex(Sex sex) {
		if(sex == null) return "-";
		switch(sex) {
			case M: return "M";
			case F: return "F";
			default: return "-";
		}
	}

	private final String lblPatientCode					= "";
	private final String lblWardDepartment				= "";
	private final String lblPhysician					= "";
	private final String lblReceiptReportDate			= "";
	private final String lblCollectionDate				= "";
	@Override
	public String fmtMyResult(GenomeScreenDto dto) {
		return "The test result of " + dto.patientName();
	}

	private final String lblSummaryTestIntroTitle		= "Test Description";
	private final String lblSummaryTitle				= "Summary of Test Results";
	private final String[] lblVariantTable				= new String[] {"Gene", "DNA change", "Predicted\nAA change", "Zygosity", "Class"};
	private final String lblPositiveDiseaseInfoTitle	= "Relevant Disease Information";
	private final String lblPositiveDisease				= "Disease";
	private final String lblPositiveDiseaseInfo			= "Test Guidebook";
	private final String lblPositiveInterpretationTitle	= "Interpretation";
	private final String lblDetailsTitle				= "The result details and related diseases";
	private final String lblDetailsDiseaseName			= "Disease";
	private final String lblDetailsGene					= "Gene";
	private final String lblDetailsPathogenicVariant	= "Pathogenic Variant";
	private final String lblDetailsPositive				= "Detected";
	private final String lblDetailsNegative				= "Not Detected";
	private final String lblTestInfoTitle				= "Test Information";
	private final String lblTestInfoSpecimen			= "Specimen";
	private final String testInfoSpecimen				= "Peripheral blood leukocytes";
	private final String lblTestInfoMethod				= "Method";
	private final String testInfoMethod					= "Next-Generation Sequencing; NGS";
	private final String lblTestInfoNgs					= "Next-Generation Sequencing Test";
	private final String testInfoNgs					= "This technique breaks down the genome into many pieces, reads each piece at the same time, and combines the data obtained with bioinformatics techniques to quickly decode vast amounts of genome information.";
	private final String lblSangerIgnored				= "Confirmatory Sanger sequencing was not performed according to our bioinformatic criteria.";
	private final String lblReferenceTranscriptTitle	= "Reference Transcript";
	private final String lblGeneListTitle				= "Disease associated genes";
	private final String lblLimitationTitle				= "The reports and limitation of test";
	private final String lblReferenceTitle				= "Reference";
	private final String lblClinicalMeanings			= "Clinical significance of each tested gene";
	private final String lblClinicalMeaningInfo			= "Even in people with positive result in the test, the onset and symptoms of the disease vary and it doesn’t mean that necessarily the disease necessarily occur. In addition, even in people with negative result, the disease can be caused by other genes that have not been tested or other factors.";
	@Override
	public String lblClinicalMeanings(Tier tier) {
		switch(tier) {
			case Tier1:	return "Genes with high clinical significance";
			case Tier2:	return "Genes with clinical significance partially proven";
			case Tier3:	return "Genes with low clinical significance\nThe following genes are lacking objective validity for action related to health.";
		}
		return null;
	}
}