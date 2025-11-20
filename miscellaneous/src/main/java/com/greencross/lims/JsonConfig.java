package com.greencross.lims;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JsonConfig {
	@Bean
	public ObjectMapper objectMapper() {
		return Jackson2ObjectMapperBuilder.json()
										  .visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
										  .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
										  .featuresToEnable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
										  .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
										  .featuresToEnable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN)
										  .featuresToDisable(MapperFeature.DEFAULT_VIEW_INCLUSION)
										  .modules(new JavaTimeModule())
										  .propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
										  .build();
	}
}
