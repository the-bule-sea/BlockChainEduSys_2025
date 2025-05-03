package org.fu.blockchain_backend.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class DegreeHashUtil {
    public static String calculateDegreeHash(String name, String idCardNum, String university,
                                             String major, String degreeLevel, String graduationDate) {
        try {
            String data = name + idCardNum + university + major + degreeLevel + graduationDate;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error calculating degree hash", e);
        }
    }
}