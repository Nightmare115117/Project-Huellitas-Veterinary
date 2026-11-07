package com.Proyect_DevOps.gestor.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Proyect_DevOps.gestor.dtos.CitaGestorDTO;
import com.Proyect_DevOps.gestor.dtos.GuardarVeterinarioDTO;
import com.Proyect_DevOps.gestor.dtos.MascotaGestorDTO;
import com.Proyect_DevOps.gestor.dtos.ReporteGestorDTO;
import com.Proyect_DevOps.gestor.dtos.SucursalGestorDTO;
import com.Proyect_DevOps.gestor.dtos.UsuarioGestorDTO;
import com.Proyect_DevOps.gestor.services.GestorApiService;

/**
 * Controlador REST para el módulo exclusivo del Gestor de sucursal (/gestor/{correo}/...).
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@RestController
@RequestMapping("/gestor/{correo}")
public class GestorApiController {

    @Autowired
    private GestorApiService gestorApiService;

    /**
     * 2.1 Obtiene la dirección y RFC de la sucursal del gestor.
     */
    @GetMapping("/sucursal")
    public ResponseEntity<SucursalGestorDTO> getSucursal(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("correo") String correo) {
        gestorApiService.validarGestor(jwt, correo);
        SucursalGestorDTO dto = gestorApiService.getSucursal(correo);
        return ResponseEntity.ok(dto);
    }

    /**
     * 2.2 Obtiene las citas de mascotas adscritas a la sucursal del gestor.
     */
    @GetMapping("/citas")
    public ResponseEntity<List<CitaGestorDTO>> getCitas(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("correo") String correo) {
        gestorApiService.validarGestor(jwt, correo);
        List<CitaGestorDTO> citas = gestorApiService.getCitas(correo);
        return ResponseEntity.ok(citas);
    }

    /**
     * 2.2b Actualización parcial de una cita (asignar/desasignar veterinario o cambiar estado).
     */
    @PutMapping("/citas/{idCita}")
    public ResponseEntity<Void> actualizarCita(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("correo") String correo,
            @PathVariable("idCita") int idCita,
            @RequestBody Map<String, Object> cambios) {
        gestorApiService.validarGestor(jwt, correo);
        gestorApiService.actualizarCita(correo, idCita, cambios);
        return ResponseEntity.noContent().build();
    }

    /**
     * 2.3 Obtiene los veterinarios pertenecientes a la sucursal del gestor.
     */
    @GetMapping("/veterinarios")
    public ResponseEntity<List<UsuarioGestorDTO>> getVeterinarios(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("correo") String correo) {
        gestorApiService.validarGestor(jwt, correo);
        List<UsuarioGestorDTO> vets = gestorApiService.getVeterinarios(correo);
        return ResponseEntity.ok(vets);
    }

    /**
     * 2.3b Registra un nuevo veterinario adscrito a la sucursal del gestor.
     */
    @PostMapping("/veterinarios")
    public ResponseEntity<UsuarioGestorDTO> crearVeterinario(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("correo") String correo,
            @RequestBody GuardarVeterinarioDTO dto) {
        gestorApiService.validarGestor(jwt, correo);
        UsuarioGestorDTO creado = gestorApiService.crearVeterinario(correo, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /**
     * 2.3c Actualiza la información de un veterinario de la sucursal.
     */
    @PutMapping("/veterinarios/{idUsuario}")
    public ResponseEntity<Void> actualizarVeterinario(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("correo") String correo,
            @PathVariable("idUsuario") int idUsuario,
            @RequestBody GuardarVeterinarioDTO dto) {
        gestorApiService.validarGestor(jwt, correo);
        gestorApiService.actualizarVeterinario(correo, idUsuario, dto);
        return ResponseEntity.noContent().build();
    }

    /**
     * 2.3d Elimina a un veterinario de la sucursal si no tiene citas ni recetas.
     */
    @DeleteMapping("/veterinarios/{idUsuario}")
    public ResponseEntity<Void> eliminarVeterinario(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("correo") String correo,
            @PathVariable("idUsuario") int idUsuario) {
        gestorApiService.validarGestor(jwt, correo);
        gestorApiService.eliminarVeterinario(correo, idUsuario);
        return ResponseEntity.noContent().build();
    }

    /**
     * 2.4 Obtiene los dueños con al menos una mascota en la sucursal.
     */
    @GetMapping("/duenos")
    public ResponseEntity<List<UsuarioGestorDTO>> getDuenos(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("correo") String correo) {
        gestorApiService.validarGestor(jwt, correo);
        List<UsuarioGestorDTO> duenos = gestorApiService.getDuenos(correo);
        return ResponseEntity.ok(duenos);
    }

    /**
     * 2.5 Obtiene las mascotas de la sucursal del gestor.
     */
    @GetMapping("/mascotas")
    public ResponseEntity<List<MascotaGestorDTO>> getMascotas(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("correo") String correo) {
        gestorApiService.validarGestor(jwt, correo);
        List<MascotaGestorDTO> mascotas = gestorApiService.getMascotas(correo);
        return ResponseEntity.ok(mascotas);
    }

    /**
     * 2.6 Obtiene el reporte financiero y operativo de la sucursal en un rango de fechas.
     */
    @GetMapping("/reportes")
    public ResponseEntity<ReporteGestorDTO> getReportes(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("correo") String correo,
            @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        gestorApiService.validarGestor(jwt, correo);
        ReporteGestorDTO reporte = gestorApiService.getReportes(correo, desde, hasta);
        return ResponseEntity.ok(reporte);
    }
}
