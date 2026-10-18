package com.example.Proyect_DevOps.geolocalizacion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Proyect_DevOps.geolocalizacion.models.CiudadModel;

/**
 * Repositorio JPA para la entidad {@link CiudadModel}.
 * Permite el acceso a datos y operaciones de consulta para Ciudad.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Repository
public interface CiudadRepository extends JpaRepository<CiudadModel, Integer>{

}
