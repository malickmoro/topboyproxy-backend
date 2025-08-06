/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.topboy.dto;

/**
 *
 * @author plutus
 */
public class AggregatedSalesData {
    private String label;
    private int sales;
    private double revenue;

    public AggregatedSalesData(String label, int sales, double revenue) {
        this.label = label;
        this.sales = sales;
        this.revenue = revenue;
    }

    // Getters and setters
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public int getSales() { return sales; }
    public void setSales(int sales) { this.sales = sales; }
    public double getRevenue() { return revenue; }
    public void setRevenue(double revenue) { this.revenue = revenue; }
}
