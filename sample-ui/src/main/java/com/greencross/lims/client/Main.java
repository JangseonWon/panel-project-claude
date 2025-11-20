package com.greencross.lims.client;

import com.google.gwt.core.client.EntryPoint;
import com.greencross.lims.api.SampleApi;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;
import org.jboss.elemento.Elements;
import org.jboss.elemento.HTMLContainerBuilder;

public class Main implements EntryPoint {
	private final HTMLContainerBuilder<HTMLDivElement> div = Elements.div().css("top");
	@Override
	public void onModuleLoad() {
		Elements.body().add(div);
		String hash = DomGlobal.window.location.hash;
		if(hash.contains("-")) hash = hash.replace("-", "");
		long sid = Long.parseLong(hash.substring(1));
		SampleApi.sample(sid, sample->{
			div.add(SampleElement.build(sample));
			SampleApi.requests(sid, requests->{
				RequestTabElement tab = RequestTabElement.build(requests);
				div.add(tab);
			});
		});
	}
}
