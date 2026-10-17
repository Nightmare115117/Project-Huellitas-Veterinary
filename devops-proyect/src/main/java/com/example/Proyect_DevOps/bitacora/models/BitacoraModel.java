package com.example.Proyect_DevOps.bitacora.models;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "bitacora")
public class BitacoraModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idBitacora;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false)
    private String usuario;

    private String rol;

    @Column(nullable = false)
    private String accion;

    private String modulo;

    @Column(length = 1000)
    private String detalle;

    private String ip;

    public BitacoraModel() {
    }

    public BitacoraModel(String usuario, String rol, String accion, String modulo, String detalle, String ip) {
        this.fechaHora = LocalDateTime.now();
        this.usuario = usuario;
        this.rol = rol;
        this.accion = accion;
        this.modulo = modulo;
        this.detalle = detalle;
        this.ip = ip;
    }

    @PrePersist
    public void prePersist() {
        if (this.fechaHora == null) {
            this.fechaHora = LocalDateTime.now();
        }
    }

    public int getIdBitacora() {
        return idBitacora;
    }

    public void setIdBitacora(int idBitacora) {
        this.idBitacora = idBitacora;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "BitacoraModel{" +
                "idBitacora=" + idBitacora +
                ", fechaHora=" + fechaHora +
                ", usuario='" + usuario + '\'' +
                ", rol='" + rol + '\'' +
                ", accion='" + accion + '\'' +
                ", modulo='" + modulo + '\'' +
                ", detalle='" + detalle + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }
}
