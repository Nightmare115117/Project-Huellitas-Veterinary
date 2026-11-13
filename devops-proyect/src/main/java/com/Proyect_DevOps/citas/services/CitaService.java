package com.Proyect_DevOps.citas.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.Proyect_DevOps.citas.dtos.CitaDTO;
import com.Proyect_DevOps.citas.models.CitaModel;
import com.Proyect_DevOps.citas.repositories.CitaRepository;
import com.Proyect_DevOps.mascotas.models.MascotaModel;
import com.Proyect_DevOps.mascotas.repositories.MascotaRepository;
import com.Proyect_DevOps.users.models.UsuarioModel;
import com.Proyect_DevOps.users.repositories.UsuarioRepository;
import com.Proyect_DevOps.common.services.GenericServiceImpl;
import com.Proyect_DevOps.utilities.HMACUtil;

/**
 * Servicio de negocio para la gestión operativa de citas veterinarias.
 * Maneja el agendamiento, conteo de citas activas por usuario, consulta por mascota
 * y cancelación de citas agendadas.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Service
public class CitaService extends GenericServiceImpl<CitaModel, Integer, CitaRepository> {

    /**
     * Repositorio de usuarios para resolución mediante huella criptográfica HMAC.
     */
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Repositorio de mascotas para validar y asociar la mascota a la cita.
     */
    @Autowired
    private MascotaRepository mascotaRepository;

    /**
     * Convierte una entidad {@link CitaModel} a su representación {@link CitaDTO}.
     *
     * @param citaM Entidad de la cita a convertir.
     * @return Objeto DTO desacoplado con los datos principales de la cita.
     */
    private CitaDTO convertirADTO(CitaModel citaM) {
        String nombreVeterinario =
            citaM.getUsuarioVeterinario() != null
                ? citaM.getUsuarioVeterinario().getNombre() + " " + citaM.getUsuarioVeterinario().getPaterno()
                : "Pendiente de asignación";
        return new CitaDTO(
            citaM.getIdCita(),
            nombreVeterinario,
            citaM.getUsuarioMascota() != null ? citaM.getUsuarioMascota().getCorreo() : null,
            citaM.getMascotaModel() != null ? citaM.getMascotaModel().getIdMascota() : 0,
            citaM.getFecha(),
            citaM.getEntradaAgendada(),
            citaM.getHoraSalida(),
            citaM.getEstadoCita());
    }

    /**
     * Cuenta el número de citas activas (estado 1) que tiene agendadas un usuario según su correo.
     *
     * @param correo Correo electrónico del usuario.
     * @return Cantidad de citas activas del usuario.
     * @throws ResponseStatusException Si el usuario no fue encontrado (404).
     */
    public long countByUsuarioCitas (String correo){
        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByCorreoHMAC(HMACUtil.GenerarHuella(correo));
        if (usuarioOpt.isPresent()) {
            UsuarioModel usuario = usuarioOpt.get();
            return repository.countByUsuarioMascotaAndEstadoCita(usuario, 1);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
    }

    /**
     * Obtiene el listado completo de citas de un usuario en formato DTO.
     *
     * @param correo Correo electrónico del usuario.
     * @return Lista de {@link CitaDTO} con las citas del usuario.
     * @throws ResponseStatusException Si el usuario no existe.
     */
    public List<CitaDTO> getCitasByUser (String correo) {
        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByCorreoHMAC(HMACUtil.GenerarHuella(correo));
        if (usuarioOpt.isPresent()) {
            UsuarioModel usuario = usuarioOpt.get();
            List<CitaDTO> listaDTO = new ArrayList<>();
            for (CitaModel elemento : repository.findByUsuarioMascota(usuario)) {
                listaDTO.add(convertirADTO(elemento));
            }
            return listaDTO;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
    }

    /**
     * Obtiene las citas de un usuario para una mascota específica.
     *
     * @param correo Correo electrónico del usuario dueño.
     * @param idMascota Identificador de la mascota.
     * @return Lista de {@link CitaDTO} asociadas a esa mascota y usuario.
     * @throws ResponseStatusException Si el usuario o la mascota no son encontrados.
     */
    public List<CitaDTO> getCitasByUserAndPet (String correo, int idMascota) {
        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByCorreoHMAC(HMACUtil.GenerarHuella(correo));
        List<CitaDTO> listaDTO = new ArrayList<>();
        UsuarioModel usuario;
        if (usuarioOpt.isPresent()) {
            usuario = usuarioOpt.get();
            Optional<MascotaModel> mascotaOpt = mascotaRepository.findById(idMascota);
            if (mascotaOpt.isPresent()) {
                MascotaModel mascota = mascotaOpt.get();
                for (CitaModel elemento : repository.findByUsuarioMascotaAndMascotaModel(usuario, mascota)) {
                    listaDTO.add(convertirADTO(elemento));
                }
                return listaDTO;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mascota no encontrada");
            }    
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
    }

    /**
     * Crea y persiste una nueva cita médica veterinaria a partir de los datos recibidos en el DTO.
     *
     * @param citaDTO Datos de la cita a registrar.
     * @return {@link CitaDTO} creado con su identificador persistido.
     * @throws ResponseStatusException Si el usuario o la mascota no existen en el sistema.
     */
    public CitaDTO createCita(CitaDTO citaDTO){
        if (citaDTO.correoCliente() == null || citaDTO.correoCliente().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Correo del cliente requerido");
        }
        if (citaDTO.fecha() == null || citaDTO.entradaAgendada() == null
                || !LocalDateTime.of(citaDTO.fecha(), citaDTO.entradaAgendada()).isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cita debe tener una fecha y hora futuras");
        }
        if (citaDTO.estadoCita() != 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Una cita nueva debe iniciar en estado programado");
        }
        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByCorreoHMAC(HMACUtil.GenerarHuella(citaDTO.correoCliente()));
        if (usuarioOpt.isPresent()) {
            UsuarioModel usuario = usuarioOpt.get();
            Optional<MascotaModel> mascotaOpt = mascotaRepository.findById(citaDTO.idMascota());
            if (mascotaOpt.isPresent()) {
                MascotaModel mascota = mascotaOpt.get();
                if (mascota.getUsuario() == null
                        || mascota.getUsuario().getIdUsuario() != usuario.getIdUsuario()) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La mascota no pertenece al cliente");
                }
                CitaModel cita = new CitaModel(mascota, usuario, citaDTO.fecha(), citaDTO.entradaAgendada(), citaDTO.estadoCita());
                repository.save(cita);
                return convertirADTO(cita);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mascota no encontrada");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
    }

    /**
     * Cancela una cita médica si pertenece al solicitante o si este tiene acceso privilegiado.
     *
     * @param idCita Identificador de la cita médica a cancelar.
     * @return {@code true} si la cita fue cancelada exitosamente.
     * @param correoSolicitante Correo del usuario que solicita la cancelación.
     * @param accesoPrivilegiado {@code true} si el usuario puede administrar citas ajenas.
     * @throws ResponseStatusException Si la cita no existe o el usuario no tiene acceso.
     */
    public boolean cancelarCita(int idCita, String correoSolicitante, boolean accesoPrivilegiado) {
        CitaModel cita = repository.findById(idCita)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada"));

        if (!accesoPrivilegiado) {
            UsuarioModel solicitante = usuarioRepository
                .findByCorreoHMAC(HMACUtil.GenerarHuella(correoSolicitante))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
            if (cita.getUsuarioMascota() == null
                    || cita.getUsuarioMascota().getIdUsuario() != solicitante.getIdUsuario()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No autorizado para cancelar esta cita");
            }
        }

        cita.setEstadoCita(3);
        repository.save(cita);
        return true;
    }
}
