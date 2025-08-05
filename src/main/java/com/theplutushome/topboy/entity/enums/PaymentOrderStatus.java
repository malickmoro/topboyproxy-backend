/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.topboy.entity.enums;

/**
 *
 * @author plutus
 */
public enum PaymentOrderStatus {
    PENDING,        // Order created but not paid
    PROCESSING,     // Payment successful, assigning proxies
    COMPLETED,      // Proxies assigned and SMS sent
    FAILED,         // Payment failed or not enough codes
    CANCELLED       // User cancelled or timed out
}