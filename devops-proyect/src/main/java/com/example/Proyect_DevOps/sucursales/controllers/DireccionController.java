package com.example.Proyect_DevOps.sucursales.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Proyect_DevOps.common.controllers.GenericController;
import com.example.Proyect_DevOps.sucursales.models.DireccionModel;
import com.example.Proyect_DevOps.sucursales.services.DireccionService;

@RestController
@RequestMapping("/direccion")
public class DireccionController extends GenericController<DireccionModel, Integer, DireccionService> {

}
