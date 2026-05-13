package com.example.Proyect_DevOps.geolocalizacion.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "estados")
public class EstadoModel {
    
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int idEstado;
        private String nombre;

        @ManyToOne
        @JoinColumn(name = "idPais")
        private PaisModel pais;

        public EstadoModel(String nombre, PaisModel pais) {
            this.nombre = nombre;
            this.pais = pais;
        }

        public EstadoModel() {
        }

        public int getIdEstado() {
            return idEstado;
        }

        public void setIdEstado(int idEstado) {
            this.idEstado = idEstado;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public PaisModel getPais() {
            return pais;
        }
        
        public void setPais(PaisModel pais) {
            this.pais = pais;
        }

        @Override
        public String toString() {
            return "[idEstado=" + idEstado + ", nombre=" + nombre + ", pais=" + pais.getIdPais() + "]";
        }
}