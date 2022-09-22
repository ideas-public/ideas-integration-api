package com.ideas.ngi.ideas.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Capability;
import feign.Feign;
import feign.Logger;
import feign.Retryer;
import feign.codec.Decoder;
import feign.httpclient.ApacheHttpClient;
import feign.jackson.JacksonEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IDeaSClientFactory {

    private final ObjectMapper objectMapper;
    private final Logger.Level logLevel;
    private final IDeaSRetryProperties ideaSRetryProperties;
    private final IDeaSProperties iDeaSProperties;

    @SuppressWarnings("unchecked")
    public <T> T build(IDeaSDataType dataType) {
        IDeaSClient client = IDeaSClient.get(dataType);
        return (T) Feign.builder()
                .client(new ApacheHttpClient())
                .contract(new SpringMvcContract())
                .logLevel(logLevel)
                .encoder(new JacksonEncoder(objectMapper))
                .decoder(httpClientDecoder(objectMapper))
                .errorDecoder(new IDeaSErrorDecoder())
                .retryer(new Retryer.Default(ideaSRetryProperties.getPeriod().toMillis(), ideaSRetryProperties.getMaxPeriod().toMillis(), ideaSRetryProperties.getMaxAttempts()))
                .target(client.getClazz(), iDeaSProperties.getUrl());
    }

    private Decoder httpClientDecoder(ObjectMapper objectMapper) {
        HttpMessageConverter<Object> jacksonConverter = new MappingJackson2HttpMessageConverter(objectMapper);
        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);

        return new IDeaSDecoder(new ResponseEntityDecoder(new SpringDecoder(objectFactory)));
    }
}
