package com.gcgenome.lims.api;

import com.gcgenome.lims.dto.*;
import com.gcgenome.lims.dto.Slice;
import com.gcgenome.lims.dto.Request;
import elemental2.dom.RequestInit;
import elemental2.dom.Response;
import lombok.experimental.UtilityClass;

import static elemental2.core.Global.JSON;

@UtilityClass
public class ReceptApi {
	public void requests(Query query, Callback<Slice<Request>> callback) {
		ProgressApi.open(false);
		RequestInit init = RequestInit.create();
		init.setMethod("POST");
		init.setHeaders(new String[][] {
				{"Content-Type", "application/json; charset=utf-8"}
		});
		init.setBody(JSON.stringify(query));
		FetchApi.request("/requests", init)
				.then(response->ResponseToSlice.map(response, Request.class))
				.then(result->{
					callback.onSuccess(result);
					return null;
		}).finally_(ProgressApi::close);
	}
}
