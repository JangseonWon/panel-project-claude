package com.greencross.lims.worker.cancer;

import com.greencross.lims.entity.Analysis;
import com.greencross.lims.entity.Request;
import com.greencross.lims.worker.Tester;
import com.greencross.lims.worker.TesterImpl;
import com.greencross.lims.worker.cancer.impl.v1_1.SnvAllv1_1;
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
	private final static Tester BRCA = new TesterImpl("21Cancer033_21G-BRCA015-A_20210422-171-5100.annotation.txt", 202104221715100L,
													  new Request().pk(new Request.RequestPK().sample(202104221715100L).service("S096")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("a118c948-74b9-4929-abf9-35d8a50e6f37"), "21Cancer033", 0, "202104221715100:S096"))
															  .panel("G-BRCA").serial("21Cancer033_21G-BRCA015-A_20210422-171-5100")
															  .sample(202104221715100L).service("S096"));
	private final static Tester BRCA_R = new TesterImpl("21Cancer033_21G-BRCA013-A-R_20210416-171-5119.annotation.txt", 202104161715119L,
													  new Request().pk(new Request.RequestPK().sample(202104161715119L).service("Z138")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("a118c948-74b9-4929-abf9-35d8a50e6f37"), "21Cancer033", 0, "202104161715119:Z138"))
															  .panel("G-BRCA").serial("21Cancer033_21G-BRCA013-A-R_20210416-171-5119")
															  .sample(202104161715119L).service("Z138"));
	private final static Tester CAN = new TesterImpl("21Cancer033_can-09_20210422-171-5160.annotation.txt", 202104221715160L,
													  new Request().pk(new Request.RequestPK().sample(202104221715160L).service("X009")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("025394fd-39ec-4cdf-9398-f62959aacdf5"), "21Cancer033", 9, "202104221715160:X009"))
															  .panel("can").serial("21Cancer033_can-09_20210422-171-5160")
															  .sample(202104221715160L).service("X009"));
	private final static Tester Control = new TesterImpl("21Cancer034-16_Positive.annotation.txt", null,
													  null,
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("025394fd-39ec-4cdf-9398-f62959aacdf5"), "21Cancer034", 16, "Positive"))
															  .panel(null).serial("21Cancer034-16_Positive")
															  .sample(null).service(null));
	private final static Tester[] TESTERS = new Tester[] {
		BRCA, CAN, BRCA_R, Control,
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
