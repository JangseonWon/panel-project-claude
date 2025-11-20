package com.greencross.lims.report.tmp;

import com.greencross.lims.report.HasServiceCode;
import com.greencross.lims.report.builder.AbstractReportDto;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class BallondorDto extends AbstractReportDto implements HasServiceCode {
	private String code;
	private String result;
	private String summary;
	private String hgvsc;
	private String vaf;
	private String sample;
	private String info;
}
