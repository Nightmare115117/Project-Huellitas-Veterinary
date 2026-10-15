package com.example.Proyect_DevOps.utilities;

import org.springframework.stereotype.Service;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

@Service 
public class JWTUtil {

    private static final String KEY = obtenerClave();

    private static String obtenerKey() {
        try {
            Dotenv dotenv = Dotenv.load();
            return  dotenv.get("JWT_SECRET");
        } catch (DotenvException e) {
            return System.getenv("JWT_SECRET");
        }
    }

    private static String obtenerClave() {
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

        return key;
    }

    
   
}