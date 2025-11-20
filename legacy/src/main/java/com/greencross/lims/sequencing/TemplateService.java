package com.greencross.lims.sequencing;

import com.greencross.lims.dto.Sheet;
import com.greencross.lims.entity.AnalysisSequencing;
import com.greencross.lims.entity.AnalysisTemplate;
import com.greencross.lims.entity.BatchSequencing;
import com.greencross.lims.entity.BatchTemplate;
import com.greencross.lims.trans.SheetToDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service("TemplateServiceSequencing")
public class TemplateService {
	@PersistenceContext
	private EntityManager em;
	@Transactional(readOnly = true)
	public Sheet batch() {
		return SheetToDTO.map(em.find(BatchTemplate.class, BatchSequencing._ID));
	}
	@Transactional(readOnly = true)
	public Sheet analysis() {
		return SheetToDTO.map(em.find(AnalysisTemplate.class, AnalysisSequencing._ID));
	}
}
