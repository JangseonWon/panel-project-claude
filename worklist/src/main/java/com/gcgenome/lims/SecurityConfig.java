package com.gcgenome.lims;

import com.gcgenome.lims.dao.UserActivatedDAO;
import com.gcgenome.lims.entity.UserActivated;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

@Configuration
@Order(2)
@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final UserActivatedDAO userRepo;
	public SecurityConfig(UserActivatedDAO userRepo) {
		this.userRepo = userRepo;
	}
	@Override
	public void configure(WebSecurity http) {
		http.ignoring().antMatchers(HttpMethod.OPTIONS, "/**")
			.antMatchers(HttpMethod.POST, "/actuator/routes")
			.antMatchers(HttpMethod.GET, "/swagger-ui/**")
			.antMatchers(HttpMethod.GET, "/v2/api-docs")
			.antMatchers(HttpMethod.GET, "/swagger-resources/**")
			.antMatchers(HttpMethod.GET, "/*.html", "/favicon.ico", "/resources/img/**", "/css/**", "/font/**", "/sound/**", "/*/*.js");
	}
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(new UserNameAuthenticationFilter(userRepo), BasicAuthenticationFilter.class)
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.exceptionHandling().authenticationEntryPoint((request, response, e)->{
				e.printStackTrace();
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}).and()
			.authorizeRequests().anyRequest().authenticated().and()
			.formLogin().disable()
			.csrf().disable();
	}
	@Bean
	public AuditorAware<UserActivated> auditorProvider() {
		return new SpringSecurityAuditor();
	}
	public static class SpringSecurityAuditor implements AuditorAware<UserActivated> {
		@Override
		public Optional<UserActivated> getCurrentAuditor() {
			try {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				if (authentication==null || !authentication.isAuthenticated()) return Optional.empty();
				Object principal = authentication.getPrincipal();
				if(principal instanceof UserActivated) return Optional.of((UserActivated) principal);
				else return Optional.empty();
			} catch(Exception e) {
				e.printStackTrace();
				return Optional.empty();
			}
		}
	}

	public static class UserNameAuthenticationFilter extends OncePerRequestFilter {
		private final UserActivatedDAO userRepo;
		public UserNameAuthenticationFilter(UserActivatedDAO userRepo) {
			this.userRepo = userRepo;
		}
		@Override
		protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
			String userId = request.getHeader("X-USER-ID");
			if(userId == null || userId.trim().isEmpty()) throw new SecurityException();
			UserDetails user = userRepo.loadUserByUsername(userId);
			Authentication auth = new Authentication() {
				@Override
				public Collection<? extends GrantedAuthority> getAuthorities() {
					return user.getAuthorities();
				}

				@Override
				public Object getCredentials() {
					return user;
				}

				@Override
				public Object getDetails() {
					return user;
				}

				@Override
				public Object getPrincipal() {
					return user;
				}

				@Override
				public boolean isAuthenticated() {
					return true;
				}

				@Override
				public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

				}
				@Override
				public String getName() {
					return user.getUsername();
				}
			};
			SecurityContextHolder.getContext().setAuthentication(auth);
			filterChain.doFilter(request, response);
		}
	}
}
