package com.greencross.lims.kanban;

import com.gcgenome.lims.dto.Issue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class IssueController {
	private final Logger Log = LoggerFactory.getLogger(getClass());
	private final IssueService svc;
	public IssueController(IssueService svc) {
		this.svc = svc;
	}
	@RequestMapping(value="/issues", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<Issue> issues() {
		return svc.listActivated();
	}

	@RequestMapping(value="/issue/{sample}/{service}", method= RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void discard(@PathVariable long sample, @PathVariable String service) {
		svc.discard(sample, service);
	}
}
