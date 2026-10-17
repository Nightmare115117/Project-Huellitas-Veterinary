package com.example.Proyect_DevOps.users.controllers;

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

import com.example.Proyect_DevOps.bitacora.services.BitacoraService;
import com.example.Proyect_DevOps.common.controllers.GenericController;
import com.example.Proyect_DevOps.users.models.UsuarioModel;
import com.example.Proyect_DevOps.users.services.UsuarioService;
import com.example.Proyect_DevOps.utilities.JWTUtil;

@RestController
@RequestMapping("/usuario")
public class UsuarioController extends GenericController<UsuarioModel, Integer, UsuarioService> {

    private final JWTUtil jwtUtil;
    private final BitacoraService bitacoraService;

    public UsuarioController(JWTUtil jwtUtil, @Autowired(required = false) BitacoraService bitacoraService) {
        this.jwtUtil = jwtUtil;
        this.bitacoraService = bitacoraService;
    }

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
                UsuarioModel usuario = service.validacionDeLogin(correo, contrasena);
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

                return ResponseEntity.ok(Map.of("login",true, "token", jwtUtil.generarToken(correo, rolId, rolNombre)));
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
        }   catch (IllegalArgumentException e) {
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

    @GetMapping("/Rol")
    public ResponseEntity<Integer> getRol(@RequestParam("correo") String correo) {
        Integer idRol = service.buscarIdRol(correo);
        return ResponseEntity.ok(idRol);
    }

    @GetMapping("/Nombre/{correo:.+}")
    public ResponseEntity<Map<String, String>> getNombre(@PathVariable String correo) {
        String nombre = service.extraerNombre(correo);
        return ResponseEntity.ok(Map.of("nombre", nombre));
    }
}
