package com.example.Proyect_DevOps.sucursales.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Proyect_DevOps.sucursales.models.SucursalModel;

/**
 * Repositorio JPA para la entidad {@link SucursalModel}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Repository
public interface SucursalRepository extends JpaRepository<SucursalModel, Integer>{

}
