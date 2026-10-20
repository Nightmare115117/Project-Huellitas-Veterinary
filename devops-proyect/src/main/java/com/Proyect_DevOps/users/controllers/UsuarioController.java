package com.Proyect_DevOps.users.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpServletRequest;

import com.Proyect_DevOps.bitacora.services.BitacoraService;
import com.Proyect_DevOps.common.controllers.GenericController;
import com.Proyect_DevOps.users.models.UsuarioModel;
import com.Proyect_DevOps.users.services.UsuarioService;
import com.Proyect_DevOps.utilities.JWTUtil;

/**
 * Controlador REST para la gestión de usuarios y autenticación en el sistema.
 * Proporciona endpoints para inicio de sesión (login con emisión de JWT), consulta de roles
 * y obtención del nombre del usuario mediante su correo electrónico.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@RestController
@RequestMapping("/usuario")
public class UsuarioController extends GenericController<UsuarioModel, Integer, UsuarioService> {

    /**
     * Utilidad para la emisión y codificación de tokens JWT.
     */
    private final JWTUtil jwtUtil;

    /**
     * Servicio de auditoría para registrar intentos de autenticación exitosos y fallidos.
     */
    private final BitacoraService bitacoraService;


    /**
     * Constructor con inyección de dependencias para autenticación y bitácora.
     *
     * @param jwtUtil Utilidad de generación de tokens JWT.
     * @param bitacoraService Servicio opcional para registro de eventos en la bitácora.
     */
    public UsuarioController(JWTUtil jwtUtil, @Autowired(required = false) BitacoraService bitacoraService) {
        this.jwtUtil = jwtUtil;
        this.bitacoraService = bitacoraService;
    }

    /**
     * Endpoint HTTP POST para autenticación de usuarios (login).
     * Valida el correo y la contraseña, emite un token JWT si las credenciales son válidas
     * y registra el evento de inicio de sesión en la bitácora de auditoría.
     *
     * @param credenciales Mapa con los campos {@code correo} y {@code contrasena}.
     * @param request Petición HTTP para obtener la dirección IP del cliente.
     * @return {@link ResponseEntity} con código 200 (OK) y token JWT si es exitoso,
     *         o código 400 / 401 si las credenciales son incorrectas o faltan campos.
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credenciales, HttpServletRequest request) {
        String correo = credenciales.get("correo");
        String contrasena = credenciales.get("contrasena");
        if (contrasena == null) {
            contrasena = credenciales.get("contraseña");
        }
        String ip = request != null ? request.getRemoteAddr() : "Desconocida";
        try {
            if ((correo != null && contrasena != null) && (!correo.isBlank() && !contrasena.isBlank())) {
                UsuarioModel usuario = service.validacionDeLogin(correo, contrasena, 0);
                if (!usuario.isMFANeeded()) {
                    String rolNombre = usuario.getRol() != null ? usuario.getRol().getNombre() : "Sin rol";
                    int rolId = usuario.getRol() != null ? usuario.getRol().getIdRol() : 0;

                    if (bitacoraService != null) {
                        bitacoraService.registrarAccion(
                            correo,
                            rolNombre,
                            "LOGIN_EXITOSO",
                            "Autenticación",
                            "Inicio de sesión correcto.",
                            ip
                        );
                    }

                    return ResponseEntity.ok(Map.of("login", true, "token", jwtUtil.generarToken(correo, rolId, rolNombre)));
                } else {
                    int idRol = usuario.getRol().getIdRol();
                    String nombreRol = usuario.getRol().getNombre();
                    return ResponseEntity.ok(Map.of("login", false, "mfaneed", true, "temToken", jwtUtil.generarTokenTemp(correo, idRol, nombreRol)));
                }
            } else {
                if (bitacoraService != null) {
                    bitacoraService.registrarAccion(
                        correo != null ? correo : "Anónimo",
                        "Desconocido",
                        "LOGIN_FALLIDO",
                        "Autenticación",
                        "Intento de inicio de sesión con campos incompletos.",
                        ip
                    );
                }
                return ResponseEntity.badRequest().body(Map.of("login", false));
            }
        } catch (IllegalArgumentException e) {
            if (bitacoraService != null) {
                bitacoraService.registrarAccion(
                    correo != null ? correo : "Anónimo",
                    "Desconocido",
                    "LOGIN_FALLIDO",
                    "Autenticación",
                    "Credenciales incorrectas o usuario inexistente.",
                    ip
                );
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("login", false));
        }
    }



    /**
     * Endpoint HTTP GET para consultar el identificador del rol asignado a un usuario mediante su correo.
     *
     * @param correo Correo electrónico del usuario enviado como parámetro de consulta.
     * @return {@link ResponseEntity} con código 200 (OK) y el ID del rol o -1 si no fue encontrado.
     */
    @GetMapping("/Rol")
    public ResponseEntity<Integer> getRol(@RequestParam("correo") String correo) {
        Integer idRol = service.buscarIdRol(correo);
        return ResponseEntity.ok(idRol);
    }

    /**
     * Endpoint HTTP GET para consultar el nombre completo de un usuario a partir de su correo electrónico.
     *
     * @param correo Correo del usuario especificado en la ruta.
     * @return {@link ResponseEntity} con código 200 (OK) y mapa con el nombre completo del usuario.
     */
    @GetMapping("/Nombre/{correo:.+}")
    public ResponseEntity<Map<String, String>> getNombre(@PathVariable String correo) {
        String nombre = service.extraerNombre(correo);
        return ResponseEntity.ok(Map.of("nombre", nombre));
    }
}
