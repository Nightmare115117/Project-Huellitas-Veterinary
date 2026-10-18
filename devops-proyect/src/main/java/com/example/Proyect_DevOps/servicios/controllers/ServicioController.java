package com.example.Proyect_DevOps.servicios.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.example.Proyect_DevOps.common.controllers.GenericController;
import com.example.Proyect_DevOps.servicios.models.ServicioModel;
import com.example.Proyect_DevOps.servicios.services.ServicioService;

/**
 * Controlador REST para la gestión de servicios veterinarios.
 * Expone rutas bajo el endpoint {@code /servicio} para listar, crear, actualizar y eliminar servicios ofrecidos.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Tag(name = "Servicios", description = "Catálogo de servicios veterinarios")
@RestController
@RequestMapping("/servicio")
public class ServicioController extends GenericController<ServicioModel, Integer, ServicioService> {

}
