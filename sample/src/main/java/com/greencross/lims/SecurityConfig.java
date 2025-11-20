package com.greencross.lims;

import com.greencross.lims.dao.UserActivatedDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter;
import reactor.core.publisher.Mono;

@Configuration
@Order(2)
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {
	private final UserActivatedDAO repo;
	public SecurityConfig(UserActivatedDAO repo) {
		this.repo = repo;
	}
	@Bean
	public AuthenticationWebFilter authenticationWebFilter() {
		AuthenticationWebFilter filter = new AuthenticationWebFilter(new UserDetailsRepositoryReactiveAuthenticationManager(userId->Mono.just(repo.loadUserByUsername(userId))));
		filter.setServerAuthenticationConverter(exchange -> Mono.fromCallable(()->{
			String id = exchange.getRequest().getHeaders().getFirst("X-USER-ID");
			if(id == null || id.isBlank()) return null;
			return new UsernamePasswordAuthenticationToken(id, "");
		}));
		return filter;
	}
	@Bean
	public SecurityWebFilterChain resourceFilterChain(ServerHttpSecurity http) {
		return http.addFilterAt(authenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
				   .cors().and()
				   .httpBasic().disable()
				   .formLogin().disable()
				   .csrf().disable()
				   .headers().frameOptions().mode(XFrameOptionsServerHttpHeadersWriter.Mode.SAMEORIGIN).and()
				   .exceptionHandling()
				   .authenticationEntryPoint((swe, e)->Mono.fromRunnable(()->swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
				   .accessDeniedHandler((swe, e)->Mono.fromRunnable(()->swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN))).and()
				   .authorizeExchange()
				   .pathMatchers("/img/**", "/css/**", "/font/**", "/*.html", "/*/*.js",
								 "/samples/*.html", "/samples/*/*.js", "/samples/*/*.css", "/samples/font/*").permitAll()
				   .pathMatchers(HttpMethod.OPTIONS).permitAll()
				   .anyExchange().authenticated().and()
				   .build();
	}
}
