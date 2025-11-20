package com.greencross.lims.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greencross.lims.dto.QueryServerside;
import com.greencross.lims.entity.Analysis;
import com.greencross.lims.entity.InterpretationReserved;
import com.greencross.lims.entity.Snv;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SnvService {
	private final SnvDAO dao;
	private final SnvReportedDAO dao2;
	private final ObjectMapper om;
	@PersistenceContext
	private EntityManager em;
	public SnvService(SnvDAO dao, SnvReportedDAO dao2, ObjectMapper om) {
		this.dao = dao;
		this.dao2 = dao2;
		this.om = om;
	}

	@Transactional(readOnly = true)
	public List<Map<String, Object>> snvs(long sample, String service) {
		return dao2.findSnvByRequest(sample, service).map(snv->{
			String batch = snv.pk().snv().split(":", -1)[1];
			Map<String, Object> var = dao.find(batch, snv.pk().snv(), Map.class);
			try {
				String id = (String) var.get("snv");
				List<Snv> proven = em.createQuery("SELECT s FROM Snv s WHERE snv LIKE :snv", Snv.class)
									 .setParameter("snv", "%" + id).getResultList();
				String reported = om.writeValueAsString(proven.stream().map(s->s.pk().sample() + ":" + s.pk().service() + "=" + s.classification()).collect(Collectors.toList()));
				var.put("reported", reported);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			// Fill Interpretation reserved
			String id = (String) var.get("snv");
			List<InterpretationReserved> interpretations = em.createNativeQuery(
					"(SELECT * FROM interpretation_reserved WHERE snv='" + id + "' AND service='\" + service + \"' ORDER BY create_at DESC LIMIT 1) " +
					"UNION (SELECT * FROM interpretation_reserved WHERE snv='" + id + "' AND service='*' ORDER BY create_at DESC LIMIT 1) ", InterpretationReserved.class
			).getResultList();
			var exactMatchByService = interpretations.stream().filter(s->s.pk().service().equals(service)).findFirst();
			if(exactMatchByService.isPresent()) var.put("interpretation", exactMatchByService.get().interpretation());
			else if(interpretations.size()>0) var.put("interpretation", interpretations.stream().findFirst().get().interpretation());
			return SnvToDTO.map(snv, var);
		}).collect(Collectors.toList());
	}
	@Transactional
	public void create(long sample, String service, String snv, String classification) {
		Snv.SnvPK pk = Snv.SnvPK.builder().sample(sample).service(service).snv(snv).build();
		if(classification!=null && !"".equalsIgnoreCase(classification)) {
			Snv entity = dao2.find(pk).orElse(new Snv().pk(pk));
			dao2.merge(entity.classification(classification));
		} else if(dao2.exists(pk)) dao2.remove(dao2.em().getReference(Snv.class, pk));
	}
	@Transactional
	public void delete(long sample, String service, String snv) {
		Snv.SnvPK pk = Snv.SnvPK.builder().sample(sample).service(service).snv(snv).build();
		if(dao2.exists(pk)) dao2.remove(dao2.em().getReference(Snv.class, pk));
	}
	public Page<Document> snvs(long sample, String service, String batch, int row, QueryServerside query) {
		String request = sample + ":" + service;
		return (Page<Document>) em.createQuery("SELECT e FROM Analysis e WHERE batch=:batch AND row=:row AND request=:request")
								.setParameter("batch", batch).setParameter("row", row).setParameter("request", request)
								.getResultList().stream()
								.map(analysis-> dao.search(((Analysis)analysis).pk(), query)
												   .map(h->h.getContent())
												   .map(document -> {
													   try {
														   String snv = document.getString("snv");
														   List<Snv> proven = em.createQuery("SELECT s FROM Snv s WHERE snv LIKE :snv", Snv.class)
																				.setParameter("snv", "%" + snv).getResultList();
														   String reported = om.writeValueAsString(proven.stream().map(s->s.pk().sample() + ":" + s.pk().service() + "=" + s.classification()).collect(Collectors.toList()));
														   document.append("reported", reported);
													   } catch (JsonProcessingException e) {
														   e.printStackTrace();
													   }
												   	return document;
												   }))
								.findFirst()
								.orElse(Page.empty());

	}
}
