package com.Proyect_DevOps.users.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Proyect_DevOps.common.controllers.GenericController;
import com.Proyect_DevOps.users.models.RolModel;
import com.Proyect_DevOps.users.services.RolService;

/**
 * Controlador REST para la gestión de roles de usuarios.
 * Ofrece rutas para la creación y manipulación de roles de seguridad.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@RestController
@RequestMapping("/rol")
public class RolController extends GenericController<RolModel, Integer, RolService> {

    /**
     * Endpoint HTTP POST para crear un rol a partir de su nombre descriptivo.
     *
     * @param nombreRol Nombre del rol a registrar enviado en el cuerpo de la petición.
     * @return {@link ResponseEntity} con código 200 (OK) y mensaje de confirmación, o 401 si no pudo ser insertado.
     */
    @PostMapping("/insert")
    public ResponseEntity<?> insertRol(@RequestBody String nombreRol) {
        if (service.insertRol(nombreRol)) {
            return ResponseEntity.ok(Map.of("Message", "Rol CreadoExitosamente"));
        } else {
            return ResponseEntity.status(401).body(Map.of("Message", "El Rol no se pudo insertar"));
        }
    }
}
