package com.example.Proyect_DevOps.consultaMedica.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Proyect_DevOps.common.controllers.GenericController;
import com.example.Proyect_DevOps.consultaMedica.models.ConsultaMedicaModel;
import com.example.Proyect_DevOps.consultaMedica.services.ConsultaMedicaService;

@RestController
@RequestMapping({"/consulta_Medica", "/consulta-medica", "/consulta"})
public class ConsultaMedicaController extends GenericController<ConsultaMedicaModel, Integer, ConsultaMedicaService> {

}
