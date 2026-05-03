package com.example.Proyect_DevOps.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class CitaDTO {
    
    private int idCita;
    private String nombreVeterinario;
    private String correoCliente;
    private int idMascota;
    private LocalDate fecha;
    private LocalTime entradaAgendada;
    private LocalTime horaSalida;
    private int estadoCita;
}
