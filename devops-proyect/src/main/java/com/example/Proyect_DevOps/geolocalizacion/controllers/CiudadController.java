package com.example.Proyect_DevOps.geolocalizacion.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Proyect_DevOps.common.controllers.GenericController;
import com.example.Proyect_DevOps.geolocalizacion.models.CiudadModel;
import com.example.Proyect_DevOps.geolocalizacion.services.CiudadService;

@RestController
@RequestMapping("/ciudad")
public class CiudadController extends GenericController<CiudadModel, Integer, CiudadService> {

}
