package com.example.Proyect_DevOps.citas.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.example.Proyect_DevOps.mascotas.models.MascotaModel;
import com.example.Proyect_DevOps.servicios.models.ServicioModel;
import com.example.Proyect_DevOps.users.models.UsuarioModel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "citas")
public class CitaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCita;

    @ManyToOne
    @JoinColumn(name = "IdMascota")
    private MascotaModel mascotaModel;

    @ManyToOne
    @JoinColumn(name = "IdUsuarioM")
    private UsuarioModel usuarioMascota;

    @ManyToOne
    @JoinColumn(name = "IdUsuarioV")
    private UsuarioModel usuarioVeterinario;
    private LocalDate fecha;
    private LocalTime entradaAgendada;
    private LocalTime horaSalida;
    private int estadoCita;

    @ManyToMany
    @JoinTable(name= "Cita-Servicio", joinColumns = @JoinColumn(name = "IdCita"),
    inverseJoinColumns = @JoinColumn(name = "IdServicio"))
    public List<ServicioModel> servicios = new ArrayList<>();

    public CitaModel(MascotaModel mascotaModel, UsuarioModel usuarioMascota, 
        UsuarioModel usuarioVeterinario, LocalDate fecha, LocalTime entradaAgendada, int estadoCita){
        this.mascotaModel = mascotaModel;
        this.usuarioMascota = usuarioMascota;
        this.usuarioVeterinario = usuarioVeterinario;
        this.fecha = fecha;
        this.entradaAgendada = entradaAgendada;
        this.horaSalida = null;
        this.estadoCita = estadoCita;
    }

    public CitaModel(){
    }

    public CitaModel(MascotaModel mascota, UsuarioModel usuario, LocalDate fecha, LocalTime entradaAgendada,
            int estadoCita) {
        this.mascotaModel = mascota;
        this.usuarioMascota = usuario;
        this.fecha = fecha;
        this.entradaAgendada = entradaAgendada;
        this.estadoCita = estadoCita;
    }

    public int getIdCita(){
        return idCita;
    }

    public void setIdCita(int idServicio){
        this.idCita = idServicio;
    }

    public MascotaModel getMascotaModel(){
        return mascotaModel;
    }

    public void setMasotaModel(MascotaModel mascotaModel){
        this.mascotaModel = mascotaModel;
    }

    public UsuarioModel getUsuarioMascota(){
        return usuarioMascota;
    }

    public void setUsuarioMascota(UsuarioModel usuarioMascota){
        this.usuarioMascota = usuarioMascota;
    }

    public UsuarioModel getUsuarioVeterinario(){
        return usuarioVeterinario;
    }

    public void setUsuarioVeterinario(UsuarioModel usuarioVeterinario){
        this.usuarioVeterinario = usuarioVeterinario;
    }

    public LocalDate getFecha(){
        return fecha;
    }

    public void setFecha(LocalDate fecha){
        this.fecha = fecha;
    }

    public LocalTime getEntradaAgendada(){
        return entradaAgendada;
    }

    public void setEntradaAgendada(LocalTime entradaAgendada){
        this.entradaAgendada = entradaAgendada;
    }

    public LocalTime getHoraSalida(){
        return horaSalida;
    }

    public void setHoraSalida(LocalTime horaSalida){
        this.horaSalida = horaSalida;
    }

    public int getEstadoCita(){
        return estadoCita;
    }

    public void setEstadoCita(int estado_cita){
        this.estadoCita = estado_cita;
    }

    public List<ServicioModel> getListaServicios(){
        return servicios;
    }

    public void setListaServicios(List<ServicioModel> servicios){
        this.servicios = servicios;
    }

    @Override
    public String toString(){
        return "idCita=" + idCita + 
        ", idMascota=" + mascotaModel.getIdMascota() + 
        ", idUsuarioM=" + usuarioMascota.getIdUsuario() + 
        ", idUsuarioV=" + usuarioVeterinario.getIdUsuario() +
        ", fecha=" + fecha.toString() + 
        ", estado_cita=" + estadoCita;
    }
}
