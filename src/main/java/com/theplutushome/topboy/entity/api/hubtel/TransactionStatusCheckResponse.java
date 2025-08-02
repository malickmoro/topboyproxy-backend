package com.theplutushome.optimus.entity.api.hubtel;

public class TransactionStatusCheckResponse {
    private String message;
    private String responseCode;
    private TransactionStatusCheckData data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public TransactionStatusCheckData getData() {
        return data;
    }

    public void setData(TransactionStatusCheckData data) {
        this.data = data;
    }

    public static class TransactionStatusCheckData {
        private String date;
        private String status;
        private String transactionId;
        private String externalTransactionId;
        private String paymentMethod;
        private String clientReference;
        private String currencyCode;
        private double amount;
        private double charges;
        private double amountAfterCharges;
        private boolean isFulfilled;


        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public String getClientReference() {
            return clientReference;
        }

        public void setClientReference(String clientReference) {
            this.clientReference = clientReference;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

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

        public boolean isFulfilled() {
            return isFulfilled;
        }

        public void setFulfilled(boolean fulfilled) {
            isFulfilled = fulfilled;
        }
    }
}

