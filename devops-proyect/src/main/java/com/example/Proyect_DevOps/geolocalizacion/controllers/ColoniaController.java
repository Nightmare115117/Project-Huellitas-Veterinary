package com.example.Proyect_DevOps.geolocalizacion.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Proyect_DevOps.common.controllers.GenericController;
import com.example.Proyect_DevOps.geolocalizacion.models.ColoniaModel;
import com.example.Proyect_DevOps.geolocalizacion.services.ColoniaService;

@RestController
@RequestMapping("/colonia")
public class ColoniaController extends GenericController<ColoniaModel, Integer, ColoniaService> {

}
