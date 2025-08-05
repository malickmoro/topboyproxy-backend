package com.theplutushome.optimus.entity.api.redde;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReddeDebitResponse {
    private String status;
    private String reason;
    private String transactionid;
    private String clienttransid;
    private String statusdate;

    @Override
    public String toString() {
        return "ReddeDebitResponse [status=" + status + ", reason=" + reason + ", transactionid=" + transactionid
                + ", clienttransid=" + clienttransid + ", statusdate=" + statusdate + "]";
    }
}
