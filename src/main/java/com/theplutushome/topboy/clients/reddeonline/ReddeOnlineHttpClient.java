package com.theplutushome.topboy.clients.reddeonline;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

import com.theplutushome.optimus.entity.api.redde.ReddeCheckoutRequest;
import com.theplutushome.optimus.entity.api.redde.ReddeCheckoutResponse;
import com.theplutushome.optimus.entity.api.redde.ReddeDebitRequest;
import com.theplutushome.optimus.entity.api.redde.ReddeDebitResponse;
import com.theplutushome.optimus.entity.api.redde.ReddeTransactionResponse;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

public interface ReddeOnlineHttpClient {

    @PostExchange("/receive")
    public ReddeDebitResponse initiatePayment(@RequestBody @Valid ReddeDebitRequest debitRequest);

    @GetExchange("/status/{transactionId}")
    public ReddeTransactionResponse verifyPayment(@PathVariable String transactionId);

    @PostExchange("/checkout")
    public ReddeCheckoutResponse initiateCheckout(@RequestBody @Valid ReddeCheckoutRequest checkoutRequest);
}
