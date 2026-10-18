package com.example.Proyect_DevOps.servicios.models;

import java.util.ArrayList;
import java.util.List;

import com.example.Proyect_DevOps.citas.models.CitaModel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa un servicio ofrecido por la clínica veterinaria (ej. Vacunación, Consulta General, Cirugía, Estética).
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Entity
@Table(name = "servicios")
public class ServicioModel {
    
    /**
     * Identificador único autoincremental del servicio.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idServicio;

    /**
     * Nombre descriptivo del servicio veterinario.
     */
    private String nombre;

    /**
     * Costo o tarifa monetaria asignada al servicio.
     */
    private double costo;

    /**
     * Lista de citas médicas donde se ha solicitado o brindado este servicio.
     */
    @ManyToMany(mappedBy = "servicios")
    private List<CitaModel> citas = new ArrayList<>();

    /**
     * Constructor con identificador, nombre y costo.
     *
     * @param idServicio Identificador del servicio.
     * @param nombre Nombre del servicio.
     * @param costo Tarifa del servicio.
     */
    public ServicioModel(int idServicio, String nombre, double costo) {
        this.idServicio = idServicio;
        this.nombre = nombre;
        this.costo = costo;
    }

    /**
     * Constructor por defecto requerido por JPA.
     */
    public ServicioModel() {
    }

    /**
     * Obtiene el identificador del servicio.
     *
     * @return Identificador numérico único.
     */
    public int getIdServicio() {
        return idServicio;
    }
    
    /**
     * Establece el identificador del servicio.
     *
     * @param id Identificador a asignar.
     */
    public void setIdServicio(int id){
        this.idServicio = id;
    }

    /**
     * Obtiene el nombre del servicio veterinario.
     *
     * @return Nombre del servicio.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del servicio veterinario.
     *
     * @param nombre Nombre a asignar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el costo del servicio.
     *
     * @return Costo o precio unitario.
     */
    public double getCosto() {
        return costo;
    }

    /**
     * Establece el costo del servicio.
     *
     * @param costo Precio a asignar.
     */
    public void setCosto(double costo) {
        this.costo = costo;
    }

    /**
     * Obtiene las citas vinculadas a este servicio.
     *
     * @return Lista de {@link CitaModel}.
     */
    public List<CitaModel> getListaCitas(){
        return citas;
    }

    /**
     * Establece las citas asociadas al servicio.
     *
     * @param citas Lista de citas.
     */
    public void setListasCitas(List<CitaModel> citas){
        this.citas = citas;
    }

    /**
     * Representación textual del servicio.
     *
     * @return Cadena formateada del servicio.
     */
    @Override
    public String toString() {
        return "idServicio=" + idServicio + ", Nombre=" + nombre + ", Costo=" + costo;
    }
}
