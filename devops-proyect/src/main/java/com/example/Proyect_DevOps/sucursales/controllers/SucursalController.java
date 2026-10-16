package com.example.Proyect_DevOps.sucursales.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Proyect_DevOps.common.controllers.GenericController;
import com.example.Proyect_DevOps.sucursales.models.SucursalModel;
import com.example.Proyect_DevOps.sucursales.services.SucursalService;

@RestController
@RequestMapping("/sucursal")
public class SucursalController extends GenericController<SucursalModel, Integer, SucursalService> {

}
