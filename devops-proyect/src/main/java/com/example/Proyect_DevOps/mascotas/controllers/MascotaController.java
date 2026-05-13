package com.example.Proyect_DevOps.mascotas.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Proyect_DevOps.mascotas.dtos.MascotaDTO;
import com.example.Proyect_DevOps.mascotas.services.MascotaService;

@RestController
@RequestMapping("/mascota")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    @GetMapping("/{correo}")
    public List<MascotaDTO> getListMascota(@PathVariable String correo) {
        return mascotaService.getAllMascotaByUser(correo);
    }
    
    @GetMapping("/usuario/{correo}")
    public long contarMascotasPorUsuario(@PathVariable String correo) {
        return mascotaService.getCountMascotasByUser(correo);
    }
    
}
