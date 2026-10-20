package com.Proyect_DevOps.mascotas.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa una raza específica perteneciente a una especie animal.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Entity
@Table(name = "razas")
public class RazaModel {

    /**
     * Identificador único autoincremental de la raza.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRaza;

    /**
     * Nombre distintivo de la raza (ej. Labrador Retriever, Siamés).
     */
    private String nombre;
    
    /**
     * Especie a la que pertenece esta raza.
     */
    @ManyToOne
    @JoinColumn(name = "idEspecie")
    private EspecieModel especie;

    /**
     * Constructor con nombre y especie.
     *
     * @param nombre Nombre de la raza.
     * @param especie Especie a la que pertenece.
     */
    public RazaModel(String nombre, EspecieModel especie) {
        this.nombre = nombre;
        this.especie = especie;
    }

    /**
     * Constructor por defecto requerido por JPA.
     */
    public RazaModel() {
    }
    
    /**
     * Obtiene el identificador de la raza.
     *
     * @return Identificador numérico.
     */
    public int getIdRaza() {
        return idRaza;
    }
    
    /**
     * Establece el identificador de la raza.
     *
     * @param idRaza Identificador a asignar.
     */
    public void setIdRaza(int idRaza) {
        this.idRaza = idRaza;
    }
    
    /**
     * Obtiene el nombre de la raza.
     *
     * @return Nombre de la raza.
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Establece el nombre de la raza.
     *
     * @param nombre Nombre descriptivo a asignar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la especie vinculada.
     *
     * @return Entidad {@link EspecieModel}.
     */
    public EspecieModel getEspecie(){
        return especie;
    }

    /**
     * Establece la especie vinculada a la raza.
     *
     * @param especie Instancia de {@link EspecieModel}.
     */
    public void setEspecie(EspecieModel especie){
        this.especie = especie;
    }
    
    /**
     * Representación textual de la raza.
     *
     * @return Cadena formateada de la raza.
     */
    @Override
    public String toString() {
        return "idRaza=" + idRaza + ", nombre=" + nombre + ", especie=" + (especie != null ? especie.getIdEspecie() : "null");
    }
}
