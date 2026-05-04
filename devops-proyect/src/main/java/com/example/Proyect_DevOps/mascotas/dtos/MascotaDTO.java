package com.example.Proyect_DevOps.mascotas.dtos;

import java.time.LocalDate;

import com.example.Proyect_DevOps.mascotas.models.RazaModel;

import lombok.Data;

@Data
public class MascotaDTO {

    private int idMascota;
    private String nombre;
    private LocalDate fechaNacimiento;
    private double peso;
    private int status;
    private RazaModel raza;

}
