package com.Proyect_DevOps.mascotas.models;

import java.time.LocalDate;

import com.Proyect_DevOps.sucursales.models.SucursalModel;
import com.Proyect_DevOps.users.models.UsuarioModel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa a una mascota registrada en la clínica veterinaria.
 * Asocia a la mascota con su raza, la sucursal de registro y su dueño (cliente).
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Entity
@Table(name = "mascotas")
public class MascotaModel {

    /**
     * Identificador único autoincremental de la mascota.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idMascota;

    /**
     * Nombre de la mascota.
     */
    private String nombre;

    /**
     * Fecha de nacimiento de la mascota.
     */
    private LocalDate fechaNacimiento;

    /**
     * Estado activo o inactivo de la mascota en el sistema (ej. 1=Activo, 0=Baja).
     */
    private int status;

    /**
     * Raza a la que pertenece la mascota.
     */
    @ManyToOne
    @JoinColumn(name = "idRaza")
    private RazaModel raza;

    /**
     * Sucursal médica en la cual se encuentra adscrita la mascota.
     */
    @ManyToOne
    @JoinColumn(name = "idSucursal")
    private SucursalModel sucursal;

    /**
     * Usuario propietario o dueño de la mascota.
     */
    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private UsuarioModel usuario;

    /**
     * Constructor completo para registrar una nueva mascota.
     *
     * @param nombre Nombre de la mascota.
     * @param fechaNacimiento Fecha de nacimiento.
     * @param peso Parámetro de peso referencial.
     * @param status Estado de la mascota.
     * @param raza Raza asignada.
     * @param sucursal Sucursal asignada.
     * @param usuario Usuario dueño de la mascota.
     */
    public MascotaModel(String nombre, LocalDate fechaNacimiento, double peso, int status, RazaModel raza, SucursalModel sucursal, UsuarioModel usuario) {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.status = status;
        this.raza = raza;
        this.sucursal = sucursal;
        this.usuario = usuario;
    }

    /**
     * Constructor por defecto requerido por JPA.
     */
    public MascotaModel() {
    }

    /**
     * Obtiene el identificador de la mascota.
     *
     * @return Identificador numérico único.
     */
    public int getIdMascota() {
        return idMascota;
    }
    
    /**
     * Establece el identificador de la mascota.
     *
     * @param idMascota Identificador a asignar.
     */
    public void setIdMascota(int idMascota) {
        this.idMascota = idMascota;
    }

    /**
     * Obtiene el nombre de la mascota.
     *
     * @return Nombre de la mascota.
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Establece el nombre de la mascota.
     *
     * @param nombre Nombre a asignar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    /**
     * Obtiene la fecha de nacimiento.
     *
     * @return Fecha {@link LocalDate}.
     */
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    
    /**
     * Establece la fecha de nacimiento.
     *
     * @param fechaNacimiento Fecha a asignar.
     */
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    
    /**
     * Obtiene el estado de la mascota.
     *
     * @return Código de estado.
     */
    public int getStatus() {
        return status;
    }
    
    /**
     * Establece el estado de la mascota.
     *
     * @param status Código a asignar.
     */
    public void setStatus(int status) {
        this.status = status;
    }
    
    /**
     * Obtiene la raza de la mascota.
     *
     * @return Entidad {@link RazaModel}.
     */
    public RazaModel getRaza() {
        return raza;
    }
    
    /**
     * Establece la raza de la mascota.
     *
     * @param raza Raza a asignar.
     */
    public void setRaza(RazaModel raza) {
        this.raza = raza;
    }
    
    /**
     * Obtiene la sucursal asignada.
     *
     * @return Entidad {@link SucursalModel}.
     */
    public SucursalModel getSucursal() {
        return sucursal;
    }
 
    /**
     * Establece la sucursal de atención.
     *
     * @param sucursal Sucursal a vincular.
     */
    public void setSucursal(SucursalModel sucursal) {
        this.sucursal = sucursal;
    }
    
    /**
     * Obtiene el usuario dueño.
     *
     * @return Entidad {@link UsuarioModel}.
     */
    public UsuarioModel getUsuario(){
        return usuario;
    }

    /**
     * Establece el usuario dueño de la mascota.
     *
     * @param usuario Dueño a asignar.
     */
    public void setUsuario(UsuarioModel usuario){
        this.usuario = usuario;
    }

    /**
     * Representación textual de la mascota.
     *
     * @return Cadena formateada de la mascota.
     */
    @Override
    public String toString() {
        return "idMascota=" + idMascota + ", nombre=" + nombre + ", fechaNacimiento=" + fechaNacimiento
        + ", status=" + status + ", raza=" + (raza != null ? raza.getIdRaza() : "null") + ", sucursal=" + (sucursal != null ? sucursal.getIdSucursal() : "null") + 
        ", usuario=" + (usuario != null ? usuario.getIdUsuario() : "null");
    }   
}
