package com.gcgenome.lims;

import com.gcgenome.lims.dao.UserActivatedDAO;
import com.gcgenome.lims.entity.UserActivated;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Configuration
@Order(2)
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class SecurityConfig {
	private final UserActivatedDAO repo;
	public SecurityConfig(UserActivatedDAO repo) {
		this.repo = repo;
	}
	@Bean
	public AuthenticationWebFilter authenticationWebFilter() {
		ReactiveAuthenticationManager mgr = new ReactiveAuthenticationManager() {
			private void preAuthenticationChecks(UserDetails user) {
				if (!user.isAccountNonLocked()) throw new LockedException("");
				if (!user.isEnabled()) throw new DisabledException("");
				if (!user.isAccountNonExpired()) throw new AccountExpiredException("");
			}
			private void postAuthenticationChecks(UserDetails user) {
				if (!user.isCredentialsNonExpired()) throw new CredentialsExpiredException("");
			}
			@Override
			public Mono<Authentication> authenticate(Authentication authentication) {
				String username = authentication.getName();
				return Mono.justOrEmpty(retrieveUser(username))
						   .doOnNext(this::preAuthenticationChecks)
						   .doOnNext(this::postAuthenticationChecks)
						   .map(this::createUsernamePasswordAuthenticationToken)
						   .doOnSuccess(SecurityContextHolder.getContext()::setAuthentication);
			}
			private UserDetails retrieveUser(String userName) {
				return repo.loadUserByUsername(userName);
			}
			private Authentication createUsernamePasswordAuthenticationToken(UserDetails userDetails) {
				return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
															   userDetails.getAuthorities());
			}
		};
		AuthenticationWebFilter filter = new AuthenticationWebFilter(mgr);
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
				   .pathMatchers("/img/**", "/css/**", "/font/**", "/*.html", "/*/*.js").permitAll()
				   .pathMatchers(HttpMethod.OPTIONS).permitAll()
				   .anyExchange().authenticated().and()
				   .build();
	}
	@Bean
	public AuditorAware<UserActivated> auditorProvider() {
		return ()->{
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			System.out.println("Auth:" + authentication);
			if(authentication==null) return Optional.empty();
			Object principal = authentication.getPrincipal();
			System.out.println("Principal:" + principal);
			if(principal instanceof UserActivated) return Optional.of((UserActivated) principal);
			else return Optional.empty();
		};
	}
}
