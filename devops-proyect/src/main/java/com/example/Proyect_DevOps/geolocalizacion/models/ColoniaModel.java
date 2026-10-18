package com.example.Proyect_DevOps.geolocalizacion.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa una colonia, barrio o vecindario dentro de una ciudad.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Entity
@Table(name = "colonias")
public class ColoniaModel {

    /**
     * Identificador único autoincremental de la colonia.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idColonia;

    /**
     * Nombre de la colonia o sector.
     */
    private String nombre;

    /**
     * Ciudad a la que pertenece la colonia.
     */
    @ManyToOne
    @JoinColumn(name = "idCiudad")
    private CiudadModel ciudad;

    /**
     * Constructor con nombre y ciudad.
     *
     * @param nombre Nombre de la colonia.
     * @param ciudad Ciudad asociada.
     */
    public ColoniaModel(String nombre, CiudadModel ciudad) {
        this.nombre = nombre;
        this.ciudad = ciudad;
    }

    /**
     * Constructor por defecto requerido por JPA.
     */
    public ColoniaModel() {
    }

    /**
     * Obtiene el identificador de la colonia.
     *
     * @return Identificador numérico.
     */
    public int getIdColonia() {
        return idColonia;
    }

    /**
     * Establece el identificador de la colonia.
     *
     * @param idColonia Identificador a asignar.
     */
    public void setIdColonia(int idColonia) {
        this.idColonia = idColonia;
    }

    /**
     * Obtiene el nombre de la colonia.
     *
     * @return Nombre de la colonia.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la colonia.
     *
     * @param nombre Nombre a asignar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la ciudad asociada.
     *
     * @return Entidad {@link CiudadModel}.
     */
    public CiudadModel getCiudad() {
        return ciudad;
    }

    /**
     * Asocia la ciudad a la colonia.
     *
     * @param ciudad Ciudad a vincular.
     */
    public void setCiudad(CiudadModel ciudad) {
        this.ciudad = ciudad;
    }
    
    /**
     * Representación textual de la colonia.
     *
     * @return Cadena formateada de la colonia.
     */
    @Override
    public String toString() {
        return "[idColonia=" + idColonia + ", nombre=" + nombre + ", ciudad=" + (ciudad != null ? ciudad.getIdCiudad() : "null") + "]";
    }
}
