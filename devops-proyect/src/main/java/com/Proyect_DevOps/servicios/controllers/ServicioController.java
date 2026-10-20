package com.Proyect_DevOps.servicios.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Proyect_DevOps.common.controllers.GenericController;
import com.Proyect_DevOps.servicios.models.ServicioModel;
import com.Proyect_DevOps.servicios.services.ServicioService;

/**
 * Controlador REST para la gestión de servicios veterinarios.
 * Expone rutas bajo el endpoint {@code /servicio} para listar, crear, actualizar y eliminar servicios ofrecidos.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@RestController
@RequestMapping("/servicio")
public class ServicioController extends GenericController<ServicioModel, Integer, ServicioService> {

}
