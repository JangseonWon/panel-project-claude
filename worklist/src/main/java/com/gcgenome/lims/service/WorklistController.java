package com.gcgenome.lims.service;

import com.gcgenome.lims.dto.QueryServerside;
import com.gcgenome.lims.dto.Sheet;
import com.gcgenome.lims.dto.Worklist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class WorklistController {
	private final Logger Log = LoggerFactory.getLogger(getClass());
	private final WorklistService svc;
	public WorklistController(WorklistService svc) {
		this.svc = svc;
	}
	@RequestMapping(value="/worklist/template", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Mono<Sheet> template() {
		return Mono.justOrEmpty(svc.template());
	}
	@RequestMapping(value="/worklists", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Page<Worklist> worklists(@RequestBody QueryServerside query) {
		return svc.list(query);
	}
	@RequestMapping(value="/worklists/{worklist}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Worklist get(@PathVariable int worklist) {
		return svc.get(worklist).orElseThrow(()->new RuntimeException("Can't find worklist:" + worklist));
	}
	@RequestMapping(value="/worklists", method= RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public synchronized Worklist create() {
		return svc.create();
	}
	@RequestMapping(value="/worklists/{worklist}", method= RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Worklist update(@PathVariable int worklist, @RequestBody Map<String, String> dto) {
		return svc.update(worklist, dto);
	}
	@RequestMapping(value="/worklists/{worklist}", method= RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable int worklist) {
		svc.delete(worklist);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String exception(Exception e) {
		Log.error(e.getMessage(), e);
		return e.getMessage();
	}
}
