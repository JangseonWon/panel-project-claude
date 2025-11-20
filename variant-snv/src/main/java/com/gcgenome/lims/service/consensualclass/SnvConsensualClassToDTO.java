package com.gcgenome.lims.service.consensualclass;

import com.gcgenome.lims.entity.SnvConsensualClass;
import com.gcgenome.lims.entity.User;
import com.gcgenome.lims.service.LocalDateTimeToEpoch;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SnvConsensualClassToDTO {
	public com.gcgenome.lims.dto.SnvConsensualClass map(SnvConsensualClass entity) {
		com.gcgenome.lims.dto.SnvConsensualClass dto = new com.gcgenome.lims.dto.SnvConsensualClass();
		User<?> creator = entity.user();
		if(creator!=null) dto.createBy(creator.name());
		User<?> editor = entity.lastModifiedBy();
		if(editor!=null) dto.lastModifyBy(editor.name());
		if(entity.lastModifyAt()!=null) dto.lastModifyAt(LocalDateTimeToEpoch.map(entity.lastModifyAt()));
		return dto.snv(entity.snv()).clazz(entity.classification()).comment(entity.comment())
				  .createAt(LocalDateTimeToEpoch.map(entity.createTime()));
	}
}
