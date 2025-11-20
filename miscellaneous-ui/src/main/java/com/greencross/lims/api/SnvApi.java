package com.greencross.lims.api;

import com.greencross.lims.dto.Promise;
import com.greencross.lims.dto.Query;
import elemental2.dom.RequestInit;
import elemental2.dom.Response;
import lombok.experimental.UtilityClass;

import static elemental2.core.Global.JSON;

@UtilityClass
public class SnvApi {
	public Promise<Response> search(Query query) {
		RequestInit request = RequestInit.create();
		request.setMethod("POST");
		request.setHeaders(new String[][] {
				new String[] {"Accept", "application/json"},
				new String[] {"Content-Type", "application/json"}
		});
		request.setBody(JSON.stringify(query));
		return FetchApi.request("/misc/snvs", request);
	}
}
