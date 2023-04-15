package com.mikhalov.taskonaut.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
public class KeyGeneratorUtil {

    public static void main(String[] args) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey secretKey = keyGenerator.generateKey();
            String base64EncodedSecretKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            log.info("Base64 Encoded Secret Key: '{}'", base64EncodedSecretKey);
        } catch (NoSuchAlgorithmException e) {
            log.error("Error generating secret key: '{}'", e.getMessage());
        }
    }
}