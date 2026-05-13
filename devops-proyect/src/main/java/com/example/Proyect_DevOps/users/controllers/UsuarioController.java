package com.example.Proyect_DevOps.users.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Proyect_DevOps.users.models.UsuarioModel;
import com.example.Proyect_DevOps.users.services.UsuarioService;


@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        
        String correo = body.get("correo");
        String contrasena = body.get("contrasena");

        boolean valido = usuarioService.validacionDeLogin(correo, contrasena);

        if (valido) {
            return ResponseEntity.ok(Map.of("mensaje", "Login Exitoso", "login", true));
        } else {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }
    }

    @GetMapping("/Rol")
    public Integer obtenerIdRol(@RequestParam String correo) {
        return usuarioService.buscarIdRol(correo);
    }
    
    @GetMapping("/Nombre/{correo}")
    public Map<String,  String> extraerNombre(@PathVariable String correo) {
        return Map.of("nombre", usuarioService.extraerNombre(correo));
    }
    
    @GetMapping
    public List<UsuarioModel> getUsuarios() {
        return usuarioService.mostrarUsuarios();
    }
    
    @PostMapping("/guardar")
    public ResponseEntity<UsuarioModel> postMethodName(@RequestBody UsuarioModel usuario) {
        UsuarioModel nuevo = usuarioService.guardaUsuario(usuario);
        return ResponseEntity.status(201).body(nuevo);
    }

    
}