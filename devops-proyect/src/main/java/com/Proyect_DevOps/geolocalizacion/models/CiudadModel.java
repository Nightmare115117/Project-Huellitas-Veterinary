package com.Proyect_DevOps.geolocalizacion.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa a una ciudad o municipio perteneciente a un estado.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Entity
@Table(name = "ciudades")
public class CiudadModel {

    /**
     * Identificador único autoincremental de la ciudad.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCiudad;

    /**
     * Nombre de la ciudad o localidad.
     */
    private String nombre;
    
    /**
     * Estado o entidad federativa a la que pertenece la ciudad.
     */
    @ManyToOne
    @JoinColumn(name = "idEstado")
    private EstadoModel idEstado;

    /**
     * Constructor con nombre y estado.
     *
     * @param nombre Nombre de la ciudad.
     * @param idEstado Estado asociado.
     */
    public CiudadModel(String nombre, EstadoModel idEstado) {
        this.nombre = nombre;
        this.idEstado = idEstado;
    }

    /**
     * Constructor por defecto requerido por JPA.
     */
    public CiudadModel() {
    }
    
    /**
     * Obtiene el identificador de la ciudad.
     *
     * @return Identificador numérico.
     */
    public int getIdCiudad() {
        return idCiudad;
    }

    /**
     * Establece el identificador de la ciudad.
     *
     * @param idCiudad Identificador a asignar.
     */
    public void setIdCiudad(int idCiudad) {
        this.idCiudad = idCiudad;
    }
    
    /**
     * Obtiene el nombre de la ciudad.
     *
     * @return Nombre de la ciudad.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la ciudad.
     *
     * @param nombre Nombre a asignar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    /**
     * Obtiene el estado vinculado a la ciudad.
     *
     * @return Entidad {@link EstadoModel}.
     */
    public EstadoModel getIdEstado() {
        return idEstado;
    }

    /**
     * Asocia el estado a la ciudad.
     *
     * @param idEstado Estado a vincular.
     */
    public void setIdEstado(EstadoModel idEstado) {
        this.idEstado = idEstado;
    }

    /**
     * Obtiene el estado vinculado a la ciudad (alias de conveniencia).
     *
     * @return Entidad {@link EstadoModel}.
     */
    public EstadoModel getEstado() {
        return idEstado;
    }

    /**
     * Asocia el estado a la ciudad (alias de conveniencia).
     *
     * @param estado Estado a vincular.
     */
    public void setEstado(EstadoModel estado) {
        this.idEstado = estado;
    }
    
    /**
     * Representación textual de la ciudad.
     *
     * @return Cadena formateada de la ciudad.
     */
    @Override
    public String toString() {
        return "[idCiudad=" + idCiudad + ", nombre=" + nombre + ", idEstado=" + (idEstado != null ? idEstado.getIdEstado() : "null") + "]";
    }
}
