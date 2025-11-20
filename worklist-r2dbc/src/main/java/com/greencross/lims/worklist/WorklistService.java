package com.greencross.lims.worklist;

import com.greencross.lims.entity.Worklist;
import com.greencross.lims.repo.WorklistRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class WorklistService {
	//@Value("${worklist.id}")
	//private UUID sheetId;
	private final WorklistRepository dao;

	public WorklistService(WorklistRepository dao) {
		this.dao = dao;
	}
/*
	@Transactional(value="transactionManagerLims", readOnly = true)
	public Sheet template() {
		return SheetToDTO.map(dao.em().find(com.greencross.lims.entity.WorklistTemplate.class, sheetId));
	}*/

	@Transactional
	public Mono<Integer> create() {
		return dao.findLastIndex();
	}
	@Transactional
	public Flux<Worklist> test() {
		return dao.findAll();
	}
	/*@Transactional("transactionManagerLims")
	public synchronized Worklist create() {
		int max = dao.findLastIndex();
		return WorklistToDTO.map(dao.merge(new WorklistRareDisease().worklist(max+1)));
	}*/
/*
	@Transactional(value="transactionManagerLims", readOnly = true)
	public Page<Worklist> list(Query query) {
		return dao.search(query).map(WorklistToDTO::map);
	}

	@Transactional(value="transactionManagerLims", readOnly = true)
	public Optional<Worklist> get(int worklist) {
		return dao.find(new WorklistRareDisease.WorklistRareDiseasePK(worklist)).map(WorklistToDTO::map);
	}

	@Transactional("transactionManagerLims")
	public Worklist update(int worklist, Map<String, String> values) {
		return dao.find(new WorklistRareDisease.WorklistRareDiseasePK(worklist))
				  .map(entity -> {
					  if (entity.value() == null) entity.value(new HashMap<>());
					  values.forEach((key, value) -> {
						  if("title".equals(key)) entity.title(value!=null?value.trim():null);
						  else if("state".equals(key)) entity.state(com.greencross.lims.entity.Worklist.WorklistState.valueOf(value.trim()));
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

	@Transactional("transactionManagerLims")
	public void delete(int worklist) {
		dao.remove(dao.em().getReference(WorklistRareDisease.class, new WorklistRareDisease.WorklistRareDiseasePK(worklist)));
	}*/
}
