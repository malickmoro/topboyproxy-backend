package com.theplutushome.optimus.entity.api.redde;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReddeCheckoutRequest {
    private double amount;
    private String apikey;
    private String appid;
    private String description;
    private String failurecallback;
    private String logolink;
    private String merchantname;
    private String clienttransid;
    private String successcallback;
}
