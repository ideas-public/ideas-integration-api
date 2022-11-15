package com.ideas.ngi.ideas.client.props;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Data
@RefreshScope
@AllArgsConstructor
@ConfigurationProperties(prefix = "ideas.client")
public class IDeaSProperties {
    private String url;
}