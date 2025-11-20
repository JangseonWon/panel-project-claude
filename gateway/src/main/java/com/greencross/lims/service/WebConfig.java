package com.greencross.lims.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.WebFilter;

import java.util.concurrent.TimeUnit;

//@Configuration
//@EnableAsync
//@EnableWebFlux
public class WebConfig implements WebFluxConfigurer {
	private final ObjectMapper objectMapper;
	public WebConfig(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	@Bean
	public WebFilter addViewControllers() {
		return (exchange, chain) -> {
			if (exchange.getRequest().getURI().getPath().equals("/")) return chain.filter(exchange.mutate().request(exchange.getRequest().mutate().path("/index.html").build()).build());
			else return chain.filter(exchange);
		};
	}
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**")
				.addResourceLocations("classpath:/static/")
				.setCacheControl(CacheControl.maxAge(1, TimeUnit.MINUTES))
				.resourceChain(false);
	}
}
