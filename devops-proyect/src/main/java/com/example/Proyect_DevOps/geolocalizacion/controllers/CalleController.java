package com.example.Proyect_DevOps.geolocalizacion.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Proyect_DevOps.common.controllers.GenericController;
import com.example.Proyect_DevOps.geolocalizacion.models.CalleModel;
import com.example.Proyect_DevOps.geolocalizacion.services.CalleService;

@RestController
@RequestMapping("/calle")
public class CalleController extends GenericController<CalleModel, Integer, CalleService> {

}
