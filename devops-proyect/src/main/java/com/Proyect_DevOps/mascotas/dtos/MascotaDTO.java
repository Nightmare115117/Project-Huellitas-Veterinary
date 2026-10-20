package com.Proyect_DevOps.mascotas.dtos;

import java.time.LocalDate;

import com.Proyect_DevOps.mascotas.models.RazaModel;

/**
 * Objeto de transferencia de datos (DTO) para la mascota.
 * Desacopla la estructura interna de la entidad MascotaModel para la exposición en respuestas API.
 *
 * @param idMascota Identificador único de la mascota.
 * @param nombre Nombre de la mascota.
 * @param fechaNacimiento Fecha de nacimiento de la mascota.
 * @param status Estado de la mascota (ej. 1=Activo, 0=Inactivo).
 * @param raza Raza asociada a la mascota.
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
public record MascotaDTO(
    int idMascota,
    String nombre,
    LocalDate fechaNacimiento,
    int status,
    RazaModel raza) {
}
