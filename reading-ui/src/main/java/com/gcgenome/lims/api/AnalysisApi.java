package com.gcgenome.lims.api;

import com.gcgenome.lims.dto.*;
import elemental2.dom.RequestInit;
import elemental2.dom.Response;
import elemental2.promise.Promise;
import lombok.experimental.UtilityClass;

import static elemental2.core.Global.JSON;

@UtilityClass
public class AnalysisApi {
	public void template(String subject, Callback<AnalysisTemplate> callback) {
		ProgressApi.open(false);
		FetchApi.request("/readings/" + subject + "/template", null)
				.then(Response::text)
				.then(r->{
					ProgressApi.close();
					if(r!=null && !r.trim().isEmpty()) {
						AnalysisTemplate template = (AnalysisTemplate) JSON.parse(r);
						return Promise.resolve(template);
					} else return Promise.resolve((AnalysisTemplate)null);
				}).then(template->{
					callback.onSuccess(template);
					return null;
				});
	}
	public void list(String subject, Query query, Callback<Slice<Analysis>> callback) {
		RequestInit init = RequestInit.create();
		init.setMethod("POST");
		init.setHeaders(new String[][] {
				{"Content-Type", "application/json"}
		});
		init.setBody(JSON.stringify(query));
		ProgressApi.open(false);
		FetchApi.request("/readings/" + subject + "/analysis", init)
				.then(Response::text)
				.then(r->{
					ProgressApi.close();
					if(r!=null && !r.trim().isEmpty()) {
						Slice<Analysis> template = (Slice<Analysis>) JSON.parse(r);
						return Promise.resolve(template);
					} else return Promise.resolve((Slice<Analysis>)null);
				}).then(template->{
					callback.onSuccess(template);
					return null;
				});
	}
}
