package com.example.Proyect_DevOps.utilities;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HexFormat;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

public class HMACUtil {

    private static final String ALGORITHM = "HmacSHA256";
    private static final String KEY = obtenerClave();

    public static void validarHMACKey() {
        try {
            Dotenv dotenv = Dotenv.load();
            String key = dotenv.get("HMAC_SECRET");
        
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
        } catch (DotenvException e) {
            String key = System.getenv("HMAC_KEY");
            
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
        }   
    }

    private static String obtenerClave() {
        try {
            Dotenv dotenv = Dotenv.load();
            String key = dotenv.get("HMAC_SECRET");
        
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

        } catch (DotenvException e) {
            String key = System.getenv("HMAC_KEY");
            
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
    }

    public static String GenerarHuella(String dato) {
        try {
            byte[] keyHex = HexFormat.of().parseHex(KEY);
            SecretKeySpec key = new SecretKeySpec(keyHex, ALGORITHM);
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(key);

            byte[] hmac = mac.doFinal(dato.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hmac);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo generar la firma del correo");
        }
    }
}
