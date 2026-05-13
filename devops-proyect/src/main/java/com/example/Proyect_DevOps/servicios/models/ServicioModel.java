package com.example.Proyect_DevOps.servicios.models;

import java.util.ArrayList;
import java.util.List;

import com.example.Proyect_DevOps.citas.models.CitaModel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "servicios")
public class ServicioModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idServicio;

    private String nombre;
    private double costo;

    @ManyToMany(mappedBy = "servicios")
    private List<CitaModel> citas = new ArrayList<>();

    public ServicioModel(int idServicio, String nombre, double costo) {
        this.idServicio = idServicio;
        this.nombre = nombre;
        this.costo = costo;
    }

    public ServicioModel() {
    }

    public int getIdServicio() {
        return idServicio;
    }
    
    public void setIdServicio(int id){
        this.idServicio = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public List<CitaModel> getListaCitas(){
        return citas;
    }

    public void setListasCitas(List<CitaModel> citas){
        this.citas = citas;
    }

    @Override
    public String toString() {
        return "idServicio=" + idServicio + ", Nombre=" + nombre + ", Costo=" + costo;
    }
}