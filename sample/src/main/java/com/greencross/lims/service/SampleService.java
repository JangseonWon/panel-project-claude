package com.greencross.lims.service;

import com.greencross.lims.dto.Request;
import com.greencross.lims.dto.Sample;
import com.greencross.lims.entity.Patient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly=true)
public class SampleService {
	private final SampleDAO dao;
	public SampleService(SampleDAO dao) {
		this.dao = dao;
	}
	public Optional<Sample> get(long id) {
		return dao.find(id).map(SampleToDTO::map);
	}
	public List<Request> request(long id) {
		return dao.find(id)
				  .map(com.greencross.lims.entity.Sample::requests)
				  .orElseThrow(()->new RuntimeException("Can't find sample:" + id))
				  .stream().filter(r->!r.deleted() && !r.canceled()).map(RequestToDTO::map)
				  .collect(Collectors.toList());
	}
	public List<Sample> siblings(long id) {
		return dao.find(id).map(com.greencross.lims.entity.Sample::patient)
				  .map(Patient::samples)
				  .map(list->list.stream().map(SampleToDTO::map).collect(Collectors.toList()))
				  .orElse(List.of());
	}
}
