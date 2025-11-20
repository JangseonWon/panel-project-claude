package com.greencross.lims.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greencross.lims.dto.Page;
import com.greencross.lims.dto.Service;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.stream.Stream;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class Router {
	private final ReactiveDiscoveryClient discoveryClient;
	private final LoadBalancerClient loadBalancerClient;
	private final ObjectMapper OM;
	public Router(ReactiveDiscoveryClient discoveryClient, LoadBalancerClient loadBalancerClient, ObjectMapper om) {
		this.discoveryClient = discoveryClient;
		this.loadBalancerClient = loadBalancerClient;
		OM = om;
	}

	@Bean
	public RouterFunction<ServerResponse> serviceRouterInstance() {
		return route(GET("/services"), this::services);
	}

	private Mono<ServerResponse> services(ServerRequest request) {
		return list(request).map(this::json)
					 .flatMap(list->ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(list))
					 .switchIfEmpty(ServerResponse.noContent().build())
					 .onErrorResume(e->ServerResponse.badRequest().bodyValue(e.getMessage()));
	}
	public Mono<Service> list(ServerRequest request) {
		//WebClient.create().mutate().
		return discoveryClient.getServices()
							  .filter(service->!"panel-service-gateway".equalsIgnoreCase(service))
							  .map(loadBalancerClient::choose)
							  .map(svc->svc.getUri() + "/services")
							  .map(WebClient::create)
							  .flatMap(client->request(client, request))
							  .filter(Objects::nonNull)
							  .collect(Service::new, (svc, page)->{
								  if(svc.children() == null) svc.children(new Page[]{page});
								  else {
									  Page[] pages = Stream.concat(Arrays.stream(svc.children()), Stream.of(page))
														   .sorted(Comparator.comparing(Page::order, Comparator.nullsLast(Comparator.naturalOrder())))
														   .toArray(Page[]::new);
									  svc.children(pages);
								  }
							  }).map(svc->svc.title("패널검사").order("5").prefix("/panel-service"))
							  .onErrorContinue((ignore, o)->{});
	}
	private Flux<Page> request(WebClient client,ServerRequest request) {
		return client.get()
					 .headers(h->request.headers().asHttpHeaders().forEach(h::addAll))
					 .accept(MediaType.APPLICATION_JSON)
					 .retrieve()
					 .toEntity(String.class)
					 .filter(r->r.getStatusCode()==HttpStatus.OK)
					 .map(HttpEntity::getBody)
					 .map(str->{
						 try {
							 return OM.readValue(str, Page[].class);
						 } catch (JsonProcessingException e) {
							 throw new RuntimeException();
						 }
					 }).flatMapMany(Flux::fromArray)
					 .timeout(Duration.ofMillis(300));
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
