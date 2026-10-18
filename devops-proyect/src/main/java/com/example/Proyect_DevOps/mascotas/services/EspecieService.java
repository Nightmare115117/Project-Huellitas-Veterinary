package com.example.Proyect_DevOps.mascotas.services;

import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.common.services.GenericServiceImpl;
import com.example.Proyect_DevOps.mascotas.models.EspecieModel;
import com.example.Proyect_DevOps.mascotas.repositories.EspecieRepository;

/**
 * Servicio de negocio para la administración de especies animales.
 * Extiende las operaciones genéricas de {@link GenericServiceImpl}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Service
public class EspecieService extends GenericServiceImpl<EspecieModel, Integer, EspecieRepository> {

}
