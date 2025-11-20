package com.greencross.lims;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greencross.lims.trans.PageHttpMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.CacheControl;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	private final String resources;
	private final ObjectMapper mapper;
	public WebMvcConfig(ObjectMapper mapper, @Value("${server.resources}") String resources) {
		this.resources = resources;
		this.mapper = mapper;
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController( "/" ).setViewName( "forward:/reading.html" );
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**")
				.addResourceLocations(resources)
				.setCacheControl(CacheControl.maxAge(1, TimeUnit.MINUTES))
				.resourceChain(false);
	}
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new ResourceHttpMessageConverter());
		converters.add(new StringHttpMessageConverter());
		converters.add(new PageHttpMessageConverter(mapper));
	}
}
