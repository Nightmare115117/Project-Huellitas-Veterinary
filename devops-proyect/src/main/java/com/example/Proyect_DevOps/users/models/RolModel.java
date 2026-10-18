package com.example.Proyect_DevOps.users.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa un rol de usuario dentro del sistema veterinario.
 * Define los niveles de acceso y permisos (ej. 'Admin', 'Veterinario', 'Cliente').
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Entity
@Table(name = "roles")
public class RolModel {

    /**
     * Identificador único autoincremental del rol.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRol;

    /**
     * Nombre distintivo del rol (ej. Administrador, Veterinario, Cliente).
     */
    private String nombre;

    /**
     * Constructor con el nombre del rol.
     *
     * @param nombre Nombre descriptivo del rol.
     */
    public RolModel(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Constructor por defecto requerido por JPA.
     */
    public RolModel() {
    }
    
    /**
     * Obtiene el identificador del rol.
     *
     * @return Identificador numérico único del rol.
     */
    public int getIdRol() {
        return idRol;
    }
    
    /**
     * Establece el identificador del rol.
     *
     * @param idRol Identificador numérico a asignar.
     */
    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }
    
    /**
     * Obtiene el nombre del rol.
     *
     * @return Nombre del rol.
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Establece el nombre del rol.
     *
     * @param nombre Nombre descriptivo a asignar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    /**
     * Representación textual de la entidad Rol.
     *
     * @return Cadena formateada con el identificador y nombre del rol.
     */
    @Override
    public String toString() {
        return "idRol=" + idRol + ", nombre=" + nombre;
    }
}
