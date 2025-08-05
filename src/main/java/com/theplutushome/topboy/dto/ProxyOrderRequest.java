/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.topboy.dto;

import com.theplutushome.topboy.entity.enums.CodeCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author plutus
 */
@Data
public class ProxyOrderRequest {
    @NotNull
    private CodeCategory category;

    @Min(1)
    private int quantity;

    @NotBlank
    private String phoneNumber;
}
