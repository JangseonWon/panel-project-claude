package com.greencross.lims.webhook;

import com.greencross.lims.jandiwebhook.Webhook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WebhookHandler {
    private final Webhook defaultWebhook;
    private final Webhook cancerWebhook;
    private final Webhook brcaWebhook;
    private final Webhook dgsWebhook;
    private final Pattern indexPattern = Pattern.compile("analysis-snv-\\d{2}(.+)\\d{3}");

    public WebhookHandler(@Qualifier("default") Webhook defaultWebhook, @Qualifier("cancer") Webhook cancerWebhook, @Qualifier("brca") Webhook brcaWebhook, @Qualifier("dgs") Webhook dgsWebhook) {
        this.defaultWebhook = defaultWebhook;
        this.cancerWebhook = cancerWebhook;
        this.brcaWebhook = brcaWebhook;
        this.dgsWebhook = dgsWebhook;
    }

    public void sendError(Throwable error, String fileName){
        try {
            defaultWebhook.send("Error : " + error.getClass().getName() +"\nMessage : " + error.getMessage() + "\nFile Name : " + fileName);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendProgress(String serial, List<VariantCountRequest> requests, long hits) {
        var request = requests.get(0);
        try {
            String requestString = requests.stream().map(req -> req.getSample() + "/" + req.getService()).collect(Collectors.joining(", "));
            Matcher indexMatch = indexPattern.matcher(request.getIndex());
            String message = parseMessage(serial, request, requestString, hits);
            if(!indexMatch.find()){
                defaultWebhook.send(message);
                log.error("Can't find Index with webhook message? Check regex.");
                return;
            }
            switch (indexMatch.group(1).toLowerCase()) {
                case "brca" -> brcaWebhook.send(message);
                case "dgs" -> dgsWebhook.send(message);
                case "cancer", "g2200201-cancer" -> cancerWebhook.send(message);
            }
            defaultWebhook.send(message);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private String parseMessage(String serial, VariantCountRequest request, String requestString, long hits){
        return "Serial : " + serial + "\n분석 : " + request.getIndex().replace("analysis-snv-", "") + "\n의뢰 : " + requestString +"\n변이 수 : " + hits;
    }
}
