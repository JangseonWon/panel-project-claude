package com.greencross.lims.api;

import com.greencross.lims.dto.*;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import lombok.experimental.UtilityClass;

import java.util.Map;

import static elemental2.core.Global.JSON;

@UtilityClass
public class BatchApi {
	public void template(Callback<BatchTemplate> callback) {
		Api api = new Api().method(Method.GET).url("/sequencing/templates/batch");
		ProgressApi.open(false);
		RequestApi.request(api, json->{
			BatchTemplate result = (BatchTemplate) JSON.parse(json);
			callback.onSuccess(result);
			ProgressApi.close();
		});
	}
	public void batches(Query query, Callback<Slice<Batch>> callback) {
		Api api = new Api().method(Method.POST).contentType("application/json; charset=utf-8").param(JSON.stringify(query)).url("/sequencing/batches");
		ProgressApi.open(false);
		RequestApi.request(api, json->{
			Slice<Batch> result = (Slice<Batch>) JSON.parse(json);
			callback.onSuccess(result);
			ProgressApi.close();
		});
	}
	public void batch(int batch, Callback<Batch> callback){
		Api api = new Api().method(Method.GET).contentType("application/json; charset=utf-8").url("/sequencing/batches/" + batch);
		RequestApi.request(api, json->{
			Batch result = (Batch)JSON.parse(json);
			callback.onSuccess(result);
		});
	}
	public void add(Callback<Batch> callback) {
		Api api = new Api().method(Method.PUT).contentType("application/json; charset=utf-8").url("/sequencing/batches");
		ProgressApi.open(false);
		RequestApi.request(api, json->{
			Batch result = (Batch) JSON.parse(json);
			callback.onSuccess(result);
			ProgressApi.close();
		});
	}
	public void save(int batch, Map<String, String> values, Callback<Batch> callback){
		JsPropertyMap<Object> data = Js.asPropertyMap(new Object());
		values.forEach(data::set);
		Api api = new Api().method(Method.PATCH)
						   .contentType("application/json; charset=utf-8")
						   .param(JSON.stringify(data))
						   .url("/sequencing/batches/" + batch);
		RequestApi.request(api, json->{
			Batch result = (Batch)JSON.parse(json);
			callback.onSuccess(result);
		});
	}
	public void merge(int[] batches, Callback<Batch> callback){
		Api api = new Api().method(Method.POST)
						   .contentType("application/json; charset=utf-8")
						   .param(JSON.stringify(batches))
						   .url("/sequencing/batches/merge");
		RequestApi.request(api, json->{
			Batch result = (Batch)JSON.parse(json);
			callback.onSuccess(result);
		});
	}
	public void delete(int batch, Callback<Void> callback){
		Api api = new Api().method(Method.DELETE).url("/sequencing/batches/" + batch);
		RequestApi.request(api, json->{
			callback.onSuccess(null);
		});
	}
	public void rebuild(int batch, Callback<Batch> callback){
		Api api = new Api().method(Method.PATCH).url("/sequencing/batches/" + batch + "/rebuild");
		RequestApi.request(api, json->{
			Batch result = (Batch)JSON.parse(json);
			callback.onSuccess(result);
		});
	}
	public void checkout(RequestReference[] serials, Callback<Void> callback) {

	}
}
