/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.topboy.service;

import com.theplutushome.topboy.dto.AggregatedSalesData;
import com.theplutushome.topboy.dto.AggregatedSalesResponse;
import com.theplutushome.topboy.dto.RevenueStatistics;
import com.theplutushome.topboy.dto.SaleDTO;
import com.theplutushome.topboy.dto.SalesResponse;
import com.theplutushome.topboy.dto.SalesStatistics;
import com.theplutushome.topboy.entity.enums.CodeCategory;
import com.theplutushome.topboy.entity.SaleLog;
import com.theplutushome.topboy.repository.SaleLogRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
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

    public SalesResponse getSalesWithStatistics(String startDate, String endDate, String category, String period) {
        try {
            List<SaleLog> saleLogs = getFilteredSales(startDate, endDate, category);

            List<SaleDTO> sales = saleLogs.stream()
                    .map(SaleDTO::fromSaleLog)
                    .collect(Collectors.toList());

            SalesStatistics statistics = calculateStatistics(saleLogs, period);

            return new SalesResponse(sales, statistics);
        } catch (Exception e) {
            log.error("Error generating sales stats", e);
            throw new RuntimeException("Failed to generate sales stats", e);
        }
    }

    private List<SaleLog> getFilteredSales(String startDate, String endDate, String category) {
        Specification<SaleLog> spec = Specification.where(null);

        if (startDate != null && !startDate.isBlank()) {
            LocalDate start = LocalDate.parse(startDate);
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("timestamp"), start.atStartOfDay()));
        }

        if (endDate != null && !endDate.isBlank()) {
            LocalDate end = LocalDate.parse(endDate);
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("timestamp"), end.atTime(23, 59, 59)));
        }

        if (category != null && !category.isBlank()) {
            CodeCategory cat = CodeCategory.valueOf(category.toUpperCase());
            spec = spec.and((root, query, cb) -> cb.equal(root.get("category"), cat));
        }

        return saleLogRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "timestamp"));
    }

    private SalesStatistics calculateStatistics(List<SaleLog> logs, String period) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).toLocalDate().atStartOfDay();

        List<SaleLog> periodFiltered = logs;
        if ("daily".equalsIgnoreCase(period)) {
            periodFiltered = logs.stream().filter(l -> l.getTimestamp().isAfter(startOfDay)).toList();
        } else if ("monthly".equalsIgnoreCase(period)) {
            periodFiltered = logs.stream().filter(l -> l.getTimestamp().isAfter(startOfMonth)).toList();
        }

        int total = logs.size();
        int daily = (int) logs.stream().filter(l -> l.getTimestamp().isAfter(startOfDay)).count();
        int monthly = (int) logs.stream().filter(l -> l.getTimestamp().isAfter(startOfMonth)).count();

        int totalRevenue = logs.stream().mapToInt(s -> Optional.ofNullable(s.getAmount()).orElse(0)).sum();
        int dailyRevenue = logs.stream()
                .filter(l -> l.getTimestamp().isAfter(startOfDay))
                .mapToInt(s -> Optional.ofNullable(s.getAmount()).orElse(0))
                .sum();
        int monthlyRevenue = logs.stream()
                .filter(l -> l.getTimestamp().isAfter(startOfMonth))
                .mapToInt(s -> Optional.ofNullable(s.getAmount()).orElse(0))
                .sum();

        Map<String, Integer> byCategory = new HashMap<>();
        for (CodeCategory cat : CodeCategory.values()) {
            int count = (int) periodFiltered.stream()
                    .filter(l -> l.getCategory() == cat)
                    .count();
            byCategory.put(cat.name(), count);
        }

        return new SalesStatistics(
                total,
                daily,
                monthly,
                new RevenueStatistics(totalRevenue, dailyRevenue, monthlyRevenue),
                byCategory
        );
    }

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

    public AggregatedSalesResponse getAggregatedSales(String startDate, String endDate, String category, String period) {
        try {
            List<SaleLog> saleLogs = getFilteredSales(startDate, endDate, category);
            List<AggregatedSalesData> aggregatedData = new ArrayList<>();

            switch (period) {
                case "daily" ->
                    aggregatedData = aggregateByDay(saleLogs);
                case "weekly" ->
                    aggregatedData = aggregateByWeek(saleLogs);
                case "monthly" ->
                    aggregatedData = aggregateByMonth(saleLogs);
            }

            return new AggregatedSalesResponse(aggregatedData);
        } catch (Exception e) {
            log.error("Error generating aggregated sales data", e);
            throw new RuntimeException("Failed to generate aggregated sales data", e);
        }
    }

    private List<AggregatedSalesData> aggregateByDay(List<SaleLog> saleLogs) {
        Map<String, AggregatedSalesData> dayMap = new LinkedHashMap<>();

        // Initialize all days of the week
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        for (String day : days) {
            dayMap.put(day, new AggregatedSalesData(day, 0, 0.0));
        }

        // Aggregate sales by day of week
        for (SaleLog sale : saleLogs) {
            LocalDate saleDate = sale.getTimestamp().toLocalDate();
            String dayOfWeek = saleDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

            AggregatedSalesData existing = dayMap.get(dayOfWeek);
            existing.setSales(existing.getSales() + 1);
            existing.setRevenue(existing.getRevenue() + sale.getAmount());
        }

        return new ArrayList<>(dayMap.values());
    }

    private List<AggregatedSalesData> aggregateByWeek(List<SaleLog> saleLogs) {
        Map<Integer, AggregatedSalesData> weekMap = new TreeMap<>();

        for (SaleLog sale : saleLogs) {
            LocalDate saleDate = sale.getTimestamp().toLocalDate();
            int weekOfMonth = (saleDate.getDayOfMonth() - 1) / 7 + 1;

            weekMap.computeIfAbsent(weekOfMonth, week
                    -> new AggregatedSalesData("Week " + week, 0, 0.0));

            AggregatedSalesData existing = weekMap.get(weekOfMonth);
            existing.setSales(existing.getSales() + 1);
            existing.setRevenue(existing.getRevenue() + sale.getAmount());
        }

        return new ArrayList<>(weekMap.values());
    }

    private List<AggregatedSalesData> aggregateByMonth(List<SaleLog> saleLogs) {
        Map<String, AggregatedSalesData> monthMap = new LinkedHashMap<>();

        // Initialize all months
        String[] months = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};
        for (String month : months) {
            monthMap.put(month, new AggregatedSalesData(month, 0, 0.0));
        }

        // Aggregate sales by month
        for (SaleLog sale : saleLogs) {
            LocalDate saleDate = sale.getTimestamp().toLocalDate();
            String month = saleDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

            AggregatedSalesData existing = monthMap.get(month);
            existing.setSales(existing.getSales() + 1);
            existing.setRevenue(existing.getRevenue() + sale.getAmount());
        }

        return new ArrayList<>(monthMap.values());
    }

}
