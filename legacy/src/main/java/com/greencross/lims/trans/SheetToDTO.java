package com.greencross.lims.trans;

import com.greencross.lims.dto.Sheet;
import com.greencross.lims.dto.TableTemplate;
import com.greencross.lims.entity.AnalysisTemplate;
import com.greencross.lims.entity.BatchTemplate;
import com.greencross.lims.entity.TupleTemplate;
import com.greencross.lims.entity.WorklistTemplate;
import lombok.experimental.UtilityClass;

import java.util.Arrays;

@UtilityClass
public class SheetToDTO {
	public Sheet map(com.greencross.lims.entity.Sheet entity) {
		Sheet dto = null;
		if(entity instanceof WorklistTemplate) {
			WorklistTemplate cast = (WorklistTemplate)entity;
			dto = new com.greencross.lims.dto.WorklistTemplate().active(cast.active())
																.values(cast.values()!=null?Arrays.asList(cast.values()):null)
																.type(Sheet.SheetType.WORKLIST);
		} else if(entity instanceof BatchTemplate) {
			BatchTemplate cast = (BatchTemplate)entity;
			dto = new com.greencross.lims.dto.BatchTemplate().active(cast.active()).type(Sheet.SheetType.BATCH);
		} else if(entity instanceof AnalysisTemplate) dto = new com.greencross.lims.dto.AnalysisTemplate().type(Sheet.SheetType.ANALYSIS);
		else if(entity instanceof TupleTemplate) dto = new TableTemplate().type(Sheet.SheetType.TABLE);
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
