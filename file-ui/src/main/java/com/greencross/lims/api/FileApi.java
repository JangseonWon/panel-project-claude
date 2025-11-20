package com.greencross.lims.api;

import com.greencross.lims.dto.File;
import com.greencross.lims.dto.Promise;
import elemental2.dom.FormData;
import elemental2.dom.RequestInit;
import elemental2.dom.Response;
import lombok.experimental.UtilityClass;

import static elemental2.core.Global.JSON;

@UtilityClass
public class FileApi {
	public Promise<File[]> list(long sampleId, String service) {
		return FetchApi.request("/samples/" + sampleId + "/services/" + service + "/files", null)
					   .then(Response::text)
					   .then(r->{
						   if(r!=null && !r.trim().isEmpty()) return Promise.resolve((File[])JSON.parse(r));
						   else return Promise.resolve((File[])null);
					   });
	}
	public Promise<Response> upload(long sampleId, String service, FormData form) {
		RequestInit request = RequestInit.create();
		request.setMethod("PUT");
		request.setBody(form);
		return FetchApi.request("/samples/" + sampleId + "/services/" + service + "/files/" + form.get("file").asFile().name, request);
	}
	public Promise<Response> delete(long sampleId, String service, String fileName) {
		RequestInit request = RequestInit.create();
		request.setMethod("DELETE");
		return FetchApi.request("/samples/" + sampleId + "/services/" + service + "/files/" + fileName, request);
	}
}
