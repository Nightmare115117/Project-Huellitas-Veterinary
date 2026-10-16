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

import com.example.Proyect_DevOps.common.controllers.GenericController;
import com.example.Proyect_DevOps.users.models.UsuarioModel;
import com.example.Proyect_DevOps.users.services.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController extends GenericController<UsuarioModel, Integer, UsuarioService> {

    @PostMapping("/guardar")
    public ResponseEntity<UsuarioModel> guardarUsuario(@RequestBody UsuarioModel usuario) {
        UsuarioModel guardado = service.guardaUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Boolean>> login(@RequestBody Map<String, String> credenciales) {
        String correo = credenciales.get("correo");
        String contrasena = credenciales.get("contrasena");
        if (correo == null || contrasena == null) {
            contrasena = credenciales.get("contraseña");
        }
        boolean ok = correo != null && contrasena != null && service.validacionDeLogin(correo, contrasena);
        return ResponseEntity.ok(Map.of("login", ok));
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
