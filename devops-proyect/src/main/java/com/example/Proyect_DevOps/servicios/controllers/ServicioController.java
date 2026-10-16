package com.example.Proyect_DevOps.servicios.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Proyect_DevOps.common.controllers.GenericController;
import com.example.Proyect_DevOps.servicios.models.ServicioModel;
import com.example.Proyect_DevOps.servicios.services.ServicioService;

@RestController
@RequestMapping("/servicio")
public class ServicioController extends GenericController<ServicioModel, Integer, ServicioService> {

}
