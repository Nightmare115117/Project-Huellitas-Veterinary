package com.example.Proyect_DevOps.mascotas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Proyect_DevOps.mascotas.models.MascotaModel;
import com.example.Proyect_DevOps.users.models.UsuarioModel;

/**
 * Repositorio JPA para la entidad {@link MascotaModel}.
 * Facilita la consulta y conteo de mascotas pertenecientes a un usuario dueño.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Repository
public interface MascotaRepository extends JpaRepository<MascotaModel, Integer>{

    /**
     * Obtiene el listado de mascotas registradas por un usuario específico.
     *
     * @param usuario Usuario propietario de las mascotas.
     * @return Lista de mascotas del usuario.
     */
    List<MascotaModel> findByUsuario(UsuarioModel usuario);

    /**
     * Cuenta cuántas mascotas tiene asociadas un usuario.
     *
     * @param usuario Usuario propietario a consultar.
     * @return Total de mascotas registradas.
     */
    long countByUsuario(UsuarioModel usuario);

}
