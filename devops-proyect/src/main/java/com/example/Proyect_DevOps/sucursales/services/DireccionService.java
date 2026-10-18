package com.example.Proyect_DevOps.sucursales.services;

import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.common.services.GenericServiceImpl;
import com.example.Proyect_DevOps.sucursales.models.DireccionModel;
import com.example.Proyect_DevOps.sucursales.repositories.DireccionRepository;

/**
 * Servicio de negocio para la gestión de direcciones físicas de sucursales.
 * Extiende las operaciones genéricas de {@link GenericServiceImpl}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Service
public class DireccionService extends GenericServiceImpl<DireccionModel, Integer, DireccionRepository> {

}
