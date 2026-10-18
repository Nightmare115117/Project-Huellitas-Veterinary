package com.example.Proyect_DevOps.consultaMedica.dtos;

/**
 * Objeto de transferencia de datos (DTO) para la entidad Tratamiento.
 * Representa de manera desacoplada la prescripción de medicamentos, costos, estado y el nombre de la mascota vinculada.
 *
 * @param idTratamiento Identificador único del tratamiento médico.
 * @param medicamento Nombre o denominación del fármaco prescrito.
 * @param descripcion Instrucciones, dosis o pautas del tratamiento.
 * @param costo Precio o importe monetario del tratamiento.
 * @param estatus Estado del tratamiento (ej. 0=Incompleto/En curso, 1=Completado).
 * @param mascotaNombre Nombre de la mascota a quien se le recetó el tratamiento.
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
public record TratamientoDTO(
    int idTratamiento,
    String medicamento,
    String descripcion,
    double costo,
    int estatus,
    String mascotaNombre) {
}
