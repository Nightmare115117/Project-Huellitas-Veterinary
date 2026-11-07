package com.Proyect_DevOps.gestor.dtos;

/**
 * Conteo de citas agrupadas por código de estado.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
public class CitasPorEstadoDTO {
    private int estado;
    private long total;

    public CitasPorEstadoDTO() {
    }

    public CitasPorEstadoDTO(int estado, long total) {
        this.estado = estado;
        this.total = total;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
