package com.gcgenome.lims.api;

import com.gcgenome.lims.dto.Api;
import com.gcgenome.lims.dto.Method;
import com.gcgenome.lims.dto.Report;
import elemental2.dom.Blob;
import elemental2.dom.DomGlobal;
import elemental2.dom.RequestInit;
import elemental2.dom.Response;
import elemental2.promise.Promise;
import lombok.experimental.UtilityClass;

import static elemental2.core.Global.JSON;

@UtilityClass
public class ReportApi {
	public Promise<Report[]> reports(long sample, String service) {
		return FetchApi.request("/samples/" + sample + "/services/" + service + "/reports", null)
						.then(Response::text)
						.then(r->{
							if(r!=null && !r.trim().isEmpty()) return Promise.resolve((Report[]) JSON.parse(r));
							else return Promise.resolve((Report[])null);
						});
	}
	public Promise<Report> print(long sample, String service, String description) {
		RequestInit request = RequestInit.create();
		request.setMethod("PUT");
		request.setHeaders(new String[][] {
				new String[] {"Content-Type", "text/plain; charset=utf-8"}
		});
		request.setBody(description);
		return FetchApi.request("/samples/" + sample + "/services/" + service + "/print", request)
						.then(Response::text)
						.then(r->{
							if(r!=null && !r.trim().isEmpty()) return Promise.resolve((Report) JSON.parse(r));
							else return Promise.resolve((Report)null);
						});
	}
	public Promise<Response> publish(long sample, String service, long createAt) {
		RequestInit request = RequestInit.create();
		request.setMethod("PUT");
		request.setHeaders(new String[][] {
				new String[] {"Content-Type", "application/json; charset=utf-8"}
		});
		return FetchApi.request("/samples/" + sample + "/services/" + service + "/reports/" + createAt + "/publish", request);
	}
	public Promise<Blob> download(String url) {
		return FetchApi.request(url, null)
						.then(Response::blob)
						.then(blob->Promise.resolve(blob.slice(0, blob.size, "application/pdf")));
	}
	public void download(String url, String fileName) {
		Api api = new Api().method(Method.GET).url(url).fileName(fileName);
		DomGlobal.window.parent.postMessage(JSON.stringify(api), "*");
	}
	public Promise<String> state(long sample, String service) {
		return FetchApi.request("/samples/" + sample + "/services/" + service + "/state", null).then(Response::text);
	}

	public static Promise<Blob> downloadLogs(long sample, String service) {
		return FetchApi.request("/samples/"+sample+"/services/"+service+"/reports/pdf", null).then(Response::blob);
	}
}
