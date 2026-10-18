package com.example.Proyect_DevOps.sucursales.services;

import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.common.services.GenericServiceImpl;
import com.example.Proyect_DevOps.sucursales.models.GestorModel;
import com.example.Proyect_DevOps.sucursales.repositories.GestorRepository;

/**
 * Servicio de negocio para la administración de gestores de sucursal.
 * Extiende las operaciones genéricas de {@link GenericServiceImpl}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Service
public class GestorService extends GenericServiceImpl<GestorModel, Integer, GestorRepository> {

}
