package com.example.Proyect_DevOps.users.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Proyect_DevOps.users.services.RolService;

@RestController
@RequestMapping("/rol")
public class RolController {

    @Autowired 
    private RolService service;

    @PostMapping("/insert")
    public ResponseEntity<?> insertRol(@RequestBody String nombreRol) {

        if (service.insertRol(nombreRol)) {
            return ResponseEntity.ok(Map.of("Message", "Rol CreadoExitosamente"));
        } else {
            return ResponseEntity.status(401).body(Map.of("Message", "El Rol no se pudo insertar"));
        }
    }
}
