package com.example.Proyect_DevOps.sucursales.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

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
@Tag(name = "Sucursales", description = "Administración de sucursales")
@RestController
@RequestMapping("/sucursal")
public class SucursalController extends GenericController<SucursalModel, Integer, SucursalService> {

}
