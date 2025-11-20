package com.greencross.lims.api;

import com.greencross.lims.dto.*;
import elemental2.dom.RequestInit;
import elemental2.dom.Response;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

import static elemental2.core.Global.JSON;
import static elemental2.core.Global.encodeURI;

@UtilityClass
public class IssueApi {
	public Promise<Issue[]> search(Query query) {
		ProgressApi.open(false);
		RequestInit request = RequestInit.create();
		request.setMethod("POST");
		request.setHeaders(new String[][] {
				new String[] {"Accept", "application/json"},
				new String[] {"Content-Type", "application/json; charset=utf-8"}
		});
		request.setBody(JSON.stringify(query));
		return FetchApi.request(encodeURI("/issues"), request)
				.then(Response::text)
				.then(r->{
					if(r!=null && !r.trim().isEmpty()) return Promise.resolve((Issue[])JSON.parse(r));
					else return Promise.resolve((Issue[])null);
				}).finally_(ProgressApi::close);
	}
}
