package com.gcgenome.lims.api;

import com.gcgenome.lims.dto.Report;
import elemental2.dom.RequestInit;
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
	public Promise<Object> auto(long sampleId, String service, Object interpretation) {
		RequestInit request = RequestInit.create();
		request.setMethod("POST");
		request.setHeaders(new String[][] {
				new String[] {"Accept", "application/json"},
				new String[] {"Content-Type", "application/json; charset=utf-8"}
		});
		request.setBody(JSON.stringify(interpretation));
		return FetchApi.request("/samples/" + sampleId + "/services/" + service + "/auto-interpret", request)
					   .then(Response::text)
					   .then(r->{
						   if(r!=null && !r.trim().isEmpty()) return Promise.resolve(JSON.parse(r));
						   else return Promise.resolve((Object)null);
					   });
	}
	public Promise<Object> negative(long sampleId, String service) {
		RequestInit request = RequestInit.create();
		request.setHeaders(new String[][] {
				new String[] {"Accept", "application/json"},
				new String[] {"Content-Type", "application/json; charset=utf-8"}
		});
		return FetchApi.request("/samples/" + sampleId + "/services/" + service + "/negative-interpret", request)
					   .then(Response::text)
					   .then(r->{
						   if(r!=null && !r.trim().isEmpty()) return Promise.resolve(JSON.parse(r));
						   else return Promise.resolve((Object)null);
					   });
	}
	public Promise<Object> save(long sampleId, String service, Object interpretation) {
		RequestInit request = RequestInit.create();
		request.setMethod("PUT");
		request.setHeaders(new String[][] {
			new String[] {"Accept", "application/json"},
			new String[] {"Content-Type", "application/json; charset=utf-8"}
		});
		request.setBody(JSON.stringify(interpretation));
		return FetchApi.request("/samples/" + sampleId + "/services/" + service + "/interpretation", request)
					   .then(Response::text)
					   .then(r->{
						   if(r!=null && !r.trim().isEmpty()) return Promise.resolve(JSON.parse(r));
						   else return Promise.resolve((Report[])null);
					   });
	}
}
