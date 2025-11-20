package com.greencross.lims.kanban;

import com.gcgenome.lims.dto.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class CommentController {
	private final Logger Log = LoggerFactory.getLogger(getClass());
	private final CommentService svc;
	public CommentController(CommentService svc) {
		this.svc = svc;
	}
	@RequestMapping(value="/issue/{issueId}/comments", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<Comment> comments(@PathVariable String issueId) {
		return svc.list(UUID.fromString(issueId));
	}
}
