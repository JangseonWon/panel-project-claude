package com.greencross.lims.api;

import com.greencross.lims.dto.*;
import elemental2.dom.DomGlobal;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import lombok.experimental.UtilityClass;

import java.util.Map;

import static elemental2.core.Global.JSON;

@UtilityClass
public class AnalysisApi {
	public void template(Callback<AnalysisTemplate> callback) {
		Api api = new Api().method(Method.GET).url("/library/analysis/template");
		ProgressApi.open(false);
		RequestApi.request(api, json->{
			AnalysisTemplate result = (AnalysisTemplate) JSON.parse(json);
			callback.onSuccess(result);
			ProgressApi.close();
		});
	}
	public void add(int batch, Callback<Analysis> callback) {
		ProgressApi.open(false);
		Api api0 = new Api().method(Method.GET).url("/library/batches/" + batch + "/analysis/last-row");
		RequestApi.request(api0, value->{
			int rowMax = Integer.parseInt(value);
			Api api = new Api().method(Method.PUT).contentType("application/json; charset=utf-8").url("/library/batches/" + batch + "/analysis/" + (rowMax+1));
			ProgressApi.open(false);
			RequestApi.request(api, json->{
				Analysis result = (Analysis) JSON.parse(json);
				callback.onSuccess(result);
				ProgressApi.close();
			});
		});
	}
	public void save(int batch, int row, Map<String, String> values, Callback<Analysis> callback) {
		JsPropertyMap<Object> data = Js.asPropertyMap(new Object());
		values.forEach(data::set);
		Api api = new Api().method(Method.PATCH)
				.contentType("application/json; charset=utf-8")
				.param(JSON.stringify(data))
				.url("/library/batches/" + batch + "/analysis/" + row);
		RequestApi.request(api, json->{
			Analysis result = (Analysis) JSON.parse(json);
			callback.onSuccess(result);
		});
	}
	public void merge(int[] worklists, Callback<Analysis> callback){}
	public void delete(int batch, int row, Callback<Void> callback){
		Api api = new Api().method(Method.DELETE).url("/library/batches/" + batch + "/analysis/" + row);
		RequestApi.request(api, json->{
			callback.onSuccess(null);
		});
	}
	public void analysis(int batch, Query query, Callback<Analysis[]> callback) {
		Api api = new Api().method(Method.POST).contentType("application/json; charset=utf-8").param(JSON.stringify(query)).url("/library/batches/" + batch + "/analysis");
		ProgressApi.open(false);
		RequestApi.request(api, json->{
			Analysis[] result = (Analysis[])JSON.parse(json);
			callback.onSuccess(result);
			ProgressApi.close();
		});
	}
}
