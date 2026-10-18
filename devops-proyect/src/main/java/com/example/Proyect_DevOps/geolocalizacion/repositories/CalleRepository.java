package com.example.Proyect_DevOps.geolocalizacion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Proyect_DevOps.geolocalizacion.models.CalleModel;

/**
 * Repositorio JPA para la entidad {@link CalleModel}.
 * Permite el acceso a datos y operaciones de consulta para Calle.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Repository
public interface CalleRepository extends JpaRepository<CalleModel, Integer>{

}
