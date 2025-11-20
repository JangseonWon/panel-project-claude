package com.gcgenome.lims.service;

import com.gcgenome.lims.dto.Work;
import com.gcgenome.lims.entity.Patient;
import com.gcgenome.lims.entity.Request;
import com.gcgenome.lims.entity.Sample;
import com.gcgenome.lims.entity.WorkRedo;
import com.gcgenome.lims.trans.LocalDateTimeToEpoch;
import com.gcgenome.lims.trans.LocalDateToEpoch;
import com.gcgenome.lims.trans.ServiceToDTO;
import lombok.experimental.UtilityClass;

@UtilityClass
public class QueueToDTO {
	public Work map(WorkRedo entity) {
		Request request = entity.request();
		Sample smp = request.sample();
		Work dto = new Work().id(entity.pk().serial())
							 .createTime(LocalDateTimeToEpoch.map(entity.createTime()))
							 .sample(entity.request().pk().sample())
							 .sampleType(smp.sampleType())
							 .remark(smp.remark())
							 .service(ServiceToDTO.map(request.service()));
		if(request.dateRequest()!=null) dto.dateRequest(LocalDateToEpoch.map(request.dateRequest()));
		if(request.dateStart()!=null) dto.dateStart(LocalDateToEpoch.map(request.dateStart()));
		if(request.dateDue()!=null) dto.dateDue(LocalDateToEpoch.map(request.dateDue()));
		if(request.tat()!=null) dto.tat(request.tat());
		Patient pat = smp.patient();
		if(pat!=null) {
			String customerName = pat.customerName2();
			if(customerName == null) customerName = pat.customerName();
			dto.patientName(pat.name())
			   .patientCode(pat.code())
			   .customerName(customerName)
			   .mrn(pat.mrn());
		}
		return dto;
	}
}
