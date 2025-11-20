package com.gcgenome.lims.api;

import com.gcgenome.lims.dto.Comment;
import elemental2.dom.RequestInit;
import elemental2.dom.Response;
import elemental2.promise.Promise;
import lombok.experimental.UtilityClass;

import static elemental2.core.Global.JSON;
import static elemental2.core.Global.encodeURI;

@UtilityClass
public class CommentApi {
	public Promise<Comment[]> find(String snvId) {
		return FetchApi.request(encodeURI("/snvs/" + snvId + "/comment"))
					   .then(Response::text)
					   .then(r->{
						   if(r!=null && !r.trim().isEmpty()) return Promise.resolve((Comment[])JSON.parse(r));
						   else return Promise.resolve((Comment[])null);
					   });
	}
	public Promise<Comment> save(String snvId, String comment) {
		RequestInit request = RequestInit.create();
		request.setMethod("PUT");
		request.setBody(comment);
		return FetchApi.request(encodeURI("/snvs/" + snvId + "/comment"), request)
					   .then(Response::text)
					   .then(r->{
						   if(r!=null && !r.trim().isEmpty()) return Promise.resolve((Comment)JSON.parse(r));
						   else return Promise.resolve((Comment)null);
					   });
	}
	public Promise<Comment> save(String snvId, long createAt, String comment) {
		RequestInit request = RequestInit.create();
		request.setMethod("PATCH");
		request.setBody(comment);
		return FetchApi.request(encodeURI("/snvs/" + snvId + "/comment/" + createAt), request)
					   .then(Response::text)
					   .then(r->{
						   if(r!=null && !r.trim().isEmpty()) return Promise.resolve((Comment)JSON.parse(r));
						   else return Promise.resolve((Comment)null);
					   });
	}
	public Promise<Response> delete(String snvId, long createAt) {
		RequestInit request = RequestInit.create();
		request.setMethod("DELETE");
		return FetchApi.request(encodeURI("/snvs/" + snvId + "/comment/" + createAt), request);
	}
}
