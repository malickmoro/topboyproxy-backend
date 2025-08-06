/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.topboy.dto;

import java.util.List;

/**
 *
 * @author plutus
 */
public class AggregatedSalesResponse {
    private List<AggregatedSalesData> data;
    private String error;

    public AggregatedSalesResponse(List<AggregatedSalesData> data) {
        this.data = data;
    }

    public AggregatedSalesResponse(String error) {
        this.error = error;
    }

    // Getters and setters
    public List<AggregatedSalesData> getData() { return data; }
    public void setData(List<AggregatedSalesData> data) { this.data = data; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}