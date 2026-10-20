package com.Proyect_DevOps.geolocalizacion.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa una calle o vialidad perteneciente a una colonia.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Entity
@Table(name = "calles")
public class CalleModel {

    /**
     * Identificador único autoincremental de la calle.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCalle;

    /**
     * Nombre de la calle o avenida.
     */
    private String nombre;

    /**
     * Colonia a la que pertenece la calle.
     */
    @ManyToOne
    @JoinColumn(name = "idColonia")
    private ColoniaModel colonia;
    
    /**
     * Constructor con nombre y colonia.
     *
     * @param nombre Nombre de la calle.
     * @param colonia Colonia vinculada.
     */
    public CalleModel(String nombre, ColoniaModel colonia) {
        this.nombre = nombre;
        this.colonia = colonia;
    }

    /**
     * Constructor por defecto requerido por JPA.
     */
    public CalleModel() {
    }

    /**
     * Obtiene el identificador de la calle.
     *
     * @return Identificador numérico.
     */
    public int getIdCalle() {
        return idCalle;
    }

    /**
     * Establece el identificador de la calle.
     *
     * @param idCalle Identificador a asignar.
     */
    public void setIdCalle(int idCalle) {
        this.idCalle = idCalle;
    }

    /**
     * Obtiene el nombre de la calle.
     *
     * @return Nombre de la calle.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la calle.
     *
     * @param nombre Nombre a asignar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la colonia asociada.
     *
     * @return Entidad {@link ColoniaModel}.
     */
    public ColoniaModel getColonia() {
        return colonia;
    }
    
    /**
     * Asocia la colonia a la calle.
     *
     * @param colonia Colonia a vincular.
     */
    public void setColonia(ColoniaModel colonia) {
        this.colonia = colonia;
    }

    /**
     * Representación textual de la calle.
     *
     * @return Cadena formateada de la calle.
     */
    @Override
    public String toString() {
        return "[idCalle=" + idCalle + ", nombre=" + nombre + ", colonia=" + (colonia != null ? colonia.getIdColonia() : "null") + "]";
    }
}
