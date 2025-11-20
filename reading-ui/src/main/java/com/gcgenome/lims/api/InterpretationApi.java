package com.gcgenome.lims.api;

import elemental2.dom.Response;
import elemental2.promise.Promise;
import lombok.experimental.UtilityClass;

import static elemental2.core.Global.JSON;

@UtilityClass
public class InterpretationApi {
	public Promise<Object> interpretation(long sampleId, String service) {
		return FetchApi.request("/samples/" + sampleId + "/services/" + service + "/interpretation", null)
					   .then(Response::text)
					   .then(r->{
						   if(r!=null && !r.trim().isEmpty()) return Promise.resolve(JSON.parse(r));
						   else return Promise.resolve((Object)null);
					   });
	}
}
