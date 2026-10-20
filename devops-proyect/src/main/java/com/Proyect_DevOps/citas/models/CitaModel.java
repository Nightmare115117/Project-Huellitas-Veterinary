package com.Proyect_DevOps.citas.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.Proyect_DevOps.mascotas.models.MascotaModel;
import com.Proyect_DevOps.servicios.models.ServicioModel;
import com.Proyect_DevOps.users.models.UsuarioModel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entidad JPA que modela una cita médica veterinaria.
 * Representa la asociación entre una mascota, el usuario dueño (cliente), el veterinario responsable,
 * los servicios solicitados y los horarios de atención.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Entity
@Table(name = "citas")
public class CitaModel {

    /**
     * Identificador único autoincremental de la cita.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCita;

    /**
     * Mascota que recibirá la atención médica en la cita.
     */
    @ManyToOne
    @JoinColumn(name = "IdMascota")
    private MascotaModel mascotaModel;

    /**
     * Usuario propietario o responsable de la mascota (cliente).
     */
    @ManyToOne
    @JoinColumn(name = "IdUsuarioM")
    private UsuarioModel usuarioMascota;

    /**
     * Usuario veterinario asignado para atender la consulta.
     */
    @ManyToOne
    @JoinColumn(name = "IdUsuarioV")
    private UsuarioModel usuarioVeterinario;

    /**
     * Fecha programada para la cita.
     */
    private LocalDate fecha;

    /**
     * Hora de entrada o inicio programado de la consulta.
     */
    private LocalTime entradaAgendada;

    /**
     * Hora de salida o conclusión real de la consulta.
     */
    private LocalTime horaSalida;

    /**
     * Estado operativo de la cita (1=Activa/Pendiente, 2=En proceso, 3=Cancelada, 4=Completada).
     */
    private int estadoCita;

    /**
     * Lista de servicios veterinarios asociados a la cita médica.
     */
    @ManyToMany
    @JoinTable(name= "Cita-Servicio", joinColumns = @JoinColumn(name = "IdCita"),
    inverseJoinColumns = @JoinColumn(name = "IdServicio"))
    public List<ServicioModel> servicios = new ArrayList<>();

    /**
     * Constructor completo con veterinario asignado.
     *
     * @param mascotaModel Mascota a atender.
     * @param usuarioMascota Dueño de la mascota.
     * @param usuarioVeterinario Veterinario asignado.
     * @param fecha Fecha de la cita.
     * @param entradaAgendada Hora programada de entrada.
     * @param estadoCita Código de estado de la cita.
     */
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

    /**
     * Constructor por defecto requerido por JPA.
     */
    public CitaModel(){
    }

    /**
     * Constructor simplificado para agendar cita sin veterinario previamente asignado.
     *
     * @param mascota Mascota a atender.
     * @param usuario Dueño de la mascota.
     * @param fecha Fecha de la cita.
     * @param entradaAgendada Hora de entrada agendada.
     * @param estadoCita Código del estado inicial.
     */
    public CitaModel(MascotaModel mascota, UsuarioModel usuario, LocalDate fecha, LocalTime entradaAgendada,
            int estadoCita) {
        this.mascotaModel = mascota;
        this.usuarioMascota = usuario;
        this.fecha = fecha;
        this.entradaAgendada = entradaAgendada;
        this.estadoCita = estadoCita;
    }

    /**
     * Obtiene el identificador de la cita.
     *
     * @return Identificador numérico de la cita.
     */
    public int getIdCita(){
        return idCita;
    }

    /**
     * Establece el identificador de la cita.
     *
     * @param idCita Identificador numérico a asignar.
     */
    public void setIdCita(int idCita){
        this.idCita = idCita;
    }

    /**
     * Obtiene la mascota asociada a la cita.
     *
     * @return Modelo de la mascota {@link MascotaModel}.
     */
    public MascotaModel getMascotaModel(){
        return mascotaModel;
    }

    /**
     * Asocia la mascota a la cita médica.
     *
     * @param mascotaModel Instancia de {@link MascotaModel}.
     */
    public void setMasotaModel(MascotaModel mascotaModel){
        this.mascotaModel = mascotaModel;
    }

