package com.example.Proyect_DevOps.geolocalizacion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Proyect_DevOps.geolocalizacion.models.ColoniaModel;

/**
 * Repositorio JPA para la entidad {@link ColoniaModel}.
 * Permite el acceso a datos y operaciones de consulta para Colonia.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Repository
public interface ColoniaRepository extends JpaRepository<ColoniaModel, Integer>{

}
