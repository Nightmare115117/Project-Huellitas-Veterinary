package com.Proyect_DevOps.mascotas.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa una especie biológica animal (ej. Canino, Felino, Ave, Reptil).
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Entity
@Table(name = "especies")
public class EspecieModel {

    /**
     * Identificador único autoincremental de la especie.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEspecie;

    /**
     * Nombre común o taxonómico de la especie.
     */
    private String nombre;

    /**
     * Constructor con el nombre de la especie.
     *
     * @param nombre Nombre descriptivo de la especie.
     */
    public EspecieModel(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Constructor por defecto requerido por JPA.
     */
    public EspecieModel() {
    }

    /**
     * Obtiene el identificador de la especie.
     *
     * @return Identificador numérico único.
     */
    public int getIdEspecie() {
        return idEspecie;
    }
    
    /**
     * Establece el identificador de la especie.
     *
     * @param idEspecie Identificador a asignar.
     */
    public void setIdEspecie(int idEspecie) {
        this.idEspecie = idEspecie;
    }
    
    /**
     * Obtiene el nombre de la especie.
     *
     * @return Nombre de la especie.
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Establece el nombre de la especie.
     *
     * @param nombre Nombre a asignar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    /**
     * Representación textual de la especie.
     *
     * @return Cadena formateada de la entidad.
     */
    @Override
    public String toString() {
        return "idEspecie=" + idEspecie + ", nombre=" + nombre;
    }
}
