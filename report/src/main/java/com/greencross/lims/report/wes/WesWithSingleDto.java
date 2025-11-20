package com.greencross.lims.report.wes;

import com.gcgenome.lims.report.Dto;
import com.greencross.lims.report.builder.AbstractReportDto;
import com.greencross.lims.report.single.SingleGeneDto;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class WesWithSingleDto extends AbstractReportDto implements Dto {
	private SingleGeneDto single;
	private WesDto wes;
}
