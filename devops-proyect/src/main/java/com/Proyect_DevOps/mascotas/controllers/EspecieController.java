package com.Proyect_DevOps.mascotas.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Proyect_DevOps.common.controllers.GenericController;
import com.Proyect_DevOps.mascotas.models.EspecieModel;
import com.Proyect_DevOps.mascotas.services.EspecieService;

/**
 * Controlador REST para el catálogo de especies de animales.
 * Expone rutas bajo el endpoint {@code /especie}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@RestController
@RequestMapping("/especie")
public class EspecieController extends GenericController<EspecieModel, Integer, EspecieService> {

}
