package com.greencross.lims.report.genomescreen.kokr;

import com.gcgenome.lims.test.genomescreen.TestInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

// 심장돌연사 지놈 스크린
@EqualsAndHashCode(callSuper = true)
@Getter
@Accessors(fluent = true)
public class GenomeScreenTemplateN185KoKr extends GenomeScreenTemplateN089KoKr {
	private final String lblPatientName = "고객번호";
	public GenomeScreenTemplateN185KoKr(GenomeScreenResourceN089KoKr resource, TestInfo testInfo) {
		super(resource, testInfo);
	}
}
