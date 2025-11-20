package com.greencross.lims.kanban;

import com.gcgenome.lims.dto.Comment;
import com.greencross.lims.entity.Issue;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentService {
	private final CommentDAO dao;
	public CommentService(CommentDAO dao) {
		this.dao = dao;
	}
	@Transactional(readOnly = true)
	public List<Comment> list(UUID issueId) {
		Issue parent = dao.em().find(Issue.class, issueId);
		if(parent == null) return List.of();
		return dao.list(parent).map(CommentToDTO::map).collect(Collectors.toList());
	}
}
