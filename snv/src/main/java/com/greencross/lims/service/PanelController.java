package com.greencross.lims.service;

import com.gcgenome.lims.dto.Panel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PanelController {
	private final PanelService svc;
	public PanelController(PanelService svc) {
		this.svc = svc;
	}
	@RequestMapping(value="/panels", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Panel[] list() {
		return svc.list();
	}
}
