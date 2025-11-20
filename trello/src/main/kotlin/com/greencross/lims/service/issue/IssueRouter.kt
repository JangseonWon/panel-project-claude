package com.greencross.lims.service.issue

import com.greencross.lims.dto.Query_
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono

@Configuration
open class IssueRouter(
    private var handler: IssueHandler
) {
    @Bean("SnvRouter")
    open fun route() = router {
        POST("/issues", contentType(MediaType("application", "json", Charsets.UTF_8)), ::search)
    }
    private fun search(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(Query_::class.java)
            .map(handler::search)
            .flatMap { page->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                    .header("X-Total-Count", page.totalElements.toString())
                    .header("X-Total-Page", page.totalPages.toString())
                    .bodyValue(page.content)
            }.switchIfEmpty(ServerResponse.status(HttpStatus.NO_CONTENT).build())
    }
}