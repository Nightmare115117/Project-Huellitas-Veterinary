package com.Proyect_DevOps.gestor.dtos;

/**
 * DTO plano para representar usuarios (veterinarios o dueños) en el módulo de gestor.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
public class UsuarioGestorDTO {
    private int idUsuario;
    private String nombre;
    private String paterno;
    private String materno;
    private String correo;

    public UsuarioGestorDTO() {
    }

    public UsuarioGestorDTO(int idUsuario, String nombre, String paterno, String materno, String correo) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.paterno = paterno;
        this.materno = materno;
        this.correo = correo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPaterno() {
        return paterno;
    }

    public void setPaterno(String paterno) {
        this.paterno = paterno;
    }

    public String getMaterno() {
        return materno;
    }

    public void setMaterno(String materno) {
        this.materno = materno;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
