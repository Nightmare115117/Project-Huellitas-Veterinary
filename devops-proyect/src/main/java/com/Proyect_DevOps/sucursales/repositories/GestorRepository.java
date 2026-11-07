package com.Proyect_DevOps.sucursales.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Proyect_DevOps.sucursales.models.GestorModel;
import com.Proyect_DevOps.users.models.UsuarioModel;

/**
 * Repositorio JPA para la entidad {@link GestorModel}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Repository
public interface GestorRepository extends JpaRepository<GestorModel, Integer>{

    /**
     * Busca el registro del gestor por la entidad de usuario asociada.
     *
     * @param usuario Usuario del sistema vinculado.
     * @return {@link Optional} con el gestor correspondiente.
     */
    Optional<GestorModel> findByUsuario(UsuarioModel usuario);

    /**
     * Busca el registro del gestor por el ID del usuario vinculado.
     *
     * @param idUsuario Identificador de usuario.
     * @return {@link Optional} con el gestor correspondiente.
     */
    Optional<GestorModel> findByUsuario_IdUsuario(int idUsuario);
}
