package com.example.Proyect_DevOps.consultaMedica.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Proyect_DevOps.common.controllers.GenericController;
import com.example.Proyect_DevOps.consultaMedica.dtos.TratamientoDTO;
import com.example.Proyect_DevOps.consultaMedica.models.TratamientoModel;
import com.example.Proyect_DevOps.consultaMedica.services.TratamientoService;

@RestController
@RequestMapping("/tratamiento")
public class TratamientoController extends GenericController<TratamientoModel, Integer, TratamientoService> {

    @GetMapping("/usuario/{correo:.+}")
    public List<TratamientoDTO> getTratamientosPorUsuario(@PathVariable String correo){
        return service.mostrarTratamientosPorUsuario(correo);
    }

    @GetMapping("/contar/{correo:.+}")
    public long contarTratamientosIncompletos(@PathVariable String correo) {
        return service.contarTratamientosIncompletos(correo);
    }

    @GetMapping("/usuario/{correo:.+}/{idMascota:[0-9]+}")
    public List<TratamientoDTO> getTratamientosPorUsuarioYMascota(@PathVariable String correo, @PathVariable int idMascota) {
        return service.mostrarTratamientosPorUsuarioYMascota(correo, idMascota);
    }
}
