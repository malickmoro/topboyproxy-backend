/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.topboy.service;

import com.theplutushome.topboy.dto.SaleDTO;
import com.theplutushome.topboy.entity.CodeCategory;
import com.theplutushome.topboy.entity.SaleLog;
import com.theplutushome.topboy.repository.SaleLogRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 *
 * @author plutus
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SalesService {
    
     private final SaleLogRepository saleLogRepository;
    
    public List<SaleDTO> getSales() {
        try {
            List<SaleLog> saleLogs = saleLogRepository.findAllByOrderByTimestampDesc();
            
            return saleLogs.stream()
                .map(SaleDTO::fromSaleLog)
                .collect(Collectors.toList());
                
        } catch (Exception e) {
            log.error("Error retrieving sales data", e);
            throw new RuntimeException("Failed to retrieve sales data", e);
        }
    }
}