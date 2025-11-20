package com.gcgenome.lims.client;

import com.gcgenome.lims.dto.Query;
import com.gcgenome.lims.client.worklist.WorkElement;
import com.gcgenome.lims.client.worklist.WorklistElement;

public class Main extends AbstractEntryPoint {
	private WorklistElement worklist;
	private WorkElement work;
	/*static {
		ColumnBuilderLink.onopen = link->{
			if("_self()".equalsIgnoreCase(link.target())) DomGlobal.window.location.replace(link.href());
			else WindowApi.open(link.href(), link.target(), null, true);
		};
	}*/
	@Override
	public AbstractScene<?>[] elements(Query query) {
		worklist = new WorklistElement(query);
		work = new WorkElement(query);
		return new AbstractScene[] {
			worklist, work
		};
	}

	@Override
	protected AbstractScene<?> prepare(String param) {
		if(param == null || param.isEmpty() || param.endsWith(".html")) {
			worklist.update();
			return worklist;
		} else {
			int idx = Integer.parseInt(param);
			work.parent(idx).update();
			return work;
		}
	}

	@Override
	protected String toParentUrl(String param) {
		if(param == null || param.isEmpty() || param.endsWith(".html")) return "패널검사/Worklist";
		else return "패널검사/Worklist/" + param;
	}
}