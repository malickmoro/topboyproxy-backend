/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.topboy.dto;

import com.theplutushome.topboy.entity.CodeCategory;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author plutus
 */
@Data
@AllArgsConstructor
public class AvailableCategoryDTO {
    private CodeCategory category;
    private int availableCount;
    private int unitPrice;
    private String description;
}
