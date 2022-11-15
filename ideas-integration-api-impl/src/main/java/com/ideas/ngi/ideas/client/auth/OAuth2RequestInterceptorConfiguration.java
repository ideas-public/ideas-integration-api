package com.ideas.ngi.ideas.client.auth;

import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
@RequiredArgsConstructor
public class OAuth2RequestInterceptorConfiguration {

    public static final String AUTH_SERVER_NAME = "pmsInboundAuth";
    private final OAuth2Provider oAuth2Provider;

    @Bean
    public RequestInterceptor pmsInboundAuthInterceptor() {
        return requestTemplate -> requestTemplate.header(HttpHeaders.AUTHORIZATION,
                oAuth2Provider.getAuthenticationToken(AUTH_SERVER_NAME));
    }
}
