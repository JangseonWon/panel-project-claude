package com.greencross.lims;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greencross.lims.dto.Page;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class Router {
	private final ObjectMapper OM;
	public Router(ObjectMapper om) {
		OM = om;
	}

	@Bean
	public RouterFunction<ServerResponse> routerInstance() {
		return route(GET("/services"), this::pages);
	}

	private Mono<ServerResponse> pages(ServerRequest request) {
		return Flux.just(new Page().icon("fa-clipboard-list").title("Worklist").uri("/panel-service/worklist.html").order("2"))
				   .collectList()
				   .map(this::json)
				   .flatMap(list->ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(list))
				   .switchIfEmpty(ServerResponse.noContent().build())
				   .onErrorResume(e->ServerResponse.badRequest().bodyValue(e.getMessage()));
	}

	private String json(Object obj) {
		try {
			return OM.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
