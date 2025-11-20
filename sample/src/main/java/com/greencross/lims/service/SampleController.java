package com.greencross.lims.service;

import com.greencross.lims.dto.Request;
import com.greencross.lims.dto.Sample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SampleController {
	private final Logger Log = LoggerFactory.getLogger(getClass());
	private final SampleService svc;
	public SampleController(SampleService svc) {
		this.svc = svc;
	}
	@GetMapping(value="/samples/{id:[0-9-]+}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Sample get(@PathVariable String id) {
		return svc.get(Long.parseLong(id.replace("-", ""))).orElseThrow(()->new RuntimeException("Can't find sample:" + id));
	}
	@GetMapping(value="/samples/{id:[0-9-]+}/requests", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<Request> requests(@PathVariable String id) {
		return svc.request(Long.parseLong(id.replace("-", "")));
	}

	@GetMapping(value="/samples/{id:[0-9-]+}/siblings", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<Sample> siblings(@PathVariable String id) {
		return svc.siblings(Long.parseLong(id.replace("-", "")));
	}
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String exception(Exception e) {
		Log.error(e.getMessage(), e);
		return e.getMessage();
	}
}
