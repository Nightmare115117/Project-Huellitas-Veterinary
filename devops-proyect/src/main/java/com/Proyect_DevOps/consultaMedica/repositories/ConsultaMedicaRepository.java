package com.Proyect_DevOps.consultaMedica.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Proyect_DevOps.citas.models.CitaModel;
import com.Proyect_DevOps.consultaMedica.models.ConsultaMedicaModel;

/**
 * Repositorio JPA para la entidad {@link ConsultaMedicaModel}.
 * Permite realizar operaciones CRUD y consultas sobre historias clínicas y consultas veterinarias.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Repository
public interface ConsultaMedicaRepository extends JpaRepository<ConsultaMedicaModel, Integer>{

    /**
     * Busca la consulta médica vinculada a una cita.
     *
     * @param cita Cita médica asociada.
     * @return {@link Optional} con la consulta médica.
     */
    Optional<ConsultaMedicaModel> findByCita(CitaModel cita);

    /**
     * Busca la consulta médica por el identificador de la cita.
     *
     * @param idCita Identificador de la cita.
     * @return {@link Optional} con la consulta médica.
     */
    Optional<ConsultaMedicaModel> findByCita_IdCita(int idCita);

    /**
     * Comprueba si una cita ya cuenta con una consulta médica registrada.
     *
     * @param idCita Identificador de la cita.
     * @return {@code true} si ya tiene consulta, {@code false} de lo contrario.
     */
    boolean existsByCita_IdCita(int idCita);

    /**
     * Comprueba si un veterinario tiene alguna consulta médica registrada a través de sus citas.
     *
     * @param idUsuario Identificador del veterinario.
     * @return {@code true} si tiene consultas, {@code false} de lo contrario.
     */
    boolean existsByCita_UsuarioVeterinario_IdUsuario(int idUsuario);

    /**
     * Obtiene las recetas o consultas médicas asignadas a un veterinario, ordenadas por fecha reciente.
     *
     * @param idUsuario Identificador del veterinario.
     * @return Lista de consultas ordenadas por fecha de cita desc y idConsulta desc.
     */
    List<ConsultaMedicaModel> findByCita_UsuarioVeterinario_IdUsuarioOrderByCita_FechaDescIdConsultaDesc(int idUsuario);
}
