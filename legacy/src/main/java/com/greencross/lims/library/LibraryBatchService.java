package com.greencross.lims.library;

import com.greencross.lims.dto.Batch;
import com.gcgenome.lims.dto.BatchReference;
import com.greencross.lims.dto.Query;
import com.greencross.lims.entity.AnalysisLibrary;
import com.greencross.lims.entity.BatchLibrary;
import com.greencross.lims.trans.BatchToDTO;
import com.greencross.lims.trans.LocalDateToEpoch;
import com.greencross.lims.util.AnalysisUtil;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LibraryBatchService {
	private final BatchLibraryDAO dao;
	public LibraryBatchService(BatchLibraryDAO dao) {
		this.dao = dao;
	}
	@Transactional(readOnly = true)
	public Page<Batch> list(Query query) {
		return dao.search(query).map(BatchToDTO::map);
	}
	@Transactional(readOnly=true)
	public Optional<Batch> get(int batch) {
		return dao.find(BatchLibrary.BatchLibraryPK.builder().batch(batch).build()).map(BatchToDTO::map);
	}
	@Transactional
	public Batch update(int batch, Map<String, String> values) {
		return dao.find(BatchLibrary.BatchLibraryPK.builder().batch(batch).build())
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
				  .orElseThrow(() -> new RuntimeException("Can't find BatchLibrary:" + batch));
	}
	@Transactional
	public void delete(int batch) {
		dao.remove(dao.em().getReference(BatchLibrary.class, BatchLibrary.BatchLibraryPK.builder().batch(batch).build()));
	}
	@Transactional(readOnly = true)
	public int findLastBatch() {
		return dao.findLastBatch().orElse(0);
	}
	@Transactional
	public com.greencross.lims.entity.Batch<?> create(int batch, @Nullable BatchReference target) {
		if(target!=null) {
			com.greencross.lims.entity.Batch<?> prev = dao.em().getReference(com.greencross.lims.entity.Batch.class, new com.greencross.lims.entity.Batch.BatchPK().sheet(UUID.fromString(target.sheet())).batch(target.batch()));
			com.greencross.lims.entity.Batch<?> entity = dao.merge(new BatchLibrary().batch(batch).pk(BatchLibrary.BatchLibraryPK.builder().batch(batch).build()))
														   .title(prev.title()).value(prev.value());
			List<AnalysisLibrary> analysis = prev.analysis().stream().map(p -> {
				AnalysisLibrary child = new AnalysisLibrary(AnalysisLibrary.AnalysisLibraryPK.builder().batch(entity.batch()).row(p.row()).build());
				child.serial(p.serial()).row(p.row()).sort(p.sort())
						.sample(p.sample()).service(p.service())
						.requests(p.requests()).sort(p.sort()).value(p.value());
				return child;
			}).sorted().collect(Collectors.toList());
			analysis.stream().peek(this::assignValues).forEach(dao.em()::merge);
			return dao.merge(entity);
		} else return dao.merge(new BatchLibrary().batch(batch).pk(BatchLibrary.BatchLibraryPK.builder().batch(batch).build()).title("Untitled"));
	}
	@Transactional
	public void mergeChildren(List<Integer> targets) {
		BatchLibrary merged = targets.stream()
				.map(batch->dao.find(BatchLibrary.BatchLibraryPK.builder().batch(batch).build())).filter(Optional::isPresent).map(Optional::get)
				.reduce((batch1, batch2) -> {
					BatchLibrary primary = batch1.batch() > batch2.batch() ? batch2 : batch1;
					BatchLibrary secondary = batch1.batch() > batch2.batch() ? batch1 : batch2;
					secondary.analysis().forEach(primary.analysis()::add);
					return primary;
				}).get();
		Comparator<AnalysisLibrary> comp = (a, b)->{
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
		BatchLibrary merged = targets.stream()
				.map(batch->dao.find(BatchLibrary.BatchLibraryPK.builder().batch(batch).build())).filter(Optional::isPresent).map(Optional::get)
				.reduce((batch1, batch2) -> {
					BatchLibrary primary = batch1.batch() > batch2.batch() ? batch2 : batch1;
					BatchLibrary secondary = batch1.batch() > batch2.batch() ? batch1 : batch2;
					primary.title(new StringBuilder(primary.title()).append("_").append(secondary.title()).toString());
					return primary;
				}).get();
		targets.stream().filter(batch->!merged.batch().equals(batch)).map(batch->dao.em().getReference(BatchLibrary.class, BatchLibrary.BatchLibraryPK.builder().batch(batch).build())).forEach(dao.em()::remove);
		return BatchToDTO.map(dao.merge(merged));
	}
	@Transactional
	public void rebuild(int batch) {
		dao.find(BatchLibrary.BatchLibraryPK.builder().batch(batch).build())
				.ifPresent(entity->AnalysisUtil.rebuild(dao.em(), entity.analysis().stream().sorted().collect(Collectors.toList()), entity));
	}
	private static final UUID REQUEST_NAME = UUID.fromString("2e3aa713-2b35-4523-85e7-83de5293f150");
	private static final UUID REQUEST_ID = UUID.fromString("1bad34dc-6cfd-441e-8aef-de757bc6d471");
	private static final UUID PATIENT_NAME = UUID.fromString("5e5e7085-c856-4daa-a866-76ec1218eacf");
	private static final UUID CUSTOMER_NAME = UUID.fromString("da308c3a-d46f-4b8f-9a71-22d791b3e1b8");
	private static final UUID TAT = UUID.fromString("018025f2-efd3-419e-b0f1-62b75f0e6d41");
	private void assignValues(com.greencross.lims.entity.Analysis<?> analysis) {
		if(analysis.requests()==null || analysis.requests().isEmpty()) return;
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
