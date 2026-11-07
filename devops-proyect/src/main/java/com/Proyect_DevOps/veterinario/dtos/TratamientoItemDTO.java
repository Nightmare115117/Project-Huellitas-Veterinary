package com.Proyect_DevOps.veterinario.dtos;

/**
 * DTO para un ítem individual de tratamiento farmacológico prescrito en una receta.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
public class TratamientoItemDTO {
    private String medicamento;
    private String descripcion;
    private double costo;

    public TratamientoItemDTO() {
    }

    public TratamientoItemDTO(String medicamento, String descripcion, double costo) {
        this.medicamento = medicamento;
        this.descripcion = descripcion;
        this.costo = costo;
    }

    public String getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }
}
