package com.theplutushome.topboy.config;

import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.Credentials;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.core5.http.HttpHost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@PropertySource("classpath:application.yml")
@Configuration
public class RestClientConfig {

    private final String PROXY_SERVER_HOST;
    private final int PROXY_SERVER_PORT;
    private final String PROXY_USERNAME;
    private final String PROXY_PASSWORD;

    @Autowired
    public RestClientConfig(Environment env) {
        this.PROXY_SERVER_HOST = Objects.requireNonNull(env.getProperty("proxy.server.host"), "Proxy server host is required");
        this.PROXY_USERNAME = Objects.requireNonNull(env.getProperty("proxy.server.username"), "Proxy username is required");
        this.PROXY_PASSWORD = Objects.requireNonNull(env.getProperty("proxy.server.password"), "Proxy password is required");
        this.PROXY_SERVER_PORT = Integer.parseInt(Objects.requireNonNull(env.getProperty("proxy.server.port")));
    }

    @Bean(name = "cryptomusClient")
    public RestClient cryptomusRestClient() {
        return RestClient.builder()
                .baseUrl("https://api.heleket.com/")
                .build();
    }

    @Bean(name = "hubtelReceiveMoneyClient")
    public RestClient hubtelRestClient() {
        return RestClient.builder()
                .baseUrl("https://rmp.hubtel.com/merchantaccount/merchants/")
                .build();
    }

//    @Bean
//    public  RestTemplateBuilder restTemplateBuilder(){
//        return new RestTemplateBuilder();
//    }
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Define proxy
        HttpHost proxy = new HttpHost(PROXY_SERVER_HOST, PROXY_SERVER_PORT);
        AuthScope authScope = new AuthScope(PROXY_SERVER_HOST, PROXY_SERVER_PORT);

        // Set credentials for the proxy
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        Credentials proxyCredentials = new UsernamePasswordCredentials(PROXY_USERNAME, PROXY_PASSWORD.toCharArray());
        credentialsProvider.setCredentials(authScope, proxyCredentials);

        // Configure HttpClient with proxy and credentials
        CloseableHttpClient httpClient = HttpClients.custom()
                .setRoutePlanner(new DefaultProxyRoutePlanner(proxy))
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();

        // Set HttpClient in RestTemplate
        return builder
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(httpClient))
                .build();
    }

    @Bean("hubtelPaymentUrlGenerationClient")
    public RestClient hubtelPaymentUrlGenerationRestClient() {
        return RestClient.builder()
                .baseUrl("https://payproxyapi.hubtel.com")
                .build();
    }

    @Bean(name = "hubtelSMSClient")
    public RestClient hubtelSMSClient() {
        return RestClient.builder()
                .baseUrl("https://sms.hubtel.com/v1/messages")
                .build();
    }

    @Bean(name = "reddeOnlineClient")
    public RestClient reddeOnlineClient() {
        return RestClient.builder()
                .baseUrl("https://api.reddeonline.com/v1")
                .build();
    }
}
