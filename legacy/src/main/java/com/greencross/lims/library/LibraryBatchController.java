package com.greencross.lims.library;

import com.greencross.lims.dto.Batch;
import com.gcgenome.lims.dto.BatchReference;
import com.greencross.lims.dto.Query;
import com.greencross.lims.trans.BatchToDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class LibraryBatchController {
	private final Logger Log = LoggerFactory.getLogger(getClass());
	private final LibraryBatchService svc;
	public LibraryBatchController(LibraryBatchService svc) {
		this.svc = svc;
	}
	@RequestMapping(value="/library/batches", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Page<Batch> batches(@RequestBody Query query) {
		return svc.list(query);
	}
	@RequestMapping(value="/library/batches/last-batch-index", method= RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public int findLastBatch() {
		return svc.findLastBatch();
	}
	@RequestMapping(value="/library/batches/{batch}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Batch find(@PathVariable int batch) {
		return svc.get(batch).orElseThrow(()->new RuntimeException("Can't find Library Batch:" + batch));
	}
	@RequestMapping(value="/library/batches/{batch}", method= RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Batch create(@PathVariable int batch, @Nullable @RequestBody(required = false) BatchReference target) {
		return BatchToDTO.map(svc.create(batch, target));
	}
	@RequestMapping(value="/library/batches/merge", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Batch merge(@RequestBody List<Integer> targets) {
		svc.mergeChildren(targets);
		return svc.mergeParent(targets);
	}
	@RequestMapping(value="/library/batches/{batch}", method= RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Batch update(@PathVariable int batch, @RequestBody Map<String, String> dto) {
		return svc.update(batch, dto);
	}
	@RequestMapping(value="/library/batches/{batch}", method= RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable int batch) {
		svc.delete(batch);
	}
	@RequestMapping(value="/library/batches/{batch}/rebuild", method= RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Batch rebuild(@PathVariable int batch) {
		svc.rebuild(batch);
		return svc.get(batch).orElse(null);
	}
}
