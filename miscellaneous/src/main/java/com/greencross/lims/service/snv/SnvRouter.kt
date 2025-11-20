package com.greencross.lims.service.snv

import com.greencross.lims.dto.QueryServerside
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono

@Configuration
open class SnvRouter(
    private var handler: SnvHandler
) {
    @Bean("SnvRouter")
    open fun route() = router {
        POST("/misc/snvs", ::search)
    }
    private fun search(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(QueryServerside::class.java)
            .flatMap { p->handler.snvs(p) }
            .flatMap { page->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                    .header("X-Total-Count", page.totalElements.toString())
                    .header("X-Total-Page", page.totalPages.toString())
                    .bodyValue(page.content)
            }.switchIfEmpty(ServerResponse.status(HttpStatus.NO_CONTENT).build())
    }
}