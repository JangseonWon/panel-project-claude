package com.greencross.lims.sample;

import com.greencross.lims.dto.Analysis;
import com.greencross.lims.dto.Work;
import com.greencross.lims.entity.RequestRareDisease;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.trans.AnalysisToDTO;
import com.greencross.lims.worklist.WorkToDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service("RequestServiceSample")
@Transactional(readOnly = true)
public class RequestService {
	private final RequestDAO dao;
	public RequestService(RequestDAO dao) {
		this.dao = dao;
	}
	public List<Work> works(long sample, String service) {
		RequestRareDisease entity = dao.find(com.greencross.lims.entity.Request.RequestPK.builder().sample(sample).service(service).build())
									   .orElseThrow(()->new RuntimeException("Can't find Request:" + sample + "/" + service));
		return entity.works().stream().map(WorkToDTO::map).collect(Collectors.toList());
	}
	public List<Analysis> dna(long sample) {
		return dao.em().getReference(Sample.class, sample).dna().stream().map(AnalysisToDTO::map).collect(Collectors.toList());
	}
	public List<Analysis> library(long sample) {
		return dao.em().getReference(Sample.class, sample).library().stream().map(AnalysisToDTO::map).collect(Collectors.toList());
	}
	public List<Analysis> sequencing(long sample) {
		return dao.em().getReference(Sample.class, sample).sequencing().stream().map(AnalysisToDTO::map).collect(Collectors.toList());
	}
}
