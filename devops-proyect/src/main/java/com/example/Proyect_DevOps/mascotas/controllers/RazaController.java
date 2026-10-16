package com.example.Proyect_DevOps.mascotas.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Proyect_DevOps.common.controllers.GenericController;
import com.example.Proyect_DevOps.mascotas.models.RazaModel;
import com.example.Proyect_DevOps.mascotas.services.RazaService;

@RestController
@RequestMapping("/raza")
public class RazaController extends GenericController<RazaModel, Integer, RazaService> {

    @GetMapping("/especie/{idEspecie:[0-9]+}")
    public List<RazaModel> getRazasByEspecie(@PathVariable int idEspecie) {
        return service.findByEspecieId(idEspecie);
    }
}