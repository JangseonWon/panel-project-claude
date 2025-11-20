package com.greencross.lims.dna;

import com.greencross.lims.dto.Analysis;
import com.greencross.lims.dto.Query;
import com.gcgenome.lims.dto.RequestReference;
import com.greencross.lims.entity.AnalysisDna;
import com.greencross.lims.entity.BatchDna;
import com.greencross.lims.trans.AnalysisToDTO;
import com.greencross.lims.util.AnalysisUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DnaAnalysisService {
	private final AnalysisDnaDAO dao;
	public DnaAnalysisService(AnalysisDnaDAO dao) {
		this.dao = dao;
	}
	@Transactional(readOnly = true)
	public Page<Analysis> list(Query query) {
		return dao.search(query).map(AnalysisToDTO::map);
	}
	@Transactional(readOnly = true)
	public List<Analysis> list(int batch, Query query) {
		return dao.list(batch, query).map(AnalysisToDTO::map).collect(Collectors.toList());
	}
	@Transactional(readOnly=true)
	public Optional<Analysis> get(int batch, int row) {
		return dao.find(AnalysisDna.AnalysisDnaPK.builder().batch(batch).row(row).build()).map(AnalysisToDTO::map);
	}
	@Transactional(readOnly = true)
	public int findLastRow(int batch) {
		BatchDna parent = dao.em().getReference(BatchDna.class, BatchDna.BatchDnaPK.builder().batch(batch).build());
		return dao.findLastRow(parent).orElse(0);
	}
	@Transactional
	public AnalysisDna create(int batch, int row, RequestReference target) {
		AnalysisDna entity = new AnalysisDna(AnalysisDna.AnalysisDnaPK.builder().batch(batch).row(row).build());
		entity.batch(dao.em().getReference(BatchDna.class, BatchDna.BatchDnaPK.builder().batch(batch).build())).row(entity.pk().row());
		if(target!=null) {
			com.greencross.lims.entity.Request.RequestPK pk = com.greencross.lims.entity.Request.RequestPK.builder().sample(target.sample()).service(target.service()).build();
			com.greencross.lims.entity.Request req = dao.em().getReference(com.greencross.lims.entity.Request.class, pk);
			entity.serial(target.serial()).request(req).requests().add(req);
		}
		return dao.merge(entity);
	}
	@Transactional
	public Analysis update(int batch, int row, Map<String, String> values) {
		return dao.find(AnalysisDna.AnalysisDnaPK.builder().batch(batch).row(row).build())
				.map(entity -> {
					if (entity.value() == null) entity.value(new HashMap<>());
					values.forEach((key, value) -> {
						if("sort".equals(key)) {
							entity.sort(value!=null?value.trim():null);
						} else if("serial".equals(key)) {
							entity.serial(value!=null?value.trim():null);
						} else try {
							UUID id = UUID.fromString(key);
							if ("null".equals(value) || value == null || value.trim().isEmpty()) entity.value().remove(id);
							else entity.value().put(id, value);
						} catch (Exception ignore) {}
					});
					return entity;
				}).map(entity -> dao.merge(entity))
				.map(AnalysisToDTO::map)
				.orElseThrow(() -> new RuntimeException("Can't find AnalysisDna:" + batch + ", " + row));
	}
	@Transactional
	public void delete(int batch, int row) {
		dao.remove(dao.em().getReference(AnalysisDna.class, AnalysisDna.AnalysisDnaPK.builder().batch(batch).row(row).build()));
	}
	@Transactional
	public void rebuild(int batch) {
		BatchDna parent = dao.em().getReference(BatchDna.class, BatchDna.BatchDnaPK.builder().batch(batch).build());
		AnalysisUtil.rebuild(dao.em(), parent.analysis().stream().sorted().collect(Collectors.toList()), parent);
	}
}
