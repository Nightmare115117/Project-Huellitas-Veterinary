package com.example.Proyect_DevOps.consultaMedica.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Proyect_DevOps.common.controllers.GenericController;
import com.example.Proyect_DevOps.consultaMedica.models.ConsultaMedicaModel;
import com.example.Proyect_DevOps.consultaMedica.services.ConsultaMedicaService;

/**
 * Controlador REST para la gestión de consultas médicas veterinarias.
 * Expone rutas bajo los prefijos {@code /consulta_Medica}, {@code /consulta-medica} y {@code /consulta}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@RestController
@RequestMapping({"/consulta_Medica", "/consulta-medica", "/consulta"})
public class ConsultaMedicaController extends GenericController<ConsultaMedicaModel, Integer, ConsultaMedicaService> {

}
