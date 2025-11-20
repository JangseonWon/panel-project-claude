package com.greencross.lims.worker.brca.v2;

import com.greencross.lims.entity.Analysis;
import com.greencross.lims.entity.Request;
import com.greencross.lims.worker.SnvWorkerImpl;
import com.greencross.lims.worker.Tester;
import com.greencross.lims.worker.TesterImpl;
import com.greencross.lims.worker.brca.impl.v2.SnvCandidatesv2;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

@ExtendWith(SpringExtension.class)
class SnvCandidatesv2Test {
    private final Logger Log = LoggerFactory.getLogger(getClass());
    private final static Tester Z137_01 = new TesterImpl("21BRCA014_20290219-171-5075_21BRCA144-02_I.annotation.filter_Candidates.txt", 202902191715075L,
            new Request().pk(new Request.RequestPK().sample(202902191715075L).service("Z137")),
            new Analysis(new Analysis.AnalysisPK(UUID.fromString("a118c948-74b9-4929-abf9-35d8a50e6f37"), "21BRCA014", 2, "202902191715075:Z137"))
                    .panel("BRCA").serial("21BRCA014_20290219-171-5075_21BRCA144-02_I")
                    .sample(202902191715075L).service("Z137"));
    private final static Tester Z137_02 = new TesterImpl("21BRCA014_20290218-171-5075_BRCA-1_I.annotation.filter_Candidates.txt", 202902181715075L,
            new Request().pk(new Request.RequestPK().sample(202902181715075L).service("Z137")),
            new Analysis(new Analysis.AnalysisPK(UUID.fromString("a118c948-74b9-4929-abf9-35d8a50e6f37"), "21BRCA014", 1, "202902181715075:Z137"))
                    .panel("BRCA").serial("21BRCA014_20290218-171-5075_BRCA-1_I")
                    .sample(202902181715075L).service("Z137"));

    private final static Tester Z138_01 = new TesterImpl("21BRCA014_20290219-171-5075_21BRCA144-02_I.annotation.filter_Candidates.txt", 202902191715075L,
            new Request().pk(new Request.RequestPK().sample(202902191715075L).service("Z138")),
            new Analysis(new Analysis.AnalysisPK(UUID.fromString("a118c948-74b9-4929-abf9-35d8a50e6f37"), "21BRCA014", 2, "202902191715075:Z138"))
                    .panel("BRCA").serial("21BRCA014_20290219-171-5075_21BRCA144-02_I")
                    .sample(202902191715075L).service("Z138"));
    private final static Tester Z138_02 = new TesterImpl("21BRCA014_20290218-171-5075_BRCA-1_I.annotation.filter_Candidates.txt", 202902181715075L,
            new Request().pk(new Request.RequestPK().sample(202902181715075L).service("Z138")),
            new Analysis(new Analysis.AnalysisPK(UUID.fromString("a118c948-74b9-4929-abf9-35d8a50e6f37"), "21BRCA014", 1, "202902181715075:Z138"))
                    .panel("BRCA").serial("21BRCA014_20290218-171-5075_BRCA-1_I")
                    .sample(202902181715075L).service("Z138"));
    private final static Tester X009_01 = new TesterImpl("21BRCA015_20290222-171-5289_21TP53-006-17_I.annotation.filter_Candidates.txt", 202902221715289L,
            new Request().pk(new Request.RequestPK().sample(202902221715289L).service("X009")),
            new Analysis(new Analysis.AnalysisPK(UUID.fromString("a118c948-74b9-4929-abf9-35d8a50e6f37"), "21BRCA015", 17, "202902221715289:X009"))
                    .panel("TP53").serial("21BRCA015_20290222-171-5289_21TP53-006-17_I")
                    .sample(202902221715289L).service("X009"));

    private final static Tester X009_02 = new TesterImpl("21BRCA015_20290222-171-5289_BRCA-1_I.annotation.filter_Candidates.txt", 202902221715289L,
            new Request().pk(new Request.RequestPK().sample(202902221715289L).service("X009")),
            new Analysis(new Analysis.AnalysisPK(UUID.fromString("a118c948-74b9-4929-abf9-35d8a50e6f37"), "21BRCA015", 1, "202902221715289:X009"))
                    .panel("BRCA").serial("21BRCA015_20290222-171-5289_BRCA-1_I")
                    .sample(202902221715289L).service("X009"));

    private final static Tester G068_01 = new TesterImpl("21BRCA012_20290209-171-5213_21cele-B021-07_I.annotation.filter_Candidates.txt", 202902091715213L,
            new Request().pk(new Request.RequestPK().sample(202902091715213L).service("G068")),
            new Analysis(new Analysis.AnalysisPK(UUID.fromString("a118c948-74b9-4929-abf9-35d8a50e6f37"), "21BRCA012", 7, "202902091715213:G068"))
                    .panel("cele-B").serial("21BRCA012_20290209-171-5213_21cele-B021-07_I")
                    .sample(202902091715213L).service("G068"));

    private final static Tester G068_02 = new TesterImpl("21BRCA012_20290209-171-5213_BRCA-1_I.annotation.filter_Candidates.txt", 202902091715213L,
            new Request().pk(new Request.RequestPK().sample(202902091715213L).service("G068")),
            new Analysis(new Analysis.AnalysisPK(UUID.fromString("a118c948-74b9-4929-abf9-35d8a50e6f37"), "21BRCA012", 1, "202902091715213:G068"))
                    .panel("BRCA").serial("21BRCA012_20290209-171-5213_BRCA-1_I")
                    .sample(202902091715213L).service("G068"));

