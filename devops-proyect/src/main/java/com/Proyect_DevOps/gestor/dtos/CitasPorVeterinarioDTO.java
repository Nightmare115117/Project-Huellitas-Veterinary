package com.Proyect_DevOps.gestor.dtos;

/**
 * Conteo de citas asignadas a un veterinario o sin asignar.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
public class CitasPorVeterinarioDTO {
    private String veterinario;
    private long total;

    public CitasPorVeterinarioDTO() {
    }

    public CitasPorVeterinarioDTO(String veterinario, long total) {
        this.veterinario = veterinario;
        this.total = total;
    }

    public String getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(String veterinario) {
        this.veterinario = veterinario;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
