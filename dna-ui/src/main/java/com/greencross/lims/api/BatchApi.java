package com.greencross.lims.api;

import com.greencross.lims.dto.*;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static elemental2.core.Global.JSON;

@UtilityClass
public class BatchApi {
	public void template(Callback<BatchTemplate> callback) {
		Api api = new Api().method(Method.GET).url("/dna/batch/template");
		ProgressApi.open(false);
		RequestApi.request(api, json->{
			BatchTemplate result = (BatchTemplate) JSON.parse(json);
			callback.onSuccess(result);
			ProgressApi.close();
		});
	}
	public void batches(Query query, Callback<Slice<Batch>> callback) {
		Api api = new Api().method(Method.POST).contentType("application/json; charset=utf-8").param(JSON.stringify(query)).url("/dna/batches");
		ProgressApi.open(false);
		RequestApi.request(api, json->{
			Slice<Batch> result = (Slice<Batch>) JSON.parse(json);
			callback.onSuccess(result);
			ProgressApi.close();
		});
	}
	public void batch(int batch, Callback<Batch> callback){
		Api api = new Api().method(Method.GET).contentType("application/json; charset=utf-8").url("/dna/batches/" + batch);
		RequestApi.request(api, json->{
			Batch result = (Batch)JSON.parse(json);
			callback.onSuccess(result);
		});
	}
	public void add(Callback<Batch> callback) {
		ProgressApi.open(false);
		Api api0 = new Api().method(Method.GET).url("/dna/batches/last-batch-index");
		RequestApi.request(api0, value->{
			int batchMax = Integer.parseInt(value);
			Api api = new Api().method(Method.PUT).contentType("application/json; charset=utf-8").url("/dna/batches/" + (batchMax+1));
			RequestApi.request(api, json->{
				Batch result = (Batch) JSON.parse(json);
				callback.onSuccess(result);
				ProgressApi.close();
			});
		});
	}
	public void save(int batch, Map<String, String> values, Callback<Batch> callback){
		JsPropertyMap<Object> data = Js.asPropertyMap(new Object());
		values.forEach(data::set);
		Api api = new Api().method(Method.PATCH)
						   .contentType("application/json; charset=utf-8")
						   .param(JSON.stringify(data))
						   .url("/dna/batches/" + batch);
		RequestApi.request(api, json->{
			Batch result = (Batch)JSON.parse(json);
			callback.onSuccess(result);
		});
	}
	public void merge(int[] batches, Callback<Batch> callback){
		Api api = new Api().method(Method.POST)
						   .contentType("application/json; charset=utf-8")
						   .param(JSON.stringify(batches))
						   .url("/dna/batches/merge");
		RequestApi.request(api, json->{
			Batch result = (Batch)JSON.parse(json);
			callback.onSuccess(result);
		});
	}
	public void delete(int batch, Callback<Void> callback){
		Api api = new Api().method(Method.DELETE).url("/dna/batches/" + batch);
		RequestApi.request(api, json->{
			callback.onSuccess(null);
		});
	}
	public void rebuild(int batch, Callback<Batch> callback){
		Api api = new Api().method(Method.PATCH).url("/dna/batches/" + batch + "/rebuild");
		RequestApi.request(api, json->{
			Batch result = (Batch)JSON.parse(json);
			callback.onSuccess(result);
		});
	}
	public void checkout(BatchReference[] batches, Callback<Void> callback) {
		ProgressApi.open(true);
		AtomicInteger complete = new AtomicInteger(0);
		Api api0 = new Api().method(Method.GET).url("/library/batches/last-batch-index");
		RequestApi.request(api0, value->{
			int batchMax = Integer.parseInt(value);
			for(int i = 0; i < batches.length; ++i) {
				BatchReference batch = batches[i];
				Api api = new Api().method(Method.PUT).contentType("application/json; charset=utf-8")
						.param(JSON.stringify(batch)).url("/library/batches/" + (batchMax+1+i));
				RequestApi.request(api, json->{
					int completed = complete.incrementAndGet();
					if (completed >= batches.length) {
						ProgressApi.close();
						callback.onSuccess(null);
					} else ProgressApi.progress(completed / (double) batches.length);
				});
			}
		});
	}
}
