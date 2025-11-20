package com.greencross.lims.report.tmp;

import com.gcgenome.lims.test.tmp.Ballondor;
import com.greencross.lims.report.builder.AbstractReportTemplate;

public interface BallondorTemplate extends AbstractReportTemplate<BallondorResource> {
	Ballondor testInfo();
	String info();
}
