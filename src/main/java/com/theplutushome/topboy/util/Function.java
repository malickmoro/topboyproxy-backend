package com.theplutushome.topboy.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;

public class Function {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Verify the webhook signature from Cryptomus.
     *
     * @param receivedData The full data received from the webhook as a map.
     * @param apiPaymentKey Your API payment key.
     * @return True if the signature is valid, false otherwise.
     */
    public static boolean verifyWebhookSignature(Map<String, Object> receivedData, String apiPaymentKey) throws Exception {
        // Step 1: Extract and remove the sign from the received data
        String receivedSign = (String) receivedData.get("sign");
        if (receivedSign == null) {
            throw new IllegalArgumentException("Missing 'sign' in the received data.");
        }
        receivedData.remove("sign");

        String jsonData = escapeSlashesInJson(objectMapper.writeValueAsString(receivedData));
        String base64EncodedData = Base64.getEncoder().encodeToString(jsonData.getBytes(StandardCharsets.UTF_8));
        String generatedHash = generateMd5Hash(base64EncodedData + apiPaymentKey);

        return generatedHash.equals(receivedSign);
    }

    /**
     * Escape slashes in the JSON string to match PHP's behavior.
     *
     * @param jsonData The JSON string.
     * @return Escaped JSON string.
     */
    private static String escapeSlashesInJson(String jsonData) {
        return jsonData.replace("/", "\\/");
    }

    /**
     * Generate an MD5 hash for the input string.
     *
     * @param input The input string.
     * @return MD5 hash as a hexadecimal string.
     */
    private static String generateMd5Hash(String input) throws Exception {
        return getString(input);
    }

    private static String getString(String input) throws NoSuchAlgorithmException {
        return getMD5String(input);
    }

    private static String getMD5String(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    public static String generateSign(Object data, String apiKey) throws NoSuchAlgorithmException {
        Gson gson = new Gson();
        String dataEncoded;
        if (data == null) {
            dataEncoded = "";
        } else {
            dataEncoded = gson.toJson(data);
        }

        String encodedData = Base64.getEncoder().encodeToString(dataEncoded.getBytes(StandardCharsets.UTF_8));
        String concatenatedString = encodedData + apiKey;

        return getMD5String(concatenatedString);
    }

    public static String generateReferralCode() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int CODE_LENGTH = 8;

        SecureRandom random = new SecureRandom();
        StringBuilder referralCode = new StringBuilder();

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            referralCode.append(CHARACTERS.charAt(index));
        }

        return referralCode.toString();
    }

    public static String generateAuthorizationKey(String clientId, String clientSecret) {
        String credentials = clientId + ":" + clientSecret;
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    public static String randomOTPCode() {
        var CHARACTERS = "0123456789";
        var CODE_LENGTH = 6;

        var random = new SecureRandom();
        StringBuilder otpCode = new StringBuilder();

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            otpCode.append(CHARACTERS.charAt(index));
        }
        return otpCode.toString();
    }

    public static String generateFourDigitCode() {
        String CHARACTERS = "0123456789";
        int CODE_LENGTH = 4;

        SecureRandom random = new SecureRandom();
        StringBuilder otpCode = new StringBuilder();

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            otpCode.append(CHARACTERS.charAt(index));
        }

        return otpCode.toString();
    }

    public static String generateOtpPrefix() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int CODE_LENGTH = 4;

        SecureRandom random = new SecureRandom();
        StringBuilder prefix = new StringBuilder();

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            prefix.append(CHARACTERS.charAt(index));
        }

        return prefix.toString();
    }

    public static void printInfo(Object info) {
        System.out.println("Info: " + info);
    }
}
