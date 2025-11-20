package com.gcgenome.lims.service;

import com.gcgenome.lims.dto.RequestSnv;
import com.gcgenome.lims.entity.*;
import lombok.experimental.UtilityClass;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class SnvToDTO {
	public RequestSnv map(Snv entity) {
		Request request = entity.request();
		Sample smp = request.sample();
		List<Analysis> analysis = request.analysis();
		RequestSnv dto = new RequestSnv().tier(entity.classification())
										 .sample(request.pk().sample())
										 .barcode(smp.barcode())
										 .sampleType(smp.sampleType())
										 .remark(smp.remark())
										 .service(ServiceToDTO.map(request.service()));
		if(request.dateRequest()!=null) dto.dateRequest(LocalDateToEpoch.map(request.dateRequest()));
		if(request.dateStart()!=null) dto.dateStart(LocalDateToEpoch.map(request.dateStart()));
		if(request.dateDue()!=null) dto.dateDue(LocalDateToEpoch.map(request.dateDue()));
		if(request.dateSampling()!=null) dto.dateDue(LocalDateToEpoch.map(request.dateSampling()));
		if(request.tat()!=null) dto.tat(request.tat());
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
		// entity.customInfos().stream().forEach(k->info.put(k.desc(), k.value()));
		if(analysis!=null && !analysis.isEmpty()) {
			Analysis last = analysis.stream().max(Comparator.comparing(Analysis::createTime)).get();
			dto.batch(last.batch()).row(last.row()).serial(last.serial());
		}
		return dto.values(info);
	}
}
