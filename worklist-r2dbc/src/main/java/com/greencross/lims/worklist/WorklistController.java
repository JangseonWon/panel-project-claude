package com.greencross.lims.worklist;

import com.greencross.lims.entity.Worklist;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin
@RestController
public class WorklistController {
	private final WorklistService svc;
	public WorklistController(WorklistService svc) {
		this.svc = svc;
	}
	@RequestMapping(value="/worklist/test", method= RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Mono<Integer> template() {
		return svc.create();
	}
	@RequestMapping(value="/worklist/test2", method= RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Flux<Worklist> template2() {
		return svc.test();
	}
}
