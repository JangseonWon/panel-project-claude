package com.greencross.lims.sequencing;

import com.greencross.lims.dto.Analysis;
import com.greencross.lims.dto.Query;
import com.gcgenome.lims.dto.RequestReference;
import com.greencross.lims.trans.AnalysisToDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class SequencingAnalysisController {
	private final SequencingAnalysisService svc;
	public SequencingAnalysisController(SequencingAnalysisService svc) {
		this.svc = svc;
	}
	@RequestMapping(value="/sequencing/batches/{batch}/analysis", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<Analysis> analysis(@PathVariable int batch, @RequestBody Query query) {
		return svc.list(batch, query);
	}
	@RequestMapping(value="/sequencing/batches/{batch}/analysis/last-row", method= RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public int findLastRow(@PathVariable int batch) {
		return svc.findLastRow(batch);
	}
	@RequestMapping(value="/sequencing/batches/{batch}/analysis/{row}", method= RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public synchronized Analysis create(@PathVariable int batch, @PathVariable int row, @Nullable @RequestBody(required = false) RequestReference target) {
		return AnalysisToDTO.map(svc.create(batch, row, target));
	}
	@RequestMapping(value="/sequencing/batches/{batch}/analysis/{row}", method= RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Analysis update(@PathVariable int batch, @PathVariable int row, @RequestBody Map<String, String> dto) {
		return svc.update(batch, row, dto);
	}
	@RequestMapping(value="/sequencing/batches/{batch}/analysis/{row}", method= RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable int batch, @PathVariable int row) {
		svc.delete(batch, row);
		svc.rebuild(batch);
	}
	@RequestMapping(value="/sequencing/analysis", method={RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Page<Analysis> find(@RequestBody Query query) {
		return svc.list(query);
	}
}
