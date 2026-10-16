package com.example.Proyect_DevOps.sucursales.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sucursales")
public class SucursalModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSucursal;

    @OneToOne
    @JoinColumn(name = "idDireccion")
    private DireccionModel direccion;

    @OneToOne
    @JoinColumn(name = "idGestor")
    private GestorModel gestor;

    public SucursalModel(DireccionModel direccion, GestorModel gestor) {
        this.direccion = direccion;
        this.gestor = gestor;
    }

    public SucursalModel() {
    }

    public int getIdSucursal() {
        return idSucursal;
    }
    
    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public GestorModel getGestor() {
        return gestor;
    }
    
    public void setGestor(GestorModel gestor) {
        this.gestor = gestor;
    }

    public DireccionModel getDireccion() {
        return direccion;
    }

    public void setDireccion(DireccionModel direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return "idSucursal=" + idSucursal + ", direccion=" + (direccion != null ? direccion.getIdDireccion() : "null") + ", gestor=" + (gestor != null ? gestor.getIdGestor() : "null");
    }
}
