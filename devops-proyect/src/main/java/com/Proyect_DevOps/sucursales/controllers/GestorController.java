package com.Proyect_DevOps.sucursales.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Proyect_DevOps.common.controllers.GenericController;
import com.Proyect_DevOps.sucursales.models.GestorModel;
import com.Proyect_DevOps.sucursales.services.GestorService;

/**
 * Controlador REST para la gestión de gestores de sucursal.
 * Expone rutas bajo el endpoint {@code /gestor}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@RestController
@RequestMapping("/gestor")
public class GestorController extends GenericController<GestorModel, Integer, GestorService> {

}
