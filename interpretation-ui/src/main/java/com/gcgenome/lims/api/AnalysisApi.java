package com.gcgenome.lims.api;

import com.gcgenome.lims.dto.Analysis2;
import elemental2.dom.Response;
import elemental2.promise.Promise;
import lombok.experimental.UtilityClass;

import static elemental2.core.Global.JSON;

@UtilityClass
public class AnalysisApi {
	public Promise<Analysis2[]> analysis(long sampleId) {
		return FetchApi.request("/samples/" + sampleId + "/analysis", null)
				.then(Response::text)
				.then(r -> {
					if (r != null && !r.trim().isEmpty()) return Promise.resolve((Analysis2[]) JSON.parse(r));
					else return Promise.resolve((Analysis2[]) null);
				});
	}
}
