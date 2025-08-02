/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.topboy.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import lombok.Data;

/**
 *
 * @author plutus
 */
@Entity
@Data
public class ProxyCode {
    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    private CodeCategory category;

    private boolean sold = false;

    private LocalDateTime soldAt;
    private String buyerPhone;
    private LocalDateTime createdAt;
    
    @PrePersist
    public void setCreatedAt(){
        this.createdAt = LocalDateTime.now();
    }
}
