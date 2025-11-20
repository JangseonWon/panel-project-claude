package com.greencross.lims.publish;

import com.greencross.lims.entity.UserActivated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
public class PublishController {
	private final Logger Log = LoggerFactory.getLogger(getClass());
	private final PublishService svc;
	public PublishController(PublishService svc) {
		this.svc = svc;
	}
	@RequestMapping(value="/samples/{sample}/services/{service}/reports/{createAt}/publish",  method= RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public Mono<Void> publish(@AuthenticationPrincipal UserActivated user, @PathVariable long sample, @PathVariable String service, @PathVariable long createAt) throws IOException {
		svc.publish(user, sample, service, createAt);
		return Mono.empty();
	}
	@RequestMapping(value="/samples/{sample}/services/{service}/state",  method= RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Mono<String> state(@AuthenticationPrincipal UserActivated user, @PathVariable long sample, @PathVariable String service) {
		return Mono.just(svc.state(sample, service));
	}
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String exception(Exception e) {
		Log.error(e.getMessage(), e);
		return e.getMessage();
	}
}
