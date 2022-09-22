package com.ideas.ngi.ideas.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.time.Duration;

@Data
@RefreshScope
@AllArgsConstructor
@ConfigurationProperties(prefix = "ideas.client.retry")
public class IDeaSRetryProperties {
    private Duration period;
    private Duration maxPeriod;
    private int maxAttempts;
}
