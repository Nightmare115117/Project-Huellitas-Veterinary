package com.Proyect_DevOps.veterinario.dtos;

/**
 * DTO para la consulta de citas desde la vista del médico veterinario.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
public class CitaVeterinarioDTO {
    private int idCita;
    private String fecha;
    private String entradaAgendada;
    private int estadoCita;
    private String mascotaNombre;
    private String especie;
    private String raza;
    private String duenoNombre;
    private boolean tieneReceta;

    public CitaVeterinarioDTO() {
    }

    public CitaVeterinarioDTO(int idCita, String fecha, String entradaAgendada, int estadoCita,
                              String mascotaNombre, String especie, String raza, String duenoNombre, boolean tieneReceta) {
        this.idCita = idCita;
        this.fecha = fecha;
        this.entradaAgendada = entradaAgendada;
        this.estadoCita = estadoCita;
        this.mascotaNombre = mascotaNombre;
        this.especie = especie;
        this.raza = raza;
        this.duenoNombre = duenoNombre;
        this.tieneReceta = tieneReceta;
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

    public String getEntradaAgendada() {
        return entradaAgendada;
    }

    public void setEntradaAgendada(String entradaAgendada) {
        this.entradaAgendada = entradaAgendada;
    }

    public int getEstadoCita() {
        return estadoCita;
    }

    public void setEstadoCita(int estadoCita) {
        this.estadoCita = estadoCita;
    }

    public String getMascotaNombre() {
        return mascotaNombre;
    }

    public void setMascotaNombre(String mascotaNombre) {
        this.mascotaNombre = mascotaNombre;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getDuenoNombre() {
        return duenoNombre;
    }

    public void setDuenoNombre(String duenoNombre) {
        this.duenoNombre = duenoNombre;
    }

    public boolean isTieneReceta() {
        return tieneReceta;
    }

    public void setTieneReceta(boolean tieneReceta) {
        this.tieneReceta = tieneReceta;
    }
}
