/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.topboy.dto;

import com.theplutushome.topboy.entity.SaleLog;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author plutus
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleDTO {
    private Long id;
    private LocalDateTime date;
    private String phoneNumber;
    private String proxyCode;
    private String category;
    private int amount;

    public static SaleDTO fromSaleLog(SaleLog sale) {
        return new SaleDTO(
            sale.getId(),
            sale.getTimestamp(),
            sale.getPhoneNumber(),
            sale.getCode().getCode(),
            sale.getCategory().name(),
            sale.getAmount()
        );
    }
}

