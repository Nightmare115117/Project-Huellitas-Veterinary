package com.example.Proyect_DevOps.bitacora.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Proyect_DevOps.bitacora.models.BitacoraModel;

/**
 * Repositorio JPA para la entidad {@link BitacoraModel}.
 * Proporciona métodos de persistencia y consultas ordenadas para auditoría histórica.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Repository
public interface BitacoraRepository extends JpaRepository<BitacoraModel, Integer> {

    /**
     * Obtiene todos los eventos de auditoría ordenados cronológicamente de forma descendente (los más recientes primero).
     *
     * @return Lista de registros de bitácora ordenados por fecha y hora descendente.
     */
    List<BitacoraModel> findAllByOrderByFechaHoraDesc();

    /**
     * Obtiene todos los eventos de auditoría ordenados por su identificador único de forma descendente.
     *
     * @return Lista de registros de bitácora ordenados por ID descendente.
     */
    List<BitacoraModel> findAllByOrderByIdBitacoraDesc();
}
