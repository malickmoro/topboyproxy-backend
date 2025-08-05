package com.theplutushome.topboy.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.theplutushome.topboy.entity.PaymentCallback;

public interface PaymentCallbackRepository extends JpaRepository<PaymentCallback, Integer> {

    Optional<PaymentCallback> findPaymentCallbackByClientReference(String clientReference);
}
