package com.gcgenome.lims.trans;

import com.gcgenome.lims.dto.Request;
import com.gcgenome.lims.dto.Worklist;
import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class WorklistToDTO {
	public Worklist map(com.gcgenome.lims.entity.Worklist entity) {
		return new Worklist().worklist(entity.worklist())
							 .createTime(LocalDateTimeToEpoch.map(entity.createTime()))
							 .state(entity.state()!=null?entity.state().name():null)
							 .title(entity.title())
							 .user(entity.user()!=null?entity.user().name():null)
							 .values(entity.value())
							 .sampleCnt(entity.workCnt())
							 .services(entity.services().stream()
											 .map(ServiceToDTO::map)
											 .filter(Objects::nonNull)
											 .toArray(Request.Service[]::new));
	}
}
