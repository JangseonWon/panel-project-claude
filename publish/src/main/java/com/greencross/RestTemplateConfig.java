package com.greencross;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class RestTemplateConfig {
    @Bean
    @LoadBalanced
    public WebClient.Builder LBWebclientBuilder() {
        return WebClient.builder();
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplateLoadBalanced() {
        return new RestTemplate();
    }
}
