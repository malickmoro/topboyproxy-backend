package com.theplutushome.optimus.entity.api.redde;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReddeCallback {
    private String reason;
    private String clienttransid;
    private String clientreference;
    private String telcotransid;
    private String transactionid;
    private String statusdate;
    private String status;

    @Override
    public String toString() {
        return "ReddeCallback [reason=" + reason + ", clienttransid=" + clienttransid + ", clientreference="
                + clientreference + ", telcotransid=" + telcotransid + ", transactionid=" + transactionid
                + ", statusdate=" + statusdate + ", status=" + status + "]";
    }
}

