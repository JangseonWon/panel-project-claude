package com.greencross.lims.file;

import com.gcgenome.lims.dto.File;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FileToDto {
	public File map(com.greencross.lims.entity.File entity) {
		return new File().sample(entity.pk().sample())
						 .service(entity.pk().service())
						 .sequence(entity.pk().sequence())
						 .createAt(LocalDateTimeToEpoch.map(entity.createTime()))
						 .creator(entity.user()!=null?entity.user().name():null)
						 .name(entity.name())
						 .extension(entity.extension())
						 .size(entity.size())
						 .url(null);
	}
}
