package com.Proyect_DevOps.gestor.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.Proyect_DevOps.citas.models.CitaModel;
import com.Proyect_DevOps.citas.repositories.CitaRepository;
import com.Proyect_DevOps.consultaMedica.models.ConsultaMedicaModel;
import com.Proyect_DevOps.consultaMedica.models.TratamientoModel;
import com.Proyect_DevOps.consultaMedica.repositories.ConsultaMedicaRepository;
import com.Proyect_DevOps.geolocalizacion.models.CalleModel;
import com.Proyect_DevOps.geolocalizacion.models.CiudadModel;
import com.Proyect_DevOps.geolocalizacion.models.ColoniaModel;
import com.Proyect_DevOps.geolocalizacion.models.EstadoModel;
import com.Proyect_DevOps.geolocalizacion.models.PaisModel;
import com.Proyect_DevOps.gestor.dtos.CitaGestorDTO;
import com.Proyect_DevOps.gestor.dtos.CitasPorEstadoDTO;
import com.Proyect_DevOps.gestor.dtos.CitasPorVeterinarioDTO;
import com.Proyect_DevOps.gestor.dtos.GuardarVeterinarioDTO;
import com.Proyect_DevOps.gestor.dtos.MascotaGestorDTO;
import com.Proyect_DevOps.gestor.dtos.ReporteGestorDTO;
import com.Proyect_DevOps.gestor.dtos.ServicioTopDTO;
import com.Proyect_DevOps.gestor.dtos.SucursalGestorDTO;
import com.Proyect_DevOps.gestor.dtos.UsuarioGestorDTO;
import com.Proyect_DevOps.mascotas.models.MascotaModel;
import com.Proyect_DevOps.mascotas.repositories.MascotaRepository;
import com.Proyect_DevOps.servicios.models.ServicioModel;
import com.Proyect_DevOps.sucursales.models.DireccionModel;
import com.Proyect_DevOps.sucursales.models.GestorModel;
import com.Proyect_DevOps.sucursales.models.SucursalModel;
import com.Proyect_DevOps.sucursales.repositories.GestorRepository;
import com.Proyect_DevOps.sucursales.repositories.SucursalRepository;
import com.Proyect_DevOps.users.models.RolModel;
import com.Proyect_DevOps.users.models.UsuarioModel;
import com.Proyect_DevOps.users.repositories.RolRepository;
import com.Proyect_DevOps.users.repositories.UsuarioRepository;
import com.Proyect_DevOps.utilities.AESUtil;
import com.Proyect_DevOps.utilities.HMACUtil;

