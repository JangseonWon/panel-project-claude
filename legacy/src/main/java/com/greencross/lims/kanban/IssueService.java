package com.greencross.lims.kanban;

import com.gcgenome.lims.dto.Issue;
import com.greencross.lims.entity.Request;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IssueService {
	private final IssueDAO dao;
	public IssueService(IssueDAO dao) {
		this.dao = dao;
	}
	@Transactional(readOnly = true)
	public List<Issue> listActivated() {
		return dao.findByActivated(true).map(IssueToDTO::map).collect(Collectors.toList());
	}
	@Transactional
	public void discard(long sample, String service) {
		dao.find(Request.RequestPK.builder().sample(sample).service(service).build())
		   .map(issue->issue.activated(false))
		   .ifPresent(dao::merge);
	}
}
