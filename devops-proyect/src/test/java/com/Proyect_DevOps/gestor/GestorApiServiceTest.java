package com.Proyect_DevOps.gestor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;

import com.Proyect_DevOps.citas.models.CitaModel;
import com.Proyect_DevOps.citas.repositories.CitaRepository;
import com.Proyect_DevOps.consultaMedica.repositories.ConsultaMedicaRepository;
import com.Proyect_DevOps.geolocalizacion.models.CalleModel;
import com.Proyect_DevOps.geolocalizacion.models.CiudadModel;
import com.Proyect_DevOps.geolocalizacion.models.ColoniaModel;
import com.Proyect_DevOps.geolocalizacion.models.EstadoModel;
import com.Proyect_DevOps.geolocalizacion.models.PaisModel;
import com.Proyect_DevOps.gestor.dtos.CitaGestorDTO;
import com.Proyect_DevOps.gestor.dtos.GuardarVeterinarioDTO;
import com.Proyect_DevOps.gestor.dtos.ReporteGestorDTO;
import com.Proyect_DevOps.gestor.dtos.SucursalGestorDTO;
import com.Proyect_DevOps.gestor.dtos.UsuarioGestorDTO;
import com.Proyect_DevOps.gestor.services.GestorApiService;
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

public class GestorApiServiceTest {

    private GestorApiService service;
    private UsuarioRepository usuarioRepository;
    private GestorRepository gestorRepository;
    private SucursalRepository sucursalRepository;
    private CitaRepository citaRepository;
    private ConsultaMedicaRepository consultaMedicaRepository;
    private MascotaRepository mascotaRepository;
    private RolRepository rolRepository;
    private Argon2PasswordEncoder argon2PasswordEncoder;

