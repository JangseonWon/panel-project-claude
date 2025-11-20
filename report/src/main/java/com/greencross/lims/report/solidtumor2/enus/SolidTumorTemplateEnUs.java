package com.greencross.lims.report.solidtumor2.enus;

import com.gcgenome.lims.test.solidtumor2.TestInfo;
import com.greencross.lims.report.builder.LogoType;
import com.greencross.lims.report.builder.Sex;
import com.greencross.lims.report.solidtumor2.SolidTumorDto;
import com.greencross.lims.report.solidtumor2.SolidTumorResource;
import com.greencross.lims.report.solidtumor2.SolidTumorTemplate;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Accessors(fluent = true)
public class SolidTumorTemplateEnUs implements SolidTumorTemplate {
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

	private final String lblCancerCategory				= "Cancer tissue";
	private final String lblCancerType					= "Cancer type";
	private final String lblSummary						= "SUMMARY";//"결과 요약";
	private final String lblReferT3NextPage				= "* For Tier 3 mutation, please refer to the detailed mutation information in the back page.";
	private final String lblQcSummary					= "QC RESULT";//"QC 결과";
	private final String lblDetailsResult				= "VARIANT DETAIL";//"결과 상세";
	private final String lblDetailsVariant				= "TOTAL VARIANT LIST";//"변이 상세";
	private final String lblTestInfo					= "TEST INFORMATION";//"검사 정보";
	private final String lblTestMethod					= "Test Method";//"1. 검사 방법";
	private final String lblTestQc						= "QC Information";//"2. QC 정보";
	private final String lblTestLimitation				= "Limitations";//"3. 검사의 한계";
	private final String lblTestVariantCategorizations	= "Classification";//"4. 변이의 해석 및 분류";
	private final String lblTestGeneInfo				= "Gene List";//"5. 유전자 정보";
	private final String lblQcDetailsGuide				= "";//"* 자세한 QC 기준은 검사 정보를 참조하십시오.";
	private final String lblTestVariantCategorizationDetails = "Somatic Variants are classified into four stages according " +
			"to the evidence level and clinical significance of the mutation. Tier 4 is not reported.";
	private final String etc = "etc.";
	@Override
	public String lblTierSignificanceCategory(SolidTumorDto.Tier tier) {
		switch(tier) {
			case Tier1: return "Strong clinical significance";
			case Tier2: return "Potential clinical significance";
			case Tier3: return "Unknown clinical significance ";
			case Tier4: return "Benign or likely benign";
		}
		return null;
	}
	@Override
	public String lblTierSignificanceDetails(SolidTumorDto.Tier tier) {
		switch(tier) {
			case Tier1: return "Level A and Level B evidence";
			case Tier2: return "Level C and Level D evidence";
			case Tier3: return "Not observed at a significant allele frequency in the general or specific subpopulation databases, " +
					"or pan-cancer or tumor-specific variant databases. No convincing published evidence of cancer association.";
			case Tier4: return "Observed at significant allele frequency in the general or specific subpopulation databases. " +
					"No existing published evidence of cancer association.";
		}
		return null;
	}
	@Override
	public String lblEvidenceLevelDetails(SolidTumorDto.EvidenceLevel evidenceLevel) {
		switch(evidenceLevel) {
			case LevelA: return "FDA-approved therapy Included in professional guidelines.";
			case LevelB: return "Well-powered studies with consensus from experts in the field.";
			case LevelC: return "FDA-approved therapies for different tumor types or investigational therapies. Multiple small published studies with some consensus.";
			case LevelD: return "Preclinical trials or a few case reports without consensus.";
		}
		return null;
	}

	private final SolidTumorResource resource;
	private final LogoType logoType;
	private final TestInfo testInfo;
	public SolidTumorTemplateEnUs(SolidTumorResource resource, TestInfo testInfo, LogoType logoType) {
		this.resource = resource;
		this.testInfo = testInfo;
		this.logoType = logoType;
	}
	public final SolidTumorResource resource() {
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
	public final LogoType logoType() {
		return this.logoType;
	}
}
