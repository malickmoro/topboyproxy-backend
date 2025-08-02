package com.theplutushome.topboy.entity.api.hubtel;

public class PaymentLinkRequest {

    private double totalAmount;
    private String description;
    private String callbackUrl;
    private String returnUrl;
    private String merchantAccountNumber;
    private String cancellationUrl;
    private String clientReference;

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getMerchantAccountNumber() {
        return merchantAccountNumber;
    }

    public void setMerchantAccountNumber(String merchantAccountNumber) {
        this.merchantAccountNumber = merchantAccountNumber;
    }

    public String getCancellationUrl() {
        return cancellationUrl;
    }

    public void setCancellationUrl(String cancellationUrl) {
        this.cancellationUrl = cancellationUrl;
    }

    public String getClientReference() {
        return clientReference;
    }

    public void setClientReference(String clientReference) {
        this.clientReference = clientReference;
    }

    @Override
    public String toString() {
        return "PaymentLinkRequest{" +
                "totalAmount=" + totalAmount +
                ", description='" + description + '\'' +
                ", callbackUrl='" + callbackUrl + '\'' +
                ", returnUrl='" + returnUrl + '\'' +
                ", merchantAccountNumber='" + merchantAccountNumber + '\'' +
                ", cancellationUrl='" + cancellationUrl + '\'' +
                ", clientReference='" + clientReference + '\'' +
                '}';
    }
}
