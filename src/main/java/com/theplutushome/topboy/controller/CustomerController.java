package com.theplutushome.topboy.controller;

import com.theplutushome.optimus.entity.api.hubtel.HubtelCallBack;
import com.theplutushome.optimus.entity.api.redde.ReddeCheckoutRequest;
import com.theplutushome.optimus.entity.api.redde.ReddeCheckoutResponse;
import com.theplutushome.topboy.clients.hubtel.HubtelRestClient;
import com.theplutushome.topboy.clients.reddeonline.ReddeOnlineRestClient;
import com.theplutushome.topboy.dto.AvailableCategoryDTO;
import com.theplutushome.topboy.dto.ProxyOrderInternalRequest;
import com.theplutushome.topboy.dto.ProxyOrderRequest;
import com.theplutushome.topboy.entity.PaymentCallback;
import com.theplutushome.topboy.entity.enums.CodeCategory;
import com.theplutushome.topboy.entity.enums.PaymentOrderStatus;
import com.theplutushome.topboy.entity.ProxyCode;
import com.theplutushome.topboy.entity.ProxyOrder;
import com.theplutushome.topboy.entity.SaleLog;
import com.theplutushome.topboy.entity.api.hubtel.PaymentLinkRequest;
import com.theplutushome.topboy.entity.api.hubtel.PaymentLinkResponse;
import com.theplutushome.topboy.entity.api.redde.ReddeCallback;
import com.theplutushome.topboy.repository.PaymentCallbackRepository;
import com.theplutushome.topboy.repository.ProxyCodeRepository;
import com.theplutushome.topboy.repository.ProxyPriceConfigRepository;
import com.theplutushome.topboy.repository.SaleLogRepository;
import com.theplutushome.topboy.service.OrderService;
import com.theplutushome.topboy.util.Function;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
@Slf4j
@PropertySource("classpath:application.yml")
public class CustomerController {

    private final ProxyCodeRepository proxyCodeRepository;
    private final ProxyPriceConfigRepository proxyPriceConfigRepository;
    private final ReddeOnlineRestClient reddeOnlineRestClient;
    private final PaymentCallbackRepository paymentCallbackRepository;
    private final SaleLogRepository saleLogRepository;
    private final OrderService orderService;
    private final HubtelRestClient hubtelClient;
    private final String MERCHANT_CODE;
    private final String CALLBACK_URL;
    private final String RETURN_URL;
    private final String CANCELLATION_URL;
    private final String LOGO_URL;
    private final String REDDE_KEY;
    private final String REDDE_APP_ID;

    public CustomerController(Environment env, ProxyCodeRepository proxyCodeRepository, OrderService orderService,
            HubtelRestClient hubtelRestClient, ProxyPriceConfigRepository priceConfigRepository, SaleLogRepository saleLogRepository,
            ReddeOnlineRestClient reddeOnlineRestClient, PaymentCallbackRepository paymentCallbackRepository) {
        this.proxyCodeRepository = proxyCodeRepository;
        this.proxyPriceConfigRepository = priceConfigRepository;
        this.reddeOnlineRestClient = reddeOnlineRestClient;
        this.paymentCallbackRepository = paymentCallbackRepository;
        this.saleLogRepository = saleLogRepository;
        this.orderService = orderService;
        this.hubtelClient = hubtelRestClient;
        this.MERCHANT_CODE = Objects.requireNonNull(env.getProperty("pos_sales_id"), "Merchant Code not set");
        this.CALLBACK_URL = Objects.requireNonNull(env.getProperty("callback_url"), "Callback not set");
        this.CANCELLATION_URL = Objects.requireNonNull(env.getProperty("cancellation_url"), "Cancellation Url not set");
        this.RETURN_URL = Objects.requireNonNull(env.getProperty("return_url"), "Return Url not set");
        this.LOGO_URL = Objects.requireNonNull(env.getProperty("logo_url"), "Logo url not set");
        this.REDDE_KEY = Objects.requireNonNull(env.getProperty("redde_online_api_key"), "Redde online api key not set");
        this.REDDE_APP_ID = Objects.requireNonNull(env.getProperty("redde_online_app_id"), "Redde online app id not set");
    }

