package com.example.Proyect_DevOps.citas.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

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
