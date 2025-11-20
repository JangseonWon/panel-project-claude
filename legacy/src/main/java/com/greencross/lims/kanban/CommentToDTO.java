package com.greencross.lims.kanban;

import com.gcgenome.lims.dto.Comment;
import com.greencross.lims.trans.LocalDateTimeToEpoch;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CommentToDTO {
	public Comment map(com.greencross.lims.entity.Comment entity) {
		return new Comment().createTime(LocalDateTimeToEpoch.map(entity.createTime()))
							.user(entity.user()!=null?entity.user().name():null)
							.content(entity.content());
	}
}
