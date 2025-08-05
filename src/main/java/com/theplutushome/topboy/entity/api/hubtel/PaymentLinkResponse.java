package com.theplutushome.topboy.entity.api.hubtel;

public class PaymentLinkResponse {
    private String responseCode;
    private String status;
    private ResultData data;

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

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }

    public static class ResultData {
        private String checkoutUrl;
        private String checkoutId;
        private String clientReference;
        private String message;
        private String checkoutDirectUrl;

        public String getCheckoutUrl() {
            return checkoutUrl;
        }

        public void setCheckoutUrl(String checkoutUrl) {
            this.checkoutUrl = checkoutUrl;
        }

        public String getCheckoutId() {
            return checkoutId;
        }

        public void setCheckoutId(String checkoutId) {
            this.checkoutId = checkoutId;
        }

        public String getClientReference() {
            return clientReference;
        }

        public void setClientReference(String clientReference) {
            this.clientReference = clientReference;
        }

        public String getCheckoutDirectUrl() {
            return checkoutDirectUrl;
        }

        public void setCheckoutDirectUrl(String checkoutDirectUrl) {
            this.checkoutDirectUrl = checkoutDirectUrl;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
