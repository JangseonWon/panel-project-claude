package com.greencross;

import com.greencross.lims.jandiwebhook.JandiWebhook;
import com.greencross.lims.jandiwebhook.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JandiWebhookConfig {
	@Value("${webhook.url}")
	private String url;
	@Bean
	public Webhook webhook(){
		return new JandiWebhook(url);
	}
}
