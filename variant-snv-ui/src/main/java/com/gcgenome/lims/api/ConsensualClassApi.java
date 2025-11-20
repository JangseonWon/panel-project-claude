package com.gcgenome.lims.api;

import com.gcgenome.lims.dto.SnvConsensualClass;
import elemental2.dom.RequestInit;
import elemental2.dom.Response;
import elemental2.promise.Promise;
import lombok.experimental.UtilityClass;

import static elemental2.core.Global.JSON;
import static elemental2.core.Global.encodeURI;

@UtilityClass
public class ConsensualClassApi {
	public Promise<SnvConsensualClass> findLast(String snvId) {
		return FetchApi.request(encodeURI("/snvs/" + snvId + "/consensual-class"))
					   .then(Response::text)
					   .then(r->{
						   if(r!=null && !r.trim().isEmpty()) return Promise.resolve((SnvConsensualClass)JSON.parse(r));
						   else return Promise.resolve((SnvConsensualClass)null);
					   });
	}
	public Promise<SnvConsensualClass> save(String snvId, String clazz, String comment) {
		RequestInit request = RequestInit.create();
		request.setMethod("PUT");
		request.setBody(comment);
		return FetchApi.request(encodeURI("/snvs/" + snvId + "/consensual-class/" + clazz), request)
					   .then(Response::text)
					   .then(r->{
						   if(r!=null && !r.trim().isEmpty()) return Promise.resolve((SnvConsensualClass)JSON.parse(r));
						   else return Promise.resolve((SnvConsensualClass)null);
					   });
	}
	public Promise<SnvConsensualClass> save(String snvId, String comment) {
		RequestInit request = RequestInit.create();
		request.setMethod("PATCH");
		request.setBody(comment);
		return FetchApi.request(encodeURI("/snvs/" + snvId + "/consensual-class"), request)
					   .then(Response::text)
					   .then(r->{
						   if(r!=null && !r.trim().isEmpty()) return Promise.resolve((SnvConsensualClass)JSON.parse(r));
						   else return Promise.resolve((SnvConsensualClass)null);
					   });
	}
}
