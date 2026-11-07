package com.Proyect_DevOps.gestor.dtos;

import java.util.List;

/**
 * Reporte integral para el gestor de sucursal con métricas de citas, ingresos y servicios destacados.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
public class ReporteGestorDTO {
    private int totalCitas;
    private List<CitasPorEstadoDTO> citasPorEstado;
    private List<CitasPorVeterinarioDTO> citasPorVeterinario;
    private double ingresosServicios;
    private double ingresosTratamientos;
    private List<ServicioTopDTO> serviciosTop;

    public ReporteGestorDTO() {
    }

    public ReporteGestorDTO(int totalCitas, List<CitasPorEstadoDTO> citasPorEstado,
                            List<CitasPorVeterinarioDTO> citasPorVeterinario, double ingresosServicios,
                            double ingresosTratamientos, List<ServicioTopDTO> serviciosTop) {
        this.totalCitas = totalCitas;
        this.citasPorEstado = citasPorEstado;
        this.citasPorVeterinario = citasPorVeterinario;
        this.ingresosServicios = ingresosServicios;
        this.ingresosTratamientos = ingresosTratamientos;
        this.serviciosTop = serviciosTop;
    }

    public int getTotalCitas() {
        return totalCitas;
    }

    public void setTotalCitas(int totalCitas) {
        this.totalCitas = totalCitas;
    }

    public List<CitasPorEstadoDTO> getCitasPorEstado() {
        return citasPorEstado;
    }

    public void setCitasPorEstado(List<CitasPorEstadoDTO> citasPorEstado) {
        this.citasPorEstado = citasPorEstado;
    }

    public List<CitasPorVeterinarioDTO> getCitasPorVeterinario() {
        return citasPorVeterinario;
    }

    public void setCitasPorVeterinario(List<CitasPorVeterinarioDTO> citasPorVeterinario) {
        this.citasPorVeterinario = citasPorVeterinario;
    }

    public double getIngresosServicios() {
        return ingresosServicios;
    }

    public void setIngresosServicios(double ingresosServicios) {
        this.ingresosServicios = ingresosServicios;
    }

    public double getIngresosTratamientos() {
        return ingresosTratamientos;
    }

    public void setIngresosTratamientos(double ingresosTratamientos) {
        this.ingresosTratamientos = ingresosTratamientos;
    }

    public List<ServicioTopDTO> getServiciosTop() {
        return serviciosTop;
    }

    public void setServiciosTop(List<ServicioTopDTO> serviciosTop) {
        this.serviciosTop = serviciosTop;
    }
}
