package com.Proyect_DevOps.exception;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador global de excepciones para los controladores REST.
 * Intercepta excepciones no controladas y formatea respuestas HTTP uniformes en formato JSON.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Captura excepciones de tipo {@link RuntimeException} ocurridas durante el procesamiento de peticiones.
     *
     * @param e La excepción en tiempo de ejecución capturada.
     * @return {@link ResponseEntity} con código 400 (Bad Request) y mapa con el mensaje descriptivo del error.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> manejarExcepcion(RuntimeException e) {
        return ResponseEntity
        .badRequest()
        .body(
            Map.of("success", false,
        "message", e.getMessage() != null ? e.getMessage() : "Error en la solicitud"
        ));
    }
}
