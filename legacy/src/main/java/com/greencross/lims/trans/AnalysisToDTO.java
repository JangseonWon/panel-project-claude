package com.greencross.lims.trans;

import com.greencross.lims.dto.Analysis;
import com.greencross.lims.entity.Batch;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Request;
import com.greencross.lims.entity.Sample;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AnalysisToDTO {
	public Analysis map(com.greencross.lims.entity.Analysis<?> entity) {
		Analysis dto = new Analysis().sheet(entity.pk().sheet().toString())
									 .batch(entity.pk().batch())
									 .row(entity.row())
									 .serial(entity.serial())
									 .sort(entity.sort())
									 .createTime(LocalDateTimeToEpoch.map(entity.createTime()))
									 .values(entity.value());
		Batch<?> batch = entity.batch();
		if(batch!=null) dto.batchTitle(batch.title());
		// 분석에 사용한 샘플 정보
		Sample smp = entity.sample();
		if(smp!=null) {
			dto.sample(smp.id());
			Patient pat = smp.patient();
			if(pat!=null) {
				dto.patientName(pat.name())
				   .patientCode(pat.code());
			}
		}
		// 연결된 의뢰들
		try {
			if (entity.requests() != null) dto.requests(entity.requests().stream().map(AnalysisToDTO::map).toArray(Analysis.AnalysisRequest[]::new));
		} catch(Exception ignore){}
		return dto;
	}
	public Analysis.AnalysisRequest map(Request entity) {
		Analysis.AnalysisRequest dto = new Analysis.AnalysisRequest();
		dto.sample(entity.pk().sample()).service(ServiceToDTO.map(entity.service()));
		return dto;
	}
}
