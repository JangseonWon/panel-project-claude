package com.greencross.lims.worklist;

import com.greencross.lims.dto.Query;
import com.greencross.lims.dto.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
public class RequestController {
	private final Logger Log = LoggerFactory.getLogger(getClass());
	private final RequestService svc;
	public RequestController(RequestService svc) {
		this.svc = svc;
	}
	@RequestMapping(value="/requests",  method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Page<Request> search(@RequestBody Query query) {
		return svc.list(query);
	}
	@Retryable(value= SQLException.class, maxAttempts = 20, backoff = @Backoff(delay=100))
	@RequestMapping(value="/sample/{sample}/service/{service}",  method= RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public synchronized Request reception(@PathVariable long sample, @PathVariable String service) {
		return svc.reception(sample, service);
	}
}