    private final static Tester G069_01 = new TesterImpl("21BRCA012_20290209-171-5213_21cele-B021-07_I.annotation.filter_Candidates.txt", 202902091715213L,
            new Request().pk(new Request.RequestPK().sample(202902091715213L).service("G069")),
            new Analysis(new Analysis.AnalysisPK(UUID.fromString("a118c948-74b9-4929-abf9-35d8a50e6f37"), "21BRCA012", 7, "202902091715213:G069"))
                    .panel("cele-B").serial("21BRCA012_20290209-171-5213_21cele-B021-07_I")
                    .sample(202902091715213L).service("G069"));

    private final static Tester G069_02 = new TesterImpl("21BRCA012_20290209-171-5213_BRCA-1_I.annotation.filter_Candidates.txt", 202902091715213L,
            new Request().pk(new Request.RequestPK().sample(202902091715213L).service("G069")),
            new Analysis(new Analysis.AnalysisPK(UUID.fromString("a118c948-74b9-4929-abf9-35d8a50e6f37"), "21BRCA012", 1, "202902091715213:G069"))
                    .panel("BRCA").serial("21BRCA012_20290209-171-5213_BRCA-1_I")
                    .sample(202902091715213L).service("G069"));
    private final static Tester N001_01 = new TesterImpl("21BRCA012_20290210-171-5049_21W-BRCA012-06_I.annotation.filter_Candidates.txt", 202902101715049L,
            new Request().pk(new Request.RequestPK().sample(202902101715049L).service("N001")),
            new Analysis(new Analysis.AnalysisPK(UUID.fromString("a118c948-74b9-4929-abf9-35d8a50e6f37"), "21BRCA012", 6, "202902101715049:N001"))
                    .panel("W-BRCA").serial("21BRCA012_20290210-171-5049_21W-BRCA012-06_I")
                    .sample(202902101715049L).service("N001"));

    private final static Tester N001_02 = new TesterImpl("21BRCA012_20290210-171-5049_BRCA-1_I.annotation.filter_Candidates.txt", 202902101715049L,
            new Request().pk(new Request.RequestPK().sample(202902101715049L).service("N001")),
            new Analysis(new Analysis.AnalysisPK(UUID.fromString("a118c948-74b9-4929-abf9-35d8a50e6f37"), "21BRCA012", 1, "202902101715049:N001"))
                    .panel("BRCA").serial("21BRCA012_20290210-171-5049_BRCA-1_I")
                    .sample(202902101715049L).service("N001"));

    private final static Tester N101_01 = new TesterImpl("21BRCA012_20290210-100-5213_21CGS007-14_I.annotation.filter_Candidates.txt", 202902101005213L,
            new Request().pk(new Request.RequestPK().sample(202902101005213L).service("N101")),
            new Analysis(new Analysis.AnalysisPK(UUID.fromString("a118c948-74b9-4929-abf9-35d8a50e6f37"), "21BRCA012", 14, "202902101005213:N101"))
                    .panel("CGS").serial("21BRCA012_20290210-100-5213_21CGS007-14_I")
                    .sample(202902101005213L).service("N101"));

    private final static Tester N101_02 = new TesterImpl("21BRCA012_20290210-100-5213_BRCA-1_I.annotation.filter_Candidates.txt", 202902101005213L,
            new Request().pk(new Request.RequestPK().sample(202902101005213L).service("N101")),
            new Analysis(new Analysis.AnalysisPK(UUID.fromString("a118c948-74b9-4929-abf9-35d8a50e6f37"), "21BRCA012", 1, "202902101005213:N101"))
                    .panel("BRCA").serial("21BRCA012_20290210-100-5213_BRCA-1_I")
                    .sample(202902101005213L).service("N101"));

    private final static Tester N111_01 = new TesterImpl("21BRCA012_20290210-100-5213_21CGS007-14_I.annotation.filter_Candidates.txt", 202902101005213L,
            new Request().pk(new Request.RequestPK().sample(202902101005213L).service("N111")),
            new Analysis(new Analysis.AnalysisPK(UUID.fromString("a118c948-74b9-4929-abf9-35d8a50e6f37"), "21BRCA012", 14, "202902101005213:N111"))
                    .panel("CGS").serial("21BRCA012_20290210-100-5213_21CGS007-14_I")
                    .sample(202902101005213L).service("N111"));
    private final static Tester N111_02 = new TesterImpl("21BRCA012_20290210-100-5213_BRCA-1_I.annotation.filter_Candidates.txt", 202902101005213L,
            new Request().pk(new Request.RequestPK().sample(202902101005213L).service("N111")),
            new Analysis(new Analysis.AnalysisPK(UUID.fromString("a118c948-74b9-4929-abf9-35d8a50e6f37"), "21BRCA012", 1, "202902101005213:N111"))
                    .panel("BRCA").serial("21BRCA012_20290210-100-5213_BRCA-1_I")
                    .sample(202902101005213L).service("N111"));
    private final static Tester[] TESTERS = new Tester[]{
            Z137_01, Z137_02,
            Z138_01, Z138_02,
            X009_01, X009_02,
            G068_01, G068_02,
            G069_01, G069_02,
            N001_01,  N001_02,
            N101_01,  N101_02,
            N111_01, N111_02

    };

    @Test
    @DisplayName("파일명으로부터 Sample ID를 제대로 추출하는지 테스트")
    void testSample() {
        for (Tester test : TESTERS) {
            Log.info(test.input());
            test.testSampleExtract(SnvCandidatesv2.PROCESSOR.sample(test.input()));
        }
    }

    @Test
    @DisplayName("파일명으로부터 Analysis 객체를 제대로 생성하는지 테스트")
    void testAnalysis() {
        for (Tester test : TESTERS) {
            Log.info(test.input());
            test.testAnalysisExtract(SnvCandidatesv2.PROCESSOR.map(test.input(), test.requestSample()));
        }
    }
}
