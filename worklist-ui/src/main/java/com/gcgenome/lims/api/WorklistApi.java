package com.gcgenome.lims.api;

import com.gcgenome.lims.dto.*;
import com.gcgenome.lims.dto.Slice;
import com.gcgenome.lims.dto.Work;
import com.gcgenome.lims.dto.Worklist;
import com.gcgenome.lims.dto.WorklistTemplate;
import elemental2.dom.RequestInit;
import elemental2.dom.Response;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import lombok.experimental.UtilityClass;

import java.util.Map;

import static elemental2.core.Global.JSON;

@UtilityClass
public class WorklistApi {
	public void template(Callback<WorklistTemplate> callback) {
		ProgressApi.open(false);
		FetchApi.request("/worklist/template")
				.then(Response::json)
				.then(json->{
					WorklistTemplate result = (WorklistTemplate) json;
					callback.onSuccess(result);
					return null;
				}).finally_(ProgressApi::close);
	}
	public void worklists(Query query, Callback<Slice<Worklist>> callback) {
		ProgressApi.open(false);
		RequestInit init = RequestInit.create();
		init.setMethod("POST");
		init.setHeaders(new String[][] {
				{"Content-Type", "application/json; charset=utf-8"}
		});
		init.setBody(JSON.stringify(query));
		FetchApi.request("/worklists", init)
				.then(response -> ResponseToSlice.map(response, Worklist.class))
				.then(result->{
					callback.onSuccess(result);
					return null;
				}).finally_(ProgressApi::close);
	}
	public void add(Callback<Worklist> callback) {
		ProgressApi.open(false);
		RequestInit init = RequestInit.create();
		init.setMethod("PUT");
		init.setHeaders(new String[][] {
				{"Content-Type", "application/json; charset=utf-8"}
		});
		FetchApi.request("/worklists", init)
				.then(Response::json)
				.then(json->{
					Worklist result = (Worklist) json;
					callback.onSuccess(result);
					return null;
				}).finally_(ProgressApi::close);;
	}
	public void delete(int worklist, Callback<Void> callback){
		RequestInit init = RequestInit.create();
		init.setMethod("DELETE");
		init.setHeaders(new String[][] {
				{"Content-Type", "application/json; charset=utf-8"}
		});
		FetchApi.request("/worklists/" + worklist, init)
				.then(Response::json)
				.then(json->{
					callback.onSuccess(null);
					return null;
				});
	}
	public void works(int worklist, Query query, Callback<Work[]> callback) {
		RequestInit init = RequestInit.create();
		init.setMethod("POST");
		init.setHeaders(new String[][] {
				{"Content-Type", "application/json; charset=utf-8"}
		});
		init.setBody(JSON.stringify(query));
		FetchApi.request("/worklists/" + worklist + "/works", init)
				.then(Response::json)
				.then(json->{
					Work[] result = (Work[]) json;
					callback.onSuccess(result);
					return null;
				});
	}
	public void save(int worklist, Map<String, String> map, Callback<Worklist> callback) {
		JsPropertyMap<Object> data = Js.asPropertyMap(new Object());
		map.forEach(data::set);
		RequestInit init = RequestInit.create();
		init.setMethod("PATCH");
		init.setHeaders(new String[][] {
				{"Content-Type", "application/json; charset=utf-8"}
		});
		init.setBody(JSON.stringify(data));
		FetchApi.request("/worklists/" + worklist, init)
				.then(Response::json)
				.then(json->{
					Worklist result = (Worklist) json;
					callback.onSuccess(result);
					return null;
				});
	}
	public void save(int worklist, long sample, String service, Map<String, String> map, Callback<Work> callback) {
		JsPropertyMap<Object> data = Js.asPropertyMap(new Object());
		map.forEach(data::set);
		RequestInit init = RequestInit.create();
		init.setMethod("PATCH");
		init.setHeaders(new String[][] {
				{"Content-Type", "application/json; charset=utf-8"}
		});
		init.setBody(JSON.stringify(data));
		FetchApi.request("/worklists/" + worklist + "/works/" + sample + "/" + service, init)
				.then(Response::json)
				.then(json->{
					Work result = (Work) json;
					callback.onSuccess(result);
					return null;
				});
	}
}
