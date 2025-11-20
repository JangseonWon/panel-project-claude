package com.gcgenome.lims.test.wes;

import com.gcgenome.lims.test.single.TestInfo;

public class SingleGeneTest {
    public final static com.gcgenome.lims.test.single.TestInfo N058 = com.gcgenome.lims.test.single.TestInfo.builder()
            .code("N058")
            .title("IDS gene mutation / 전용 결과보고서")
            .name("IDS gene mutation / 전용")
            .gene("IDS").target("IDS on Chromosome Xq28")
            .isNationalInsuranceTest(true).build();
    public static final com.gcgenome.lims.test.single.TestInfo[] TESTS = new TestInfo[] {
            N058
    };
}
