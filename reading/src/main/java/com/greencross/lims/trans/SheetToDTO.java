package com.greencross.lims.trans;

import com.greencross.lims.dto.Sheet;
import lombok.experimental.UtilityClass;

import java.util.Arrays;

@UtilityClass
public class SheetToDTO {
	public Sheet map(com.greencross.lims.entity.Sheet entity) {
		Sheet dto = new com.greencross.lims.dto.Sheet().type(Sheet.SheetType.ANALYSIS);
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
