package com.Proyect_DevOps.geolocalizacion.services;

import org.springframework.stereotype.Service;

import com.Proyect_DevOps.common.services.GenericServiceImpl;
import com.Proyect_DevOps.geolocalizacion.models.ColoniaModel;
import com.Proyect_DevOps.geolocalizacion.repositories.ColoniaRepository;

/**
 * Servicio de negocio para la administración del catálogo de colonias.
 * Extiende las operaciones CRUD genéricas provistas por {@link GenericServiceImpl}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Service
public class ColoniaService extends GenericServiceImpl<ColoniaModel, Integer, ColoniaRepository> {

}
