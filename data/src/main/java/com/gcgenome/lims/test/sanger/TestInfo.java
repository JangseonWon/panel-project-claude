package com.gcgenome.lims.test.sanger;

import com.gcgenome.lims.test.HasCode;
import com.gcgenome.lims.test.I18N;
import com.gcgenome.lims.test.MayBeNationalInsurance;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@Builder

public class TestInfo implements HasCode, MayBeNationalInsurance, I18N {
	private final String name;
	private final String code;
	@Builder.Default
	private final String specimen = "Genomic DNA isolated from peripheral blood leukocytes";
	@Builder.Default
	private final String method = "PCR & Sequencing";
	@Builder.Default
	private final String[] limitations = new String[] {
		"본 검사는 염기서열 분석법으로 유전자의 특정 변이를 확인하는 검사입니다.",
		"본 검사는 allele drop-out, multiple priming sites 등 각종 요인에 의해 위양성 및 위음성이 발생할 수 있습니다."
	};
	private final String[] references = new String[] {
		"GeneReviews (https://www.ncbi.nlm.nih.gov/books/NBK1116/)",
		"The Human Gene Mutation Database (http://www.hgmd.org)"
	};
	@Builder.Default
	private final boolean isNationalInsuranceTest = false;
	@Builder.Default
	private final boolean isFamilialMutationTest = false;
	@Builder.Default
	private final String i18n = "KOKR";
	public static final TestInfo N147 = TestInfo.builder()
												.code("N147")
												.name("DGS Proband/Family test")
												.build();
	public static final TestInfo S022 = TestInfo.builder()
												.code("S022")
												.isFamilialMutationTest(true)
												.name("Familial mutation")
												.limitations(new String[] {
														"본 검사는 염기서열 분석법으로 유전자의 특정 변이를 확인하는 검사입니다.",
														"본 검사는 allele drop-out, multiple priming sites 등 각종 요인에 의해 위양성 및 위음성이 발생할 수 있습니다.",
														"본 검사는 Proband에서 발견된 변이 정보에 근거하여 시행되며, Reference mRNA 또는 변이 정보가 정확하지 않을 경우 위음성이 발생할 수 있습니다."
												}).isNationalInsuranceTest(true).build();
	public static final TestInfo S121 = TestInfo.builder()
												.code("S121")
												.isFamilialMutationTest(true)
												.name("Familial mutation_study")
												.build();
	public static final TestInfo ON147 = TestInfo.builder()
												 .code("ON147")
												 .name("DGS Proband/Family Test Report")
												 .limitations(new String[] {
														 "This test identifies specific genetic variants using Sanger Sequencing.",
														 "Several factors such as allele drop-out, multiple priming sites might cause false positive and false negative."
												 }).i18n("ENUS").build();
	public static final TestInfo OT036 = TestInfo.builder()
												 .code("OT036")
												 .name("WES Family Test Report")
												 .limitations(new String[] {
														 "This test identifies specific genetic variants using Sanger Sequencing.",
														 "Several factors such as allele drop-out, multiple priming sites might cause false positive and false negative."
												 }).i18n("ENUS").build();
	public static final TestInfo OS022 = TestInfo.builder()
												 .code("OS022")
												 .isFamilialMutationTest(true)
												 .name("Familial Mutation Test Report")
												 .limitations(new String[] {
														 "This test identifies specific genetic variants using Sanger Sequencing.",
														 "Several factors such as allele drop-out, multiple priming sites might cause false positive and false negative."
												 }).i18n("ENUS").build();
	public static final TestInfo[] TESTS = new TestInfo[] {
			N147, S022, S121, ON147, OT036, OS022
	};
}
