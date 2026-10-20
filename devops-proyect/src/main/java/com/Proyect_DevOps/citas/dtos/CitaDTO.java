package com.Proyect_DevOps.citas.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Objeto de transferencia de datos (DTO) para la entidad Cita médica.
 * Facilita el intercambio de información desacoplada entre clientes web y el servicio de citas.
 *
 * @param idCita Identificador único de la cita médica.
 * @param nombreVeterinario Nombre completo del veterinario asignado a la cita.
 * @param correoCliente Correo electrónico del cliente o dueño de la mascota.
 * @param idMascota Identificador numérico de la mascota a atender.
 * @param fecha Fecha programada para la cita médica.
 * @param entradaAgendada Hora de inicio agendada para la cita.
 * @param horaSalida Hora de conclusión o salida de la cita.
 * @param estadoCita Código numérico del estado de la cita (ej. 1=Agendada, 2=En atención, 3=Cancelada, 4=Completada).
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
public record CitaDTO(
    int idCita,
    String nombreVeterinario,
    String correoCliente,
    int idMascota,
    LocalDate fecha,
    LocalTime entradaAgendada,
    LocalTime horaSalida,
    int estadoCita) {
}
