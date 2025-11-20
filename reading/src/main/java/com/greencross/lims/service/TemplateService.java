package com.greencross.lims.service;

import com.greencross.lims.dto.Sheet;
import com.greencross.lims.trans.SheetToDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TemplateService {
	private final TemplateDAO dao;

	public TemplateService(TemplateDAO dao) {
		this.dao = dao;
	}

	@Transactional(readOnly = true)
	public Optional<Sheet> template(String name) {
		return dao.findByName(name).map(SheetToDTO::map);
	}
}