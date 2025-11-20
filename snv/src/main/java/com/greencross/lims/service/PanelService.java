package com.greencross.lims.service;

import com.gcgenome.lims.dto.Panel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PanelService {
	private final PanelDAO dao;
	public PanelService(PanelDAO dao) {
		this.dao = dao;
	}
	@Transactional(readOnly=true)
	public Panel[] list() {
		return dao.all().map(PanelToDTO::map).toArray(Panel[]::new);
	}
}
