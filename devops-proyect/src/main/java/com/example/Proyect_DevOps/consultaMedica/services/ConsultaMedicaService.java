package com.example.Proyect_DevOps.consultaMedica.services;

import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.common.services.GenericServiceImpl;
import com.example.Proyect_DevOps.consultaMedica.models.ConsultaMedicaModel;
import com.example.Proyect_DevOps.consultaMedica.repositories.ConsultaMedicaRepository;

/**
 * Servicio de negocio para la gestión de consultas médicas veterinarias.
 * Hereda las capacidades de persistencia y consulta genéricas provistas por {@link GenericServiceImpl}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Service
public class ConsultaMedicaService extends GenericServiceImpl<ConsultaMedicaModel, Integer, ConsultaMedicaRepository> {

}
