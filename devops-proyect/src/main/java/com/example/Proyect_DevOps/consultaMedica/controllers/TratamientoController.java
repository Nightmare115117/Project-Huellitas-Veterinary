package com.example.Proyect_DevOps.consultaMedica.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Proyect_DevOps.consultaMedica.dtos.TratamientoDTO;
import com.example.Proyect_DevOps.consultaMedica.services.TratamientoService;

@RestController
@RequestMapping("/tratamiento")
public class TratamientoController {

    
    @Autowired
    private TratamientoService tratamientoService;

    @GetMapping("/usuario/{correo:.+}")
    public List<TratamientoDTO> getTratamientosPorUsuario(@PathVariable String correo){
        return tratamientoService.mostrarTratamientosPorUsuario(correo);
    }

    @GetMapping("/contar/{correo:.+}")
    public long contarTratamientosIncompletos(@PathVariable String correo) {
        return tratamientoService.contarTratamientosIncompletos(correo);
    }

    @GetMapping("/usuario/{correo:.+}/{idMascota}")
    public List<TratamientoDTO> getTratamientosPorUsuarioYMascota(@PathVariable String correo, @PathVariable int idMascota) {
        return tratamientoService.mostrarTratamientosPorUsuarioYMascota(correo, idMascota);
    }
}
