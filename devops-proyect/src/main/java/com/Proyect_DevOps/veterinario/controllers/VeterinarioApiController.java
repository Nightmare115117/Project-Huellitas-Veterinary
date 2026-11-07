package com.Proyect_DevOps.veterinario.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Proyect_DevOps.veterinario.dtos.CitaVeterinarioDTO;
import com.Proyect_DevOps.veterinario.dtos.CrearRecetaDTO;
import com.Proyect_DevOps.veterinario.dtos.RecetaDTO;
import com.Proyect_DevOps.veterinario.services.VeterinarioApiService;

/**
 * Controlador REST para el módulo exclusivo del Médico Veterinario (/veterinario/{correo}/...).
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@RestController
@RequestMapping("/veterinario/{correo}")
public class VeterinarioApiController {

    @Autowired
    private VeterinarioApiService veterinarioApiService;

    /**
     * 3.1 Obtiene el listado de citas asignadas al veterinario.
     */
    @GetMapping("/citas")
    public ResponseEntity<List<CitaVeterinarioDTO>> getCitas(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("correo") String correo) {
        veterinarioApiService.validarVeterinario(jwt, correo);
        List<CitaVeterinarioDTO> citas = veterinarioApiService.getCitas(correo);
        return ResponseEntity.ok(citas);
    }

    /**
     * 3.2 Obtiene las recetas médicas emitidas por el veterinario, de más recientes a antiguas.
     */
    @GetMapping("/recetas")
    public ResponseEntity<List<RecetaDTO>> getRecetas(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("correo") String correo) {
        veterinarioApiService.validarVeterinario(jwt, correo);
        List<RecetaDTO> recetas = veterinarioApiService.getRecetas(correo);
        return ResponseEntity.ok(recetas);
    }

    /**
     * 3.3 Registra una nueva receta y consulta médica asociada a una de sus citas.
     */
    @PostMapping("/recetas")
    public ResponseEntity<Void> crearReceta(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("correo") String correo,
            @RequestBody CrearRecetaDTO dto) {
        veterinarioApiService.validarVeterinario(jwt, correo);
        veterinarioApiService.crearReceta(correo, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
