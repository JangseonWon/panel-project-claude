package com.greencross.lims.worklist;

import com.greencross.lims.dto.Work;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Request;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.entity.Worklist;
import com.greencross.lims.trans.LocalDateTimeToEpoch;
import com.greencross.lims.trans.LocalDateToEpoch;
import com.greencross.lims.trans.ServiceToDTO;
import lombok.experimental.UtilityClass;

@UtilityClass
public class WorkToDTO {
	public Work map(com.greencross.lims.entity.Work entity) {
		Request request = entity.request();
		Sample smp = entity.sample();
		Worklist worklist = entity.worklist();
		Work dto = new Work().id(entity.serial())
							 .createTime(LocalDateTimeToEpoch.map(entity.createTime()))
							 .values(entity.value())
							 .sample(entity.pk().sample())
							 .sampleType(smp.sampleType())
							 .remark(smp.remark())
							 .service(ServiceToDTO.map(request.service()));
		if(worklist!=null) dto.worklist(worklist.worklist()).worklistTitle(worklist.title());
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
