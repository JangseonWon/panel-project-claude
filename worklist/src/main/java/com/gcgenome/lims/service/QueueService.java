package com.gcgenome.lims.service;

import com.gcgenome.lims.dto.QueryServerside;
import com.gcgenome.lims.dto.Work;
import com.gcgenome.lims.entity.QueueRareDisease;
import com.gcgenome.lims.entity.Request;
import com.gcgenome.lims.entity.WorkRedo;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class QueueService {
	private static final Pattern REDO_PATTERN = Pattern.compile("^(.+)-R(\\d*)$");
	private final QueueDAO dao;
	public QueueService(QueueDAO dao) {
		this.dao = dao;
	}
	@Transactional
	public Work create(long sample, String service) {
		Request request = dao.em().find(Request.class, Request.RequestPK.builder().sample(sample).service(service).build());
		String serial = request.redos().stream()
							   .map(WorkRedo::pk).map(WorkRedo.QueueItemPK::serial)
							   .map(REDO_PATTERN::matcher).filter(Matcher::find)
							   .max(Comparator.comparing(m->toInteger(m.groupCount()>=3?m.group(3):null)))
							   .map(m->m.group(1) + "-R" + (toInteger(m.groupCount()>=3?m.group(3):null)+1))
							   .orElseGet(()->request.works().get(0).serial() + "-R");

		return Optional.of(new QueueRareDisease.QueueRareDiseasePK(serial))
					   .map(new QueueRareDisease()::pk)
					   .map(entity->entity.request(request))
					   .map(dao::merge).map(QueueToDTO::map)
					   .get();
	}
	private int toInteger(String s) {
		if(s == null || s.isBlank()) return 1;
		return Integer.parseInt(s);
	}
	@Transactional(readOnly = true)
	public Page<Work> list(QueryServerside query) {
		return dao.search(query).map(QueueToDTO::map);
	}
	@Transactional
	public Work consume(String serial) {
		return dao.find(new QueueRareDisease.QueueRareDiseasePK(serial))
				  .map(entity->entity.consumed(true))
				  .map(dao::merge)
				  .map(QueueToDTO::map)
				  .orElseThrow(() -> new RuntimeException("Can't find Queue Item:" + serial));
	}
	@Transactional
	public void delete(String serial) {
		dao.remove(dao.em().getReference(QueueRareDisease.class, new QueueRareDisease.QueueRareDiseasePK(serial)));
	}
}