    @BeforeEach
    void setUp() throws Exception {
        usuarioRepository = mock(UsuarioRepository.class);
        gestorRepository = mock(GestorRepository.class);
        sucursalRepository = mock(SucursalRepository.class);
        citaRepository = mock(CitaRepository.class);
        consultaMedicaRepository = mock(ConsultaMedicaRepository.class);
        mascotaRepository = mock(MascotaRepository.class);
        rolRepository = mock(RolRepository.class);
        argon2PasswordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

        service = new GestorApiService();

        setField(service, "usuarioRepository", usuarioRepository);
        setField(service, "gestorRepository", gestorRepository);
        setField(service, "sucursalRepository", sucursalRepository);
        setField(service, "citaRepository", citaRepository);
        setField(service, "consultaMedicaRepository", consultaMedicaRepository);
        setField(service, "mascotaRepository", mascotaRepository);
        setField(service, "rolRepository", rolRepository);
        setField(service, "argon2PasswordEncoder", argon2PasswordEncoder);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testValidarGestorRechazaEmailMismatch() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn("otro@huellitas.mx");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
            service.validarGestor(jwt, "gestor@huellitas.mx")
        );
        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
    }

    @Test
    void testValidarGestorRechazaRolNoGestor() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn("gestor@huellitas.mx");
        when(jwt.getClaim("role_id")).thenReturn(1);
        when(jwt.getClaimAsString("roles")).thenReturn("Admin");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
            service.validarGestor(jwt, "gestor@huellitas.mx")
        );
        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
    }

    @Test
    void testGetSucursalExitoso() {
        String correo = "gestor@huellitas.mx";
        UsuarioModel user = new UsuarioModel();
        user.setIdUsuario(1);

        GestorModel gestor = new GestorModel("PEEF900101ABC", 20000.0, user);
        gestor.setIdGestor(1);

        PaisModel pais = new PaisModel("México", "MEX");
        EstadoModel estado = new EstadoModel("Coahuila", pais);
        CiudadModel ciudad = new CiudadModel("Saltillo", estado);
        ColoniaModel colonia = new ColoniaModel("Centro", ciudad);
        CalleModel calle = new CalleModel("Av. Juárez", colonia);
        DireccionModel dir = new DireccionModel(calle, 25000);
        SucursalModel sucursal = new SucursalModel(dir, gestor);
        sucursal.setIdSucursal(1);

        when(usuarioRepository.findByCorreoHMAC(any())).thenReturn(Optional.of(user));
        when(gestorRepository.findByUsuario(user)).thenReturn(Optional.of(gestor));
        when(sucursalRepository.findByGestor(gestor)).thenReturn(Optional.of(sucursal));

        SucursalGestorDTO dto = service.getSucursal(correo);
        assertNotNull(dto);
        assertEquals(1, dto.getIdSucursal());
        assertEquals("Av. Juárez", dto.getCalle());
        assertEquals("Centro", dto.getColonia());
        assertEquals("Saltillo", dto.getCiudad());
        assertEquals("Coahuila", dto.getEstado());
        assertEquals("México", dto.getPais());
        assertEquals(25000, dto.getCodigoPostal());
        assertEquals("PEEF900101ABC", dto.getRfcGestor());
    }

    @Test
    void testCrearVeterinarioConflictoCorreoDuplicado() {
        String correoGestor = "gestor@huellitas.mx";
        UsuarioModel user = new UsuarioModel();
        GestorModel gestor = new GestorModel("RFC", 1000, user);
        SucursalModel sucursal = new SucursalModel(null, gestor);

        when(usuarioRepository.findByCorreoHMAC(HMACUtil.GenerarHuella(correoGestor))).thenReturn(Optional.of(user));
        when(gestorRepository.findByUsuario(user)).thenReturn(Optional.of(gestor));
        when(sucursalRepository.findByGestor(gestor)).thenReturn(Optional.of(sucursal));

        when(usuarioRepository.existsByCorreoHMAC(HMACUtil.GenerarHuella("vet@huellitas.mx"))).thenReturn(true);

        GuardarVeterinarioDTO dto = new GuardarVeterinarioDTO("Luis", "Mora", null, "vet@huellitas.mx", "secret123");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
            service.crearVeterinario(correoGestor, dto)
        );
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }

    @Test
    void testActualizarCitaConflictoSiTieneConsultaYSeIntentaCancelar() {
        String correoGestor = "gestor@huellitas.mx";
        UsuarioModel user = new UsuarioModel();
        GestorModel gestor = new GestorModel("RFC", 1000, user);
        SucursalModel sucursal = new SucursalModel(null, gestor);
        sucursal.setIdSucursal(1);

        MascotaModel mascota = new MascotaModel();
        mascota.setSucursal(sucursal);

        CitaModel cita = new CitaModel();
        cita.setIdCita(10);
        cita.setMascotaModel(mascota);
        cita.setEstadoCita(1);

        when(usuarioRepository.findByCorreoHMAC(any())).thenReturn(Optional.of(user));
        when(gestorRepository.findByUsuario(user)).thenReturn(Optional.of(gestor));
        when(sucursalRepository.findByGestor(gestor)).thenReturn(Optional.of(sucursal));
        when(citaRepository.findById(10)).thenReturn(Optional.of(cita));
        when(consultaMedicaRepository.existsByCita_IdCita(10)).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
            service.actualizarCita(correoGestor, 10, Map.of("estadoCita", 3))
        );
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }

    @Test
    void testReportesValidacionFechas() {
        String correoGestor = "gestor@huellitas.mx";
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
            service.getReportes(correoGestor, LocalDate.of(2026, 12, 1), LocalDate.of(2026, 11, 1))
        );
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void testGetCitasGestor() {
        String correoGestor = "gestor@huellitas.mx";
        UsuarioModel user = new UsuarioModel();
        GestorModel gestor = new GestorModel("RFC", 1000, user);
        SucursalModel sucursal = new SucursalModel(null, gestor);
        sucursal.setIdSucursal(1);

        MascotaModel mascota = new MascotaModel();
        mascota.setNombre("Firulais");
        mascota.setSucursal(sucursal);

        UsuarioModel dueno = new UsuarioModel();
        dueno.setNombre("Ana");
        dueno.setPaterno("López");
        dueno.setMaterno("Ruiz");

        UsuarioModel vet = new UsuarioModel();
        vet.setIdUsuario(7);
        vet.setNombre("Luis");
        vet.setPaterno("Mora");

        CitaModel cita = new CitaModel();
        cita.setIdCita(10);
        cita.setFecha(LocalDate.of(2026, 11, 12));
        cita.setEntradaAgendada(LocalTime.of(10, 30, 0));
        cita.setEstadoCita(1);
        cita.setMascotaModel(mascota);
        cita.setUsuarioMascota(dueno);
        cita.setUsuarioVeterinario(vet);

        when(usuarioRepository.findByCorreoHMAC(any())).thenReturn(Optional.of(user));
        when(gestorRepository.findByUsuario(user)).thenReturn(Optional.of(gestor));
        when(sucursalRepository.findByGestor(gestor)).thenReturn(Optional.of(sucursal));
        when(citaRepository.findByMascotaModel_Sucursal_IdSucursal(1)).thenReturn(List.of(cita));

        List<CitaGestorDTO> dtos = service.getCitas(correoGestor);
        assertEquals(1, dtos.size());
        assertEquals(10, dtos.get(0).getIdCita());
        assertEquals("Firulais", dtos.get(0).getMascotaNombre());
        assertEquals("Ana López Ruiz", dtos.get(0).getDuenoNombre());
        assertEquals(7, dtos.get(0).getIdVeterinario());
        assertEquals("Luis Mora", dtos.get(0).getVeterinarioNombre());
    }

    @Test
    void testEliminarVeterinarioConCitasLanza409() {
        String correoGestor = "gestor@huellitas.mx";
        UsuarioModel user = new UsuarioModel();
        GestorModel gestor = new GestorModel("RFC", 1000, user);
        SucursalModel sucursal = new SucursalModel(null, gestor);
        sucursal.setIdSucursal(1);

        UsuarioModel vet = new UsuarioModel();
        vet.setIdUsuario(7);
        vet.setRol(new RolModel("Veterinario"));
        vet.getRol().setIdRol(4);
        vet.setSucursal(sucursal);

        when(usuarioRepository.findByCorreoHMAC(any())).thenReturn(Optional.of(user));
        when(gestorRepository.findByUsuario(user)).thenReturn(Optional.of(gestor));
        when(sucursalRepository.findByGestor(gestor)).thenReturn(Optional.of(sucursal));
        when(usuarioRepository.findById(7)).thenReturn(Optional.of(vet));
        when(citaRepository.existsByUsuarioVeterinario_IdUsuario(7)).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
            service.eliminarVeterinario(correoGestor, 7)
        );
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }

    @Test
    void testEliminarVeterinarioExitoso() {
        String correoGestor = "gestor@huellitas.mx";
        UsuarioModel user = new UsuarioModel();
        GestorModel gestor = new GestorModel("RFC", 1000, user);
        SucursalModel sucursal = new SucursalModel(null, gestor);
        sucursal.setIdSucursal(1);

        UsuarioModel vet = new UsuarioModel();
        vet.setIdUsuario(7);
        vet.setRol(new RolModel("Veterinario"));
        vet.getRol().setIdRol(4);
        vet.setSucursal(sucursal);

        when(usuarioRepository.findByCorreoHMAC(any())).thenReturn(Optional.of(user));
        when(gestorRepository.findByUsuario(user)).thenReturn(Optional.of(gestor));
        when(sucursalRepository.findByGestor(gestor)).thenReturn(Optional.of(sucursal));
        when(usuarioRepository.findById(7)).thenReturn(Optional.of(vet));
        when(citaRepository.existsByUsuarioVeterinario_IdUsuario(7)).thenReturn(false);
        when(consultaMedicaRepository.existsByCita_UsuarioVeterinario_IdUsuario(7)).thenReturn(false);

        service.eliminarVeterinario(correoGestor, 7);
    }
}
