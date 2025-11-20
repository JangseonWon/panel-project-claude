package com.greencross.lims.dna;

import com.greencross.lims.dto.Batch;
import com.greencross.lims.dto.Query;
import com.gcgenome.lims.dto.RequestReference;
import com.greencross.lims.entity.AnalysisDna;
import com.greencross.lims.entity.BatchDna;
import com.greencross.lims.trans.BatchToDTO;
import com.greencross.lims.trans.LocalDateToEpoch;
import com.greencross.lims.util.AnalysisUtil;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class DnaBatchService {
	private final BatchDnaDAO dao;
	public DnaBatchService(BatchDnaDAO dao) {
		this.dao = dao;
	}
	@Transactional(readOnly = true)
	public Page<Batch> list(Query query) {
		return dao.search(query).map(BatchToDTO::map);
	}
	@Transactional(readOnly=true)
	public Optional<Batch> get(int batch) {
		return dao.find(BatchDna.BatchDnaPK.builder().batch(batch).build()).map(BatchToDTO::map);
	}
	@Transactional
	public Batch update(int batch, Map<String, String> values) {
		return dao.find(BatchDna.BatchDnaPK.builder().batch(batch).build())
				  .map(entity -> {
					  if (entity.value() == null) entity.value(new HashMap<>());
					  values.forEach((key, value) -> {
						  if("title".equals(key)) entity.title(value!=null?value.trim():null);
						  else try {
							  UUID id = UUID.fromString(key);
							  if ("null".equals(value) || value == null || value.trim().isEmpty()) entity.value().remove(id);
							  else entity.value().put(id, value);
						  } catch (Exception ignore) {}
					  });
					  return entity;
				  }).map(entity -> dao.merge(entity))
				  .map(BatchToDTO::map)
				  .orElseThrow(() -> new RuntimeException("Can't find BatchDna:" + batch));
	}
	@Transactional
	public void delete(int batch) {
		dao.remove(dao.em().getReference(BatchDna.class, BatchDna.BatchDnaPK.builder().batch(batch).build()));
	}
	@Transactional(readOnly = true)
	public int findLastBatch() {
		return dao.findLastBatch().orElse(0);
	}
	@Transactional
	public com.greencross.lims.entity.Batch create(int batch, @Nullable List<RequestReference> targets) {
		com.greencross.lims.entity.Batch<?> entity = dao.merge(new BatchDna().batch(batch).pk(BatchDna.BatchDnaPK.builder().batch(batch).build()).title("Untitled"));
		if(targets!=null) {
			List<AnalysisDna> analysis = targets.stream().map(ref -> {
				com.greencross.lims.entity.Request.RequestPK pk = com.greencross.lims.entity.Request.RequestPK.builder().sample(ref.sample()).service(ref.service()).build();
				com.greencross.lims.entity.Request req = dao.em().getReference(com.greencross.lims.entity.Request.class, pk);
				AnalysisDna child = new AnalysisDna(AnalysisDna.AnalysisDnaPK.builder().batch(entity.batch()).row(ref.row()).build());
				child.serial(ref.serial()).row(ref.row()).sort(String.format("%04d", ref.row()*5))
						.sample(req.sample()).service(req.service())
						.request(req).requests().add(req);
				return child;
			}).toList();
			distinct(analysis).stream().peek(this::assign).forEach(dao.em()::merge);
		}
		return entity;
	}
	private List<AnalysisDna> distinct(List<AnalysisDna> list) {
		Map<String, List<AnalysisDna>> groupBy = list.stream().collect(Collectors.groupingBy(this::patientId, toSortedList(Comparator.comparing(AnalysisDna::row))));
		Comparator<AnalysisDna> comp = (a, b)->{
			String sortA = a.sort(), sortB = b.sort();
			if(sortA!=null && sortB!=null && !sortA.equals(sortB)) return sortA.compareTo(sortB);
			String serialA = a.serial(), serialB = b.serial();
			if(serialA!=null && serialB!=null && !serialA.equals(serialB)) return serialA.compareTo(serialB);
			int rowA = a.row(), rowB = b.row();
			return Integer.compare(rowA, rowB);
		};
		AtomicInteger row = new AtomicInteger(1);
		return groupBy.values().stream()
				.map(collection->{
					AnalysisDna first = collection.get(0);
					collection.stream().skip(1).forEach(other->{
						first.requests().addAll(other.requests());
					});
					return first;
				}).sorted(comp)
				.peek(a->a.sort(String.format("%04d", row.getAndIncrement()*5)))
				.collect(Collectors.toList());
	}
	private String patientId(AnalysisDna analysis) {
		if(analysis.sample() == null) return analysis.pk().batch() + "-" + analysis.pk().row();
		if(analysis.sample().patient() == null) return String.valueOf(analysis.sample().id());
		return analysis.sample().patient().id();
	}
	private <T> Collector<T,?,List<T>> toSortedList(Comparator<? super T> c) {
		return Collectors.collectingAndThen(Collectors.toCollection(LinkedList::new), l->l.stream().sorted(c).collect(Collectors.toList()));
	}
	@Transactional
	public void mergeChildren(List<Integer> targets) {
		BatchDna merged = targets.stream()
								 .map(batch->dao.find(BatchDna.BatchDnaPK.builder().batch(batch).build())).filter(Optional::isPresent).map(Optional::get)
								 .reduce((batch1, batch2) -> {
									 BatchDna primary = batch1.batch() > batch2.batch() ? batch2 : batch1;
									 BatchDna secondary = batch1.batch() > batch2.batch() ? batch1 : batch2;
									 secondary.analysis().forEach(primary.analysis()::add);
									 return primary;
								 }).get();
		Comparator<AnalysisDna> comp = (a, b)->{
			String serialA = a.serial(), serialB = b.serial();
			if(serialA!=null && serialB!=null && !serialA.equals(serialB)) return serialA.compareTo(serialB);
			int rowA = a.row(), rowB = b.row();
			return Integer.compare(rowA, rowB);
		};
		AnalysisUtil.rebuild(dao.em(), merged.analysis().stream().sorted(comp).collect(Collectors.toList()), merged);
		dao.em().clear();
	}
	@Transactional
	public Batch mergeParent(List<Integer> targets) {
		BatchDna merged = targets.stream()
				.map(batch->dao.find(BatchDna.BatchDnaPK.builder().batch(batch).build())).filter(Optional::isPresent).map(Optional::get)
				.reduce((batch1, batch2) -> {
					BatchDna primary = batch1.batch() > batch2.batch() ? batch2 : batch1;
					BatchDna secondary = batch1.batch() > batch2.batch() ? batch1 : batch2;
					primary.title(new StringBuilder(primary.title()).append("_").append(secondary.title()).toString());
					return primary;
				}).get();
		targets.stream().filter(batch->!merged.batch().equals(batch)).map(batch->dao.em().getReference(BatchDna.class, BatchDna.BatchDnaPK.builder().batch(batch).build())).forEach(dao.em()::remove);
		return BatchToDTO.map(dao.merge(merged));
	}
	@Transactional
	public void rebuild(int batch) {
		dao.find(BatchDna.BatchDnaPK.builder().batch(batch).build())
				.ifPresent(entity->AnalysisUtil.rebuild(dao.em(), entity.analysis().stream().sorted().collect(Collectors.toList()), entity));
	}
	private static final UUID REQUEST_NAME = UUID.fromString("b465554c-57de-4441-bf46-011e025027d3");
	private static final UUID REQUEST_ID = UUID.fromString("1bad34dc-6cfd-441e-8aef-de757bc6d471");
	private static final UUID PATIENT_NAME = UUID.fromString("5e5e7085-c856-4daa-a866-76ec1218eacf");
	private static final UUID CUSTOMER_NAME = UUID.fromString("da308c3a-d46f-4b8f-9a71-22d791b3e1b8");
	private static final UUID TAT = UUID.fromString("c10ee1e8-7ad8-4686-9dbd-1f8f88ebc2ef");
	private void assign(com.greencross.lims.entity.Analysis<?> analysis) {
		String reqName = analysis.requests().stream().map(req->req.service().name()).collect(Collectors.joining(", "));
		com.greencross.lims.entity.Request req = analysis.requests().stream().findFirst().get();
		String requestId = formatSampleId(req.sample().id());
		String patientName = req.sample().patient().name();
		String customerName = req.sample().patient().customerName();
		if(req.sample().patient().customerName2()!=null && !req.sample().patient().customerName2().trim().isEmpty()) customerName = "(" + customerName + ") " + req.sample().patient().customerName2();
		analysis.value().put(REQUEST_NAME, reqName);
		analysis.value().put(REQUEST_ID, requestId);
		analysis.value().put(PATIENT_NAME, patientName);
		analysis.value().put(CUSTOMER_NAME, customerName);
		analysis.requests().stream().map(com.greencross.lims.entity.Request::dateDue).filter(Objects::nonNull)
				.mapToLong(LocalDateToEpoch::map).min().ifPresent(date->{
			analysis.value().put(TAT, String.valueOf(date));
		});
	}
	public String formatSampleId(Long id) {
		if (id == null) {
			return null;
		} else {
			String cast = String.valueOf(id);
			if (cast.length() == 15) {
				String var10000 = cast.substring(0, 8);
				return var10000 + "-" + cast.substring(8, 11) + "-" + cast.substring(11);
			} else {
				return cast;
			}
		}
	}
}
