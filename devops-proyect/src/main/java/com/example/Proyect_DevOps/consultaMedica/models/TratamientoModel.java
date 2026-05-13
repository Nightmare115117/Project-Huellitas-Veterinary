package com.example.Proyect_DevOps.consultaMedica.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tratamientos")
public class TratamientoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTratamiento;

    private String medicamento;
    private String descripcion;
    private double costo;
    private int estatus;

    @ManyToMany(mappedBy = "tratamientos")
    private List<ConsultaMedicaModel> consultas = new ArrayList<>();


    public TratamientoModel(String medicamento, String descripcion, double costo, int estatus){
        this.medicamento = medicamento;
        this.descripcion = descripcion;
        this.costo = costo;
        this.estatus = estatus;
    }

    public TratamientoModel(){
    }

    public String getMedicamento(){
        return medicamento;
    }

    public void setMedicamento(String medicamento){
        this.medicamento = medicamento;
    }

    public String getDescripcion(){
        return descripcion;
    }

    public void setDescripcion(String descripcion){
        this.descripcion = descripcion;
    }

    public double getCosto(){
        return costo;
    }

    public void setCosto(double costo){
        this.costo = costo;
    }

    public List<ConsultaMedicaModel> getListaConsultas(){
        return consultas;
    }

    public void setListaConsultas(List<ConsultaMedicaModel> consultas){
        this.consultas = consultas;
    }

    public int getStatus(){
        return estatus;
    }

    public void setStatus(int estatus){
        this.estatus = estatus;
    }

    @Override
    public String toString(){
        return "idTratamiento=" + idTratamiento + ", medicamento=" + medicamento + 
        ", descripcion=" + descripcion + ", costo=" + costo + ", estatus=" + estatus;
    }

    public int getIdTratamiento() {
        return idTratamiento;
    }

    public void setIdTratamiento(int idTratamiento) {
        this.idTratamiento = idTratamiento;
    }
}
