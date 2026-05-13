package com.example.Proyect_DevOps.users.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUsuario;
    private String nombre;
    private String paterno;
    private String materno;

    @Column(nullable = false, unique = true)
    private String correo;
    @Column(nullable = false)
    private String contraseña;

    @ManyToOne
    @JoinColumn(name = "idRol")
    private RolModel rol;
    
    public UsuarioModel(String nombre, String paterno, String materno, String correo, String contraseña, RolModel rol) {
        this.nombre = nombre;
        this.paterno = paterno;
        this.materno = materno;
        this.correo = correo;
        this.contraseña = contraseña;
        this.rol = rol;
    }

    public UsuarioModel() {
    }

    public int getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPaterno() {
        return paterno;
    }
    
    public void setPaterno(String paterno) {
        this.paterno = paterno;
    }

    public String getMaterno() {
        return materno;
    }
    
    public void setMaterno(String materno) {
        this.materno = materno;
    }

    public String getCorreo() {
        return correo;
    }
    
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }
    
    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public RolModel getRol() {
        return rol;
    }
    
    public void setRol(RolModel rol) {
        this.rol = rol;
    }
    
    @Override
    public String toString() {
        return "idUsuario=" + idUsuario + ", nombre=" + nombre + ", paterno=" + paterno + ", materno=" + materno + ", correo=" + correo + ", contraseña=" + contraseña + ", rol=" + rol.getIdRol();
    }
}