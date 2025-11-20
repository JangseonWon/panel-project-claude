package com.greencross.lims;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.codec.CodecCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfig implements WebFluxConfigurer {
    private final String resources;
    public WebConfig(ObjectMapper mapper, @Value("${server.resources}") String resources) {
        this.resources = resources;
    }
    @Bean
    public CodecCustomizer maxInMemorySizeCodecCustomizer() {
        return (configurer) -> configurer.defaultCodecs().maxInMemorySize(-1);
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations(resources)
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.MINUTES))
                .resourceChain(false);
    }
}

