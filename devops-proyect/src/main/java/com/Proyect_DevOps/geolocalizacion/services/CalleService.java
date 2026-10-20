package com.Proyect_DevOps.geolocalizacion.services;

import org.springframework.stereotype.Service;

import com.Proyect_DevOps.common.services.GenericServiceImpl;
import com.Proyect_DevOps.geolocalizacion.models.CalleModel;
import com.Proyect_DevOps.geolocalizacion.repositories.CalleRepository;

/**
 * Servicio de negocio para la administración del catálogo de calles.
 * Extiende las operaciones CRUD genéricas provistas por {@link GenericServiceImpl}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Service
public class CalleService extends GenericServiceImpl<CalleModel, Integer, CalleRepository> {

}
