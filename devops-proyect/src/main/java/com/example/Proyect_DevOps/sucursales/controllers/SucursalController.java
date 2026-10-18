package com.example.Proyect_DevOps.sucursales.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Proyect_DevOps.common.controllers.GenericController;
import com.example.Proyect_DevOps.sucursales.models.SucursalModel;
import com.example.Proyect_DevOps.sucursales.services.SucursalService;

/**
 * Controlador REST para la administración de sucursales de la clínica veterinaria.
 * Expone rutas bajo el endpoint {@code /sucursal}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@RestController
@RequestMapping("/sucursal")
public class SucursalController extends GenericController<SucursalModel, Integer, SucursalService> {

}
