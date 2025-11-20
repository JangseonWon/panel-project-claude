package com.greencross.lims.sample;

import com.greencross.lims.dto.Analysis;
import com.greencross.lims.dto.Query;
import com.greencross.lims.dto.Work;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController("RequestControllerSample")
public class RequestController {
	private final RequestService svc;
	public RequestController(RequestService svc) {
		this.svc = svc;
	}
	@RequestMapping(value="/sample/{sample}/service/{service}/works",  method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<Work> works(@PathVariable long sample, @PathVariable String service) {
		return svc.works(sample, service);
	}
	@RequestMapping(value="/sample/{sample}/dnas",  method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<Analysis> dna(@PathVariable long sample) {
		return svc.dna(sample);
	}
	@RequestMapping(value="/sample/{sample}/libraries",  method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<Analysis> library(@PathVariable long sample) {
		return svc.library(sample);
	}
	@RequestMapping(value="/sample/{sample}/sequencings",  method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<Analysis> sequencing(@PathVariable long sample) {
		return svc.sequencing(sample);
	}
}
