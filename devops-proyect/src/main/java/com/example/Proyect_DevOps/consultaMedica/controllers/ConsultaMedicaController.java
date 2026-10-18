package com.example.Proyect_DevOps.consultaMedica.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

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
@Tag(name = "Consultas Médicas", description = "Registro y consulta de consultas médicas")
@RestController
@RequestMapping({"/consulta_Medica", "/consulta-medica", "/consulta"})
public class ConsultaMedicaController extends GenericController<ConsultaMedicaModel, Integer, ConsultaMedicaService> {

}
