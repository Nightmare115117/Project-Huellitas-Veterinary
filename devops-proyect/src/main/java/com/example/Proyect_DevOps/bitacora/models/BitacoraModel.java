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

/**
 * Entidad JPA que representa una entrada en la bitácora de auditoría del sistema.
 * Registra detalles sobre la identidad del usuario, rol, acción realizada, módulo afectado,
 * mensaje informativo, dirección IP de origen y marca de tiempo.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Entity
@Table(name = "bitacora")
public class BitacoraModel {

    /**
     * Identificador único autoincremental de la entrada en la bitácora.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idBitacora;

    /**
     * Fecha y hora exacta en la que se ejecutó el evento de auditoría.
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime fechaHora;

    /**
     * Nombre de usuario o correo electrónico de quien realizó la acción, o 'Anónimo'.
     */
    @Column(nullable = false)
    private String usuario;

    /**
     * Rol con el cual se ejecutó la acción (ej. Administrador, Veterinario, Cliente).
     */
    private String rol;

    /**
     * Código de acción efectuada (ej. LOGIN_EXITOSO, CREAR_CITA, ACTUALIZAR_USUARIO).
     */
    @Column(nullable = false)
    private String accion;

    /**
     * Módulo del sistema en el que se originó la acción (ej. Citas, Mascotas, Autenticación).
     */
    private String modulo;

    /**
     * Información descriptiva o técnica contextual sobre la operación o posibles errores.
     */
    @Column(length = 1000)
    private String detalle;

    /**
     * Dirección IP remota desde donde se emitió la petición HTTP.
     */
    private String ip;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public BitacoraModel() {
    }

    /**
     * Constructor completo para registrar un nuevo evento en la bitácora.
     *
     * @param usuario Nombre o identificador del usuario actor.
     * @param rol Rol del usuario al momento de la acción.
     * @param accion Código representativo de la acción.
     * @param modulo Nombre del módulo funcional afectado.
     * @param detalle Descripción adicional o contexto del resultado.
     * @param ip Dirección IP de la solicitud.
     */
    public BitacoraModel(String usuario, String rol, String accion, String modulo, String detalle, String ip) {
        this.fechaHora = LocalDateTime.now();
        this.usuario = usuario;
        this.rol = rol;
        this.accion = accion;
        this.modulo = modulo;
        this.detalle = detalle;
        this.ip = ip;
    }

    /**
     * Método de ciclo de vida JPA invocado antes de insertar la entidad.
     * Garantiza que la marca de tiempo se establezca en caso de venir nula.
     */
    @PrePersist
    public void prePersist() {
        if (this.fechaHora == null) {
            this.fechaHora = LocalDateTime.now();
        }
    }

    /**
     * Obtiene el identificador de la bitácora.
     *
     * @return Identificador numérico único.
     */
    public int getIdBitacora() {
        return idBitacora;
    }

    /**
     * Establece el identificador de la bitácora.
     *
     * @param idBitacora Identificador numérico único.
     */
    public void setIdBitacora(int idBitacora) {
        this.idBitacora = idBitacora;
    }

    /**
     * Obtiene la fecha y hora del evento.
     *
     * @return Marca temporal {@link LocalDateTime}.
     */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /**
     * Establece la fecha y hora del evento.
     *
     * @param fechaHora Marca temporal a asignar.
     */
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    /**
     * Obtiene el identificador o correo del usuario registrado.
     *
     * @return Nombre o correo del usuario.
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Establece el usuario responsable de la acción.
     *
     * @param usuario Nombre o correo a asignar.
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene el rol del usuario.
     *
     * @return Rol asignado.
     */
    public String getRol() {
        return rol;
    }

    /**
     * Establece el rol del usuario.
     *
     * @param rol Rol a asignar.
     */
    public void setRol(String rol) {
        this.rol = rol;
    }

    /**
     * Obtiene el código o descripción de la acción realizada.
     *
     * @return Código de la acción.
     */
    public String getAccion() {
        return accion;
    }

    /**
     * Establece la acción realizada.
     *
     * @param accion Código de acción a asignar.
     */
    public void setAccion(String accion) {
        this.accion = accion;
    }

    /**
     * Obtiene el nombre del módulo afectado.
     *
     * @return Módulo funcional.
     */
    public String getModulo() {
        return modulo;
    }

    /**
     * Establece el módulo afectado.
     *
     * @param modulo Nombre del módulo a asignar.
     */
    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    /**
     * Obtiene el detalle o resumen de la operación.
     *
     * @return Detalle explicativo del evento.
     */
    public String getDetalle() {
        return detalle;
    }

    /**
     * Establece el detalle o resumen explicativo de la operación.
     *
     * @param detalle Texto descriptivo a asignar.
     */
    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    /**
     * Obtiene la dirección IP desde donde se emitió la petición.
     *
     * @return Dirección IP del cliente.
     */
    public String getIp() {
        return ip;
    }

    /**
     * Establece la dirección IP remota de la solicitud.
     *
     * @param ip Dirección IP a asignar.
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * Genera una representación en cadena con los atributos de la bitácora.
     *
     * @return Cadena formateada con la información del registro.
     */
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
