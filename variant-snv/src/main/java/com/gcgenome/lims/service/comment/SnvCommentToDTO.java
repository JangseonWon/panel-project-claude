package com.gcgenome.lims.service.comment;

import com.gcgenome.lims.dto.Comment;
import com.gcgenome.lims.entity.SnvComment;
import com.gcgenome.lims.entity.User;
import com.gcgenome.lims.service.LocalDateTimeToEpoch;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SnvCommentToDTO {
	public Comment map(SnvComment entity) {
		Comment dto = new Comment();
		User<?> creator = entity.user();
		if(creator!=null) dto.createBy(creator.name());
		User<?> editor = entity.lastModifiedBy();
		if(editor!=null) dto.lastModifyBy(editor.name());
		if(entity.lastModifyAt()!=null) dto.lastModifyAt(LocalDateTimeToEpoch.map(entity.lastModifyAt()));
		return dto.snv(entity.snv()).comment(entity.comment()).createAt(entity.pk().createTime());
	}
}
