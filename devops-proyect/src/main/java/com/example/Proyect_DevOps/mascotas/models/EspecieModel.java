package com.example.Proyect_DevOps.mascotas.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "especies")
public class EspecieModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEspecie;
    private String nombre;

    public EspecieModel(String nombre) {
        this.nombre = nombre;
    }

    public EspecieModel() {
    }

    public int getIdEspecie() {
        return idEspecie;
    }
    
    public void setIdEspecie(int idEspecie) {
        this.idEspecie = idEspecie;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @Override
    public String toString() {
        return "idEspecie=" + idEspecie + ", nombre=" + nombre;
    }
}