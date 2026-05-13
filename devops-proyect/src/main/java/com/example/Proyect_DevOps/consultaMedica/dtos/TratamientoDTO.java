package com.example.Proyect_DevOps.consultaMedica.dtos;

public record TratamientoDTO(
    int idTratamiento,
    String medicamento,
    String descripcion,
    double costo,
    int estatus,
    String mascotaNombre) {
}
