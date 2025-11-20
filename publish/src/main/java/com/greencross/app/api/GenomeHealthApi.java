package com.greencross.app.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

@Configuration
@Slf4j
public class GenomeHealthApi {
	private final Logger Log = LoggerFactory.getLogger(getClass());
	@Value("${genomehealth.api.url}")
	private String appUrl;
	@Value("${genomehealth.api.key}")
	private String apiKey;
	public String post(String publicKey, String code, Object dto) {
		try{
			String url = appUrl.replace("{:publicKey}", publicKey).replace("{:serviceCode}", code);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
			headers.set("api-key", apiKey);
			ObjectMapper objectMapper = new ObjectMapper();
			String json = objectMapper.writeValueAsString(dto);
			HttpEntity entity = new HttpEntity(json,headers);
			RestTemplate restTemplate = new RestTemplate();
			Log.info("Message sent to " + url + ":");
			Log.info(json);
			ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
			log.info(String.valueOf(response.getStatusCode()));
			log.info(String.valueOf(response.getBody()));
			return response.getBody();
		} catch (Exception e){
			log.error(e.getMessage(), e);
			return e.getMessage();
		}
	}
	public String postFile(String publicKey, String code, byte[] byteFile) {
		try {
			String url = appUrl.replace("{:publicKey}", publicKey).replace("{:serviceCode}", code) + "/pdf";
			ByteArrayResource fileResource = new ByteArrayResource(byteFile) {
				@Override
				public String getFilename() {
					return code + ".pdf";
				}
			};
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.set("api-key", apiKey);
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("file", fileResource);
			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
			log.info(String.valueOf(response.getStatusCode()));
			log.info(String.valueOf(response.getStatusCodeValue()));
			return response.getBody();
		} catch (Exception e){
			log.error(e.getMessage(), e);
			return e.getMessage();
		}
	}
}