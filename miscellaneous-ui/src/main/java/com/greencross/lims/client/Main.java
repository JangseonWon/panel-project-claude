package com.greencross.lims.client;

import com.greencross.lims.api.WindowApi;
import com.greencross.lims.dto.Query;
import elemental2.dom.DomGlobal;

public class Main extends AbstractEntryPoint {
	private MiscListElement elemMenu;
	private SnvElement elemSnv;
	@Override
	public AbstractScene<?>[] elements(Query query) {
		elemMenu = new MiscListElement(query);
		elemSnv = new SnvElement(query);
		return new AbstractScene[] {elemMenu, elemSnv};
	}

	@Override
	protected AbstractScene<?> prepare(String param) {
		if(param == null || param.isEmpty() || param.endsWith(".html")) return elemMenu;
		else if("snv".equalsIgnoreCase(param)) {
			elemSnv.update();
			return elemSnv;
		}
		else return elemMenu;
	}

	@Override
	protected String toParentUrl(String param) {
		if(param == null || param.isEmpty() || param.endsWith(".html")) return "패널검사/Misc.";
		else return "패널검사/Misc./" + param;
	}
}
