package com.theplutushome.topboy.clients.reddeonline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.theplutushome.optimus.entity.api.redde.ReddeCheckoutRequest;
import com.theplutushome.optimus.entity.api.redde.ReddeCheckoutResponse;
import com.theplutushome.optimus.entity.api.redde.ReddeDebitRequest;
import com.theplutushome.optimus.entity.api.redde.ReddeDebitResponse;
import com.theplutushome.optimus.entity.api.redde.ReddeTransactionResponse;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:application.yml")
@Component
public class ReddeOnlineRestClient implements ReddeOnlineHttpClient {

    @Autowired
    private RestClient reddeOnlineClient;

    private final String apiKey;
    private final String appId;

    @Autowired
    public ReddeOnlineRestClient(Environment env) {
        this.apiKey = env.getProperty("redde_online_api_key");
        this.appId = env.getProperty("redde_online_app_id");
    }

    @Override
    public ReddeDebitResponse initiatePayment(ReddeDebitRequest debitRequest) {
        Map<String, String> headers = new HashMap<>();
        headers.put("apikey", apiKey);
        headers.put("Content-Type", "application/json;charset=UTF-8");

        return reddeOnlineClient.post()
                .uri("/receive")
                .headers(httpHeaders -> httpHeaders.setAll(headers))
                .body(debitRequest)
                .retrieve()
                .body(ReddeDebitResponse.class);
    }

    @Override
    public ReddeTransactionResponse verifyPayment(String clienttransid) {
        Map<String, String> headers = new HashMap<>();
        headers.put("apikey", apiKey);
        headers.put("appid", appId);
        headers.put("Content-Type", "application/json;charset=UTF-8");

        return reddeOnlineClient.get()
                .uri("/status/{transactionid}", clienttransid)
                .headers(httpHeaders -> httpHeaders.setAll(headers))
                .retrieve()
                .body(ReddeTransactionResponse.class);
    }

    @Override
    public ReddeCheckoutResponse initiateCheckout(@Valid ReddeCheckoutRequest checkoutRequest) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");

        return reddeOnlineClient.post()
                .uri("/checkout")
                .headers(httpHeaders -> httpHeaders.setAll(headers))
                .body(checkoutRequest)
                .retrieve()
                .body(ReddeCheckoutResponse.class);
    }

}
