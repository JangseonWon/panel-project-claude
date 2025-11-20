package com.greencross.lims.worker;

import com.greencross.lims.entity.Analysis;
import com.greencross.lims.entity.Request;

import static org.junit.jupiter.api.Assertions.*;

public interface Tester {
	String input();
	Long sampleShouldBe();
	Request requestSample();
	Analysis analysisShouldBe();
	default void testSampleExtract(Long sample) {
		assertEquals(sampleShouldBe(), sample);
	}
	default void testAnalysisExtract(Analysis analysis) {
		assertNotNull(analysis);
		assertEquals(analysisShouldBe().pk().batch(), analysis.pk().batch());
		assertEquals(analysisShouldBe().pk().row(), analysis.pk().row());
		assertEquals(analysisShouldBe().pk().sheet(), analysis.pk().sheet());
		assertEquals(analysisShouldBe().pk().request(), analysis.pk().request());
		assertEquals(analysisShouldBe().sample(), analysis.sample());
		assertEquals(analysisShouldBe().service(), analysis.service());
		assertEquals(analysisShouldBe().serial(), analysis.serial());
		assertEquals(analysisShouldBe().panel(), analysis.panel());
	}
}
