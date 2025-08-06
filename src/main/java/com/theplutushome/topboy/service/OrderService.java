/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.topboy.service;

import com.theplutushome.optimus.entity.api.hubtel.HubtelCallBack;
import com.theplutushome.topboy.dto.ProxyOrderInternalRequest;
import com.theplutushome.topboy.entity.ProxyOrder;
import com.theplutushome.topboy.exception.NotFoundException;
import com.theplutushome.topboy.repository.ProxyOrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author plutus
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final ProxyOrderRepository proxyOrderRepository;

    @Transactional
    public void createOrder(ProxyOrderInternalRequest request) {
        ProxyOrder order = new ProxyOrder();
        order.setCategory(request.getCategory());
        order.setQuantity(request.getQuantity());
        order.setPhoneNumber(request.getPhoneNumber());
        order.setClientReference(request.getClientReference());
        order.setDescription(request.getDescription());
        order.setCallbackUrl(request.getCallbackUrl());
        order.setReturnUrl(request.getReturnUrl());
        order.setCancellationUrl(request.getCancellationUrl());
        order.setStatus(request.getStatus());

        proxyOrderRepository.save(order);
        log.info("Order created: {}", order);
    }

    public ProxyOrder findByClientReference(String clientReference) {
        return proxyOrderRepository.findByClientReference(clientReference)
                .orElseThrow(() -> {
                    log.error("Order with reference [{}] not found", clientReference);
                    return new NotFoundException("Order with reference [" + clientReference + "] not found");
                });
    }

    public void update(ProxyOrder order) {
        proxyOrderRepository.save(order);
        log.info("Order updated: {}", order);
    }

    public void saveCallback(HubtelCallBack callBack) {
        // Optional: Save callback to a separate table for audit purposes
        log.info("Callback received and saved: {}", callBack);
    }
}
