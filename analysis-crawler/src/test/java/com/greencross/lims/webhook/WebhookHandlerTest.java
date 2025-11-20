package com.greencross.lims.webhook;

import com.greencross.lims.jandiwebhook.JandiWebhook;
import org.junit.jupiter.api.Test;

class WebhookHandlerTest {
    private final WebhookHandler handler = new WebhookHandler(new JandiWebhook("https://wh.jandi.com/connect-api/webhook/17558388/67814f3b81e70468e64b72284e9844ca"), null, null, null);

    @Test
    void testWebhook() {
        handler.sendError(new Exception("Test Error"),  "test_file_name");
    }
}