/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.topboy.entity.enums;

/**
 *
 * @author plutus
 */
public enum CodeCategory {
    FIFTY(50),
    HUNDRED(100),
    TWO_HUNDRED(200),
    THREE_HUNDRED(300),
    FOUR_HUNDRED(400),
    SIX_HUNDRED(600),
    EIGHT_HUNDRED(800),
    THOUSAND(1000);

    private final int value;

    CodeCategory(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
