package com.example.Proyect_DevOps.mascotas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Proyect_DevOps.mascotas.models.EspecieModel;

/**
 * Repositorio JPA para la entidad {@link EspecieModel}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Repository
public interface EspecieRepository extends JpaRepository<EspecieModel, Integer>{

}
