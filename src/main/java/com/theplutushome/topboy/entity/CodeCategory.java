/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.topboy.entity;

/**
 *
 * @author plutus
 */
public enum CodeCategory {
    FIFTY(50),
    HUNDRED(100);

    private final int value;

    CodeCategory(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
