package com.example.Proyect_DevOps.geolocalizacion.models;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ciudades")
public class CiudadModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCiudad;
    private String nombre;
    
    @ManyToOne
    @JoinColumn(name = "idEstado")
    private EstadoModel idEstado;

    public CiudadModel(String nombre, EstadoModel idEstado) {
        this.nombre = nombre;
        this.idEstado = idEstado;
    }

    public CiudadModel() {
    }
    
    public int getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(int idCiudad) {
        this.idCiudad = idCiudad;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public EstadoModel getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(EstadoModel idEstado) {
        this.idEstado = idEstado;
    }
    
    @Override
    public String toString() {
        return "[idCiudad=" + idCiudad + ", nombre=" + nombre + ", idEstado=" + idEstado.getIdEstado() + "]";
    }
}