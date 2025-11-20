package com.greencross.lims.dna;

import com.greencross.lims.dto.Sheet;
import com.greencross.lims.entity.AnalysisDna;
import com.greencross.lims.entity.AnalysisTemplate;
import com.greencross.lims.entity.BatchDna;
import com.greencross.lims.entity.BatchTemplate;
import com.greencross.lims.trans.SheetToDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service("TemplateServiceDna")
public class TemplateService {
	@PersistenceContext
	private EntityManager em;
	@Transactional(readOnly = true)
	public Sheet batch() {
		return SheetToDTO.map(em.find(BatchTemplate.class, BatchDna._ID));
	}
	@Transactional(readOnly = true)
	public Sheet analysis() {
		return SheetToDTO.map(em.find(AnalysisTemplate.class, AnalysisDna._ID));
	}
}
