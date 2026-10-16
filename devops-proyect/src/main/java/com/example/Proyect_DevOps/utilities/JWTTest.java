package com.example.Proyect_DevOps.utilities;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

public class JWTTest {

    private static String obtenerKey() {
        try {
            Dotenv dotenv = Dotenv.load();
            return  dotenv.get("JWT_SECRET");
        } catch (DotenvException e) {
            return System.getenv("JWT_SECRET");
        }
    }

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