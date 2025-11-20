package com.gcgenome.lims.client;

import com.gcgenome.lims.dto.Query;

public class Main extends AbstractEntryPoint {
	private AnalysisCompleteListElement list;
	@Override
	public AbstractScene<?>[] elements(Query query) {
		list = new AnalysisCompleteListElement(query);
		return new AbstractScene[] {
			list
		};
	}

	@Override
	protected AbstractScene<?> prepare(String param) {
		return list;
	}

	@Override
	protected String toParentUrl(String param) {
		if(param == null || param.isEmpty() || param.endsWith(".html")) return "패널검사/Reading";
		else return "패널검사/Reading/" + param;
	}
}
