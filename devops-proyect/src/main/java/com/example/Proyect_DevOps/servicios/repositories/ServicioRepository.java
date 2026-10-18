package com.example.Proyect_DevOps.servicios.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Proyect_DevOps.servicios.models.ServicioModel;

/**
 * Repositorio JPA para la entidad {@link ServicioModel}.
 * Permite el acceso a datos y operaciones CRUD sobre el catálogo de servicios de la clínica.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Repository
public interface ServicioRepository extends JpaRepository<ServicioModel, Integer>{

}
