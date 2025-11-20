package com.gcgenome.lims.api;

import com.gcgenome.lims.dto.Query;
import elemental2.dom.RequestInit;
import elemental2.dom.Response;
import elemental2.promise.Promise;
import lombok.experimental.UtilityClass;

import static elemental2.core.Global.JSON;
import static elemental2.core.Global.encodeURI;

@UtilityClass
public class SnvApi {
	public Promise<Object> snv(String snvId) {
		return FetchApi.request(encodeURI("/snvs/" + snvId))
					   .then(Response::text)
					   .then(r->{
						   if(r!=null && !r.trim().isEmpty()) return Promise.resolve(JSON.parse(r));
						   else return Promise.resolve((Object)null);
					   });
	}
	public Promise<Response> requests(String snvId, Query query) {
		RequestInit request = RequestInit.create();
		request.setMethod("POST");
		request.setHeaders(new String[][] {
				new String[] {"Accept", "application/json"},
				new String[] {"Content-Type", "application/json; charset=utf-8"}
		});
		request.setBody(JSON.stringify(query));
		return FetchApi.request(encodeURI("/snvs/" + snvId + "/requests"), request);
	}
	public Promise<Response> samples(String snvId, Query query) {
		RequestInit request = RequestInit.create();
		request.setMethod("POST");
		request.setHeaders(new String[][] {
				new String[] {"Accept", "application/json"},
				new String[] {"Content-Type", "application/json; charset=utf-8"}
		});
		request.setBody(JSON.stringify(query));
		return FetchApi.request(encodeURI("/snvs/" + snvId + "/samples"), request);
	}
}
