package com.example.Proyect_DevOps.geolocalizacion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Proyect_DevOps.geolocalizacion.models.EstadoModel;

/**
 * Repositorio JPA para la entidad {@link EstadoModel}.
 * Permite el acceso a datos y operaciones de consulta para Estado.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Repository
public interface EstadoRepository extends JpaRepository<EstadoModel, Integer>{

}
