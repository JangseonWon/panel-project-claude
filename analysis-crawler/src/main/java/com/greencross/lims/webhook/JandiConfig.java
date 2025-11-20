package com.greencross.lims.webhook;

import com.greencross.lims.jandiwebhook.JandiWebhook;
import com.greencross.lims.jandiwebhook.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JandiConfig {
    @Value("${greencross.webhook.url}")
    private String url;
    @Value("${greencross.webhook.url.cancer}")
    private String cancerUrl;
    @Value("${greencross.webhook.url.brca}")
    private String brcaUrl;
    @Value("${greencross.webhook.url.dgs}")
    private String dgsUrl;
    @Bean("default")
    public Webhook webhook(){
        return new JandiWebhook(url);
    }

    @Bean("cancer")
    public Webhook cancerWebhook(){
        return new JandiWebhook(cancerUrl);
    }

    @Bean("brca")
    public Webhook brcaWebhook(){
        return new JandiWebhook(brcaUrl);
    }

    @Bean("dgs")
    public Webhook dgsWebhook(){
        return new JandiWebhook(dgsUrl);
    }
}
