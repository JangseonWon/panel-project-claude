package com.gcgenome.lims.test.wes;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class TestWithSingleInfo extends TestInfo {
	private final com.gcgenome.lims.test.single.TestInfo single;
	@Builder private TestWithSingleInfo(String code, String title, String panel, String[] methods, Boolean isNationalInsuranceTest, String i18n, String limitation, com.gcgenome.lims.test.single.TestInfo single) {
		super(code, title, panel, methods, isNationalInsuranceTest, i18n, limitation);
		this.single = single;
	}
	private static class TestWithSingleInfoBuilder extends TestInfoBuilder {
		TestWithSingleInfoBuilder() {
			super();
		}
	}
	public static final TestWithSingleInfo N058 = TestWithSingleInfo.builder()
																	.code("N058")
																	.title("Whole Exome Sequencing Test 결과보고서")
																	.isNationalInsuranceTest(true)
																	.single(SingleGeneTest.N058)
																	.build();
	public static final TestWithSingleInfo[] TESTS = new TestWithSingleInfo[] {
		N058
	};

	public static final String[] GENE_INCIDENTAL_FINDINGS = new String[]{
			"ABCD1","ACTA2","ACTC1","ACVRL1","APC","APOB","ATP7B","BAG3","BMPR1A","BRCA1","BRCA2","BTD","CACNA1S","CALM1","CALM2","CALM3","CASQ2","COL3A1","CYP27A1","DES","DSC2","DSG2","DSP",
			"ENG","FBN1","FLNC","GAA","GLA","HFE","HNF1A","KCNH2","KCNQ1","LDLR","LMNA","MAX","MEN1","MLH1","MSH2","MSH6","MUTYH","MYBPC3","MYH11","MYH7","MYL2","MYL3","NF2",
			"OTC","PALB2","PCSK9","PKP2","PLN","PMS2","PRKAG2","PTEN","RB1","RBM20","RET","RPE65","RYR1","RYR2","SCN5A","SDHAF2","SDHB","SDHC","SDHD","SMAD3","SMAD4","STK11","TGFBR1",
			"TGFBR2","TMEM127","TMEM43","TNNC1","TNNI3","TNNT2","TP53","TPM1","TRDN","TSC1","TSC2","TTN","TTR","VHL","WT1"
	};
}
