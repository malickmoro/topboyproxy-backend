/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.topboy.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 *
 * @author plutus
 */
@Data
public class CreateAdminUserRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
