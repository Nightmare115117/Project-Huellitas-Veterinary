package com.Proyect_DevOps.geolocalizacion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Proyect_DevOps.geolocalizacion.models.PaisModel;

/**
 * Repositorio JPA para la entidad {@link PaisModel}.
 * Permite el acceso a datos y operaciones de consulta para Pais.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Repository
public interface PaisRepository extends JpaRepository<PaisModel, Integer>{

}
