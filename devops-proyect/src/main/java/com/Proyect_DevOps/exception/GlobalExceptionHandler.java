package com.Proyect_DevOps.exception;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

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
     * Captura excepciones de tipo {@link ResponseStatusException} para respetar el código de estado HTTP y mensaje.
     *
     * @param e La excepción ResponseStatusException capturada.
     * @return {@link ResponseEntity} con el código de estado correspondiente y mensaje explicativo.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> manejarResponseStatusException(ResponseStatusException e) {
        return ResponseEntity
            .status(e.getStatusCode())
            .body(Map.of(
                "success", false,
                "message", e.getReason() != null ? e.getReason() : (e.getMessage() != null ? e.getMessage() : "Error en la solicitud")
            ));
    }

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
