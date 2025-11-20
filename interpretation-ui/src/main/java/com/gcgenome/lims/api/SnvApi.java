package com.gcgenome.lims.api;

import com.gcgenome.lims.dto.Analysis2;
import com.gcgenome.lims.dto.Query;
import com.gcgenome.lims.dto.Slice;
import elemental2.dom.RequestInit;
import elemental2.dom.Response;
import elemental2.promise.Promise;
import lombok.experimental.UtilityClass;

import static elemental2.core.Global.JSON;

@UtilityClass
public class SnvApi {
	public Promise<Object[]> reported(long sampleId, String service) {
		return FetchApi.request("/samples/" + sampleId + "/services/" + service + "/snvs", null)
					   .then(Response::text)
					   .then(r->{
						   if(r!=null && !r.trim().isEmpty()) return Promise.resolve((Object[])JSON.parse(r));
						   else return Promise.resolve(new Object[0]);
					   });
	}

	public Promise<Analysis2[]> analysis(long sampleId) {
		return FetchApi.request("/samples/" + sampleId + "/analysis", null)
					   .then(Response::text)
					   .then(r->{
						   if(r!=null && !r.trim().isEmpty()) return Promise.resolve((Analysis2[])JSON.parse(r));
						   else return Promise.resolve((Analysis2[])null);
					   });
	}
	public Promise<Slice<Object>> snvs(long sampleId, String service, String batch, int row, Query query) {
		RequestInit request = RequestInit.create();
		request.setMethod("POST");
		request.setHeaders(new String[][] {
				new String[] {"Accept", "application/json"},
				new String[] {"Content-Type", "application/json"}
		});
		request.setBody(JSON.stringify(query));
		return FetchApi.request("/samples/" + sampleId + "/services/" + service + "/batches/" + batch + "/" + row + "/snvs", request)
					   .then(Response::text)
					   .then(r->{
						   if(r!=null && !r.trim().isEmpty()) return Promise.resolve((Slice<Object>)JSON.parse(r));
						   else return Promise.resolve((Slice<Object>)null);
					   });

	}
	public Promise<Response> update(long sampleId, String service, String variant, String classification) {
		RequestInit request = RequestInit.create();
		if(classification!=null && !"" .equalsIgnoreCase(classification)) {
			request.setMethod("PUT");
			return FetchApi.request("/samples/" + sampleId + "/services/" + service + "/snvs/" + variant + "/" + classification, request);
		} else {
			request.setMethod("DELETE");
			return FetchApi.request("/samples/" + sampleId + "/services/" + service + "/snvs/" + variant, request);
		}
	}
}
