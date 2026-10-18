package com.example.Proyect_DevOps.geolocalizacion.services;

import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.common.services.GenericServiceImpl;
import com.example.Proyect_DevOps.geolocalizacion.models.EstadoModel;
import com.example.Proyect_DevOps.geolocalizacion.repositories.EstadoRepository;

/**
 * Servicio de negocio para la administración del catálogo de estados.
 * Extiende las operaciones CRUD genéricas provistas por {@link GenericServiceImpl}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Service
public class EstadoService extends GenericServiceImpl<EstadoModel, Integer, EstadoRepository> {

}
