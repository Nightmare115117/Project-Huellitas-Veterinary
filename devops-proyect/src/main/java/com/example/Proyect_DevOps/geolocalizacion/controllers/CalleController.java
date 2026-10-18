package com.example.Proyect_DevOps.geolocalizacion.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.example.Proyect_DevOps.common.controllers.GenericController;
import com.example.Proyect_DevOps.geolocalizacion.models.CalleModel;
import com.example.Proyect_DevOps.geolocalizacion.services.CalleService;

/**
 * Controlador REST para el catálogo geográfico de calles.
 * Expone rutas CRUD bajo el endpoint {@code /calle}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Tag(name = "Calles", description = "Catálogo de calles")
@RestController
@RequestMapping("/calle")
public class CalleController extends GenericController<CalleModel, Integer, CalleService> {

}
