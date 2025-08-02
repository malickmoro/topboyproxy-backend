package com.theplutushome.optimus.entity.api.hubtel;

public class PaymentResponse {

    private String Message;
    private String ResponseCode;
    private Data Data;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public Data getData() { return Data; }

    public void setData(Data data) {
        this.Data = data;
    }

    public static class Data {
        private String TransactionId;
        private String Description;
        private String ClientReference;
        private double Amount;
        private double Charges;
        private double AmountAfterCharges;
        private double AmountCharged;
        private double DeliveryFee;

        public String getTransactionId() {
            return TransactionId;
        }

        public void setTransactionId(String transactionId) {
            TransactionId = transactionId;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

        public String getClientReference() {
            return ClientReference;
        }

        public void setClientReference(String clientReference) {
            ClientReference = clientReference;
        }

        public double getAmount() {
            return Amount;
        }

        public void setAmount(double amount) {
            Amount = amount;
        }

        public double getCharges() {
            return Charges;
        }

        public void setCharges(double charges) {
            Charges = charges;
        }

        public double getAmountAfterCharges() {
            return AmountAfterCharges;
        }

        public void setAmountAfterCharges(double amountAfterCharges) {
            AmountAfterCharges = amountAfterCharges;
        }

        public double getAmountCharged() {
            return AmountCharged;
        }

        public void setAmountCharged(double amountCharged) {
            AmountCharged = amountCharged;
        }

        public double getDeliveryFee() {
            return DeliveryFee;
        }

        public void setDeliveryFee(double deliveryFee) {
            DeliveryFee = deliveryFee;
        }

        @Override
        public String toString() {
            return "Data{" + "TransactionId=" + TransactionId + ", Description=" + Description + ", ClientReference=" + ClientReference + ", Amount=" + Amount + ", Charges=" + Charges + ", AmountAfterCharges=" + AmountAfterCharges + ", AmountCharged=" + AmountCharged + ", DeliveryFee=" + DeliveryFee + '}';
        }
         
    }

    @Override
    public String toString() {
        return "PaymentResponse{" + "Message=" + Message + ", ResponseCode=" + ResponseCode + ", Data=" + Data.toString() + '}';
    }
    
    
}
