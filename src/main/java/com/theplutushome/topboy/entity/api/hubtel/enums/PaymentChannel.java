package com.theplutushome.optimus.entity.api.hubtel.enums;

import lombok.Getter;

@Getter
public enum PaymentChannel {
    MTN("mtn-gh"),
    VODAFONE("vodafone-gh"),
    AIRTELTIGO("tigo-gh");

    private final String name;

    PaymentChannel(String name) {
        this.name = name;
    }

    public static PaymentChannel fromString(String name) {
        for (PaymentChannel status : PaymentChannel.values()) {
            if (status.name.equalsIgnoreCase(name)) {
                return status;
            }
        }
        return null;
    }
}
