package com.greencross.lims.trans;

import com.greencross.lims.dto.Batch;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BatchToDTO {
	public Batch map(com.greencross.lims.entity.Batch<?> entity) {
		return new Batch().id(entity.pk().batch())
						  .sheet(entity.pk().sheet().toString())
						  .createTime(LocalDateTimeToEpoch.map(entity.createTime()))
						  .user(entity.user()!=null?entity.user().name():null)
						  .title(entity.title())
						  .values(entity.value())
						  .sampleCnt(entity.sampleCnt());
	}
}
