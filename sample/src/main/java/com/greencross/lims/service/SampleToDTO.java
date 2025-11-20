package com.greencross.lims.service;

import com.greencross.lims.dto.Sample;
import com.greencross.lims.entity.Patient;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SampleToDTO {
	public Sample map(com.greencross.lims.entity.Sample entity) {
		Sample dto = new Sample().id(entity.id())
								 .sampleType(entity.sampleType())
								 .remark(entity.remark())
								 .barcode(entity.barcode());
		Patient pat = entity.patient();
		if(pat!=null) {
			String customerName = pat.customerName2();
			if(customerName == null) customerName = pat.customerName();
			dto.patientName(pat.name())
			   .patientCode(pat.code())
			   .customerName(customerName)
			   .mrn(pat.mrn())
			   .patientSex(pat.sex()!=null?pat.sex().name():null);
		}
		if(entity.requests()!=null) entity.requests().stream().filter(s->s.dateSampling()!=null).findFirst().ifPresent(r->dto.dateSampling(LocalDateToEpoch.map(r.dateSampling())));
		return dto;
	}
}
