package com.werdersoft.personapi.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;

@Configuration
@Profile("test")
public class WireMockConfig {

    @Value("${wiremock.port}")
    private int wiremockPort;

    @Value("${wiremock.host}")
    private String wiremockHost;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer wireMockServer() {
        WireMockServer wireMockServer = new WireMockServer(wiremockPort);
        configureFor(wiremockHost, wiremockPort);
        wireMockServer.start();
        return wireMockServer;
    }


}