/**
 * Servicio de lógica de negocio para las operaciones exclusivas del rol Gestor de sucursal.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Service
public class GestorApiService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private GestorRepository gestorRepository;

    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private ConsultaMedicaRepository consultaMedicaRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private Argon2PasswordEncoder argon2PasswordEncoder;

    /**
     * Valida que el JWT pertenezca al usuario del path y cuente con rol Gestor (rol 3).
     *
     * @param jwt Token JWT autenticado.
     * @param pathCorreo Correo especificado en la ruta.
     */
    public void validarGestor(Jwt jwt, String pathCorreo) {
        if (jwt == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado");
        }
        String jwtEmail = jwt.getSubject();
        if (jwtEmail == null || !jwtEmail.equalsIgnoreCase(pathCorreo)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El correo de la ruta no coincide con el token JWT");
        }

        Object roleIdClaim = jwt.getClaim("role_id");
        String rolesClaim = jwt.getClaimAsString("roles");
        boolean esGestor = false;

        if (roleIdClaim != null) {
            if (roleIdClaim instanceof Number n && n.intValue() == 3) {
                esGestor = true;
            } else if ("3".equals(roleIdClaim.toString())) {
                esGestor = true;
            }
        }
        if (rolesClaim != null && (rolesClaim.equalsIgnoreCase("Gestor") || rolesClaim.equalsIgnoreCase("GESTOR"))) {
            esGestor = true;
        }

        if (!esGestor) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado: se requiere rol de Gestor");
        }
    }

    /**
     * Resuelve la sucursal asignada al gestor mediante la cadena:
     * usuarios.correo -> gestores.id_usuario -> sucursales.id_gestor.
     *
     * @param correo Correo electrónico del gestor.
     * @return {@link SucursalModel} administrada por el gestor.
     */
    public SucursalModel obtenerSucursalDeGestor(String correo) {
        UsuarioModel usuario = usuarioRepository.findByCorreoHMAC(HMACUtil.GenerarHuella(correo))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gestor no encontrado"));

        GestorModel gestor = gestorRepository.findByUsuario(usuario)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario no está registrado como gestor"));

        return sucursalRepository.findByGestor(gestor)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El gestor no tiene sucursal asignada"));
    }

    /**
     * 2.1 Obtiene la dirección completa y RFC de la sucursal del gestor.
     *
     * @param correo Correo del gestor.
     * @return {@link SucursalGestorDTO}.
     */
    @Transactional(readOnly = true)
    public SucursalGestorDTO getSucursal(String correo) {
        SucursalModel sucursal = obtenerSucursalDeGestor(correo);
        DireccionModel dir = sucursal.getDireccion();

        String calle = "";
        String colonia = "";
        String ciudad = "";
        String estado = "";
        String pais = "";
        int codigoPostal = 0;

        if (dir != null) {
            codigoPostal = dir.getCodigoPostal();
            CalleModel c = dir.getCalle();
            if (c != null) {
                calle = c.getNombre() != null ? c.getNombre() : "";
                ColoniaModel col = c.getColonia();
                if (col != null) {
                    colonia = col.getNombre() != null ? col.getNombre() : "";
                    CiudadModel ciu = col.getCiudad();
                    if (ciu != null) {
                        ciudad = ciu.getNombre() != null ? ciu.getNombre() : "";
                        EstadoModel est = ciu.getIdEstado();
                        if (est != null) {
                            estado = est.getNombre() != null ? est.getNombre() : "";
                            PaisModel p = est.getPais();
                            if (p != null) {
                                pais = p.getNombre() != null ? p.getNombre() : "";
                            }
                        }
                    }
                }
            }
        }

        String rfcGestor = (sucursal.getGestor() != null && sucursal.getGestor().getRfc() != null)
            ? sucursal.getGestor().getRfc() : "";

        return new SucursalGestorDTO(
            sucursal.getIdSucursal(),
            calle,
            colonia,
            ciudad,
            estado,
            pais,
            codigoPostal,
            rfcGestor
        );
    }

    /**
     * 2.2 Obtiene las citas de mascotas adscritas a la sucursal del gestor.
     *
     * @param correo Correo del gestor.
     * @return Lista de {@link CitaGestorDTO}.
     */
    @Transactional(readOnly = true)
    public List<CitaGestorDTO> getCitas(String correo) {
        SucursalModel sucursal = obtenerSucursalDeGestor(correo);
        List<CitaModel> citas = citaRepository.findByMascotaModel_Sucursal_IdSucursal(sucursal.getIdSucursal());

        List<CitaGestorDTO> dtos = new ArrayList<>();
        for (CitaModel c : citas) {
            String fecha = c.getFecha() != null ? c.getFecha().toString() : "";
            String entrada = c.getEntradaAgendada() != null ? c.getEntradaAgendada().toString() : "";
            String mascotaNombre = c.getMascotaModel() != null ? c.getMascotaModel().getNombre() : "";
            String duenoNombre = "";
            if (c.getUsuarioMascota() != null) {
                duenoNombre = formatearNombre(c.getUsuarioMascota());
            } else if (c.getMascotaModel() != null && c.getMascotaModel().getUsuario() != null) {
                duenoNombre = formatearNombre(c.getMascotaModel().getUsuario());
            }

            Integer idVet = c.getUsuarioVeterinario() != null ? c.getUsuarioVeterinario().getIdUsuario() : null;
            String vetNombre = c.getUsuarioVeterinario() != null ? formatearNombre(c.getUsuarioVeterinario()) : null;

            dtos.add(new CitaGestorDTO(
                c.getIdCita(),
                fecha,
                entrada,
                c.getEstadoCita(),
                mascotaNombre,
                duenoNombre,
                idVet,
                vetNombre
            ));
        }
        return dtos;
    }

    /**
     * 2.2b Actualización parcial de una cita (asignar/desasignar veterinario, cambiar estado).
     *
     * @param correo Correo del gestor.
     * @param idCita Identificador de la cita.
     * @param cambios Mapa con los campos a actualizar (idUsuarioV, estadoCita).
     */
    @Transactional
    public void actualizarCita(String correo, int idCita, Map<String, Object> cambios) {
        SucursalModel sucursal = obtenerSucursalDeGestor(correo);
        CitaModel cita = citaRepository.findById(idCita)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada"));

        if (cita.getMascotaModel() == null || cita.getMascotaModel().getSucursal() == null
                || cita.getMascotaModel().getSucursal().getIdSucursal() != sucursal.getIdSucursal()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La cita no pertenece a la sucursal del gestor");
        }

        if (cambios.containsKey("idUsuarioV")) {
            Object idVetObj = cambios.get("idUsuarioV");
            if (idVetObj == null) {
                cita.setUsuarioVeterinario(null);
            } else {
                int idVet = ((Number) idVetObj).intValue();
                UsuarioModel vet = usuarioRepository.findById(idVet)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Veterinario no encontrado"));

                if (vet.getRol() == null || vet.getRol().getIdRol() != 4) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario asignado debe tener rol de Veterinario");
                }
                if (vet.getSucursal() == null || vet.getSucursal().getIdSucursal() != sucursal.getIdSucursal()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El veterinario no pertenece a la sucursal del gestor");
                }
                cita.setUsuarioVeterinario(vet);
            }
        }

        if (cambios.containsKey("estadoCita")) {
            int nuevoEstado = ((Number) cambios.get("estadoCita")).intValue();
            if (nuevoEstado == 3) {
                if (consultaMedicaRepository.existsByCita_IdCita(idCita)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede cancelar una cita que ya tiene consulta o receta médica");
                }
            }
            cita.setEstadoCita(nuevoEstado);
        }

        citaRepository.save(cita);
    }

    /**
     * 2.3 Obtiene los veterinarios (rol 4) adscritos a la sucursal del gestor.
     *
     * @param correo Correo del gestor.
     * @return Lista de {@link UsuarioGestorDTO}.
     */
    @Transactional(readOnly = true)
    public List<UsuarioGestorDTO> getVeterinarios(String correo) {
        SucursalModel sucursal = obtenerSucursalDeGestor(correo);
        List<UsuarioModel> vets = usuarioRepository.findByRol_IdRolAndSucursal_IdSucursal(4, sucursal.getIdSucursal());

        List<UsuarioGestorDTO> dtos = new ArrayList<>();
        for (UsuarioModel v : vets) {
            String email = v.getCorreo();
            try {
                email = AESUtil.desencriptar(email);
            } catch (Exception e) {
                // omit
            }
            dtos.add(new UsuarioGestorDTO(v.getIdUsuario(), v.getNombre(), v.getPaterno(), v.getMaterno(), email));
        }
        return dtos;
    }

    /**
     * 2.3b Registra un nuevo veterinario adscrito a la sucursal del gestor con contraseña cifrada en Argon2id.
     *
     * @param correo Correo del gestor.
     * @param dto Datos del nuevo veterinario.
     * @return {@link UsuarioGestorDTO} recién creado.
     */
    @Transactional
    public UsuarioGestorDTO crearVeterinario(String correo, GuardarVeterinarioDTO dto) {
        SucursalModel sucursal = obtenerSucursalDeGestor(correo);

        if (dto.getNombre() == null || dto.getNombre().isBlank()
                || dto.getCorreo() == null || dto.getCorreo().isBlank()
                || dto.getContrasena() == null || dto.getContrasena().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nombre, correo y contraseña son campos requeridos");
        }

        String hmac = HMACUtil.GenerarHuella(dto.getCorreo());
        if (usuarioRepository.existsByCorreoHMAC(hmac)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo electrónico ya se encuentra registrado");
        }

        RolModel rolVet = rolRepository.findById(4).orElseGet(() -> {
            RolModel nuevo = new RolModel("Veterinario");
            nuevo.setIdRol(4);
            return rolRepository.save(nuevo);
        });

        UsuarioModel usuario = new UsuarioModel();
        usuario.setNombre(dto.getNombre().trim());
        usuario.setPaterno(dto.getPaterno() != null ? dto.getPaterno().trim() : null);
        usuario.setMaterno(dto.getMaterno() != null ? dto.getMaterno().trim() : null);
        usuario.setCorreo(AESUtil.encriptar(dto.getCorreo().trim()));
        usuario.setCorreoHMAC(hmac);
        usuario.setContraseña(argon2PasswordEncoder.encode(dto.getContrasena()));
        usuario.setRol(rolVet);
        usuario.setSucursal(sucursal);
        usuario.setMFANeeded(false);

        UsuarioModel guardado = usuarioRepository.save(usuario);
        return new UsuarioGestorDTO(guardado.getIdUsuario(), guardado.getNombre(), guardado.getPaterno(), guardado.getMaterno(), dto.getCorreo().trim());
    }

    /**
     * 2.3c Actualiza la información de un veterinario de la sucursal.
     *
     * @param correo Correo del gestor.
     * @param idUsuario Identificador del veterinario a actualizar.
     * @param dto Nuevos datos del veterinario.
     */
    @Transactional
    public void actualizarVeterinario(String correo, int idUsuario, GuardarVeterinarioDTO dto) {
        SucursalModel sucursal = obtenerSucursalDeGestor(correo);
        UsuarioModel vet = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veterinario no encontrado"));

        if (vet.getRol() == null || vet.getRol().getIdRol() != 4
                || vet.getSucursal() == null || vet.getSucursal().getIdSucursal() != sucursal.getIdSucursal()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El veterinario no pertenece a la sucursal del gestor");
        }

        if (dto.getCorreo() != null && !dto.getCorreo().isBlank()) {
            String nuevoHmac = HMACUtil.GenerarHuella(dto.getCorreo().trim());
            if (!nuevoHmac.equals(vet.getCorreoHMAC())) {
                if (usuarioRepository.existsByCorreoHMAC(nuevoHmac)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "El nuevo correo ya se encuentra en uso");
                }
                vet.setCorreoHMAC(nuevoHmac);
                vet.setCorreo(AESUtil.encriptar(dto.getCorreo().trim()));
            }
        }

        if (dto.getNombre() != null && !dto.getNombre().isBlank()) {
            vet.setNombre(dto.getNombre().trim());
        }
        if (dto.getPaterno() != null) {
            vet.setPaterno(dto.getPaterno().trim());
        }
        if (dto.getMaterno() != null) {
            vet.setMaterno(dto.getMaterno().trim());
        }

        if (dto.getContrasena() != null && !dto.getContrasena().isBlank()) {
            vet.setContraseña(argon2PasswordEncoder.encode(dto.getContrasena()));
        }

        usuarioRepository.save(vet);
    }

    /**
     * 2.3d Elimina un veterinario de la sucursal siempre que no posea citas ni recetas asociadas.
     *
     * @param correo Correo del gestor.
     * @param idUsuario Identificador del veterinario a eliminar.
     */
    @Transactional
    public void eliminarVeterinario(String correo, int idUsuario) {
        SucursalModel sucursal = obtenerSucursalDeGestor(correo);
        UsuarioModel vet = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veterinario no encontrado"));

        if (vet.getRol() == null || vet.getRol().getIdRol() != 4
                || vet.getSucursal() == null || vet.getSucursal().getIdSucursal() != sucursal.getIdSucursal()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El veterinario no pertenece a la sucursal del gestor");
        }

        boolean tieneCitas = citaRepository.existsByUsuarioVeterinario_IdUsuario(idUsuario);
        boolean tieneConsultas = consultaMedicaRepository.existsByCita_UsuarioVeterinario_IdUsuario(idUsuario);

        if (tieneCitas || tieneConsultas) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El veterinario puede tener citas o recetas relacionadas");
        }

        usuarioRepository.delete(vet);
    }

    /**
     * 2.4 Obtiene los dueños que tienen al menos una mascota adscrita a la sucursal del gestor.
     *
     * @param correo Correo del gestor.
     * @return Lista de {@link UsuarioGestorDTO}.
     */
    @Transactional(readOnly = true)
    public List<UsuarioGestorDTO> getDuenos(String correo) {
        SucursalModel sucursal = obtenerSucursalDeGestor(correo);
        List<MascotaModel> mascotas = mascotaRepository.findBySucursal_IdSucursal(sucursal.getIdSucursal());

        Map<Integer, UsuarioGestorDTO> duenosMap = new LinkedHashMap<>();
        for (MascotaModel m : mascotas) {
            UsuarioModel u = m.getUsuario();
            if (u != null && !duenosMap.containsKey(u.getIdUsuario())) {
                String email = u.getCorreo();
                try {
                    email = AESUtil.desencriptar(email);
                } catch (Exception e) {
                    // ignore
                }
                duenosMap.put(u.getIdUsuario(), new UsuarioGestorDTO(
                    u.getIdUsuario(),
                    u.getNombre(),
                    u.getPaterno(),
                    u.getMaterno(),
                    email
                ));
            }
        }
        return new ArrayList<>(duenosMap.values());
    }

    /**
     * 2.5 Obtiene las mascotas adscritas a la sucursal del gestor.
     *
     * @param correo Correo del gestor.
     * @return Lista de {@link MascotaGestorDTO}.
     */
    @Transactional(readOnly = true)
    public List<MascotaGestorDTO> getMascotas(String correo) {
        SucursalModel sucursal = obtenerSucursalDeGestor(correo);
        List<MascotaModel> mascotas = mascotaRepository.findBySucursal_IdSucursal(sucursal.getIdSucursal());

        List<MascotaGestorDTO> dtos = new ArrayList<>();
        for (MascotaModel m : mascotas) {
            String especie = (m.getRaza() != null && m.getRaza().getEspecie() != null && m.getRaza().getEspecie().getNombre() != null)
                ? m.getRaza().getEspecie().getNombre() : "";
            String raza = (m.getRaza() != null && m.getRaza().getNombre() != null) ? m.getRaza().getNombre() : "";
            String fechaNac = m.getFechaNacimiento() != null ? m.getFechaNacimiento().toString() : "";
            String dueno = m.getUsuario() != null ? formatearNombre(m.getUsuario()) : "";

            dtos.add(new MascotaGestorDTO(
                m.getIdMascota(),
                m.getNombre(),
                especie,
                raza,
                fechaNac,
                dueno
            ));
        }
        return dtos;
    }

    /**
     * 2.6 Genera el reporte operativo y financiero de la sucursal para un rango de fechas inclusivo.
     *
     * @param correo Correo del gestor.
     * @param desde Fecha inicial inclusiva.
     * @param hasta Fecha final inclusiva.
     * @return {@link ReporteGestorDTO}.
     */
    @Transactional(readOnly = true)
    public ReporteGestorDTO getReportes(String correo, LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las fechas 'desde' y 'hasta' son obligatorias");
        }
        if (desde.isAfter(hasta)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha 'desde' no puede ser posterior a 'hasta'");
        }

        SucursalModel sucursal = obtenerSucursalDeGestor(correo);
        List<CitaModel> citas = citaRepository.findByMascotaModel_Sucursal_IdSucursalAndFechaBetween(
            sucursal.getIdSucursal(), desde, hasta
        );

        int totalCitas = citas.size();

        Map<Integer, Long> countPorEstado = citas.stream()
            .collect(Collectors.groupingBy(CitaModel::getEstadoCita, Collectors.counting()));

        List<CitasPorEstadoDTO> citasPorEstado = countPorEstado.entrySet().stream()
            .map(e -> new CitasPorEstadoDTO(e.getKey(), e.getValue()))
            .sorted((a, b) -> Integer.compare(a.getEstado(), b.getEstado()))
            .toList();

        Map<String, Long> countPorVet = citas.stream()
            .collect(Collectors.groupingBy(c -> {
                if (c.getUsuarioVeterinario() != null) {
                    return formatearNombre(c.getUsuarioVeterinario());
                }
                return "Sin asignar";
            }, Collectors.counting()));

        List<CitasPorVeterinarioDTO> citasPorVeterinario = countPorVet.entrySet().stream()
            .map(e -> new CitasPorVeterinarioDTO(e.getKey(), e.getValue()))
            .sorted((a, b) -> Long.compare(b.getTotal(), a.getTotal()))
            .toList();

        List<CitaModel> citasValidas = citas.stream()
            .filter(c -> c.getEstadoCita() != 3)
            .toList();

        double ingresosServicios = 0.0;
        Map<String, Long> serviciosContador = new HashMap<>();
        Map<String, Double> serviciosIngreso = new HashMap<>();

        for (CitaModel c : citasValidas) {
            if (c.servicios != null) {
                for (ServicioModel s : c.servicios) {
                    ingresosServicios += s.getCosto();
                    String nombreServ = s.getNombre() != null ? s.getNombre() : "Servicio";
                    serviciosContador.put(nombreServ, serviciosContador.getOrDefault(nombreServ, 0L) + 1);
                    serviciosIngreso.put(nombreServ, serviciosIngreso.getOrDefault(nombreServ, 0.0) + s.getCosto());
                }
            }
        }

        double ingresosTratamientos = 0.0;
        for (CitaModel c : citasValidas) {
            Optional<ConsultaMedicaModel> consultaOpt = consultaMedicaRepository.findByCita_IdCita(c.getIdCita());
            if (consultaOpt.isPresent()) {
                ConsultaMedicaModel cm = consultaOpt.get();
                if (cm.getTratamientos() != null) {
                    for (TratamientoModel t : cm.getTratamientos()) {
                        ingresosTratamientos += t.getCosto();
                    }
                }
            }
        }

        List<ServicioTopDTO> serviciosTop = serviciosContador.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(10)
            .map(e -> new ServicioTopDTO(e.getKey(), e.getValue(), serviciosIngreso.getOrDefault(e.getKey(), 0.0)))
            .toList();

        return new ReporteGestorDTO(
            totalCitas,
            citasPorEstado,
            citasPorVeterinario,
            ingresosServicios,
            ingresosTratamientos,
            serviciosTop
        );
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
