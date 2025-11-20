package com.gcgenome.lims.service.interpretation;

import com.gcgenome.lims.dto.InterpretationReserved;
import com.gcgenome.lims.entity.InterpretationReserved.InterpretationReservedPK;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InterpretationReservedService {
	private final InterpretationReservedDao dao;
	public InterpretationReservedService(InterpretationReservedDao dao) {
		this.dao = dao;
	}
	@Transactional(readOnly=true)
	public List<InterpretationReserved> get(String id) {
		return dao.findLastBySnv(id).map(InterpretationReservedToDTO::map).collect(Collectors.toList());
	}
	@Transactional
	public void save(String id, InterpretationReserved dto) {
		InterpretationReservedPK pk = new InterpretationReservedPK().snv(id).service(dto.service()!=null?dto.service():"*");
		dao.merge(new com.gcgenome.lims.entity.InterpretationReserved().pk(pk).interpretation(dto.interpretation()));
	}
	@Transactional
	public void delete(String id, String service) {
		dao.delete(id, service);
	}
}
