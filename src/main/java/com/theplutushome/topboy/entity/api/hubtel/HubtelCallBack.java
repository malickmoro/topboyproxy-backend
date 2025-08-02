package com.theplutushome.optimus.entity.api.hubtel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HubtelCallBack {

    @JsonProperty("ResponseCode")
    private String responseCode;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("Data")
    private Data data;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Data {

        @JsonProperty("CheckoutId")
        private String checkoutId;

        @JsonProperty("SalesInvoiceId")
        private String salesInvoiceId;

        @JsonProperty("ClientReference")
        private String clientReference;

        @JsonProperty("Status")
        private String status;

        @JsonProperty("Amount")
        private double amount;

        @JsonProperty("CustomerPhoneNumber")
        private String customerPhoneNumber;

        @JsonProperty("PaymentDetails")
        private PaymentDetails paymentDetails;

        @JsonProperty("Description")
        private String description;

        // Getters and Setters for Data class fields

        public String getCheckoutId() {
            return checkoutId;
        }

        public void setCheckoutId(String checkoutId) {
            this.checkoutId = checkoutId;
        }

        public String getSalesInvoiceId() {
            return salesInvoiceId;
        }

        public void setSalesInvoiceId(String salesInvoiceId) {
            this.salesInvoiceId = salesInvoiceId;
        }

        public String getClientReference() {
            return clientReference;
        }

        public void setClientReference(String clientReference) {
            this.clientReference = clientReference;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getCustomerPhoneNumber() {
            return customerPhoneNumber;
        }

        public void setCustomerPhoneNumber(String customerPhoneNumber) {
            this.customerPhoneNumber = customerPhoneNumber;
        }

        public PaymentDetails getPaymentDetails() {
            return paymentDetails;
        }

        public void setPaymentDetails(PaymentDetails paymentDetails) {
            this.paymentDetails = paymentDetails;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class PaymentDetails {

            @JsonProperty("MobileMoneyNumber")
            private String mobileMoneyNumber;

            @JsonProperty("PaymentType")
            private String paymentType;

            @JsonProperty("Channel")
            private String channel;

            // Getters and Setters for PaymentDetails class fields

            public String getMobileMoneyNumber() {
                return mobileMoneyNumber;
            }

            public void setMobileMoneyNumber(String mobileMoneyNumber) {
                this.mobileMoneyNumber = mobileMoneyNumber;
            }

            public String getPaymentType() {
                return paymentType;
            }

            public void setPaymentType(String paymentType) {
                this.paymentType = paymentType;
            }

            public String getChannel() {
                return channel;
            }

            public void setChannel(String channel) {
                this.channel = channel;
            }
        }

        @Override
        public String toString() {
            return "Data{" +
                    "checkoutId='" + checkoutId + '\'' +
                    ", salesInvoiceId='" + salesInvoiceId + '\'' +
                    ", clientReference='" + clientReference + '\'' +
                    ", status='" + status + '\'' +
                    ", amount=" + amount +
                    ", customerPhoneNumber='" + customerPhoneNumber + '\'' +
                    ", paymentDetails=" + paymentDetails +
                    ", description='" + description + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "HubtelCallBack{" +
                "responseCode='" + responseCode + '\'' +
                ", status='" + status + '\'' +
                ", data=" + data.toString() +
                '}';
    }
}