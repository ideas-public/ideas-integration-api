package com.ideas.ngi.ideas.client.decoder;

import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * IDeaSErrorDecoder is needed to fine-grained customization of Retry mechanism
 */
@Slf4j
@RequiredArgsConstructor
public class IDeaSErrorDecoder extends ErrorDecoder.Default {

    @SneakyThrows
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 400) {
            String errorMessage = "Request failed when calling IDeaS. Error code: " + response.status();
            log.error(errorMessage);
            throw new RetryableException(response.status(), errorMessage, response.request().httpMethod(), null, response.request());
        }

        return callParentDecode(methodKey, response);
    }

    /**
     * Calls parent class decode method
     *
     * @param methodKey
     * @param response
     * @return
     */
    protected Exception callParentDecode(final String methodKey, final Response response) {
        return super.decode(methodKey, response);
    }
}
