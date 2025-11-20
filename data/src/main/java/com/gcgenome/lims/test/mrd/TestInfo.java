package com.gcgenome.lims.test.mrd;

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
	private final String code;
	@Builder.Default
	private final TestInfo[] screens = null;
	@Builder.Default
	private final TestInfo[] siblings = null;
	private final String name;
	@Builder.Default
	private final String cell = "B-cell";
	@Builder.Default
	private final String method = "Amplicon with oligonucleotide primers";
	@Builder.Default
	private final String pipeline = "LymphoTrack®";
	private final String panel;
	@Builder.Default
	private final String sequencing = "MiSeq Dx";
	@Builder.Default
	private final String reference = "hg19";
	@Builder.Default
	private final boolean isNationalInsuranceTest = false;
	@Builder.Default
	private final String i18n = "KOKR";
	private String[] genes;
	private static TestInfo N144 = TestInfo.builder()
										   .code("N144")
										   .name("(Screen) IGH 유전자 재배열 검사")
										   .genes(new String[] {"IGH"})
										   .panel("LymphoTrack IGH FR1 Assay Panel")
										   .isNationalInsuranceTest(true).build();
	private static TestInfo N145 = TestInfo.builder().code("N145")
										   .name("(Screen) IGH/IGK 유전자 재배열 검사")
										   .genes(new String[] {"IGH", "IGK"})
										   .panel("LymphoTrack IGH FR1 Assay Panel & IGK Assay Panel")
										   .isNationalInsuranceTest(true).build();
	private static TestInfo N146 = TestInfo.builder().code("N146")
										   .name("(Screen) TRB/TRG 유전자 재배열 검사")
										   .cell("T-cell")
										   .genes(new String[] {"TRB", "TRG"})
										   .panel("LymphoTrack TRB Assay Panel & TRG Assay Panel")
										   .isNationalInsuranceTest(true).build();
	private static TestInfo N151 = TestInfo.builder().code("N151").screens(new TestInfo[] {N145})
											.name("(MRD) IGH/IGK 유전자 재배열 검사")
											.genes(new String[] {"IGH", "IGK"})
											.panel("LymphoTrack IGH FR1 Assay Panel & IGK Assay Panel")
											.isNationalInsuranceTest(true).build();
	private static TestInfo N150 = TestInfo.builder().code("N150").screens(new TestInfo[] {N144, N145}).siblings(new TestInfo[]{N151})
										   .name("(MRD) IGH 유전자 재배열 검사")
										   .genes(new String[] {"IGH"})
										   .panel("LymphoTrack IGH FR1 Assay Panel")
										   .isNationalInsuranceTest(true).build();
	private static TestInfo N152 = TestInfo.builder().code("N152").screens(new TestInfo[] {N146})
										   .name("(MRD) TRB/TRG 유전자 재배열 검사")
										   .cell("T-cell")
										   .genes(new String[] {"TRB", "TRG"})
										   .panel("LymphoTrack TRB Assay Panel & TRG Assay Panel")
										   .isNationalInsuranceTest(true).build();
	public static TestInfo[] SCREEN_TESTS = {N144, N145, N146};
	public static TestInfo[] TESTS = {N150, N151, N152};
}
