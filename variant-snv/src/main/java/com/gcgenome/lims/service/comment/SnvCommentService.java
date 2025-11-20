package com.gcgenome.lims.service.comment;

import com.gcgenome.lims.dto.Comment;
import com.gcgenome.lims.entity.SnvComment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SnvCommentService {
	private final SnvCommentDao dao;
	public SnvCommentService(SnvCommentDao dao) {
		this.dao = dao;
	}
	@Transactional(readOnly=true)
	public List<Comment> findAll(String id) {
		return dao.findBySnv(id).map(SnvCommentToDTO::map).collect(Collectors.toList());
	}
	@Transactional
	public Comment save(String id, String comment) {
		SnvComment.SnvCommentPK pk = new SnvComment.SnvCommentPK().snv(id);
		return SnvCommentToDTO.map(dao.merge(new SnvComment().pk(pk).comment(comment)));
	}
	@Transactional
	public Comment update(String id, long createAt, String comment) {
		SnvComment.SnvCommentPK pk = new SnvComment.SnvCommentPK().snv(id).createTime(createAt);
		return dao.find(pk)
				  .map(s->s.comment(comment))
				  .map(dao::merge)
				  .map(SnvCommentToDTO::map)
				  .orElse(null);
	}
	@Transactional
	public void delete(String id, long createAt) {
		SnvComment.SnvCommentPK pk = new SnvComment.SnvCommentPK().snv(id).createTime(createAt);
		System.out.println(dao.find(pk).get());
		dao.find(pk).ifPresent(dao::remove);
	}
}
