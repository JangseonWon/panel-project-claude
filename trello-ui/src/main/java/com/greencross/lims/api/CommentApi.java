package com.greencross.lims.api;

import com.greencross.lims.dto.Comment;
import com.greencross.lims.dto.Promise;
import elemental2.dom.Response;
import lombok.experimental.UtilityClass;

import static elemental2.core.Global.JSON;
import static elemental2.core.Global.encodeURI;

@UtilityClass
public class CommentApi {
	public Promise<Comment[]> find(long sample, String service) {
		return FetchApi.request(encodeURI("/issue/" + sample + "/" + service + "/comments"))
				.then(Response::text)
				.then(r->{
					if(r!=null && !r.trim().isEmpty()) return Promise.resolve((Comment[])JSON.parse(r));
					else return Promise.resolve((Comment[])null);
				});
	}
}
