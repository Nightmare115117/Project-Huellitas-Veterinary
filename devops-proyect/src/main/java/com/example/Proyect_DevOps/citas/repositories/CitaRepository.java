package com.example.Proyect_DevOps.citas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Proyect_DevOps.citas.models.CitaModel;
import com.example.Proyect_DevOps.mascotas.models.MascotaModel;
import com.example.Proyect_DevOps.users.models.UsuarioModel;

/**
 * Repositorio JPA para la persistencia y consulta especializada de entidades {@link CitaModel}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Repository
public interface CitaRepository extends JpaRepository<CitaModel, Integer>{

    /**
     * Busca las citas médicas asociadas a un usuario dueño y una mascota específica.
     *
     * @param usuarioMascota Usuario propietario de la mascota.
     * @param mascotaModel Mascota atendida.
     * @return Lista de citas correspondientes a dicho usuario y mascota.
     */
    List<CitaModel> findByUsuarioMascotaAndMascotaModel(UsuarioModel usuarioMascota, MascotaModel mascotaModel);

    /**
     * Cuenta el número de citas que tiene un usuario en un estado específico.
     *
     * @param usuario Usuario propietario a consultar.
     * @param estadoCita Código del estado de la cita (ej. 1 para activas).
     * @return Cantidad de citas encontradas.
     */
    long countByUsuarioMascotaAndEstadoCita(UsuarioModel usuario, int estadoCita);

    /**
     * Obtiene el listado de todas las citas agendadas por un usuario.
     *
     * @param usuario Usuario propietario consultado.
     * @return Lista de citas pertenecientes al usuario.
     */
    List<CitaModel> findByUsuarioMascota(UsuarioModel usuario);
}
