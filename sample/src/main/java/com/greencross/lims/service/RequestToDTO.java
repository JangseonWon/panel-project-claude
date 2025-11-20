package com.greencross.lims.service;

import com.greencross.lims.dto.Request;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Sample;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class RequestToDTO {
	public Request map(com.greencross.lims.entity.Request entity) {
		Sample smp = entity.sample();
		Request dto = new Request().sample(entity.pk().sample())
								   .barcode(smp.barcode())
								   .sampleType(smp.sampleType())
								   .remark(smp.remark())
								   .service(ServiceToDTO.map(entity.service()));
		if(entity.dateRequest()!=null) dto.dateRequest(LocalDateToEpoch.map(entity.dateRequest()));
		if(entity.dateStart()!=null) dto.dateStart(LocalDateToEpoch.map(entity.dateStart()));
		if(entity.dateDue()!=null) dto.dateDue(LocalDateToEpoch.map(entity.dateDue()));
		if(entity.dateSampling()!=null) dto.dateDue(LocalDateToEpoch.map(entity.dateSampling()));
		if(entity.tat()!=null) dto.tat(entity.tat());
		Patient pat = smp.patient();
		if(pat!=null) {
			String customerName = pat.customerName2();
			if(customerName == null) customerName = pat.customerName();
			dto.patientName(pat.name())
			   .patientCode(pat.code())
			   .customerName(customerName)
			   .mrn(pat.mrn())
			   .patientSex(pat.sex()!=null?pat.sex().name():null);
		}
		Map<String, String> info = new HashMap<>();
		entity.customInfos().stream().forEach(k->info.put(k.desc(), k.value()));
		return dto.values(info);
	}
}
