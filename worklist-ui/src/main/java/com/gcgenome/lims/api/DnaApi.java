package com.gcgenome.lims.api;

import com.gcgenome.lims.dto.*;
import elemental2.dom.RequestInit;
import elemental2.dom.Response;
import elemental2.promise.Promise;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

import static elemental2.core.Global.JSON;

@UtilityClass
public class DnaApi {
	public void checkout(int worklistId, RequestReference[] serials, Callback<Void> callback) {
		ProgressApi.open(false);
		FetchApi.request("/dna/batches/last-batch-index", null)
				.then(Response::text)
				.then(value-> Promise.resolve(Integer.parseInt(value)))
				.then(batchMax->{
					RequestInit init = RequestInit.create();
					init.setMethod("PUT");
					init.setHeaders(new String[][] {
							{"Content-Type", "application/json; charset=utf-8"}
					});
					init.setBody(JSON.stringify(serials));
					return FetchApi.request("/dna/batches/" + (batchMax+1), init);
				}).then(Response::json).then(json->{
					Map<String, String> map = new HashMap<>();
					map.put("state", "CLOSE");
					WorklistApi.save(worklistId, map, callback2-> callback.onSuccess(null));
					return null;
				}).finally_(ProgressApi::close);
	}
}