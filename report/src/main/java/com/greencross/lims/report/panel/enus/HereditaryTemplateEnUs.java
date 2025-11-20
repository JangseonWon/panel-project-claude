package com.greencross.lims.report.panel.enus;

import com.gcgenome.lims.test.panel.TestInfo;
import com.greencross.lims.report.builder.AbstractReportTemplate;
import com.greencross.lims.report.builder.LogoType;
import com.greencross.lims.report.builder.Sex;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Accessors(fluent = true)
public abstract class HereditaryTemplateEnUs implements AbstractReportTemplate<HereditaryResourceEnUs> {
	protected static final DateTimeFormatter DTF 		= DateTimeFormatter.ofPattern("yyyy-MM-dd");
	// legacy
	protected final String lblMedicalInstitution		= "Institution";
	protected final String lblMedicalRecordNumber		= "MRN";
	protected final String lblRequestNumber				= "Sample ID";
	protected final String lblPatientName				= "Name";
	protected final String lblAgeSex					= "";
	protected final String lblSpecimenTypeDate			= "";
	protected final String lblPatientInfo				= "";
	protected final String lblReceiptReportDate			= "";
	private final TestInfo testInfo;
	private final LogoType logoType;

	public HereditaryTemplateEnUs(TestInfo testInfo, LogoType logoType) {
		this.testInfo = testInfo;
		this.logoType = logoType;
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
			case M: return "Male";
			case F: return "Female";
			default: return "-";
		}
	}
	public final String result(HereditaryEnUsDto.Result result) {
		if(result == null) return "-";
		switch(result) {
			case P: return "Positive";
			case I: return "Inconclusive";
			case N: return "Negative";
			default: return "-";
		}
	}
	public abstract String lblCategory();
	public abstract String lblTitle();
	private final String lblTestReport = "Test Report";
	private final String lblPatientCode = "Sample ID";
	private final String lblDoBSex = "DOB / Gender";
	private final String lblSpecimenType = "";
	private final String lblWardDepartment = "";
	private final String lblCollectionDate = "Collection Date";
	private final String lblPhysician = "Ordering Physician";
	private final String lblReceiptDate = "Received Date";
	private final String lblReportDate = "Reporting Date";

	private final String lblSummary = "Summary";
	private final String lblResult = "Results";
	private final String[] lblVariantTable = new String[] {
			"Gene", "DNA Change", "AA Change", "Zygosity", "OMIM Disease", "Inherit", "Class"
	};
	private final String lblReferenceSequence = "Reference sequence";
	private final String lblOmimDisease = "OMIM disease";
	private final String lblAbbreviation = "Abbreviations";
	private final String lblInterpretation = "Interpretation";
	private final String noDiseaseVariant = "No disease-related variant";
	private final String lblDisease = "The result details and related diseases";
	private final String lblRelevantDisease = "Relevant Disease Information";
	private final String lblDiseaseInformation = "Disease Information";
	private final String lblGeneticFeature = "Genetic features";
	private final String lblTest = "Test Information";
	private final String lblMethod = "Method";
	private final String lblTarget = "Target Region";
	private final String lblProbe = "Target enrichment method";
	private final String lblSequencing = "Massively parallel sequencing";
	private final String lblReferenceGenome = "Reference genome";
	private final String lblPipeline = "Bioinformatic pipeline";
	private final String lblCoverage = "Coverage";
	private final String lblMeanDepth = "Mean depth of coverage";
	private final String lblTargetCoverage = "% of Target Bases ≥ 10X";
	private final String lblGeneList = "Gene List";
	private final String lblLimitation = "Limitation";
	private final String lblLimitations = "This test is performed by NGS technique. The genes included in the test include the entire exon, but in some areas sequencing may not be " +
			"sufficiently covered. In addition, if a highly homologous sequence exists, the sequencing of the base may not be accurate, and exonic deletion/duplication, " +
			"regulatory or deep intronic region, repeat expansion, imprinting defect etc. may be difficult to detect. Genetic variation is divided into ve categories, " +
			"pathogenic variant (PV), likely pathogenic variant (LPV), variant of uncertain significance (VUS), likely benign variant (LBV), and benign variant (BV), " +
			"according to 2015 ACMG/AMP (Genet Med 2015;17:405-24). Likely benign variant (LBV) and Benign variant (BV) are not reported. However, the interpretation " +
			"of the variation could be changed as additional evidence builds up after the results are reported.";
	private final String lblReference = "Reference";
	public abstract String[] references();
	enum Tier {
		Tier1, Tier2, Tier3
	}
	public String lblClinicalMeanings(Tier tier) {
		switch(tier) {
			case Tier1:	return "임상적 의미가 높은 유전자";
			case Tier2:	return "임상적 의미가 일부 증명된 유전자";
			case Tier3:	return "임상적 의미가 낮은 유전자\n본 유전자는 건강에 관련된 행위가 유용하다는 객관적 타당성이 아직 부족합니다.";
		}
		return null;
	}
	abstract CancerType[] cancerTypes();
	interface CancerType {
		String title();
		String information();
		String feature();
		Gene[] genes();
	}
	interface Gene {
		String symbol();
	}
}