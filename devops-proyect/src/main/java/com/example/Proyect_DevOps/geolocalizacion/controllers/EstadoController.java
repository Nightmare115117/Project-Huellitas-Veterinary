package com.example.Proyect_DevOps.geolocalizacion.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.example.Proyect_DevOps.common.controllers.GenericController;
import com.example.Proyect_DevOps.geolocalizacion.models.EstadoModel;
import com.example.Proyect_DevOps.geolocalizacion.services.EstadoService;

/**
 * Controlador REST para el catálogo geográfico de estados.
 * Expone rutas CRUD bajo el endpoint {@code /estado}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Tag(name = "Estados", description = "Catálogo de estados")
@RestController
@RequestMapping("/estado")
public class EstadoController extends GenericController<EstadoModel, Integer, EstadoService> {

}
