/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.optimus.entity.api.hubtel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author MalickMoro-Samah
 */
public class USSDCallback {

    @JsonProperty("ResponseCode")
    private String responseCode;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("Data")
    private Data data;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Data {

        @JsonProperty("Amount")
        private double amount;

        @JsonProperty("Charges")
        private double charges;

        @JsonProperty("AmountAfterCharges")
        private double amountAfterCharges;

        @JsonProperty("Description")
        private String description;

        @JsonProperty("ClientReference")
        private String clientReference;

        @JsonProperty("TransactionId")
        private String transactionId;

        @JsonProperty("ExternalTransactionId")
        private String externalTransactionId;

        @JsonProperty("AmountCharged")
        private double amountCharged;

        @JsonProperty("OrderId")
        private String orderId;

        @JsonProperty("PaymentDate")
        private String paymentDate;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public double getCharges() {
            return charges;
        }

        public void setCharges(double charges) {
            this.charges = charges;
        }

        public double getAmountAfterCharges() {
            return amountAfterCharges;
        }

        public void setAmountAfterCharges(double amountAfterCharges) {
            this.amountAfterCharges = amountAfterCharges;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getClientReference() {
            return clientReference;
        }

        public void setClientReference(String clientReference) {
            this.clientReference = clientReference;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getExternalTransactionId() {
            return externalTransactionId;
        }

        public void setExternalTransactionId(String externalTransactionId) {
            this.externalTransactionId = externalTransactionId;
        }

        public double getAmountCharged() {
            return amountCharged;
        }

        public void setAmountCharged(double amountCharged) {
            this.amountCharged = amountCharged;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getPaymentDate() {
            return paymentDate;
        }

        public void setPaymentDate(String paymentDate) {
            this.paymentDate = paymentDate;
        }

        @Override
        public String toString() {
            return "Data{" + "amount=" + amount + ", charges=" + charges + ", amountAfterCharges=" + amountAfterCharges + ", description=" + description + ", clientReference=" + clientReference + ", transactionId=" + transactionId + ", externalTransactionId=" + externalTransactionId + ", amountCharged=" + amountCharged + ", orderId=" + orderId + ", paymentDate=" + paymentDate + '}';
        }

    }

    @Override
    public String toString() {
        return "USSDCallback{" + "responseCode=" + responseCode + ", message=" + message + ", data=" + data.toString() + '}';
    }

}
