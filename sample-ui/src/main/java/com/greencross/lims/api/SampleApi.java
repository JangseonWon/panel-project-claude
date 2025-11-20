package com.greencross.lims.api;

import com.greencross.lims.dto.*;
import jsinterop.base.Any;
import jsinterop.base.Js;
import lombok.experimental.UtilityClass;

import static elemental2.core.Global.JSON;

@UtilityClass
public class SampleApi {
	public void sample(long sampleId, Callback<Sample> callback) {
		Api api = new Api().method(Method.GET).url("/samples/" + sampleId);
		ProgressApi.open(true);
		RequestApi.request(api, json->{
			Sample result = (Sample) JSON.parse(json);
			callback.onSuccess(result);
			ProgressApi.close();
		});
	}
	public void subjects(long sampleId, String service, Callback<Any[]> callback) {
		Api api = new Api().method(Method.GET).url("/samples/" + sampleId + "/services/" + service + "/subjects");
		ProgressApi.open(false);
		RequestApi.request(api, json->{
			callback.onSuccess(Js.asArray(JSON.parse(json)));
			ProgressApi.close();
		});
	}
	public void siblings(long sampleId, Callback<Sample[]> callback) {

	}
	public void family(long sampleId, Callback<Sample[]> callback) {

	}
	public void requests(long sampleId, Callback<Request[]> callback) {
		Api api = new Api().method(Method.GET).url("/samples/" + sampleId + "/requests");
		ProgressApi.open(true);
		RequestApi.request(api, json->{
			Request[] result = (Request[]) JSON.parse(json);
			callback.onSuccess(result);
			ProgressApi.close();
		});
	}
}