    @GetMapping("/proxy-categories")
    public ResponseEntity<List<AvailableCategoryDTO>> getAvailableCategories() {
        List<AvailableCategoryDTO> result = Arrays.stream(CodeCategory.values())
                .map(category -> new AvailableCategoryDTO(
                category,
                proxyCodeRepository.countByCategoryAndSoldFalseAndArchivedFalse(category),
                Function.getUnitPrice(category, proxyPriceConfigRepository),
                category.name() + " proxies"))
                .filter(dto -> dto.getAvailableCount() > 0)
                .toList();

        return ResponseEntity.ok(result);
    }

    @PostMapping("/hubtel/checkout")
    public ResponseEntity<PaymentLinkResponse> generateInvoice(@Valid @RequestBody ProxyOrderRequest request) {
        log.info("Payment request received: {}", request);

        int available = proxyCodeRepository.countByCategoryAndSoldFalseAndArchivedFalse(request.getCategory());
        if (request.getQuantity() > available) {
            return ResponseEntity.badRequest().body(null);
        }

        // ✅ Enrich the request
        ProxyOrderInternalRequest enriched = new ProxyOrderInternalRequest();
        BeanUtils.copyProperties(request, enriched);
        enriched.setStatus(PaymentOrderStatus.PENDING);
        enriched.setDescription("Purchase of " + request.getQuantity() + " " + request.getCategory() + " proxies");
        enriched.setClientReference("TOPBOY-" + UUID.randomUUID().toString().substring(0, 15));
        enriched.setCallbackUrl(CALLBACK_URL);
        enriched.setReturnUrl(RETURN_URL);
        enriched.setCancellationUrl(CANCELLATION_URL);

        // ✅ Persist and generate link
        orderService.createOrder(enriched);
        PaymentLinkResponse paymentLink = hubtelClient.getPaymentUrl(toHubtelRequest(enriched));

        return ResponseEntity.ok(paymentLink);
    }

    @PostMapping("/redde/checkout")
    public ResponseEntity<ReddeCheckoutResponse> initiateReddeCheckout(@RequestBody @Valid ProxyOrderRequest request) {
        System.out.println("The payment request-: " + request.toString());

        ProxyOrderInternalRequest enriched = new ProxyOrderInternalRequest();
        BeanUtils.copyProperties(request, enriched);
        enriched.setStatus(PaymentOrderStatus.PENDING);
        enriched.setDescription("Purchase of " + request.getQuantity() + " " + request.getCategory() + " proxies");
        enriched.setClientReference("TOPBOY-" + UUID.randomUUID().toString().substring(0, 15));
        enriched.setCallbackUrl(CALLBACK_URL);
        enriched.setReturnUrl(RETURN_URL);
        enriched.setCancellationUrl(CANCELLATION_URL);

        orderService.createOrder(enriched);

        ReddeCheckoutResponse paymentLink = reddeOnlineRestClient.initiateCheckout(toReddeRequest(enriched));
        return ResponseEntity.ok(paymentLink);
    }

    @PostMapping("/hubtel/callback")
    public ResponseEntity<String> handlePaymentCallback(@RequestBody HubtelCallBack callback) {
        log.info("Received Hubtel Callback: {}", callback);
        PaymentCallback cb = Function.createRecordOfCallback(callback, paymentCallbackRepository);
        paymentCallbackRepository.save(cb);

        String status = callback.getStatus();
        String clientRef = callback.getData().getClientReference();

        return processCallback(clientRef, status, cb.getChannel());
    }

    @GetMapping("/redde/callback")
    public ResponseEntity<String> handleCallbackPing() {
        // return 200 OK for GET requests, no processing
        return ResponseEntity.ok("Callback endpoint is reachable");
    }

    @PostMapping("/redde/callback")
    public ResponseEntity<?> reddeCallback(@RequestBody ReddeCallback callback) {
        log.info("Redde payment callback received: {}", callback.toString());
        PaymentCallback cb = Function.createRecordOfCallback(callback, paymentCallbackRepository);
        paymentCallbackRepository.save(cb);

        String status = callback.getStatus();
        String clientRef = callback.getClienttransid();

        return processCallback(clientRef, status, cb.getProvider().name());

    }

