package com.example.Proyect_DevOps.mascotas.dtos;

import java.time.LocalDate;

import com.example.Proyect_DevOps.mascotas.models.RazaModel;

public record MascotaDTO(
    int idMascota,
    String nombre,
    LocalDate fechaNacimiento,
    double peso,
    int status,
    RazaModel raza) {
}
