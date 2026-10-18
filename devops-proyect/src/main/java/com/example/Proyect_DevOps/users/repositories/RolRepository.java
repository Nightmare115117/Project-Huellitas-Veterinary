package com.example.Proyect_DevOps.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Proyect_DevOps.users.models.RolModel;

/**
 * Repositorio JPA para la persistencia y consulta de entidades {@link RolModel}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Repository
public interface RolRepository extends JpaRepository<RolModel, Integer> {
}
