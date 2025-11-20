package com.greencross.lims.service;

import com.greencross.lims.dto.QueryServerside;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class SnvController {
	private final SnvService svc;
	public SnvController(SnvService svc) {
		this.svc = svc;
	}

	@RequestMapping(value="/samples/{sample}/services/{service}/snvs",  method= RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public List<Map<String, Object>> snvsReported(@PathVariable long sample, @PathVariable String service) {
		return svc.snvs(sample, service);
	}
	@RequestMapping(value="/samples/{sample}/services/{service}/batches/{batch}/{row}/snvs",  method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Page<Document> snvs(@PathVariable long sample, @PathVariable String service, @PathVariable String batch, @PathVariable int row, @RequestBody QueryServerside query) {
		return svc.snvs(sample, service, batch, row, query);
	}
	@RequestMapping(value="/samples/{sample}/services/{service}/snvs/{variant}/{class}",  method= RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void create(@PathVariable long sample, @PathVariable String service, @PathVariable String variant, @PathVariable("class") String classification) {
		svc.create(sample, service, variant, classification);
	}
	@RequestMapping(value="/samples/{sample}/services/{service}/snvs/{variant}",  method= RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable long sample, @PathVariable String service, @PathVariable String variant) {
		svc.delete(sample, service, variant);
	}
}
