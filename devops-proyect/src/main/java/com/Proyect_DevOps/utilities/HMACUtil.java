package com.Proyect_DevOps.utilities;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HexFormat;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

/**
 * Utilidad criptográfica para generar huellas digitales (hashes de autenticación de mensajes)
 * utilizando el algoritmo HmacSHA256. Permite indexar y buscar información protegida
 * (como correos electrónicos cifrados) de forma segura y determinista.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
public class HMACUtil {

    /**
     * Algoritmo estándar utilizado para el código de autenticación de mensaje con clave (HMAC).
     */
    private static final String ALGORITHM = "HmacSHA256";

    /**
     * Clave secreta obtenida desde el entorno.
     */
    private static final String KEY = obtenerClave();

    /**
     * Obtiene el valor de la variable HMAC_SECRET desde .env o el entorno del sistema.
     *
     * @return Cadena que representa el secreto HMAC.
     */
    private static String obtenerKey() {
        try {
            Dotenv dotenv = Dotenv.load();
            return dotenv.get("HMAC_SECRET");
        } catch (DotenvException e) {
            return System.getenv("HMAC_SECRET");
        }
    }

    /**
     * Valida de manera temprana que la clave HMAC_SECRET esté configurada correctamente.
     *
     * @throws IllegalStateException Si la clave no está presente o no tiene formato hexadecimal de 64 caracteres.
     */
    public static void validarHMACKey() {
        obtenerClave();
    }

    /**
     * Valida y devuelve la clave hexadecimal de 64 caracteres configurada para HMAC.
     *
     * @return Cadena hexadecimal de 64 caracteres.
     * @throws IllegalStateException Si la clave es nula o inválida.
     */
    private static String obtenerClave() {
        String key = obtenerKey();
            
        if (key == null || key.isBlank()) {
            throw new IllegalStateException(
                "La variable de entorno HMAC_SECRET no está configurada"
            );
        }

        if (!key.matches("[0-9a-fA-F]{64}")) {
            throw new IllegalStateException(
                "HMAC_SECRET debe ser una cadena hexadecimal de 64 caracteres"
            );
        }

        return key;   
    }

    /**
     * Genera la huella digital criptográfica (hash HMAC-SHA256) del dato proporcionado,
     * codificada en formato Base64.
     *
     * @param dato Cadena de texto a procesar (por ejemplo, el correo electrónico).
     * @return Huella digital resultante en formato Base64.
     * @throws RuntimeException Si el algoritmo no está disponible o la clave es inválida.
     */
    public static String GenerarHuella(String dato) {
        try {
            byte[] keyHex = HexFormat.of().parseHex(KEY);
            SecretKeySpec key = new SecretKeySpec(keyHex, ALGORITHM);
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(key);

            byte[] hmac = mac.doFinal(dato.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hmac);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("No se pudo generar la firma del correo");
        }
    }
}
