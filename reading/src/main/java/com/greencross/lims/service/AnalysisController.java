package com.greencross.lims.service;

import com.greencross.lims.dto.Analysis;
import com.greencross.lims.dto.QueryServerside;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AnalysisController {
	private final AnalysisService svc;
	public AnalysisController(AnalysisService svc) {
		this.svc = svc;
	}

	@RequestMapping(value="/readings/{type}/analysis", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Page<Analysis> list(@PathVariable String type, @RequestBody QueryServerside query) {
		return svc.list(type, query);
	}
	@RequestMapping(value="/samples/{sample}/analysis", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<Analysis> list(@PathVariable long sample) {
		return svc.list(sample);
	}
	@RequestMapping(value="/samples/{sample}/services/{service}/analysis/{batch}/{row}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Analysis find(@PathVariable long sample, String service, String batch, int row) {
		return svc.find(sample, service, batch, row);
	}
}
