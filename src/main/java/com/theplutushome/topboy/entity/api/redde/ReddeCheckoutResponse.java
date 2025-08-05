package com.theplutushome.optimus.entity.api.redde;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReddeCheckoutResponse {
    private String status;
    private String reason;
    private String referenceid;
    private String responsetoken;
    private String checkouturl;
    private String checkouttransid;
}
