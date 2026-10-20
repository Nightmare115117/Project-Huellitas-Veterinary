package com.Proyect_DevOps.geolocalizacion.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Proyect_DevOps.common.controllers.GenericController;
import com.Proyect_DevOps.geolocalizacion.models.CalleModel;
import com.Proyect_DevOps.geolocalizacion.services.CalleService;

/**
 * Controlador REST para el catálogo geográfico de calles.
 * Expone rutas CRUD bajo el endpoint {@code /calle}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@RestController
@RequestMapping("/calle")
public class CalleController extends GenericController<CalleModel, Integer, CalleService> {

}
