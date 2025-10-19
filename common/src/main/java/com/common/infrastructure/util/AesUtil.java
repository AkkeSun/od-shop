package com.common.infrastructure.util;

import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;

public class AesUtil {

    private static String secretKey = "testKey";

    public static String encryptText(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance("AES");

            byte[] key = new byte[16];
            int i = 0;

            for (byte b : secretKey.getBytes()) {
                key[i++ % 16] ^= b;
            }

            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"));
            return new String(Hex.encodeHex(cipher.doFinal(plainText.getBytes(
                StandardCharsets.UTF_8)))).toUpperCase();
        } catch (Exception e) {
            return plainText;
        }
    }

    public static String decryptText(String encryptedText) {
        try {
            Cipher cipher = Cipher.getInstance("AES");

            byte[] key = new byte[16];
            int i = 0;

            for (byte b : secretKey.getBytes()) {
                key[i++ % 16] ^= b;
            }

            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"));
            byte[] decodedHex = Hex.decodeHex(encryptedText);
            byte[] decryptedBytes = cipher.doFinal(decodedHex);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return encryptedText;
        }
    }

    public static boolean matches(String plainText, String encryptedText) {
        return plainText.equals(decryptText(encryptedText));
    }
}
