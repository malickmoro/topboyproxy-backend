package com.theplutushome.optimus.entity.api.redde;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ReddeTransactionResponse {
    private String status;
    private String reason;
    private String transactionid;
    private String clienttransid;
    private String clientreference;
    private String brandtransid;
    private String statusdate;

}

