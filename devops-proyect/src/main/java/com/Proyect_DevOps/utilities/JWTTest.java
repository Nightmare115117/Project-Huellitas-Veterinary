package com.Proyect_DevOps.utilities;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

/**
 * Validador de configuración para las claves secretas de tokens JWT.
 * Comprueba que la variable de entorno JWT_SECRET exista y cumpla con la longitud requerida.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
public class JWTTest {

    /**
     * Recupera la clave JWT_SECRET del entorno, archivo .env o propiedades de sistema.
     *
     * @return Cadena con la clave secreta configurada.
     */
    private static String obtenerKey() {
        try {
            Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
            String val = dotenv.get("JWT_SECRET");
            if (val != null && !val.isBlank()) {
                return val;
            }
        } catch (Throwable e) {
            // ignore
        }
        String env = System.getenv("JWT_SECRET");
        if (env != null && !env.isBlank()) {
            return env;
        }
        String prop = System.getProperty("JWT_SECRET");
        if (prop != null && !prop.isBlank()) {
            return prop;
        }
        return null;
    }

    /**
     * Valida que la clave secreta para JWT no esté vacía y posea exactamente 64 caracteres hexadecimales (256 bits).
     *
     * @throws IllegalStateException Si la variable no existe o no cuenta con el formato hexadecimal válido.
     */
    public static void validarJWTKey() {
        String key = obtenerKey();

        if (key == null || key.isBlank()) {
            throw new IllegalStateException(
                "La variable de entorno JWT_SECRET no está configurada"
            );
        }

        if (!key.matches("[0-9a-fA-F]{64}")) {
            throw new IllegalStateException(
                "JWT_SECRET debe ser una cadena hexadecimal de 64 caracteres"
            );
        }
    }  
}
