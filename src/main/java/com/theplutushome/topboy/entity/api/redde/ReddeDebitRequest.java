package com.theplutushome.optimus.entity.api.redde;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReddeDebitRequest {
    private double amount;
    private String appid;
    private String clientreference;
    private String clienttransid;
    private String description;
    private String nickname;
    private String paymentoption;
    private String walletnumber;

    @Override
    public String toString() {
        return "ReddeDebitRequest [amount=" + amount + ", appid=" + appid + ", clientreference=" + clientreference
                + ", clienttransid=" + clienttransid + ", description=" + description + ", nickname=" + nickname
                + ", paymentoption=" + paymentoption + ", walletnumber=" + walletnumber + "]";
    }

}
