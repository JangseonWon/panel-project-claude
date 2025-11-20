package com.greencross.lims.service;

import com.greencross.lims.dto.interpretation.InterpretationParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class InterpretationController {
	private final InterpretationService svc;
	public InterpretationController(InterpretationService svc) {
		this.svc = svc;
	}
	@RequestMapping(value="/samples/{sample}/services/{service}/interpretation",  method= RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object> get(@PathVariable long sample, @PathVariable String service) {
		return svc.get(sample, service);
	}
	@RequestMapping(value="/samples/{sample}/services/{service}/interpretation",  method= RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void create(@PathVariable long sample, @PathVariable String service, @RequestBody Map<String, Object> json) {
		svc.create(sample, service, json);
	}
	@RequestMapping(value="/samples/{sample}/services/{service}/interpretation",  method= RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable long sample, @PathVariable String service) {
		svc.delete(sample, service);
	}

	@RequestMapping(value="/samples/{sample}/services/{service}/auto-interpret",  method= RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Mono<Object> auto(@PathVariable long sample, @PathVariable String service, @RequestBody InterpretationParam param) {
		return svc.auto(sample, service, param);
	}
	@RequestMapping(value="/samples/{sample}/services/{service}/negative-interpret",  method= RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Object negative(@PathVariable long sample, @PathVariable String service) {
		return svc.negative(sample, service);
	}
}
