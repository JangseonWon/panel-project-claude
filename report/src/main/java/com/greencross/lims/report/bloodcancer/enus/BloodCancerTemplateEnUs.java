package com.greencross.lims.report.bloodcancer.enus;

import com.gcgenome.lims.test.bloodcancer.TestInfo;
import com.greencross.lims.report.bloodcancer.BloodCancerDto;
import com.greencross.lims.report.bloodcancer.BloodCancerResource;
import com.greencross.lims.report.bloodcancer.BloodCancerTemplate;
import com.greencross.lims.report.builder.LogoType;
import com.greencross.lims.report.builder.Sex;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Stream;

@Data
@Accessors(fluent = true)
public class BloodCancerTemplateEnUs implements BloodCancerTemplate {

	private static final DateTimeFormatter DTF 			= DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private final String lblMedicalInstitution			= "Institution";
	private final String lblMedicalRecordNumber			= "Medical record No";
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

	private final String lblSummary						= "RESULT SUMMARY";
	private final String lblDetails						= "TEST RESULT";
	private final String lblTestInfo					= "TEST INFORMATION";
	private final String lblTestMethod					= "1. TEST METHOD";
	private final String lblTestQc						= "2. QC DATA";
	private final String lblTestLimitation				= "3. TEST LIMITATIONS";
	private final String lblTestVariantCategorizations	= "4. CLASSIFICATIONS";
	private final String lblTestGeneInfo				= "5. GENE INFORMATION";
	private final String[] commonLimitations = {
		"This test was performed using sequencing analysis, and can detect SNP and small-indel variants within the analyzed region, but not structural variations such as copy number variation (CNV) and gene rearrangement.",
		"The limit of detection for SNV and small-indel variants is approximately 5%.",
		"The detected variants in this test are not re-confirmed by Sanger sequencing, ddPCR or other confirmation methods.",
		"This test does not distinguish between germline and somatic variants. If the variant allele frequency of the mutation is close to 50% or 100%, the possibility of germline variant cannot be excluded.",
		"The variants detected in this test are classified into four (tier 1~4) according to the 2017 JMD guideline (J Mol Diagn 2017;19:313-327), and tier 4 variants are not reported."
	};
	private static final String AML_MDS_MPN_MSG = "The detection sensitivity of the MLL-PTD mutation and 17p LOH/deletion can vary depending on the tumor burden, and the analytical performance may be limited. Therefore, additional confirmatory tests are recommended.";
	private static final String ALL_LYMPHOMA_MM_MSG = "The detection sensitivity of the 17p LOH/deletion can vary depending on the tumor burden, and the analytical performance may be limited. Therefore, additional confirmatory tests are recommended.";
	private final String[] lblTestLimitations;

	private final String lblTestVariantCategorizationDetails = "Somatic Variants are classified into four stages according to the evidence level and clinical significance of the mutation. Tier 4 is not reported.\n";
	@Override
	public String lblTierSignificanceCategory(BloodCancerDto.Tier tier) {
		switch(tier) {
			case Tier1: return "Strong clinical significance";
			case Tier2: return "Potential clinical significance";
			case Tier3: return "Unknown clinical significance";
			case Tier4: return "Benign or likely benign";
		}
		return null;
	}
	@Override
	public String lblTierSignificanceDetails(BloodCancerDto.Tier tier) {
		switch(tier) {
			case Tier1: return "Level A or B evidence";
			case Tier2: return "Level C or D evidence";
			case Tier3: return "Not observed at a significant allele frequency in the general or specific subpopulation databases, " +
					"or no convincing published evidence of cancer association.";
			case Tier4: return "Observed at significant allele frequency in the general databases.\n" +
					"No existing published evidence of cancer association.";
		}
		return null;
	}
	@Override
	public String lblEvidenceLevelDetails(BloodCancerDto.EvidenceLevel evidenceLevel) {
		switch(evidenceLevel) {
			case LevelA: return "Biomarkers related to predicting therapeutic response or resistance to FDA-approved therapies in specific cancer types " +
					"or biomarkers included in professional guidelines as being related to therapeutic response or resistance to drugs, " +
					"diagnosis or prognosis of cancer";
			case LevelB: return "A biomarker with a consensus among experts in a well-designed study that is associated with the treatment response " +
					"or resistance to a drug, the diagnosis or prognosis in a specific cancer type.";
			case LevelC: return "Predictive biomarkers of therapeutic response or resistance to FDA-approved drugs in other cancer types " +
					"or biomarkers eligible for clinical trial participation, and biomarkers reported to be associated with cancer diagnosis " +
					"or prognosis in several small studies.";
			case LevelD: return "Biomarkers with preclinical trials or small studies or several case reports.";
		}
		return null;
	}
	private final String lblGeneEssential = "ESSENTIAL GENE LIST";
	private final String lblGeneSelective = "ADDITIONAL GENE LIST";

	private final BloodCancerResource resource;
	private final LogoType logoType;
	private final TestInfo testInfo;
	public BloodCancerTemplateEnUs(BloodCancerResource resource, TestInfo testInfo, LogoType logoType) {
		this.resource = resource;
		this.testInfo = testInfo;
		this.logoType = logoType;
		String additionalMsg = switch (testInfo.referralDefault()) {
			case "AML", "MDS/MPN" -> AML_MDS_MPN_MSG;
			case "ALL", "Lymphoma", "Multiple Myeloma" -> ALL_LYMPHOMA_MM_MSG;
			default -> null;
		};
		this.lblTestLimitations = Stream.concat(Arrays.stream(commonLimitations), additionalMsg == null ? Stream.empty() : Stream.of(additionalMsg)).toArray(String[]::new);
	}
	public final BloodCancerResource resource() {
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
