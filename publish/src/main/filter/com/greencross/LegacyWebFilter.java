package com.greencross;

import com.greencross.lims.entity.Request;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.util.Set;

@Component
public class LegacyWebFilter implements WebFilter {
    @PersistenceContext
    EntityManager em;
    private final WebClient webClient = WebClient.builder().baseUrl("http://localhost:28977/").build();
    private final Set<Pair<Set<String>, LocalDate>> targets = Set.of(
            targetOf(LocalDate.of(2025, 5, 7),"N082", "N094", "ON082", "N064", "N065", "ON064", "ON065", "N083", "N093", "ON083", "N104", "N105", "ON104")
    );

    @NotNull
    @Override
    public Mono<Void> filter(@NotNull ServerWebExchange exchange, @NotNull WebFilterChain chain) {
        return Mono.just("").publishOn(Schedulers.boundedElastic())
                .map((s) -> em.find(Request.class, Request.RequestPK.builder().sample(Long.parseLong(parseParam(exchange, 3))).service(parseParam(exchange, 7)).build()))
                .publishOn(Schedulers.parallel())
                .filter(this::isRequestInTargets)
                .switchIfEmpty(Mono.error(new IllegalArgumentException()))
                .flatMap(req -> {
                    ServerHttpRequest request = exchange.getRequest();
                    var conn = getConn(request, webClient, exchange.getRequest().getPath().subPath(1).value());
                    return conn.exchangeToMono(entity -> {
                        var response = exchange.getResponse();
                        response.setStatusCode(entity.statusCode());
                        response.getHeaders().addAll(entity.headers().asHttpHeaders());
                        return response.writeAndFlushWith(entity.body(BodyExtractors.toDataBuffers()).window(1));
                    });
                }).onErrorResume(Exception.class, (e) -> chain.filter(exchange));
    }

    private Pair<Set<String>, LocalDate> targetOf(LocalDate date, String... services) {
        return Pair.of(Set.of(services), date);
    }
    private String parseParam(ServerWebExchange exchange, int index){
        String subPath = exchange.getRequest().getPath().subPath(index).value();
        return subPath.substring(0, subPath.indexOf("/"));
    }
    private boolean isRequestInTargets(Request req) {
        return targets.stream().anyMatch(target -> target.getFirst().contains(req.pk().service()) && req.dateRequest().isBefore(target.getSecond()));
    }

    private WebClient.RequestHeadersSpec<?> getConn(ServerHttpRequest request, WebClient client, String url) {
        WebClient.RequestHeadersSpec<?> conn = client.get();
        if (request.getMethod() == HttpMethod.GET) conn = client.get().uri(url);
        else if (request.getMethod() == HttpMethod.POST)
            conn = client.post().uri(url).body(BodyInserters.fromDataBuffers(request.getBody()));
        else if (request.getMethod() == HttpMethod.PATCH)
            conn = client.patch().uri(url).body(BodyInserters.fromDataBuffers(request.getBody()));
        else if (request.getMethod() == HttpMethod.PUT)
            conn = client.put().uri(url).body(BodyInserters.fromDataBuffers(request.getBody()));
        else if (request.getMethod() == HttpMethod.DELETE) conn = client.delete().uri(url);
        else if (request.getMethod() == HttpMethod.HEAD) conn = client.head().uri(url);
        else if (request.getMethod() == HttpMethod.OPTIONS) conn = client.options().uri(url);
        conn.headers(h -> request.getHeaders().forEach(h::addAll));
        return conn;
    }
}