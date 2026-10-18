package com.example.Proyect_DevOps.geolocalizacion.services;

import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.common.services.GenericServiceImpl;
import com.example.Proyect_DevOps.geolocalizacion.models.CiudadModel;
import com.example.Proyect_DevOps.geolocalizacion.repositories.CiudadRepository;

/**
 * Servicio de negocio para la administración del catálogo de ciudades.
 * Extiende las operaciones CRUD genéricas provistas por {@link GenericServiceImpl}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Service
public class CiudadService extends GenericServiceImpl<CiudadModel, Integer, CiudadRepository> {

}
