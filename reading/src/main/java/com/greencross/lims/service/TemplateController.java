package com.greencross.lims.service;

import com.greencross.lims.dto.Sheet;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class TemplateController {
	private final TemplateService svc;
	public TemplateController(TemplateService svc) {
		this.svc = svc;
	}

	@RequestMapping(value="/readings/{id}/template", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Sheet template(@PathVariable String id) {
		return svc.template(id).orElse(null);
	}
}
