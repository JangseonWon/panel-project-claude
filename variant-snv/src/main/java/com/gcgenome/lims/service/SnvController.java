package com.gcgenome.lims.service;

import com.gcgenome.lims.dto.QueryServerside;
import com.gcgenome.lims.dto.RequestSnv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SnvController {
	private final Logger Log = LoggerFactory.getLogger(getClass());
	private final SnvService svc;
	public SnvController(SnvService svc) {
		this.svc = svc;
	}
	@GetMapping(value="/snvs/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Object get(@PathVariable String id) {
		return svc.get(id).orElse(null);
	}
	@PostMapping(value="/snvs/{id}/samples", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<RequestSnv> samples(@PathVariable String id, @RequestBody QueryServerside query, ServerHttpResponse response) {
		Page<RequestSnv> page = svc.samples(id, query);
		HttpHeaders headers = response.getHeaders();
		headers.add("X-Total-Count", String.valueOf(page.getTotalElements()));
		headers.add("X-Total-Page", String.valueOf(page.getTotalPages()));
		headers.add("X-Current-Page", String.valueOf(page.getPageable().getPageNumber()));
		return page.getContent();
	}
	@PostMapping(value="/snvs/{id}/requests", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<Object> requests(@PathVariable String id, @RequestBody QueryServerside query, ServerHttpResponse response) {
		Page<Object> page = svc.requests(id, query);
		HttpHeaders headers = response.getHeaders();
		headers.add("X-Total-Count", String.valueOf(page.getTotalElements()));
		headers.add("X-Total-Page", String.valueOf(page.getTotalPages()));
		headers.add("X-Current-Page", String.valueOf(page.getPageable().getPageNumber()));
		return page.getContent();
	}
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String exception(Exception e) {
		Log.error(e.getMessage(), e);
		return e.getMessage();
	}
}
