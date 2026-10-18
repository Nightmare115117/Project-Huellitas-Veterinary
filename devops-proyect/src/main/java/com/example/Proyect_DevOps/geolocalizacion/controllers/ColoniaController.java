package com.example.Proyect_DevOps.geolocalizacion.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.example.Proyect_DevOps.common.controllers.GenericController;
import com.example.Proyect_DevOps.geolocalizacion.models.ColoniaModel;
import com.example.Proyect_DevOps.geolocalizacion.services.ColoniaService;

/**
 * Controlador REST para el catálogo geográfico de colonias.
 * Expone rutas CRUD bajo el endpoint {@code /colonia}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Tag(name = "Colonias", description = "Catálogo de colonias")
@RestController
@RequestMapping("/colonia")
public class ColoniaController extends GenericController<ColoniaModel, Integer, ColoniaService> {

}
