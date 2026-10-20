package com.Proyect_DevOps.sucursales.services;

import org.springframework.stereotype.Service;

import com.Proyect_DevOps.common.services.GenericServiceImpl;
import com.Proyect_DevOps.sucursales.models.SucursalModel;
import com.Proyect_DevOps.sucursales.repositories.SucursalRepository;

/**
 * Servicio de negocio para la administración integral de sucursales de la clínica veterinaria.
 * Extiende las operaciones genéricas de {@link GenericServiceImpl}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Service
public class SucursalService extends GenericServiceImpl<SucursalModel, Integer, SucursalRepository> {

}
