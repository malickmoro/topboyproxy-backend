/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.topboy.dto;

import com.theplutushome.topboy.entity.SaleLog;
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
    private String id;
    private String date;
    private String phoneNumber;
    private String proxyCode;
    private String category;
    
    
    public static SaleDTO fromSaleLog(SaleLog saleLog) {
        return new SaleDTO(
            saleLog.getId().toString(),
            saleLog.getTimestamp().toString(), // ISO format
            saleLog.getPhoneNumber(),
            saleLog.getCode() != null ? saleLog.getCode().getCode() : "N/A",
            saleLog.getCategory().name()
        );
    }
}

