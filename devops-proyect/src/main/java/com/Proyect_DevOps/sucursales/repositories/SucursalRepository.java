package com.Proyect_DevOps.sucursales.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Proyect_DevOps.sucursales.models.GestorModel;
import com.Proyect_DevOps.sucursales.models.SucursalModel;

/**
 * Repositorio JPA para la entidad {@link SucursalModel}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Repository
public interface SucursalRepository extends JpaRepository<SucursalModel, Integer>{

    /**
     * Busca la sucursal administrada por el gestor especificado.
     *
     * @param gestor Registro del gestor.
     * @return {@link Optional} con la sucursal asignada.
     */
    Optional<SucursalModel> findByGestor(GestorModel gestor);

    /**
     * Busca la sucursal por el identificador del gestor.
     *
     * @param idGestor Identificador del gestor.
     * @return {@link Optional} con la sucursal asignada.
     */
    Optional<SucursalModel> findByGestor_IdGestor(int idGestor);

    /**
     * Busca la sucursal a través del identificador de usuario del gestor.
     *
     * @param idUsuario Identificador de usuario del gestor.
     * @return {@link Optional} con la sucursal asignada.
     */
    Optional<SucursalModel> findByGestor_Usuario_IdUsuario(int idUsuario);
}
