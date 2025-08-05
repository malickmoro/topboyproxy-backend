package com.theplutushome.topboy.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.theplutushome.topboy.entity.enums.PaymentProvider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PAYMENT_CALLBACK")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentCallback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentProvider provider;
    private String responseCode;
    private String requestStatus;
    private String salesInvoiceId;
    @Column(unique = true)
    private String clientReference;
    private String status;
    private double amount;
    private String customerPhoneNumber;
    private String paymentType;
    private String channel;
    private String description;
    private String reason;
    private String clienttransid;
    private String telcotransid;
    private String transactionid;
    private String statusdate;
    private String paymentReference;
}
