/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.topboy.dto;

import java.util.List;
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
public class UploadResult {
    private int totalUploaded;
    private List<String> duplicates;
    private int blankLines;
}

