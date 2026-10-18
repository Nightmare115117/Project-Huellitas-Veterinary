package com.example.Proyect_DevOps.consultaMedica.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Proyect_DevOps.consultaMedica.models.ConsultaMedicaModel;

/**
 * Repositorio JPA para la entidad {@link ConsultaMedicaModel}.
 * Permite realizar operaciones CRUD y consultas sobre historias clínicas y consultas veterinarias.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Repository
public interface ConsultaMedicaRepository extends JpaRepository<ConsultaMedicaModel, Integer>{

}
