package com.theplutushome.topboy.clients.hubtel;

import com.theplutushome.optimus.entity.api.hubtel.*;
import com.theplutushome.topboy.entity.api.hubtel.PaymentLinkRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface HubtelHttpClient {
    @PostExchange("")
    public PaymentResponse initiatePayment(@RequestBody @Valid PaymentRequest paymentRequest);

    @GetExchange("")
    public TransactionStatusCheckResponse checkTransaction(@RequestParam(value = "clientReference", required = true) String clientReference);

    @GetExchange("")
    public SMSResponse sendSMS(@RequestParam(value = "to") String to, @RequestParam(value = "content") String content);

    @PostExchange("")
    public PaymentLinkResponse getPaymentUrl(@RequestBody PaymentLinkRequest paymentLinkRequest);
}
