/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.topboy.dto;

import com.theplutushome.topboy.entity.CodeCategory;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author plutus
 */
@Data
@AllArgsConstructor
public class ProxyCodeDTO {
    private Long id;
    private String code;
    private CodeCategory category;
    private LocalDateTime uploadedAt;
    private boolean isUsed;
    private LocalDateTime usedAt;
}