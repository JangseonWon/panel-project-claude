package com.gcgenome.lims.service.consensualclass;

import com.gcgenome.lims.dto.SnvConsensualClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class SnvConsensualClassController {
	private final Logger Log = LoggerFactory.getLogger(getClass());
	private final SnvConsensualClassService svc;
	public SnvConsensualClassController(SnvConsensualClassService svc) {
		this.svc = svc;
	}
	@GetMapping(value="/snvs/{id}/consensual-class", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public SnvConsensualClass get(@PathVariable String id) {
		return svc.get(id);
	}
	@PutMapping(value="/snvs/{id}/consensual-class/{clazz}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public SnvConsensualClass put(@PathVariable String id, @PathVariable String clazz, @RequestBody String comment) {
		svc.save(id, clazz, comment);
		return svc.get(id);
	}
	@PatchMapping(value="/snvs/{id}/consensual-class", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public SnvConsensualClass update(@PathVariable String id, @RequestBody String comment) {
		svc.update(id, comment);
		return svc.get(id);
	}
}