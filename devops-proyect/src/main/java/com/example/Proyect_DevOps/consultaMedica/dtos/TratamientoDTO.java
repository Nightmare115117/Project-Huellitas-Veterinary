package com.example.Proyect_DevOps.consultaMedica.dtos;

import lombok.Data;

@Data
public class TratamientoDTO {

    private int idTratamiento;
    private String medicamento;
    private String descripcion;
    private double costo;
    private int estatus;
    private String mascotaNombre;

}
