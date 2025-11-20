package com.gcgenome.lims.service;

import com.gcgenome.lims.dto.QueryServerside;
import com.gcgenome.lims.dto.Sheet;
import com.gcgenome.lims.dto.Worklist;
import com.gcgenome.lims.entity.WorklistRareDisease;
import com.gcgenome.lims.trans.SheetToDTO;
import com.gcgenome.lims.trans.WorklistToDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class WorklistService {
	private UUID sheetId = UUID.fromString("5fd630a8-f9fc-4a17-872a-5b3e82929d6a");
	private final WorklistDAO dao;

	public WorklistService(WorklistDAO dao) {
		this.dao = dao;
	}

	@Transactional(readOnly = true)
	public Sheet template() {
		return SheetToDTO.map(dao.em().find(com.gcgenome.lims.entity.WorklistTemplate.class, sheetId));
	}

	@Transactional
	public synchronized Worklist create() {
		int max = dao.findLastIndex();
		return WorklistToDTO.map(dao.merge(new WorklistRareDisease().worklist(max+1)));
	}

	@Transactional(readOnly = true)
	public Page<Worklist> list(QueryServerside query) {
		return dao.search(query).map(WorklistToDTO::map);
	}

	@Transactional(readOnly = true)
	public Optional<Worklist> get(int worklist) {
		return dao.find(new WorklistRareDisease.WorklistRareDiseasePK(worklist)).map(WorklistToDTO::map);
	}

	@Transactional
	public Worklist update(int worklist, Map<String, String> values) {
		return dao.find(new WorklistRareDisease.WorklistRareDiseasePK(worklist))
				  .map(entity -> {
					  if (entity.value() == null) entity.value(new HashMap<>());
					  values.forEach((key, value) -> {
						  if("title".equals(key)) entity.title(value!=null?value.trim():null);
						  else if("state".equals(key)) entity.state(com.gcgenome.lims.entity.Worklist.WorklistState.valueOf(value.trim()));
						  else try {
							  UUID id = UUID.fromString(key);
							  if ("null".equals(value) || value == null || value.trim().isEmpty()) entity.value().remove(id);
							  else entity.value().put(id, value);
						  } catch (Exception ignore) {}
					  });
					  return entity;
				  }).map(entity -> dao.merge(entity))
				  .map(WorklistToDTO::map)
				  .orElseThrow(() -> new RuntimeException("Can't find Worklist:" + worklist));
	}

	@Transactional
	public void delete(int worklist) {
		dao.remove(dao.em().getReference(WorklistRareDisease.class, new WorklistRareDisease.WorklistRareDiseasePK(worklist)));
	}
}
