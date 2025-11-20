package com.greencross.lims.report.genomescreen;

import com.gcgenome.lims.report.func.Painter;
import com.greencross.lims.report.builder.LogoType;

import java.util.function.Function;

public class GenomeScreenN185 extends GenomeScreenN089 {
	public GenomeScreenN185(GenomeScreenTemplateN089 template, GenomeScreenDto dto,
							Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> header, LogoType logoType,
							Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> sign,
							Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> footer,
							Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> page,
							Function<Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto>, Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto>> genes) {
		super(template, dto, header, logoType, sign, footer, page, genes);
		GenomeScreenResource resource = (GenomeScreenResource) template.resource();
		if(dto.medicalRecordNumber()==null && dto.patientName()!=null) dto.medicalRecordNumber(dto.patientName());
		dto.patientName(dto.medicalRecordNumber());
	}
}
