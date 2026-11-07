package com.Proyect_DevOps.gestor.dtos;

/**
 * Representa uno de los servicios veterinarios más solicitados con su volumen e ingreso monetario.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
public class ServicioTopDTO {
    private String nombre;
    private long total;
    private double ingreso;

    public ServicioTopDTO() {
    }

    public ServicioTopDTO(String nombre, long total, double ingreso) {
        this.nombre = nombre;
        this.total = total;
        this.ingreso = ingreso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public double getIngreso() {
        return ingreso;
    }

    public void setIngreso(double ingreso) {
        this.ingreso = ingreso;
    }
}
