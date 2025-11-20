package com.greencross.lims;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greencross.lims.trans.PageHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {
	private final ObjectMapper mapper;
	public WebMvcConfig(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController( "/" ).setViewName( "forward:/kanban.html" );
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new ResourceHttpMessageConverter());
		converters.add(new StringHttpMessageConverter());
		converters.add(new PageHttpMessageConverter(mapper));
		converters.add(new ByteArrayHttpMessageConverter());
	}
	/*@Bean
	public AuditorAware<String> auditorProvider() {
		return new SpringSecurityAuditor();
	}
	public static class SpringSecurityAuditor implements AuditorAware<String> {
		@Override
		public Optional<String> getCurrentAuditor() {
			try {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				if (authentication==null || !authentication.isAuthenticated()) return Optional.empty();
				Object principal = authentication.getPrincipal();
				if(principal instanceof UserActivated) return Optional.of(((UserActivated) principal).email());
				else return Optional.empty();
			} catch(Exception e) {
				e.printStackTrace();
				return Optional.empty();
			}
		}
	}*/
}
