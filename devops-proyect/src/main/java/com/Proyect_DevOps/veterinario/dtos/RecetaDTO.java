package com.Proyect_DevOps.veterinario.dtos;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO para la consulta de recetas médicas veterinarias asociadas a consultas clínicas.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
public class RecetaDTO {
    private int idConsulta;
    private int idCita;
    private String fecha;
    private String mascotaNombre;
    private String duenoNombre;
    private String motivo;
    private String diagnostico;
    private double peso;
    private String recomendaciones;
    private List<TratamientoItemDTO> tratamientos = new ArrayList<>();

    public RecetaDTO() {
    }

    public RecetaDTO(int idConsulta, int idCita, String fecha, String mascotaNombre, String duenoNombre,
                     String motivo, String diagnostico, double peso, String recomendaciones,
                     List<TratamientoItemDTO> tratamientos) {
        this.idConsulta = idConsulta;
        this.idCita = idCita;
        this.fecha = fecha;
        this.mascotaNombre = mascotaNombre;
        this.duenoNombre = duenoNombre;
        this.motivo = motivo;
        this.diagnostico = diagnostico;
        this.peso = peso;
        this.recomendaciones = recomendaciones;
        this.tratamientos = tratamientos != null ? tratamientos : new ArrayList<>();
    }

    public int getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(int idConsulta) {
        this.idConsulta = idConsulta;
    }

    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMascotaNombre() {
        return mascotaNombre;
    }

    public void setMascotaNombre(String mascotaNombre) {
        this.mascotaNombre = mascotaNombre;
    }

    public String getDuenoNombre() {
        return duenoNombre;
    }

    public void setDuenoNombre(String duenoNombre) {
        this.duenoNombre = duenoNombre;
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
