package com.example.b1esimageweb.web.Security;
import java.security.SecureRandom;

public class SecretKeyGenerator {
    public static void main(String[] args) {
        String generatedKey = generateSecretKey(256); // Adjust the key length to your needs (e.g., 128, 256 bits)
        System.out.println("Generated Secret Key: " + generatedKey);
    }

    public static String generateSecretKey(int keyLength) {
        byte[] secretKeyBytes = new byte[keyLength / 8];

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(secretKeyBytes);

        return java.util.Base64.getEncoder().encodeToString(secretKeyBytes);
    }
}

