package com.greencross.lims.report;

import com.greencross.lims.entity.Request;

import java.io.IOException;

public interface ReportFactory<T> {
	Class<T> clazz();
	boolean match(Request request);
	default byte[] build(Request request, Object interpretation) throws IOException {
		return build(request, (T)interpretation, clazz());
	}
	byte[] build(Request request, T interpretation, Class<T> clazz) throws IOException;
	String buildLongFormText(Request request) throws IOException;
	String buildShortFormText(Request request) throws IOException;
}

