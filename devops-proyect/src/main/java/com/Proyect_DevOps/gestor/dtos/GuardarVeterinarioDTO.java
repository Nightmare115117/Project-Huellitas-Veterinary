package com.Proyect_DevOps.gestor.dtos;

/**
 * DTO para la creación o actualización de veterinarios por el gestor.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
public class GuardarVeterinarioDTO {
    private String nombre;
    private String paterno;
    private String materno;
    private String correo;
    private String contrasena;

    public GuardarVeterinarioDTO() {
    }

    public GuardarVeterinarioDTO(String nombre, String paterno, String materno, String correo, String contrasena) {
        this.nombre = nombre;
        this.paterno = paterno;
        this.materno = materno;
        this.correo = correo;
        this.contrasena = contrasena;
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

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
