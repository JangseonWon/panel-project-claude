package com.gcgenome.lims.service;

import com.gcgenome.lims.dto.QueryServerside;
import com.gcgenome.lims.dto.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class QueueController {
	private final Logger Log = LoggerFactory.getLogger(getClass());
	private final QueueService svc;
	public QueueController(QueueService svc) {
		this.svc = svc;
	}
	@RequestMapping(value="/queue", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Page<Work> works(@RequestBody QueryServerside query) {
		return svc.list(query);
	}
	@RequestMapping(value="/sample/{sample}/service/{service}/queue", method= RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public synchronized Work create(@PathVariable long sample, @PathVariable String service) {
		return svc.create(sample, service);
	}
	@RequestMapping(value="/queue/{serial}/consume", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Work consume(@PathVariable String serial) {
		return svc.consume(serial);
	}
	@RequestMapping(value="/queue/{serial}", method= RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable String serial) {
		svc.delete(serial);
	}
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String exception(Exception e) {
		Log.error(e.getMessage(), e);
		return e.getMessage();
	}
}
