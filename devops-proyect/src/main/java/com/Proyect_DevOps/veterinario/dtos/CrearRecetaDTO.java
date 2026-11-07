package com.Proyect_DevOps.veterinario.dtos;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO para la creación de una nueva receta clínica y consulta médica veterinaria.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
public class CrearRecetaDTO {
    private int idCita;
    private String motivo;
    private String diagnostico;
    private double peso;
    private String recomendaciones;
    private List<TratamientoItemDTO> tratamientos = new ArrayList<>();

    public CrearRecetaDTO() {
    }

    public CrearRecetaDTO(int idCita, String motivo, String diagnostico, double peso,
                          String recomendaciones, List<TratamientoItemDTO> tratamientos) {
        this.idCita = idCita;
        this.motivo = motivo;
        this.diagnostico = diagnostico;
        this.peso = peso;
        this.recomendaciones = recomendaciones;
        this.tratamientos = tratamientos != null ? tratamientos : new ArrayList<>();
    }

    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getRecomendaciones() {
        return recomendaciones;
    }

    public void setRecomendaciones(String recomendaciones) {
        this.recomendaciones = recomendaciones;
    }

    public List<TratamientoItemDTO> getTratamientos() {
        return tratamientos;
    }

    public void setTratamientos(List<TratamientoItemDTO> tratamientos) {
        this.tratamientos = tratamientos != null ? tratamientos : new ArrayList<>();
    }
}
