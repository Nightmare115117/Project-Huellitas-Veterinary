package com.example.Proyect_DevOps.geolocalizacion.services;

import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.common.services.GenericServiceImpl;
import com.example.Proyect_DevOps.geolocalizacion.models.PaisModel;
import com.example.Proyect_DevOps.geolocalizacion.repositories.PaisRepository;

/**
 * Servicio de negocio para la administración del catálogo de países.
 * Extiende las operaciones CRUD genéricas provistas por {@link GenericServiceImpl}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Service
public class PaisService extends GenericServiceImpl<PaisModel, Integer, PaisRepository> {

}
