package com.ideas.ngi.ideas.client;

import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.Decoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;

@Slf4j
public class IDeaSDecoder implements Decoder {
    final Decoder delegate;

    public IDeaSDecoder(Decoder delegate) {
        Objects.requireNonNull(delegate, "Decoder must not be null. ");
        this.delegate = delegate;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        if (response.status() >= 400) {
            String errorMessage = "Request failed when calling IDeaS Error code: " + response.status();
            log.error(errorMessage);
            throw new RetryableException(response.status(), errorMessage, response.request().httpMethod(), null, response.request());
        }

        return delegate.decode(response, type);
    }
}
