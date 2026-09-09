package com.example.Proyect_DevOps.utilities;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HexFormat;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final String KEY = obtenerClave();

    private static String obtenerClave() {
        String key = System.getenv("AES_SECRET");

        if (key == null || key.isBlank()) {
            throw new IllegalStateException(
                "La variable de entorno AES_SECRET no está configurada"
            );
        }

        if (!key.matches("[0-9a-fA-F]{64}")) {
            throw new IllegalStateException(
                "AES_SECRET debe ser una cadena hexadecimal de 64 caracteres"
            );
        }

        return key;
    }

    public static String encriptar(String dato) {
        try {
            byte[] keyHex = HexFormat.of().parseHex(KEY);
            Cipher cipher = Cipher.getInstance(ALGORITHM);

            SecretKey key = new SecretKeySpec(keyHex, ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(dato.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception e) {
            throw new RuntimeException("Error al encriptar", e);
        }
    }

    public static String desencriptar(String datoEncriptado) {
        try {
            byte[] keyHex = HexFormat.of().parseHex(KEY);
            SecretKeySpec key = new SecretKeySpec(keyHex, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);

            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decoded = Base64.getDecoder().decode(datoEncriptado);

            return new String(cipher.doFinal(decoded), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error al desencriptar", e);
        }
    }
}