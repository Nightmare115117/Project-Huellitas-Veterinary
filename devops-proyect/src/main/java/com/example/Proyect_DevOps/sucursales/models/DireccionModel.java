package com.example.Proyect_DevOps.sucursales.models;

import com.example.Proyect_DevOps.geolocalizacion.models.CalleModel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "direcciones")
public class DireccionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDireccion;

    @ManyToOne
    @JoinColumn(name = "idCalle")
    private CalleModel calle;
    private int codigoPostal;

    public DireccionModel(CalleModel calle, int codigoPostal) {
        this.calle = calle;
        this.codigoPostal = codigoPostal;
    }

    public DireccionModel() {
    }
    
    public int getIdDireccion() {
        return idDireccion;
    }
    
    public void setIdDireccion(int idDireccion) {
        this.idDireccion = idDireccion;
    }
    
    public CalleModel getCalle() {
        return calle;
    }
    
    public void setCalle(CalleModel calle) {
        this.calle = calle;
    }
    
    public int getCodigoPostal(){
        return codigoPostal;
    }

    public void setCodigoPostal(int codigoPostal){
        this.codigoPostal = codigoPostal;
    }

    @Override
    public String toString() {
        return "idDireccion=" + idDireccion + ", calle=" + calle.getIdCalle()+ ", codigoPostal=" + codigoPostal;
    }
}
