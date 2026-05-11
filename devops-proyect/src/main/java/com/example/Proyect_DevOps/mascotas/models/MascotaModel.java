package com.example.Proyect_DevOps.mascotas.models;

import java.time.LocalDate;

import com.example.Proyect_DevOps.sucursales.models.SucursalModel;
import com.example.Proyect_DevOps.users.models.UsuarioModel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "mascotas")
public class MascotaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idMascota;
    private String nombre;
    private LocalDate fechaNacimiento;
    private int status;

    @ManyToOne
    @JoinColumn(name = "idRaza")
    private RazaModel raza;

    @ManyToOne
    @JoinColumn(name = "idSucursal")
    private SucursalModel sucursal;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private UsuarioModel usuario;

    public MascotaModel(String nombre, LocalDate fechaNacimiento, double peso, int status, RazaModel raza, SucursalModel sucursal, UsuarioModel usuario) {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.status = status;
        this.raza = raza;
        this.sucursal = sucursal;
        this.usuario = usuario;
    }

    public MascotaModel() {
    }

    public int getIdMascota() {
        return idMascota;
    }
    
    public void setIdMascota(int idMascota) {
        this.idMascota = idMascota;
    }

    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public RazaModel getRaza() {
        return raza;
    }
    
    public void setRaza(RazaModel raza) {
        this.raza = raza;
    }
    
    public SucursalModel getSucursal() {
        return sucursal;
    }
 
    public void setSucursal(SucursalModel sucursal) {
        this.sucursal = sucursal;
    }
    
    public UsuarioModel getUsuario(){
        return usuario;
    }

    public void setUsuario(UsuarioModel usuario){
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "idMascota=" + idMascota + ", nombre=" + nombre + ", fechaNacimiento=" + fechaNacimiento
        + ", status=" + status + ", raza=" + raza.getIdRaza() + ", sucursal=" + sucursal.getIdSucursal() + 
        ", usuario=" + usuario.getIdUsuario();
    }   
}