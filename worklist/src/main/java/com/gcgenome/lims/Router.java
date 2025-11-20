package com.gcgenome.lims;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcgenome.lims.dto.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Router {
	private final ObjectMapper OM;
	public Router(ObjectMapper om) {
		OM = om;
	}

	@RequestMapping(value="/services", method= RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Page[] services() {
		return new Page[] {
			new Page().icon("fa-clipboard-list").title("Worklist").uri("/panel-service/worklist.html").order("2")
		};
	}
	/*
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
	}*/
}
