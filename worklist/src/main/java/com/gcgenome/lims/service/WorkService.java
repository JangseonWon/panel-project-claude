package com.gcgenome.lims.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcgenome.lims.dto.QueryServerside;
import com.gcgenome.lims.dto.Work;
import com.gcgenome.lims.entity.WorkRareDisease;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WorkService {
	private final WorkDAO dao;
	private final ObjectMapper om;
	public WorkService(WorkDAO dao, ObjectMapper om) {
		this.dao = dao;
		this.om = om;
	}
	@Transactional
	public Work create(int worklist, long sample, String service) {
		throw new UnsupportedOperationException();
	}
	@Transactional(readOnly = true)
	public Page<Work> list(QueryServerside query) {
		return dao.search(query).map(WorkToDTO::map);
	}
	@Transactional(readOnly = true)
	public List<Work> list(int worklist, QueryServerside query) {
		return dao.list(worklist, query).map(WorkToDTO::map).collect(Collectors.toList());
	}
	@Transactional(readOnly=true)
	public Optional<Work> get(int worklist, long sample, String service) {
		return Optional.of(new WorkRareDisease.WorkRareDiseasePK(worklist, sample, service))
					   .map(dao::find)
					   .filter(Optional::isPresent)
					   .map(Optional::get)
					   .map(WorkToDTO::map)
					   .or(Optional::empty);
	}
	@Transactional
	public void save(int worklist, long sample, String service, @NotNull Work dto) {
		WorkRareDisease.WorkRareDiseasePK pk = new WorkRareDisease.WorkRareDiseasePK(worklist, sample, service);
		WorkRareDisease entity = dao.find(pk).orElse((WorkRareDisease) new WorkRareDisease().pk(pk));
		entity.serial(dto.id());
		dao.merge(entity);
	}
	/*
	Request request = entity.request();
		Sample smp = entity.sample();
		Worklist worklist = entity.worklist();
		Work dto = new Work().id(entity.serial())
							 .createTime(LocalDateTimeToEpoch.map(entity.createTime()))
							 .values(entity.value())
							 .sample(entity.pk().sample())
							 .sampleType(smp.sampleType())
							 .remark(smp.remark())
							 .service(ServiceToDTO.map(request.service()));
		if(worklist!=null) dto.worklist(worklist.worklist()).worklistTitle(worklist.title());
		if(request.dateRequest()!=null) dto.dateRequest(LocalDateToEpoch.map(request.dateRequest()));
		if(request.dateStart()!=null) dto.dateStart(LocalDateToEpoch.map(request.dateStart()));
		if(request.dateDue()!=null) dto.dateDue(LocalDateToEpoch.map(request.dateDue()));
		if(request.tat()!=null) dto.tat(request.tat());
		Patient pat = smp.patient();
		if(pat!=null) {
			String customerName = pat.customerName2();
			if(customerName == null) customerName = pat.customerName();
			dto.patientName(pat.name())
			   .patientCode(pat.code())
			   .customerName(customerName)
			   .mrn(pat.mrn());
		}
	 */
	@Transactional
	public Work update(int worklist, long sample, String service, Map<String, String> values) {
		return Optional.of(new WorkRareDisease.WorkRareDiseasePK(worklist, sample, service))
					   .map(dao::find)
					   .filter(Optional::isPresent)
					   .map(Optional::get)
					   .map(entity->{
						   if (entity.value() == null) entity.value(new HashMap<>());
						   values.forEach((key, value) -> {
							   if("serial".equals(key)) {
								   entity.serial(value!=null?value.trim():null);
							   } else try {
								   UUID id = UUID.fromString(key);
								   if ("null".equals(value) || value == null || value.trim().isEmpty()) entity.value().remove(id);
								   else entity.value().put(id, value);
							   } catch (Exception ignore) {}
						   });
						   return entity;
					   }).map(entity -> dao.merge(entity))
					   .map(WorkToDTO::map)
					   .orElseThrow(() -> new RuntimeException("Can't find Work:" + worklist + ", " + sample + ", " + service));
	}
	@Transactional
	public void delete(int worklist, long sample, String service) {
		Optional.of(new WorkRareDisease.WorkRareDiseasePK(worklist, sample, service))
				.map(pk->dao.em().getReference(WorkRareDisease.class, pk))
				.ifPresent(dao::remove);
	}
}
