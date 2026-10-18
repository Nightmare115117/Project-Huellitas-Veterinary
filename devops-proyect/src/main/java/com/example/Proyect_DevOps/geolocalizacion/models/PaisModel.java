package com.example.Proyect_DevOps.geolocalizacion.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa a un país dentro del esquema de geolocalización.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Entity
@Table(name = "paises")
public class PaisModel {

    /**
     * Identificador único autoincremental del país.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPais;

    /**
     * Nombre oficial o común del país.
     */
    private String nombre;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public PaisModel() {
    }

    /**
     * Constructor con el nombre del país.
     *
     * @param nombre Nombre descriptivo del país.
     */
    public PaisModel(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el identificador del país.
     *
     * @return Identificador numérico único.
     */
    public int getIdPais() {
        return idPais;
    }

    /**
     * Establece el identificador del país.
     *
     * @param idPais Identificador a asignar.
     */
    public void setIdPais(int idPais) {
        this.idPais = idPais;
    }

    /**
     * Obtiene el nombre del país.
     *
     * @return Nombre del país.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del país.
     *
     * @param nombre Nombre a asignar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Representación en cadena del país.
     *
     * @return Cadena formateada del país.
     */
    @Override
    public String toString() {
        return "[idPais=" + idPais + ", nombre=" + nombre + "]";
    }
}
