package com.gcgenome.lims.service.interpretation;

import com.gcgenome.lims.entity.InterpretationReserved;
import com.gcgenome.lims.entity.User;
import com.gcgenome.lims.service.LocalDateTimeToEpoch;
import lombok.experimental.UtilityClass;

@UtilityClass
public class InterpretationReservedToDTO {
	public com.gcgenome.lims.dto.InterpretationReserved map(InterpretationReserved entity) {
		com.gcgenome.lims.dto.InterpretationReserved dto = new com.gcgenome.lims.dto.InterpretationReserved();
		User<?> creator = entity.user();
		if(creator!=null) dto.createBy(creator.name());
		return dto.snv(entity.pk().snv()).service(entity.pk().service())
				  .interpretation(entity.interpretation())
				  .createAt(LocalDateTimeToEpoch.map(entity.createTime()));
	}
}
