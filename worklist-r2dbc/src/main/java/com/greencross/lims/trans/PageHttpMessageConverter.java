package com.greencross.lims.trans;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.lang.reflect.Type;

// Write only message converter
public class PageHttpMessageConverter extends MappingJackson2HttpMessageConverter {
	public PageHttpMessageConverter(ObjectMapper om) {
		super(om);
	}
	@Override
	public boolean canWrite(Class<?> clazz, @Nullable MediaType mediaType) {
		return Page.class.isAssignableFrom(clazz);
	}
	@Override
	public boolean canRead(MediaType mediaType) {
		return false;
	}
	@Override
	public boolean canRead(Class<?> clazz, @Nullable MediaType mediaType) {
		return false;
	}
	@Override
	protected void writeInternal(Object object, @Nullable Type type, HttpOutputMessage outputMessage) throws IOException {
		if(object instanceof Page) {
			HttpHeaders headers = outputMessage.getHeaders();
			Page<?> page = (Page<?>) object;
			headers.add("X-Total-Count", String.valueOf(page.getTotalElements()));
			headers.add("X-Total-Page", String.valueOf(page.getTotalPages()));
			headers.add("X-Current-Page", String.valueOf(page.getPageable().getPageNumber()));
			headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "X-Total-Count");
			headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "X-Total-Page");
			headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "X-Current-Page");
			super.writeInternal(page.getContent(), outputMessage);
		} else super.writeInternal(object, type, outputMessage);
	}
}
