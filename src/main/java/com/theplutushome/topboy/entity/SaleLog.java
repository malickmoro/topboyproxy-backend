/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.topboy.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author plutus
 */
@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
public class SaleLog {
    @Id @GeneratedValue
    private Long id;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private CodeCategory category;

    private LocalDateTime timestamp;
    
    private int amount;

    @OneToOne
    private ProxyCode code;
        
    @Enumerated(EnumType.STRING)
    private PaymentOrderStatus paymentStatus = PaymentOrderStatus.COMPLETED;
}