    /**
     * Asocia la mascota a la cita médica.
     *
     * @param mascotaModel Instancia de {@link MascotaModel}.
     */
    public void setMascotaModel(MascotaModel mascotaModel){
        this.mascotaModel = mascotaModel;
    }

    /**
     * Obtiene el usuario propietario de la mascota.
     *
     * @return Modelo de usuario {@link UsuarioModel}.
     */
    public UsuarioModel getUsuarioMascota(){
        return usuarioMascota;
    }

    /**
     * Asigna el usuario propietario de la mascota.
     *
     * @param usuarioMascota Dueño de la mascota.
     */
    public void setUsuarioMascota(UsuarioModel usuarioMascota){
        this.usuarioMascota = usuarioMascota;
    }

    /**
     * Obtiene el veterinario que atiende la cita.
     *
     * @return Veterinario asignado {@link UsuarioModel}.
     */
    public UsuarioModel getUsuarioVeterinario(){
        return usuarioVeterinario;
    }

    /**
     * Asigna el veterinario para la cita médica.
     *
     * @param usuarioVeterinario Veterinario a asignar.
     */
    public void setUsuarioVeterinario(UsuarioModel usuarioVeterinario){
        this.usuarioVeterinario = usuarioVeterinario;
    }

    /**
     * Obtiene la fecha agendada de la cita.
     *
     * @return Fecha {@link LocalDate}.
     */
    public LocalDate getFecha(){
        return fecha;
    }

    /**
     * Asigna la fecha de la cita médica.
     *
     * @param fecha Fecha a programar.
     */
    public void setFecha(LocalDate fecha){
        this.fecha = fecha;
    }

    /**
     * Obtiene la hora programada de inicio.
     *
     * @return Hora {@link LocalTime}.
     */
    public LocalTime getEntradaAgendada(){
        return entradaAgendada;
    }

    /**
     * Asigna la hora programada de inicio.
     *
     * @param entradaAgendada Hora de inicio.
     */
    public void setEntradaAgendada(LocalTime entradaAgendada){
        this.entradaAgendada = entradaAgendada;
    }

    /**
     * Obtiene la hora de conclusión de la cita.
     *
     * @return Hora de salida {@link LocalTime}.
     */
    public LocalTime getHoraSalida(){
        return horaSalida;
    }

    /**
     * Asigna la hora de salida de la cita.
     *
     * @param horaSalida Hora de término de la consulta.
     */
    public void setHoraSalida(LocalTime horaSalida){
        this.horaSalida = horaSalida;
    }

    /**
     * Obtiene el código del estado actual de la cita.
     *
     * @return Código de estado entero.
     */
    public int getEstadoCita(){
        return estadoCita;
    }

    /**
     * Asigna el código del estado de la cita.
     *
     * @param estado_cita Código de estado (1=Activa, 3=Cancelada, etc.).
     */
    public void setEstadoCita(int estado_cita){
        this.estadoCita = estado_cita;
    }

    /**
     * Obtiene la lista de servicios contratados para esta cita.
     *
     * @return Lista de {@link ServicioModel}.
     */
    public List<ServicioModel> getListaServicios(){
        return servicios;
    }

    /**
     * Asigna la lista de servicios para la cita médica.
     *
     * @param servicios Lista de servicios a vincular.
     */
    public void setListaServicios(List<ServicioModel> servicios){
        this.servicios = servicios;
    }

    /**
     * Representación textual de la cita médica con sus identificadores clave.
     *
     * @return Cadena formateada de la cita.
     */
    @Override
    public String toString(){
        return "idCita=" + idCita + 
        ", idMascota=" + (mascotaModel != null ? mascotaModel.getIdMascota() : "null") + 
        ", idUsuarioM=" + (usuarioMascota != null ? usuarioMascota.getIdUsuario() : "null") + 
        ", idUsuarioV=" + (usuarioVeterinario != null ? usuarioVeterinario.getIdUsuario() : "null") +
        ", fecha=" + (fecha != null ? fecha.toString() : "null") + 
        ", estado_cita=" + estadoCita;
    }
}
