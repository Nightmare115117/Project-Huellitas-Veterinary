package com.example.Proyect_DevOps.geolocalizacion.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.example.Proyect_DevOps.common.controllers.GenericController;
import com.example.Proyect_DevOps.geolocalizacion.models.PaisModel;
import com.example.Proyect_DevOps.geolocalizacion.services.PaisService;

/**
 * Controlador REST para el catálogo geográfico de países.
 * Expone rutas CRUD bajo el endpoint {@code /pais}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Tag(name = "Países", description = "Catálogo de países")
@RestController
@RequestMapping("/pais")
public class PaisController extends GenericController<PaisModel, Integer, PaisService> {

}
