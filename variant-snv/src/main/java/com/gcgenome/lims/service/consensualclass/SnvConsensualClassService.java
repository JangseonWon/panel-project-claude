package com.gcgenome.lims.service.consensualclass;

import com.gcgenome.lims.dto.SnvConsensualClass;
import com.gcgenome.lims.entity.SnvConsensualClass.SnvConsensualClassPK;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SnvConsensualClassService {
	private final SnvConsensualClassDao dao;
	public SnvConsensualClassService(SnvConsensualClassDao dao) {
		this.dao = dao;
	}
	@Transactional(readOnly=true)
	public SnvConsensualClass get(String id) {
		return Optional.ofNullable(dao.findLastBySnv(id)).map(SnvConsensualClassToDTO::map).orElse(null);
	}
	@Transactional
	public void save(String id, String clazz, String comment) {
		SnvConsensualClassPK pk = new SnvConsensualClassPK().snv(id);
		dao.merge(new com.gcgenome.lims.entity.SnvConsensualClass().pk(pk).classification(clazz).comment(comment));
	}
	@Transactional
	public void update(String id, String comment) {
		Optional.ofNullable(dao.findLastBySnv(id)).map(s->s.comment(comment)).ifPresent(dao::merge);
	}
}
