package com.example.Proyect_DevOps.geolocalizacion.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "calles")
public class CalleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCalle;
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "idColonia")
    private ColoniaModel colonia;
    
    public CalleModel(String nombre, ColoniaModel colonia) {
        this.nombre = nombre;
        this.colonia = colonia;
    }
    public CalleModel() {
    }

    public int getIdCalle() {
        return idCalle;
    }

    public void setIdCalle(int idCalle) {
        this.idCalle = idCalle;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ColoniaModel getColonia() {
        return colonia;
    }
    
    public void setColonia(ColoniaModel colonia) {
        this.colonia = colonia;
    }

    @Override
    public String toString() {
        return "[idCalle=" + idCalle + ", nombre=" + nombre + ", colonia=" + colonia.getIdColonia() + "]";
    }
}