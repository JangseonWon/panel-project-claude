package com.greencross.lims.kanban;

import com.gcgenome.lims.dto.Issue;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.RequestRareDisease;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.entity.Service;
import com.greencross.lims.trans.LocalDateTimeToEpoch;
import com.greencross.lims.trans.LocalDateToEpoch;
import lombok.experimental.UtilityClass;

@UtilityClass
public class IssueToDTO {
	public Issue map(IssueRareDisease entity) {
		RequestRareDisease request = entity.request();
		Sample smp = entity.sample();
		Service svc = entity.service();
		Issue dto = new Issue().sample(smp.id())
		//					   .sheet(entity.sheet()!=null?entity.sheet().toString():null)
							   .parent(entity.parent())
							   .title(entity.title())
							   .createTime(LocalDateTimeToEpoch.map(entity.createTime()));
		//					   .service(ServiceToDTO.map(entity.service()));
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
