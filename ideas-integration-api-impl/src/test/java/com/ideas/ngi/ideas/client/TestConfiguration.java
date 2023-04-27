package com.ideas.ngi.ideas.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.ideas.ngi.ideas.client.props.IDeaSProperties;
import com.ideas.ngi.ideas.client.props.IDeaSRetryProperties;
import feign.Logger;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.fasterxml.jackson.databind.DeserializationFeature.*;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

@org.springframework.boot.test.context.TestConfiguration
public class TestConfiguration {

    @Bean
    public ObjectMapper getObjectMapper() {
        var javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));

        return new ObjectMapper()
                .registerModules(new Jdk8Module(), javaTimeModule)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .enable(ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                .enable(ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .disable(FAIL_ON_MISSING_CREATOR_PROPERTIES)
                .disable(FAIL_ON_NULL_CREATOR_PROPERTIES)
                .disable(FAIL_ON_IGNORED_PROPERTIES)
                .disable(WRITE_DATES_AS_TIMESTAMPS);
    }

    @Bean
    public Logger.Level logLEvel() {
        return Logger.Level.FULL;
    }

    @Bean
    public IDeaSRetryProperties retryProperties() {
        return new IDeaSRetryProperties(Duration.ofSeconds(1L), Duration.ofSeconds(1L), 2);
    }

    @Bean
    public IDeaSProperties properties() {
        return new IDeaSProperties("https://pmsinbound-internal.stage.ideasrms.com");
    }
}
