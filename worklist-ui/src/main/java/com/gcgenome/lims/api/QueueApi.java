package com.gcgenome.lims.api;

import com.gcgenome.lims.dto.*;
import com.gcgenome.lims.dto.Slice;
import com.gcgenome.lims.dto.Work;
import elemental2.dom.RequestInit;
import elemental2.dom.Response;
import lombok.experimental.UtilityClass;

import static elemental2.core.Global.JSON;

@UtilityClass
public class QueueApi {
	public void queue(Query query, Callback<Slice<Work>> callback) {
		ProgressApi.open(false);
		RequestInit init = RequestInit.create();
		init.setMethod("POST");
		init.setHeaders(new String[][] {
				{"Content-Type", "application/json; charset=utf-8"}
		});
		init.setBody(JSON.stringify(query));
		FetchApi.request("/queue", init)
				.then(response->ResponseToSlice.map(response, Work.class))
				.then(result->{
					callback.onSuccess(result);
					return null;
				}).finally_(ProgressApi::close);

	}
	public void add(int worklist, long sample, String service, Work work, Callback<Void> callback) {
		RequestInit init = RequestInit.create();
		init.setMethod("PUT");
		init.setHeaders(new String[][] {
				{"Content-Type", "application/json; charset=utf-8"}
		});
		init.setBody(JSON.stringify(work));
		FetchApi.request("/queue/" + work.id() + "/consume", init)
				.then(Response::json).then(json->{
					callback.onSuccess(null);
					return null;
				});
	}
	public void delete(String serial, Callback<Void> callback) {
		RequestInit init = RequestInit.create();
		init.setMethod("DELETE");
		init.setHeaders(new String[][] {
				{"Content-Type", "application/json; charset=utf-8"}
		});
		FetchApi.request("/queue/" + serial, init)
				.then(Response::json).then(json->{
					callback.onSuccess(null);
					return null;
				});
	}
}
