package com.Proyect_DevOps.sucursales.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Proyect_DevOps.sucursales.models.DireccionModel;

/**
 * Repositorio JPA para la entidad {@link DireccionModel}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Repository
public interface DireccionRepository extends JpaRepository<DireccionModel, Integer>{

}
