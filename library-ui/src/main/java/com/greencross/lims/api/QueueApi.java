package com.greencross.lims.api;

import com.greencross.lims.dto.*;
import lombok.experimental.UtilityClass;

import java.util.concurrent.atomic.AtomicInteger;

import static elemental2.core.Global.JSON;

@UtilityClass
public class QueueApi {
	public void template(Callback<WorklistTemplate> callback) {
		Api api = new Api().method(Method.GET).url("/queue/template");
		ProgressApi.open(false);
		RequestApi.request(api, json->{
			WorklistTemplate result = (WorklistTemplate) JSON.parse(json);
			callback.onSuccess(result);
			ProgressApi.close();
		});
	}
	public void queue(RequestReference[] analysis, Callback<Work> callback) {
		ProgressApi.open(true);
		AtomicInteger complete = new AtomicInteger(0);
		for(RequestReference target : analysis) {
			Api api = new Api().method(Method.PUT).contentType("application/json; charset=utf-8")
							   .url("/sample/" + target.sample() + "/service/" + target.service() + "/queue");
			RequestApi.request(api, json -> {
				int completed = complete.incrementAndGet();
				if (completed >= analysis.length) {
					ProgressApi.close();
					callback.onSuccess(null);
				} else ProgressApi.progress(completed / (double) analysis.length);
			});
		}
	}
}
