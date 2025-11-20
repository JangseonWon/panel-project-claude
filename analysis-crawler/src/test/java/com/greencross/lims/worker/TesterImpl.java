package com.greencross.lims.worker;

import com.greencross.lims.entity.Analysis;
import com.greencross.lims.entity.Request;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class TesterImpl implements Tester {
	private final String input;
	private final Long sampleShouldBe;
	private final Request requestSample;
	private final Analysis analysisShouldBe;
}
