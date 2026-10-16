package com.example.Proyect_DevOps.sucursales.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Proyect_DevOps.common.controllers.GenericController;
import com.example.Proyect_DevOps.sucursales.models.GestorModel;
import com.example.Proyect_DevOps.sucursales.services.GestorService;

@RestController
@RequestMapping("/gestor")
public class GestorController extends GenericController<GestorModel, Integer, GestorService> {

}
