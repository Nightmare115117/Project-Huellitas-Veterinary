package com.example.Proyect_DevOps.consultaMedica.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Proyect_DevOps.consultaMedica.models.TratamientoModel;

/**
 * Repositorio JPA para la entidad {@link TratamientoModel}.
 * Brinda métodos de consulta por relaciones anidadas con el usuario cliente y la mascota.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Repository
public interface TratamientoRepository extends JpaRepository<TratamientoModel, Integer>{

    /**
     * Obtiene la lista de tratamientos recetados a las mascotas de un usuario identificado por su correo.
     *
     * @param correo Correo electrónico del usuario cliente.
     * @return Lista de tratamientos prescritos para ese usuario.
     */
    List<TratamientoModel> findByconsultas_cita_usuarioMascota_correo(String correo);

    /**
     * Obtiene los tratamientos recetados a una mascota específica de un usuario.
     *
     * @param correo Correo electrónico del cliente.
     * @param idMascota Identificador numérico de la mascota.
     * @return Lista de tratamientos de la mascota.
     */
    List<TratamientoModel> findByconsultas_cita_usuarioMascota_correoAndConsultas_cita_mascotaModel_idMascota(String correo, int idMascota);

    /**
     * Cuenta la cantidad de tratamientos con un estatus determinado pertenecientes a un usuario.
     *
     * @param correo Correo del usuario cliente.
     * @param estatus Código del estatus (0 para incompletos / pendientes).
     * @return Cantidad de tratamientos encontrados.
     */
    long countByconsultas_cita_usuarioMascota_correoAndEstatus(String correo, int estatus);
}
