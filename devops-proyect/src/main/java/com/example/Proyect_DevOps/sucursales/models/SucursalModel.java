package com.example.Proyect_DevOps.sucursales.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa una sucursal o clínica veterinaria de la organización.
 * Vincula una dirección física única y un gestor administrativo responsable.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Entity
@Table(name = "sucursales")
public class SucursalModel {

    /**
     * Identificador único autoincremental de la sucursal.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSucursal;

    /**
     * Dirección física donde opera la sucursal.
     */
    @OneToOne
    @JoinColumn(name = "idDireccion")
    private DireccionModel direccion;

    /**
     * Gestor o encargado administrativo de la sucursal.
     */
    @OneToOne
    @JoinColumn(name = "idGestor")
    private GestorModel gestor;

    /**
     * Constructor con dirección y gestor.
     *
     * @param direccion Dirección física asignada.
     * @param gestor Gestor administrativo responsable.
     */
    public SucursalModel(DireccionModel direccion, GestorModel gestor) {
        this.direccion = direccion;
        this.gestor = gestor;
    }

    /**
     * Constructor por defecto requerido por JPA.
     */
    public SucursalModel() {
    }

    /**
     * Obtiene el identificador de la sucursal.
     *
     * @return Identificador numérico único.
     */
    public int getIdSucursal() {
        return idSucursal;
    }
    
    /**
     * Establece el identificador de la sucursal.
     *
     * @param idSucursal Identificador a asignar.
     */
    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    /**
     * Obtiene el gestor asignado a la sucursal.
     *
     * @return Entidad {@link GestorModel}.
     */
    public GestorModel getGestor() {
        return gestor;
    }
    
    /**
     * Asocia un gestor a la sucursal.
     *
     * @param gestor Gestor a vincular.
     */
    public void setGestor(GestorModel gestor) {
        this.gestor = gestor;
    }

    /**
     * Obtiene la dirección de la sucursal.
     *
     * @return Entidad {@link DireccionModel}.
     */
    public DireccionModel getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección física de la sucursal.
     *
     * @param direccion Dirección a vincular.
     */
    public void setDireccion(DireccionModel direccion) {
        this.direccion = direccion;
    }

    /**
     * Representación textual de la sucursal.
     *
     * @return Cadena formateada de la sucursal.
     */
    @Override
    public String toString() {
        return "idSucursal=" + idSucursal + ", direccion=" + (direccion != null ? direccion.getIdDireccion() : "null") + ", gestor=" + (gestor != null ? gestor.getIdGestor() : "null");
    }
}
