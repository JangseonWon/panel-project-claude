package com.gcgenome.lims.service.comment;

import com.gcgenome.lims.dto.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SnvCommentController {
	private final Logger Log = LoggerFactory.getLogger(getClass());
	private final SnvCommentService svc;
	public SnvCommentController(SnvCommentService svc) {
		this.svc = svc;
	}
	@GetMapping(value="/snvs/{id}/comment", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<Comment> get(@PathVariable String id) {
		return svc.findAll(id);
	}
	@PutMapping(value="/snvs/{id}/comment", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Comment put(@PathVariable String id, @RequestBody String comment) {
		return svc.save(id, comment);
	}
	@PatchMapping(value="/snvs/{id}/comment/{create-at}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Comment update(@PathVariable String id, @PathVariable("create-at") long createAt, @RequestBody String comment) {
		return svc.update(id, createAt, comment);
	}
	@DeleteMapping(value="/snvs/{id}/comment/{create-at}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable String id, @PathVariable("create-at") long createAt) {
		svc.delete(id, createAt);
	}
}