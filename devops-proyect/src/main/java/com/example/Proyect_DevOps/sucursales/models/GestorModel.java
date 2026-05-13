package com.example.Proyect_DevOps.sucursales.models;

import com.example.Proyect_DevOps.users.models.UsuarioModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "gestores")
public class GestorModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idGestor;
    
    @Column(unique = true)
    private String rfc;
    private double salario;

    
    @OneToOne
    @JoinColumn(name = "idUsuario")
    private UsuarioModel usuario;

    public GestorModel(String rfc, double salario, UsuarioModel usuario) {
        this.rfc = rfc;
        this.salario = salario;
        this.usuario = usuario;
    }

    public GestorModel() {
    }
    
    
    public int getIdGestor() {
        return idGestor;
    }
    
    public void setIdGestor(int idGestor) {
        this.idGestor = idGestor;
    }
    
    
    public String getRfc() {
        return rfc;
    }
    
    public void setRfc(String rfc) {
        this.rfc = rfc;
    }
    
    
    public double getSalario() {
        return salario;
    }
    
    public void setSalario(double salario) {
        this.salario = salario;
    }
    
    public UsuarioModel getUsuario() {
        return usuario;
    }
    
    public void setUsuario(UsuarioModel usuario) {
        this.usuario = usuario;
    }
    
    @Override
    public String toString() {
        return "idGestor=" + idGestor + ", rfc=" + rfc + ", salario=" + salario + ", usuario=" + usuario.getIdUsuario();
    }
}
