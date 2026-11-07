package com.Proyect_DevOps.gestor.dtos;

/**
 * DTO para la consulta de citas de una sucursal desde el punto de vista del gestor.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
public class CitaGestorDTO {
    private int idCita;
    private String fecha;
    private String entradaAgendada;
    private int estadoCita;
    private String mascotaNombre;
    private String duenoNombre;
    private Integer idVeterinario;
    private String veterinarioNombre;

    public CitaGestorDTO() {
    }

    public CitaGestorDTO(int idCita, String fecha, String entradaAgendada, int estadoCita,
                         String mascotaNombre, String duenoNombre, Integer idVeterinario, String veterinarioNombre) {
        this.idCita = idCita;
        this.fecha = fecha;
        this.entradaAgendada = entradaAgendada;
        this.estadoCita = estadoCita;
        this.mascotaNombre = mascotaNombre;
        this.duenoNombre = duenoNombre;
        this.idVeterinario = idVeterinario;
        this.veterinarioNombre = veterinarioNombre;
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

    public String getDuenoNombre() {
        return duenoNombre;
    }

    public void setDuenoNombre(String duenoNombre) {
        this.duenoNombre = duenoNombre;
    }

    public Integer getIdVeterinario() {
        return idVeterinario;
    }

    public void setIdVeterinario(Integer idVeterinario) {
        this.idVeterinario = idVeterinario;
    }

    public String getVeterinarioNombre() {
        return veterinarioNombre;
    }

    public void setVeterinarioNombre(String veterinarioNombre) {
        this.veterinarioNombre = veterinarioNombre;
    }
}
