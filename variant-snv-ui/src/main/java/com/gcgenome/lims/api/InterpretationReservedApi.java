package com.gcgenome.lims.api;

import com.gcgenome.lims.dto.InterpretationReserved;
import elemental2.dom.RequestInit;
import elemental2.dom.Response;
import elemental2.promise.Promise;
import lombok.experimental.UtilityClass;

import static elemental2.core.Global.JSON;
import static elemental2.core.Global.encodeURI;

@UtilityClass
public class InterpretationReservedApi {
	public Promise<InterpretationReserved[]> findLast(String snvId) {
		return FetchApi.request(encodeURI("/snvs/" + snvId + "/interpretation-reserved"))
					   .then(Response::text)
					   .then(r->{
						   if(r!=null && !r.trim().isEmpty()) return Promise.resolve((InterpretationReserved[])JSON.parse(r));
						   else return Promise.resolve((InterpretationReserved[])null);
					   });
	}
	public Promise<Void> save(String snvId, InterpretationReserved reserved) {
		RequestInit request = RequestInit.create();
		request.setMethod("PUT");
		request.setHeaders(new String[][] {
				new String[] {"Accept", "application/json"},
				new String[] {"Content-Type", "application/json; charset=utf-8"}
		});
		request.setBody(JSON.stringify(reserved));
		return FetchApi.request(encodeURI("/snvs/" + snvId + "/interpretation-reserved"), request)
					   .then(d->Promise.resolve((Void)null));
	}
	public Promise<Void> delete(String snvId, InterpretationReserved reserved) {
		RequestInit request = RequestInit.create();
		request.setMethod("DELETE");
		return FetchApi.request(encodeURI("/snvs/" + snvId + "/interpretation-reserved/" + reserved.service()), request)
				.then(d->Promise.resolve((Void)null));
	}
}
