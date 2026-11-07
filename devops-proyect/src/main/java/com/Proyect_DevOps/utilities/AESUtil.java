package com.Proyect_DevOps.utilities;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

/**
 * Utilidad criptográfica para el cifrado y descifrado simétrico de datos sensibles.
 * Emplea el algoritmo AES con modo de operación GCM (Galois/Counter Mode) sin relleno (NoPadding),
 * garantizando confidencialidad e integridad criptográfica.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
public class AESUtil {

    /**
     * Nombre del esquema de cifrado: AES en modo GCM sin relleno.
     */
    private static final String ALGORITHM = "AES/GCM/NoPadding";

    /**
     * Algoritmo base de la clave simétrica.
     */
    private static final String KEY_ALGORITHM = "AES";

    /**
     * Clave secreta por defecto para entornos CI / test cuando no existe archivo .env.
     */
    private static final String DEFAULT_FALLBACK_KEY = "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef";

    /**
     * Clave secreta obtenida a través de la configuración del entorno.
     */
    private static final String KEY = obtenerClave();

    /**
     * Longitud en bytes recomendada para el vector de inicialización (IV) en AES-GCM (12 bytes = 96 bits).
     */
    private static final int IV_LENGTH = 12;

    /**
     * Longitud en bits de la etiqueta de autenticación (Tag) de GCM (128 bits).
     */
    private static final int TAG_LENGTH = 128;

    /**
     * Recupera el valor de la clave secreta desde el archivo .env, entorno o propiedades del sistema.
     *
     * @return Cadena que representa la clave secreta AES.
     */
    private static String obtenerKey() {
        try {
            Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
            String val = dotenv.get("AES_SECRET");
            if (val != null && !val.isBlank()) {
                return val;
            }
        } catch (Throwable e) {
            // ignorar si no existe .env en CI
        }
        String env = System.getenv("AES_SECRET");
        if (env != null && !env.isBlank()) {
            return env;
        }
        String prop = System.getProperty("AES_SECRET");
        if (prop != null && !prop.isBlank()) {
            return prop;
        }
        return null;
    }

    /**
     * Valida de manera explícita que la clave secreta AES esté correctamente configurada.
     *
     * @throws IllegalStateException Si la variable no existe o no tiene formato hexadecimal de 64 caracteres.
     */
    public static void validarAESKey() {
        String key = obtenerKey();
        if (key == null || key.isBlank() || !key.matches("[0-9a-fA-F]{64}")) {
            throw new IllegalStateException("La variable de entorno AES_SECRET no está configurada o es inválida");
        }
    }   

    /**
     * Valida y retorna la clave secreta en formato hexadecimal.
     *
     * @return Cadena hexadecimal de 64 caracteres (256 bits).
     */
    private static String obtenerClave() {
        String key = obtenerKey();

        if (key == null || key.isBlank() || !key.matches("[0-9a-fA-F]{64}")) {
            return DEFAULT_FALLBACK_KEY;
        }

        return key;
    }

    /**
     * Cifra un texto en claro utilizando AES-GCM-256 con un vector de inicialización (IV) aleatorio.
     * El resultado concatenado [IV + texto_cifrado] se codifica en Base64.
     *
     * @param dato Texto en claro a cifrar.
     * @return Cadena cifrada codificada en Base64.
     * @throws RuntimeException Si ocurre una falla en el proceso criptográfico.
     */
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
            System.arraycopy(IV, 0, resultado, 0, IV.length);
            System.arraycopy(cifrado, 0, resultado, IV.length, cifrado.length);

            return Base64.getEncoder().encodeToString(resultado);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException |
            IllegalBlockSizeException | BadPaddingException | IllegalArgumentException e) {
            throw new RuntimeException("Error al encriptar", e);
        }
    }

    /**
     * Desencripta una cadena cifrada en Base64 que contiene el vector IV y los datos cifrados.
     *
     * @param datoEncriptado Cadena cifrada en Base64.
     * @return Texto en claro original recuperado.
     * @throws RuntimeException Si ocurre un error en la autenticación GCM o durante el descifrado.
     */
    public static String desencriptar(String datoEncriptado) {
        try {
            byte[] keyHex = HexFormat.of().parseHex(KEY);
            SecretKeySpec key = new SecretKeySpec(keyHex, KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            byte[] decoded = Base64.getDecoder().decode(datoEncriptado);

            byte[] IV = new byte[IV_LENGTH];
            System.arraycopy(decoded, 0, IV, 0, IV_LENGTH);
            byte[] cifrado = new byte[decoded.length - IV.length];
            System.arraycopy(decoded, IV_LENGTH, cifrado, 0, decoded.length - IV_LENGTH);

            GCMParameterSpec parametros = new GCMParameterSpec(TAG_LENGTH, IV);
            cipher.init(Cipher.DECRYPT_MODE, key, parametros);

            return new String(cipher.doFinal(cifrado), StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException |
            IllegalBlockSizeException | BadPaddingException | IllegalArgumentException e) {
            throw new RuntimeException("Error al desencriptar", e);
        }
    }
}
