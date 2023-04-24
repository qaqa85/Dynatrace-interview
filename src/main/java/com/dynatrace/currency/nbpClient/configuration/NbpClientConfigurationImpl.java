package com.dynatrace.currency.nbpClient.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@ConfigurationProperties(prefix = "com.org.dyntrance.nbp-client")
class NbpClientConfigurationImpl implements NbpClientConfiguration {
    public static final String URL_ERROR_MESSAGE =
            "URL cannot be null. Set up property com.org.dyntrance.nbpClient.url";
    private String url;

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        Objects.requireNonNull(url, URL_ERROR_MESSAGE);

        this.url = url;
    }
}
