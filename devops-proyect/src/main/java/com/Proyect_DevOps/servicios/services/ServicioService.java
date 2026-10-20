package com.Proyect_DevOps.servicios.services;

import org.springframework.stereotype.Service;

import com.Proyect_DevOps.common.services.GenericServiceImpl;
import com.Proyect_DevOps.servicios.models.ServicioModel;
import com.Proyect_DevOps.servicios.repositories.ServicioRepository;

/**
 * Servicio de negocio para la administración del catálogo de servicios veterinarios.
 * Extiende las operaciones genéricas provistas por {@link GenericServiceImpl}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Service
public class ServicioService extends GenericServiceImpl<ServicioModel, Integer, ServicioRepository> {

}