    private ResponseEntity<String> processCallback(String clientRef, String status, String provider) {
        ProxyOrder order = orderService.findByClientReference(clientRef);
        if (order == null) {
            log.warn("No order found with reference: {}", clientRef);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }

        String phoneNumber = order.getPhoneNumber();
        String normalizedStatus = status.trim().toUpperCase();

        Set<String> completedStatuses = Set.of("SUCCESS", "PAID");
        Set<String> pendingStatuses = Set.of("PROGRESS", "SUBMITTED", "PROCESSING");
        Set<String> failedStatuses = Set.of("FAILED", "CANCELLED", "REJECTED");

        if (completedStatuses.contains(normalizedStatus)) {
            if (order.getStatus() != PaymentOrderStatus.PENDING) {
                log.info("Order {} already processed.", clientRef);
                return ResponseEntity.ok("Already processed");
            }

            // Mark as COMPLETED
            order.setStatus(PaymentOrderStatus.COMPLETED);
            order.setPhoneNumber(phoneNumber);
            orderService.update(order);

            PageRequest pageRequest = PageRequest.of(0, order.getQuantity());
            List<ProxyCode> availableCodes = proxyCodeRepository.findAvailableByCategoryLimited(
                    order.getCategory(), pageRequest);

            if (availableCodes.size() < order.getQuantity()) {
                log.error("Not enough codes available");
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Not enough codes available");
            }

            // Mark codes as sold and log sales
            double totalAmount = 0.0;
            for (ProxyCode code : availableCodes) {
                code.setSold(true);
                code.setSoldAt(LocalDateTime.now());
                proxyCodeRepository.save(code);

                int unitPrice = Function.getUnitPrice(code.getCategory(), proxyPriceConfigRepository);
                SaleLog salesLog = new SaleLog(
                        null,
                        phoneNumber,
                        code.getCategory(),
                        LocalDateTime.now(),
                        unitPrice,
                        code,
                        PaymentOrderStatus.COMPLETED
                );
                saleLogRepository.save(salesLog);
                totalAmount = totalAmount + unitPrice;
            }

            // Send SMS
            String codesString = availableCodes.stream()
                    .map(ProxyCode::getCode)
                    .collect(Collectors.joining("\n"));

            String smsMessage = String.format("""
        Payment Successful

        Your CDKEY(s):
        %s

        Redeem in the Exchange Menu.

        Thank you for choosing TopBoyProxy.
        """, codesString);

            String adminSms = String.format("""
        NEW PAYMENT ALERT

        AMOUNT: GHS %.2f
        PAYMENT CHANNEL : %s
        CUSTOMER: %s
        """,
                    totalAmount,
                    provider,
                    order.getPhoneNumber());

            hubtelClient.sendSMS(phoneNumber, smsMessage);
            hubtelClient.sendSMS("233599036479", adminSms);

            return ResponseEntity.ok("Order confirmed and codes delivered");
        } else if (pendingStatuses.contains(normalizedStatus)) {
            log.info("Payment still pending for order {}", clientRef);
            return ResponseEntity.ok("Payment pending – no action taken yet");
        } else if (failedStatuses.contains(normalizedStatus)) {
            log.warn("Payment failed for order {}", clientRef);
            order.setStatus(PaymentOrderStatus.FAILED);
            orderService.update(order);
            return ResponseEntity.ok("Payment failed – order marked as failed");
        } else {
            log.warn("Unrecognized payment status [{}] for order {}", status, clientRef);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unrecognized payment status");
        }
    }

    private ReddeCheckoutRequest toReddeRequest(ProxyOrderInternalRequest req) {
        ReddeCheckoutRequest request = new ReddeCheckoutRequest();
        request.setAmount(Function.calculateAmount(req, proxyPriceConfigRepository));
        request.setApikey(REDDE_KEY);
        request.setFailurecallback(req.getCancellationUrl());
        request.setSuccesscallback(req.getReturnUrl());
        request.setLogolink(LOGO_URL);
        request.setMerchantname("Top Boy Proxy");
        request.setClienttransid(req.getClientReference());
        request.setDescription("Item Purchase");
        request.setAppid(REDDE_APP_ID);
        return request;
    }

    private PaymentLinkRequest toHubtelRequest(ProxyOrderInternalRequest req) {
        PaymentLinkRequest request = new PaymentLinkRequest();
        request.setClientReference(req.getClientReference());
        request.setTotalAmount(Function.calculateAmount(req, proxyPriceConfigRepository));
        request.setDescription(req.getDescription());
        request.setCallbackUrl(req.getCallbackUrl());
        request.setReturnUrl(req.getReturnUrl());
        request.setCancellationUrl(req.getCancellationUrl());
        request.setMerchantAccountNumber(MERCHANT_CODE); // Replace with config value
        return request;
    }

}
