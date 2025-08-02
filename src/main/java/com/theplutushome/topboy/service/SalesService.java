/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.topboy.service;

import com.theplutushome.topboy.dto.SaleDTO;
import com.theplutushome.topboy.entity.CodeCategory;
import com.theplutushome.topboy.entity.SaleLog;
import com.theplutushome.topboy.repository.SaleLogRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public List<SaleDTO> getSales(String startDate, String endDate, String category) {
        try {
            List<SaleLog> saleLogs;

            boolean hasStart = startDate != null && !startDate.isBlank();
            boolean hasEnd = endDate != null && !endDate.isBlank();
            boolean hasCategory = category != null && !category.isBlank();

            LocalDateTime start = hasStart ? LocalDateTime.parse(startDate) : null;
            LocalDateTime end = hasEnd ? LocalDateTime.parse(endDate) : null;
            CodeCategory codeCategory = hasCategory ? CodeCategory.valueOf(category.toUpperCase()) : null;

            if (hasStart && hasEnd && hasCategory) {
                saleLogs = saleLogRepository.findByCategoryAndTimestampBetween(codeCategory, start, end);
            } else if (hasStart && hasEnd) {
                saleLogs = saleLogRepository.findByTimestampBetween(start, end);
            } else if (hasCategory) {
                saleLogs = saleLogRepository.findByCategory(codeCategory);
            } else {
                saleLogs = saleLogRepository.findAllByOrderByTimestampDesc();
            }

            return saleLogs.stream()
                    .map(SaleDTO::fromSaleLog)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error retrieving sales data", e);
            throw new RuntimeException("Failed to retrieve sales data", e);
        }
    }

}
