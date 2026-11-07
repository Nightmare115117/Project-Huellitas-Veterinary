package com.Proyect_DevOps.gestor.dtos;

/**
 * DTO para la consulta de mascotas pertenecientes a la sucursal del gestor.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
public class MascotaGestorDTO {
    private int idMascota;
    private String nombre;
    private String especie;
    private String raza;
    private String fechaNacimiento;
    private String duenoNombre;

    public MascotaGestorDTO() {
    }

    public MascotaGestorDTO(int idMascota, String nombre, String especie, String raza, String fechaNacimiento, String duenoNombre) {
        this.idMascota = idMascota;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.fechaNacimiento = fechaNacimiento;
        this.duenoNombre = duenoNombre;
    }

    public int getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(int idMascota) {
        this.idMascota = idMascota;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDuenoNombre() {
        return duenoNombre;
    }

    public void setDuenoNombre(String duenoNombre) {
        this.duenoNombre = duenoNombre;
    }
}
