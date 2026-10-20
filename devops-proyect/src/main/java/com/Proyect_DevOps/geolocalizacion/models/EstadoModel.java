package com.Proyect_DevOps.geolocalizacion.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa a un estado o provincia asociado a un país.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Entity
@Table(name = "estados")
public class EstadoModel {
    
    /**
     * Identificador único autoincremental del estado.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEstado;

    /**
     * Nombre del estado o provincia.
     */
    private String nombre;

    /**
     * País al que pertenece el estado.
     */
    @ManyToOne
    @JoinColumn(name = "idPais")
    private PaisModel pais;

    /**
     * Constructor con nombre y país.
     *
     * @param nombre Nombre del estado.
     * @param pais País asociado.
     */
    public EstadoModel(String nombre, PaisModel pais) {
        this.nombre = nombre;
        this.pais = pais;
    }

    /**
     * Constructor por defecto requerido por JPA.
     */
    public EstadoModel() {
    }

    /**
     * Obtiene el identificador del estado.
     *
     * @return Identificador numérico.
     */
    public int getIdEstado() {
        return idEstado;
    }

    /**
     * Establece el identificador del estado.
     *
     * @param idEstado Identificador a asignar.
     */
    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    /**
     * Obtiene el nombre del estado.
     *
     * @return Nombre del estado.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del estado.
     *
     * @param nombre Nombre a asignar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el país asociado.
     *
     * @return Entidad {@link PaisModel}.
     */
    public PaisModel getPais() {
        return pais;
    }
    
    /**
     * Asocia el país al estado.
     *
     * @param pais País a vincular.
     */
    public void setPais(PaisModel pais) {
        this.pais = pais;
    }

    /**
     * Representación textual del estado.
     *
     * @return Cadena formateada del estado.
     */
    @Override
    public String toString() {
        return "[idEstado=" + idEstado + ", nombre=" + nombre + ", pais=" + (pais != null ? pais.getIdPais() : "null") + "]";
    }
}
