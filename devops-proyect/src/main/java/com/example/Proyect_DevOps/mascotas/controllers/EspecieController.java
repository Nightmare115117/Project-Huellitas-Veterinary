package com.example.Proyect_DevOps.mascotas.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Proyect_DevOps.common.controllers.GenericController;
import com.example.Proyect_DevOps.mascotas.models.EspecieModel;
import com.example.Proyect_DevOps.mascotas.services.EspecieService;

@RestController
@RequestMapping("/especie")
public class EspecieController extends GenericController<EspecieModel, Integer, EspecieService> {

    @GetMapping("/mostrar")
    public List<EspecieModel> getEspecieModels() {
        return service.mostrarEspecies();
    }
}
