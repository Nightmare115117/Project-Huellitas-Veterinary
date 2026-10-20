package com.Proyect_DevOps.sucursales.models;

import com.Proyect_DevOps.geolocalizacion.models.CalleModel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa una dirección física o ubicación geográfica de una sucursal.
 * Contiene la calle asociada y el código postal.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Entity
@Table(name = "direcciones")
public class DireccionModel {

    /**
     * Identificador único autoincremental de la dirección.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDireccion;

    /**
     * Calle a la que pertenece esta dirección.
     */
    @ManyToOne
    @JoinColumn(name = "idCalle")
    private CalleModel calle;

    /**
     * Código postal de la dirección.
     */
    private int codigoPostal;

    /**
     * Constructor con calle y código postal.
     *
     * @param calle Calle asociada.
     * @param codigoPostal Código postal numérico.
     */
    public DireccionModel(CalleModel calle, int codigoPostal) {
        this.calle = calle;
        this.codigoPostal = codigoPostal;
    }

    /**
     * Constructor por defecto requerido por JPA.
     */
    public DireccionModel() {
    }
    
    /**
     * Obtiene el identificador de la dirección.
     *
     * @return Identificador numérico único.
     */
    public int getIdDireccion() {
        return idDireccion;
    }
    
    /**
     * Establece el identificador de la dirección.
     *
     * @param idDireccion Identificador a asignar.
     */
    public void setIdDireccion(int idDireccion) {
        this.idDireccion = idDireccion;
    }
    
    /**
     * Obtiene la calle asociada.
     *
     * @return Entidad {@link CalleModel}.
     */
    public CalleModel getCalle() {
        return calle;
    }
    
    /**
     * Asocia la calle a la dirección.
     *
     * @param calle Calle a asignar.
     */
    public void setCalle(CalleModel calle) {
        this.calle = calle;
    }
    
    /**
     * Obtiene el código postal.
     *
     * @return Código postal numérico.
     */
    public int getCodigoPostal(){
        return codigoPostal;
    }

    /**
     * Establece el código postal.
     *
     * @param codigoPostal Código postal a asignar.
     */
    public void setCodigoPostal(int codigoPostal){
        this.codigoPostal = codigoPostal;
    }

    /**
     * Representación en cadena de la dirección.
     *
     * @return Cadena formateada de la dirección.
     */
    @Override
    public String toString() {
        return "idDireccion=" + idDireccion + ", calle=" + (calle != null ? calle.getIdCalle() : "null") + ", codigoPostal=" + codigoPostal;
    }
}
