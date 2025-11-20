package com.gcgenome.lims.trans;

import com.gcgenome.lims.dto.Sheet;
import com.gcgenome.lims.entity.WorklistTemplate;
import lombok.experimental.UtilityClass;

import java.util.Arrays;

@UtilityClass
public class SheetToDTO {
	public Sheet map(com.gcgenome.lims.entity.Sheet entity) {
		Sheet dto = null;
		if(entity instanceof WorklistTemplate) {
			WorklistTemplate cast = (WorklistTemplate)entity;
			dto = new com.gcgenome.lims.dto.WorklistTemplate().active(cast.active())
																.values(cast.values()!=null?Arrays.asList(cast.values()):null)
																.type(Sheet.SheetType.WORKLIST);
		}
		return dto.id(entity.id().toString())
				  .name(entity.name())
				  .order(entity.order())
				  .fixedColumnsLeft(entity.fixedColumnsLeft())
				  .pageSize(entity.pageSize())
				  .orderColumn(entity.orderColumn())
				  .asc(entity.asc())
				  .columns(entity.columns()!=null? Arrays.asList(entity.columns()):null);
	}
}
