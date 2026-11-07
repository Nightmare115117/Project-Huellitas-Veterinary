package com.Proyect_DevOps.veterinario.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.Proyect_DevOps.citas.models.CitaModel;
import com.Proyect_DevOps.citas.repositories.CitaRepository;
import com.Proyect_DevOps.consultaMedica.models.ConsultaMedicaModel;
import com.Proyect_DevOps.consultaMedica.models.TratamientoModel;
import com.Proyect_DevOps.consultaMedica.repositories.ConsultaMedicaRepository;
import com.Proyect_DevOps.consultaMedica.repositories.TratamientoRepository;
import com.Proyect_DevOps.users.models.UsuarioModel;
import com.Proyect_DevOps.users.repositories.UsuarioRepository;
import com.Proyect_DevOps.utilities.HMACUtil;
import com.Proyect_DevOps.veterinario.dtos.CitaVeterinarioDTO;
import com.Proyect_DevOps.veterinario.dtos.CrearRecetaDTO;
import com.Proyect_DevOps.veterinario.dtos.RecetaDTO;
import com.Proyect_DevOps.veterinario.dtos.TratamientoItemDTO;

/**
 * Servicio de lógica de negocio para las operaciones exclusivas del rol Veterinario (/veterinario/{correo}/...).
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Service
public class VeterinarioApiService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private ConsultaMedicaRepository consultaMedicaRepository;

    @Autowired
    private TratamientoRepository tratamientoRepository;

    /**
     * Valida que el JWT pertenezca al usuario del path y cuente con rol Veterinario (rol 4).
     *
     * @param jwt Token JWT autenticado.
     * @param pathCorreo Correo especificado en la ruta.
     */
    public void validarVeterinario(Jwt jwt, String pathCorreo) {
        if (jwt == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado");
        }
        String jwtEmail = jwt.getSubject();
        if (jwtEmail == null || !jwtEmail.equalsIgnoreCase(pathCorreo)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El correo de la ruta no coincide con el token JWT");
        }

        Object roleIdClaim = jwt.getClaim("role_id");
        String rolesClaim = jwt.getClaimAsString("roles");
        boolean esVeterinario = false;

        if (roleIdClaim != null) {
            if (roleIdClaim instanceof Number n && n.intValue() == 4) {
                esVeterinario = true;
            } else if ("4".equals(roleIdClaim.toString())) {
                esVeterinario = true;
            }
        }
        if (rolesClaim != null && (rolesClaim.equalsIgnoreCase("Veterinario") || rolesClaim.equalsIgnoreCase("VETERINARIO"))) {
            esVeterinario = true;
        }

        if (!esVeterinario) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado: se requiere rol de Veterinario");
        }
    }

    /**
     * Obtiene el usuario veterinario por su correo electrónico validando su rol 4.
     *
     * @param correo Correo electrónico del veterinario.
     * @return Entidad {@link UsuarioModel}.
     */
    public UsuarioModel obtenerUsuarioVeterinario(String correo) {
        UsuarioModel vet = usuarioRepository.findByCorreoHMAC(HMACUtil.GenerarHuella(correo))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veterinario no encontrado"));

        if (vet.getRol() == null || vet.getRol().getIdRol() != 4) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El usuario no cuenta con rol de Veterinario");
        }
        return vet;
    }

    /**
     * 3.1 Obtiene las citas asignadas al veterinario.
     *
     * @param correo Correo del veterinario.
     * @return Lista de {@link CitaVeterinarioDTO}.
     */
    @Transactional(readOnly = true)
    public List<CitaVeterinarioDTO> getCitas(String correo) {
        UsuarioModel vet = obtenerUsuarioVeterinario(correo);
        List<CitaModel> citas = citaRepository.findByUsuarioVeterinario_IdUsuario(vet.getIdUsuario());

        List<CitaVeterinarioDTO> dtos = new ArrayList<>();
        for (CitaModel c : citas) {
            String fecha = c.getFecha() != null ? c.getFecha().toString() : "";
            String entrada = c.getEntradaAgendada() != null ? c.getEntradaAgendada().toString() : "";
            String mascotaNombre = c.getMascotaModel() != null ? c.getMascotaModel().getNombre() : "";
            String especie = (c.getMascotaModel() != null && c.getMascotaModel().getRaza() != null
                    && c.getMascotaModel().getRaza().getEspecie() != null)
                ? c.getMascotaModel().getRaza().getEspecie().getNombre() : "";
            String raza = (c.getMascotaModel() != null && c.getMascotaModel().getRaza() != null)
                ? c.getMascotaModel().getRaza().getNombre() : "";

            String duenoNombre = "";
            if (c.getUsuarioMascota() != null) {
                duenoNombre = formatearNombre(c.getUsuarioMascota());
            } else if (c.getMascotaModel() != null && c.getMascotaModel().getUsuario() != null) {
                duenoNombre = formatearNombre(c.getMascotaModel().getUsuario());
            }

            boolean tieneReceta = consultaMedicaRepository.existsByCita_IdCita(c.getIdCita());

            dtos.add(new CitaVeterinarioDTO(
                c.getIdCita(),
                fecha,
                entrada,
                c.getEstadoCita(),
                mascotaNombre,
                especie,
                raza,
                duenoNombre,
                tieneReceta
            ));
        }
        return dtos;
    }

    /**
     * 3.2 Obtiene las recetas médicas expedidas en citas del veterinario, ordenadas por más recientes primero.
     *
     * @param correo Correo del veterinario.
     * @return Lista de {@link RecetaDTO}.
     */
    @Transactional(readOnly = true)
    public List<RecetaDTO> getRecetas(String correo) {
        UsuarioModel vet = obtenerUsuarioVeterinario(correo);
        List<ConsultaMedicaModel> recetas = consultaMedicaRepository
            .findByCita_UsuarioVeterinario_IdUsuarioOrderByCita_FechaDescIdConsultaDesc(vet.getIdUsuario());

        List<RecetaDTO> dtos = new ArrayList<>();
        for (ConsultaMedicaModel cm : recetas) {
            int idCita = cm.getCita() != null ? cm.getCita().getIdCita() : 0;
            String fecha = (cm.getCita() != null && cm.getCita().getFecha() != null)
                ? cm.getCita().getFecha().toString() : "";
            String mascotaNombre = "";
            if (cm.getIdMascota() != null && cm.getIdMascota().getNombre() != null) {
                mascotaNombre = cm.getIdMascota().getNombre();
            } else if (cm.getCita() != null && cm.getCita().getMascotaModel() != null) {
                mascotaNombre = cm.getCita().getMascotaModel().getNombre();
            }

            String duenoNombre = "";
            if (cm.getCita() != null && cm.getCita().getUsuarioMascota() != null) {
                duenoNombre = formatearNombre(cm.getCita().getUsuarioMascota());
            } else if (cm.getIdMascota() != null && cm.getIdMascota().getUsuario() != null) {
                duenoNombre = formatearNombre(cm.getIdMascota().getUsuario());
            }

            List<TratamientoItemDTO> items = new ArrayList<>();
            if (cm.getTratamientos() != null) {
                for (TratamientoModel tm : cm.getTratamientos()) {
                    items.add(new TratamientoItemDTO(tm.getMedicamento(), tm.getDescripcion(), tm.getCosto()));
                }
            }

            dtos.add(new RecetaDTO(
                cm.getIdConsulta(),
                idCita,
                fecha,
                mascotaNombre,
                duenoNombre,
                cm.getMotivo(),
                cm.getDiagnostico(),
                cm.getPeso(),
                cm.getRecomendaciones(),
                items
            ));
        }
        return dtos;
    }

    /**
     * 3.3 Registra una nueva consulta médica veterinaria con sus tratamientos en una única transacción.
     *
     * @param correo Correo del veterinario autenticado.
     * @param dto Datos de la consulta y prescripción farmacológica.
     */
    @Transactional
    public void crearReceta(String correo, CrearRecetaDTO dto) {
        UsuarioModel vet = obtenerUsuarioVeterinario(correo);

        if (dto.getIdCita() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Identificador de cita inválido");
        }

        CitaModel cita = citaRepository.findById(dto.getIdCita())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada"));

        if (cita.getUsuarioVeterinario() == null || cita.getUsuarioVeterinario().getIdUsuario() != vet.getIdUsuario()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La cita no pertenece al veterinario autenticado");
        }

        if (cita.getEstadoCita() == 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede recetar una cita cancelada");
        }

        if (consultaMedicaRepository.existsByCita_IdCita(dto.getIdCita())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La cita ya cuenta con una consulta o receta médica registrada");
        }

        if (dto.getPeso() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El peso registrado debe ser mayor a 0");
        }

        if (dto.getMotivo() == null || dto.getMotivo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El motivo de consulta no puede estar vacío");
        }

        if (dto.getDiagnostico() == null || dto.getDiagnostico().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El diagnóstico veterinario no puede estar vacío");
        }

        if (dto.getTratamientos() == null || dto.getTratamientos().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe registrar al menos un tratamiento en la receta");
        }

        for (TratamientoItemDTO t : dto.getTratamientos()) {
            if (t.getMedicamento() == null || t.getMedicamento().isBlank()
                    || t.getDescripcion() == null || t.getDescripcion().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Medicamento y descripción son obligatorios en cada tratamiento");
            }
            if (t.getCosto() < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El costo del tratamiento no puede ser negativo");
            }
        }

        ConsultaMedicaModel consulta = new ConsultaMedicaModel();
        consulta.setCita(cita);
        consulta.setIdMascota(cita.getMascotaModel());
        consulta.setMotivo(dto.getMotivo().trim());
        consulta.setDiagnostico(dto.getDiagnostico().trim());
        consulta.setPeso(dto.getPeso());
        consulta.setRecomendaciones(dto.getRecomendaciones() != null ? dto.getRecomendaciones().trim() : null);

        List<TratamientoModel> tratamientosGuardados = new ArrayList<>();
        for (TratamientoItemDTO t : dto.getTratamientos()) {
            TratamientoModel nuevoTratamiento = new TratamientoModel(
                t.getMedicamento().trim(),
                t.getDescripcion().trim(),
                t.getCosto(),
                0
            );
            TratamientoModel guardado = tratamientoRepository.save(nuevoTratamiento);
            tratamientosGuardados.add(guardado);
        }

        consulta.setTratamientos(tratamientosGuardados);
        consultaMedicaRepository.save(consulta);
    }

    /**
     * Auxiliar para formatear el nombre completo de un usuario.
     */
    public static String formatearNombre(UsuarioModel u) {
        if (u == null) return "";
        StringBuilder sb = new StringBuilder();
        if (u.getNombre() != null && !u.getNombre().isBlank()) {
            sb.append(u.getNombre().trim());
        }
        if (u.getPaterno() != null && !u.getPaterno().isBlank()) {
            sb.append(" ").append(u.getPaterno().trim());
        }
        if (u.getMaterno() != null && !u.getMaterno().isBlank()) {
            sb.append(" ").append(u.getMaterno().trim());
        }
        return sb.toString().trim();
    }
}
