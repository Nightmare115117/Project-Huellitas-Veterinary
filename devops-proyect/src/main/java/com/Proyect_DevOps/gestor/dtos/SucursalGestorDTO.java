package com.Proyect_DevOps.gestor.dtos;

/**
 * DTO para la información de la sucursal asignada a un gestor.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
public class SucursalGestorDTO {
    private int idSucursal;
    private String calle;
    private String colonia;
    private String ciudad;
    private String estado;
    private String pais;
    private int codigoPostal;
    private String rfcGestor;

    public SucursalGestorDTO() {
    }

    public SucursalGestorDTO(int idSucursal, String calle, String colonia, String ciudad,
                             String estado, String pais, int codigoPostal, String rfcGestor) {
        this.idSucursal = idSucursal;
        this.calle = calle;
        this.colonia = colonia;
        this.ciudad = ciudad;
        this.estado = estado;
        this.pais = pais;
        this.codigoPostal = codigoPostal;
        this.rfcGestor = rfcGestor;
    }

    public int getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public int getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(int codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getRfcGestor() {
        return rfcGestor;
    }

    public void setRfcGestor(String rfcGestor) {
        this.rfcGestor = rfcGestor;
    }
}
