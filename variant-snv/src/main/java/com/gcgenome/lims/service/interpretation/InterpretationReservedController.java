package com.gcgenome.lims.service.interpretation;

import com.gcgenome.lims.dto.InterpretationReserved;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InterpretationReservedController {
	private final Logger Log = LoggerFactory.getLogger(getClass());
	private final InterpretationReservedService svc;
	public InterpretationReservedController(InterpretationReservedService svc) {
		this.svc = svc;
	}
	@GetMapping(value="/snvs/{id}/interpretation-reserved", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<InterpretationReserved> get(@PathVariable String id) {
		return svc.get(id);
	}
	@PutMapping(value="/snvs/{id}/interpretation-reserved", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void put(@PathVariable String id, @RequestBody InterpretationReserved dto) {
		svc.save(id, dto);
	}
	@DeleteMapping(value="/snvs/{id}/interpretation-reserved/{service}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable String id, @PathVariable String service) {
		svc.delete(id, service);
	}
}