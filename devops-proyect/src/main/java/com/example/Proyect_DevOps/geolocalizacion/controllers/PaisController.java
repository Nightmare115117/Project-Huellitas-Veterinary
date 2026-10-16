package com.example.Proyect_DevOps.geolocalizacion.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Proyect_DevOps.common.controllers.GenericController;
import com.example.Proyect_DevOps.geolocalizacion.models.PaisModel;
import com.example.Proyect_DevOps.geolocalizacion.services.PaisService;

@RestController
@RequestMapping("/pais")
public class PaisController extends GenericController<PaisModel, Integer, PaisService> {

}
