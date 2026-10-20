package com.Proyect_DevOps.sucursales.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Proyect_DevOps.common.controllers.GenericController;
import com.Proyect_DevOps.sucursales.models.DireccionModel;
import com.Proyect_DevOps.sucursales.services.DireccionService;

/**
 * Controlador REST para la gestión de direcciones de sucursales.
 * Expone rutas bajo el endpoint {@code /direccion}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@RestController
@RequestMapping("/direccion")
public class DireccionController extends GenericController<DireccionModel, Integer, DireccionService> {

}
