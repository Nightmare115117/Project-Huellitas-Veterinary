package com.Proyect_DevOps.sucursales.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Proyect_DevOps.sucursales.models.GestorModel;

/**
 * Repositorio JPA para la entidad {@link GestorModel}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Repository
public interface GestorRepository extends JpaRepository<GestorModel, Integer>{

}
