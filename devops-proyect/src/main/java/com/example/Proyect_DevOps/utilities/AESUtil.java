package com.example.Proyect_DevOps.utilities;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

public class AESUtil {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final String KEY_ALGORITHM = "AES";
    private static final String KEY = obtenerClave();

    private static final int IV_LENGTH = 12;
    private static final int TAG_LENGTH = 128;

    public static void validarAESKey() {
        try {
            Dotenv dotenv = Dotenv.load();
            String key = dotenv.get("AES_SECRET");
        
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
        }   
    }

    private static String obtenerClave() {
        try {
            Dotenv dotenv = Dotenv.load();
            String key = dotenv.get("AES_SECRET");
        
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
            String key = System.getenv("AES_KEY");
            
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

    public static String encriptar(String dato) {
        try {
            byte[] keyHex = HexFormat.of().parseHex(KEY);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecureRandom ivRandom = new SecureRandom();
            byte[] IV = new byte[IV_LENGTH];
            ivRandom.nextBytes(IV);
            GCMParameterSpec parametros = new GCMParameterSpec(TAG_LENGTH, IV); 
            SecretKey key = new SecretKeySpec(keyHex, KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, parametros);
            
            byte[] cifrado = cipher.doFinal(dato.getBytes(StandardCharsets.UTF_8));

            byte[] resultado = new byte[IV_LENGTH + cifrado.length];
            System.arraycopy(IV,0, resultado,0, IV.length);
            System.arraycopy(cifrado, 0, resultado, IV.length, cifrado.length);

            return Base64.getEncoder().encodeToString(resultado);
        } catch (Exception e) {
            throw new RuntimeException("Error al encriptar", e);
        }
    }

    public static String desencriptar(String datoEncriptado) {
        try {
            byte[] keyHex = HexFormat.of().parseHex(KEY);
            SecretKeySpec key = new SecretKeySpec(keyHex, KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            byte[] decoded = Base64.getDecoder().decode(datoEncriptado);

            byte[] IV = new byte[IV_LENGTH];
            System.arraycopy(decoded, 0, IV, 0, IV_LENGTH);
            byte[] cifrado = new byte[decoded.length - IV.length];
            System.arraycopy(decoded, IV_LENGTH, cifrado,0, decoded.length - IV_LENGTH);

            GCMParameterSpec parametros = new GCMParameterSpec(TAG_LENGTH, IV);
            cipher.init(Cipher.DECRYPT_MODE, key, parametros);

            return new String(cipher.doFinal(cifrado), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error al desencriptar", e);
        }
    }
}