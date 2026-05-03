package com.example.Proyect_DevOps.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Proyect_DevOps.dtos.CitaDTO;
import com.example.Proyect_DevOps.services.CitaService;



@RestController
@RequestMapping("/cita")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @GetMapping("/usuario/{correo}")
    public long contarCitasPorUsuario(@PathVariable String correo) {
        return citaService.countByUsuarioCitas(correo);
    }

    @GetMapping("/{correo}")
    public List<CitaDTO> conseguirListaDeCitas(@PathVariable String correo) {
        return citaService.getCitasByUser(correo);
    }

    @GetMapping("/{correo}/{idMascota}")
    public List<CitaDTO> conseguirListaDeCitasPorMascota(@PathVariable String correo, @PathVariable int idMascota){
        return citaService.getCitasByUserAndPet(correo, idMascota);
    }
    
    @PostMapping("/crear")
    public CitaDTO crearCita(@RequestBody CitaDTO citaDTO){
        return citaService.createCita(citaDTO);
    }
}