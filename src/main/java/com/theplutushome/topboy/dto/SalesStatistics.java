/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.topboy.dto;

import java.util.Map;
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
public class SalesStatistics {
    private int total;
    private int daily;
    private int monthly;
    private RevenueStatistics revenue;
    private Map<String, Integer> byCategory;
}

