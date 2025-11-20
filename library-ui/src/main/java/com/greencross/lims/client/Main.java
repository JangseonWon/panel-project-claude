package com.greencross.lims.client;

import com.greencross.lims.api.WindowApi;
import com.greencross.lims.client.analysis.AnalysisElement;
import com.greencross.lims.client.analysis.BatchElement;
import com.greencross.lims.dto.Query;
import com.greencross.lims.sheet.column.ColumnBuilderLink;
import elemental2.dom.DomGlobal;

public class Main extends AbstractEntryPoint {
	private BatchElement batch;
	private AnalysisElement analysis;
	static {
		ColumnBuilderLink.onopen = link->{
			if("_self()".equalsIgnoreCase(link.target())) DomGlobal.window.location.replace(link.href());
			else WindowApi.open(link.href(), link.target(), null, true);
		};
	}
	@Override
	public AbstractScene<?>[] elements(Query query) {
		batch = new BatchElement(query);
		analysis = new AnalysisElement(query);
		return new AbstractScene[] {
			batch, analysis
		};
	}

	@Override
	protected AbstractScene<?> prepare(String param) {
		if(param == null || param.isEmpty() || param.endsWith(".html")) {
			batch.update();
			return batch;
		} else {
			int idx = Integer.parseInt(param);
			analysis.parent(idx).update();
			return analysis;
		}
	}

	@Override
	protected String toParentUrl(String param) {
		if(param == null || param.isEmpty() || param.endsWith(".html")) return "패널검사/Library";
		else return "패널검사/Library/" + param;
	}
}
