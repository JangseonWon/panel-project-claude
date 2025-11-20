package com.greencross.lims.library;

import com.greencross.lims.dto.Sheet;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController("TemplateControllerLibrary")
public class TemplateController {
	private final TemplateService svc;
	public TemplateController(TemplateService svc) {
		this.svc = svc;
	}
	@RequestMapping(value="/library/batch/template", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Sheet batch() {
		return svc.batch();
	}
	@RequestMapping(value="/library/analysis/template", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Sheet analysis() {
		return svc.analysis();
	}
}
