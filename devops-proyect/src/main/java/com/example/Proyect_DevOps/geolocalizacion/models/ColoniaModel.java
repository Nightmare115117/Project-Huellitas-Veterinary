package com.example.Proyect_DevOps.geolocalizacion.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "colonias")
public class ColoniaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idColonia;
    private String nombre;
    @ManyToOne
    @JoinColumn(name = "idCiudad")
    private CiudadModel ciudad;

    public ColoniaModel(String nombre, CiudadModel ciudad) {
        this.nombre = nombre;
        this.ciudad = ciudad;
    }

    public ColoniaModel() {
    }

    public int getIdColonia() {
        return idColonia;
    }

    public void setIdColonia(int idColonia) {
        this.idColonia = idColonia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public CiudadModel getCiudad() {
        return ciudad;
    }

    public void setCiudad(CiudadModel ciudad) {
        this.ciudad = ciudad;
    }
    
    @Override
    public String toString() {
        return "[idColonia=" + idColonia + ", nombre=" + nombre + ", ciudad=" + ciudad.getIdCiudad() + "]";
    }
}
