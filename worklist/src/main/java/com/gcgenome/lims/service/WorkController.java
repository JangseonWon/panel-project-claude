package com.gcgenome.lims.service;

import com.gcgenome.lims.dto.QueryServerside;
import com.gcgenome.lims.dto.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class WorkController {
	private final Logger Log = LoggerFactory.getLogger(getClass());
	private final WorkService svc;

	public WorkController(WorkService svc) {
		this.svc = svc;
	}
	@RequestMapping(value="/works",  method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Page<Work> search(@RequestBody QueryServerside query) {
		return svc.list(query);
	}
	@RequestMapping(value="/worklists/{worklist}/works", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<Work> Works(@PathVariable int worklist, @RequestBody QueryServerside query) {
		return svc.list(worklist, query);
	}
	@RequestMapping(value="/worklists/{worklist}/works/{sample}/{service}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Work get(@PathVariable int worklist, @PathVariable long sample, @PathVariable String service) {
		return svc.get(worklist, sample, service).orElseThrow(()->new RuntimeException("Can't find Work:" + worklist + ", " + sample + ", " + service));
	}
	@RequestMapping(value="/worklists/{worklist}/works/{sample}/{service}", method= RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void create(@PathVariable int worklist, @PathVariable long sample, @PathVariable String service, @RequestBody(required = false) Work dto) {
		if(dto == null) svc.create(worklist, sample, service);
		else svc.save(worklist, sample, service, dto);
	}
	@RequestMapping(value="/worklists/{worklist}/works/{sample}/{service}", method= RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Work update(@PathVariable int worklist, @PathVariable long sample, @PathVariable String service, @RequestBody Map<String, String> dto) {
		return svc.update(worklist, sample, service, dto);
	}
	@RequestMapping(value="/worklists/{worklist}/works/{sample}/{service}", method= RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable int worklist, @PathVariable long sample, @PathVariable String service) {
		svc.delete(worklist, sample, service);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String exception(Exception e) {
		Log.error(e.getMessage(), e);
		return e.getMessage();
	}
}
