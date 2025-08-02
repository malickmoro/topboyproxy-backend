package com.theplutushome.topboy.controller;

import com.theplutushome.optimus.entity.api.hubtel.HubtelCallBack;
import com.theplutushome.optimus.entity.api.hubtel.PaymentLinkResponse;
import com.theplutushome.topboy.clients.hubtel.HubtelRestClient;
import com.theplutushome.topboy.dto.AvailableCategoryDTO;
import com.theplutushome.topboy.dto.ProxyOrderInternalRequest;
import com.theplutushome.topboy.dto.ProxyOrderRequest;
import com.theplutushome.topboy.entity.CodeCategory;
import com.theplutushome.topboy.entity.PaymentOrderStatus;
import com.theplutushome.topboy.entity.ProxyCode;
import com.theplutushome.topboy.entity.ProxyOrder;
import com.theplutushome.topboy.entity.ProxyPriceConfig;
import com.theplutushome.topboy.entity.SaleLog;
import com.theplutushome.topboy.entity.api.hubtel.PaymentLinkRequest;
import com.theplutushome.topboy.repository.ProxyCodeRepository;
import com.theplutushome.topboy.repository.ProxyPriceConfigRepository;
import com.theplutushome.topboy.repository.SaleLogRepository;
import com.theplutushome.topboy.service.OrderService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
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
    private final SaleLogRepository saleLogRepository;
    private final OrderService orderService;
    private final HubtelRestClient hubtelClient;
    private final String MERCHANT_CODE;
    private final String CALLBACK_URL;
    private final String RETURN_URL;
    private final String CANCELLATION_URL;

    public CustomerController(Environment env, ProxyCodeRepository proxyCodeRepository, OrderService orderService,
            HubtelRestClient hubtelRestClient, ProxyPriceConfigRepository priceConfigRepository, SaleLogRepository saleLogRepository) {
        this.proxyCodeRepository = proxyCodeRepository;
        this.proxyPriceConfigRepository = priceConfigRepository;
        this.saleLogRepository = saleLogRepository;
        this.orderService = orderService;
        this.hubtelClient = hubtelRestClient;
        this.MERCHANT_CODE = Objects.requireNonNull(env.getProperty("pos_sales_id"), "Merchant Code not set");
        this.CALLBACK_URL = Objects.requireNonNull(env.getProperty("callback_url"), "Callback not set");
        this.CANCELLATION_URL = Objects.requireNonNull(env.getProperty("cancellation_url"), "Cancellation Url not set");
        this.RETURN_URL = Objects.requireNonNull(env.getProperty("return_url"), "Return Url not set");
    }

    @GetMapping("/proxy-categories")
    public ResponseEntity<List<AvailableCategoryDTO>> getAvailableCategories() {
        List<AvailableCategoryDTO> result = Arrays.stream(CodeCategory.values())
                .map(category -> new AvailableCategoryDTO(
                category,
                proxyCodeRepository.countByCategoryAndSoldFalse(category),
                getUnitPrice(category),
                category.name() + " proxies"))
                .filter(dto -> dto.getAvailableCount() > 0)
                .toList();

        return ResponseEntity.ok(result);
    }

    @PostMapping("/generate-invoice")
    public ResponseEntity<PaymentLinkResponse> generateInvoice(@Valid @RequestBody ProxyOrderRequest request) {
        log.info("Payment request received: {}", request);

        int available = proxyCodeRepository.countByCategoryAndSoldFalse(request.getCategory());
        if (request.getQuantity() > available) {
            return ResponseEntity.badRequest().body(null);
        }

        // ✅ Enrich the request
        ProxyOrderInternalRequest enriched = new ProxyOrderInternalRequest();
        BeanUtils.copyProperties(request, enriched);
        enriched.setStatus(PaymentOrderStatus.PENDING);
        enriched.setDescription("Purchase of " + request.getQuantity() + " " + request.getCategory() + " proxies");
        enriched.setClientReference(UUID.randomUUID().toString());
        enriched.setCallbackUrl(CALLBACK_URL);
        enriched.setReturnUrl(RETURN_URL);
        enriched.setCancellationUrl(CANCELLATION_URL);

        // ✅ Persist and generate link
        orderService.createOrder(enriched);
        PaymentLinkResponse paymentLink = hubtelClient.getPaymentUrl(toHubtelRequest(enriched));

        return ResponseEntity.ok(paymentLink);
    }

    private PaymentLinkRequest toHubtelRequest(ProxyOrderInternalRequest req) {
        PaymentLinkRequest request = new PaymentLinkRequest();
        request.setClientReference(req.getClientReference());
        request.setTotalAmount(calculateAmount(req));
        request.setDescription(req.getDescription());
        request.setCallbackUrl(req.getCallbackUrl());
        request.setReturnUrl(req.getReturnUrl());
        request.setCancellationUrl(req.getCancellationUrl());
        request.setMerchantAccountNumber(MERCHANT_CODE); // Replace with config value
        return request;
    }

    private double calculateAmount(ProxyOrderInternalRequest req) {
        return req.getQuantity() * getUnitPrice(req.getCategory());
    }

    private int getUnitPrice(CodeCategory category) {
        Optional<ProxyPriceConfig> priceInfo = proxyPriceConfigRepository.findByCategory(category);
        if (priceInfo.isPresent() == false) {
            return 0;
        }

        return priceInfo.get().getPrice();
    }

    @PostMapping("/callback")
    public ResponseEntity<String> handlePaymentCallback(@RequestBody HubtelCallBack callback) {
        log.info("Received Hubtel Callback: {}", callback);

        String status = callback.getStatus();
        String clientRef = callback.getData().getClientReference();
        String phoneNumber = callback.getData().getCustomerPhoneNumber();

        ProxyOrder order = orderService.findByClientReference(clientRef);
        if (order == null) {
            log.warn("No order found with reference: {}", clientRef);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }

        if ("Success".equalsIgnoreCase(status)) {
            if (order.getStatus() != PaymentOrderStatus.PENDING) {
                log.info("Order {} already processed.", clientRef);
                return ResponseEntity.ok("Already processed");
            }

            // Mark as CONFIRMED
            order.setStatus(PaymentOrderStatus.COMPLETED);
            order.setPhoneNumber(phoneNumber);
            orderService.update(order);

            PageRequest pageRequest = PageRequest.of(0, order.getQuantity());
            List<ProxyCode> availableCodes = proxyCodeRepository.findAvailableByCategoryLimited(
                    order.getCategory(), pageRequest);

            if (availableCodes.size() < order.getQuantity()) {
                log.error("Not enough codes available");
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Not enough codes");
            }

            // Mark codes as sold and log sale
            for (ProxyCode code : availableCodes) {
                code.setSold(true);
                code.setSoldAt(LocalDateTime.now());
                proxyCodeRepository.save(code);

                SaleLog log = new SaleLog(
                        null,
                        phoneNumber,
                        code.getCategory(),
                        LocalDateTime.now(),
                        code
                );
                saleLogRepository.save(log);
            }

            // Send code to customer (optional)
            String smsMessage = "Your proxy code purchase is complete. Enjoy!";
            hubtelClient.sendSMS(phoneNumber, smsMessage);

            return ResponseEntity.ok("Order confirmed and codes delivered");
        } else {
            order.setStatus(PaymentOrderStatus.FAILED);
            orderService.update(order);
            return ResponseEntity.ok("Payment failed");
        }
    }

}
