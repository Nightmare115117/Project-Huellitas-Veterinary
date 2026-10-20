package com.Proyect_DevOps.geolocalizacion.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Proyect_DevOps.common.controllers.GenericController;
import com.Proyect_DevOps.geolocalizacion.models.CiudadModel;
import com.Proyect_DevOps.geolocalizacion.services.CiudadService;

/**
 * Controlador REST para el catálogo geográfico de ciudades.
 * Expone rutas CRUD bajo el endpoint {@code /ciudad}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@RestController
@RequestMapping("/ciudad")
public class CiudadController extends GenericController<CiudadModel, Integer, CiudadService> {

}
