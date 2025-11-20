package com.greencross.lims.worker.brca.v1_1;

import com.greencross.lims.entity.Analysis;
import com.greencross.lims.entity.Request;
import com.greencross.lims.worker.Tester;
import com.greencross.lims.worker.TesterImpl;
import com.greencross.lims.worker.brca.impl.v1_1.SnvAllv1_1;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;
@Disabled
@ExtendWith(SpringExtension.class)
class SnvAllv2Test {
	private final Logger Log = LoggerFactory.getLogger(getClass());
	private final static Tester BRCA = new TesterImpl("I_21BRCA014-05_21BRCA144_20210218-171-5075.annotation.txt", 202102181715075L,
			new Request().pk(new Request.RequestPK().sample(202102181715075L).service("Z138")),
			new Analysis(new Analysis.AnalysisPK(UUID.fromString("a118c948-74b9-4929-abf9-35d8a50e6f37"), "21BRCA014", 5, "202102181715075:Z138"))
					.panel("BRCA").serial("I_21BRCA014-05_21BRCA144_20210218-171-5075")
					.sample(202102181715075L).service("Z138"));
	private final static Tester BRCA_R = new TesterImpl("I_21BRCA013-07_21BRCA127-R2_20210209-171-5193.annotation.txt", 202102091715193L,
			new Request().pk(new Request.RequestPK().sample(202102091715193L).service("Z138")),
			new Analysis(new Analysis.AnalysisPK(UUID.fromString("a118c948-74b9-4929-abf9-35d8a50e6f37"), "21BRCA013", 7, "202102091715193:Z138"))
					.panel("BRCA").serial("I_21BRCA013-07_21BRCA127-R2_20210209-171-5193")
					.sample(202102091715193L).service("Z138"));
	private final static Tester TP53 = new TesterImpl("I_21BRCA015-17_21TP53-006_20210222-171-5289.annotation.txt", 202102221715289L,
			new Request().pk(new Request.RequestPK().sample(202102221715289L).service("X009")),
			new Analysis(new Analysis.AnalysisPK(UUID.fromString("a118c948-74b9-4929-abf9-35d8a50e6f37"), "21BRCA015", 17, "202102221715289:X009"))
					.panel("TP53").serial("I_21BRCA015-17_21TP53-006_20210222-171-5289")
					.sample(202102221715289L).service("X009"));
	private final static Tester CELE = new TesterImpl("I_21BRCA012-07_21cele-B021_20210209-171-5213.annotation.txt", 202102091715213L,
			new Request().pk(new Request.RequestPK().sample(202102091715213L).service("G068")),
			new Analysis(new Analysis.AnalysisPK(UUID.fromString("a118c948-74b9-4929-abf9-35d8a50e6f37"), "21BRCA012", 7, "202102091715213:G068"))
					.panel("cele-B").serial("I_21BRCA012-07_21cele-B021_20210209-171-5213")
					.sample(202102091715213L).service("G068"));
	private final static Tester WBRCA = new TesterImpl("I_21BRCA012-06_21W-BRCA005_20210210-171-5049.annotation.txt", 202102101715049L,
			new Request().pk(new Request.RequestPK().sample(202102101715049L).service("N001")),
			new Analysis(new Analysis.AnalysisPK(UUID.fromString("a118c948-74b9-4929-abf9-35d8a50e6f37"), "21BRCA012", 6, "202102101715049:N001"))
					.panel("W-BRCA").serial("I_21BRCA012-06_21W-BRCA005_20210210-171-5049")
					.sample(202102101715049L).service("N001"));
	private final static Tester Control = new TesterImpl("I_21BRCA013-14_Positive.annotation.txt", null,
			null,
			new Analysis(new Analysis.AnalysisPK(UUID.fromString("a118c948-74b9-4929-abf9-35d8a50e6f37"), "21BRCA013", 14, "Positive"))
					.panel(null).serial("I_21BRCA013-14_Positive")
					.sample(null).service(null));
	private final static Tester[] TESTERS = new Tester[] {
			BRCA, TP53, BRCA_R, CELE, WBRCA, Control,
	};

	@Test
	@DisplayName("파일명으로부터 Sample ID를 제대로 추출하는지 테스트")
	void testSample() {
		for(Tester test: TESTERS) {
			Log.info(test.input());
			test.testSampleExtract(SnvAllv1_1.PROCESSOR.sample(test.input()));
		}
	}
	@Test
	@DisplayName("파일명으로부터 Analysis 객체를 제대로 생성하는지 테스트")
	void testAnalysis() {
		for(Tester test: TESTERS) {
			Log.info(test.input());
			test.testAnalysisExtract(SnvAllv1_1.PROCESSOR.map(test.input(), test.requestSample()));
		}
	}
}