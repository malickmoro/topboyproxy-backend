/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.topboy.dto;

import com.theplutushome.topboy.entity.enums.CodeCategory;
import lombok.Data;

/**
 *
 * @author plutus
 */
@Data
public class PurchaseRequest {
    private String phoneNumber;
    private CodeCategory category;
    private String paymentReference; // From Hubtel
}
