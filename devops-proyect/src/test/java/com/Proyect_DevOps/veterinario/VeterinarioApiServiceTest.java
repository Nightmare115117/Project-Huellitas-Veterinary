package com.Proyect_DevOps.veterinario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;

import com.Proyect_DevOps.citas.models.CitaModel;
import com.Proyect_DevOps.citas.repositories.CitaRepository;
import com.Proyect_DevOps.consultaMedica.models.ConsultaMedicaModel;
import com.Proyect_DevOps.consultaMedica.models.TratamientoModel;
import com.Proyect_DevOps.consultaMedica.repositories.ConsultaMedicaRepository;
import com.Proyect_DevOps.consultaMedica.repositories.TratamientoRepository;
import com.Proyect_DevOps.mascotas.models.EspecieModel;
import com.Proyect_DevOps.mascotas.models.MascotaModel;
import com.Proyect_DevOps.mascotas.models.RazaModel;
import com.Proyect_DevOps.users.models.RolModel;
import com.Proyect_DevOps.users.models.UsuarioModel;
import com.Proyect_DevOps.users.repositories.UsuarioRepository;
import com.Proyect_DevOps.utilities.HMACUtil;
import com.Proyect_DevOps.veterinario.dtos.CitaVeterinarioDTO;
import com.Proyect_DevOps.veterinario.dtos.CrearRecetaDTO;
import com.Proyect_DevOps.veterinario.dtos.RecetaDTO;
import com.Proyect_DevOps.veterinario.dtos.TratamientoItemDTO;
import com.Proyect_DevOps.veterinario.services.VeterinarioApiService;

public class VeterinarioApiServiceTest {

    private VeterinarioApiService service;
    private UsuarioRepository usuarioRepository;
    private CitaRepository citaRepository;
    private ConsultaMedicaRepository consultaMedicaRepository;
    private TratamientoRepository tratamientoRepository;

    @BeforeEach
    void setUp() throws Exception {
        usuarioRepository = mock(UsuarioRepository.class);
        citaRepository = mock(CitaRepository.class);
        consultaMedicaRepository = mock(ConsultaMedicaRepository.class);
        tratamientoRepository = mock(TratamientoRepository.class);

        service = new VeterinarioApiService();

        setField(service, "usuarioRepository", usuarioRepository);
        setField(service, "citaRepository", citaRepository);
        setField(service, "consultaMedicaRepository", consultaMedicaRepository);
        setField(service, "tratamientoRepository", tratamientoRepository);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testValidarVeterinarioRechazaEmailDistinto() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn("otro@huellitas.mx");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
            service.validarVeterinario(jwt, "vet@huellitas.mx")
        );
        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
    }

    @Test
    void testValidarVeterinarioRechazaRolNoVeterinario() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn("vet@huellitas.mx");
        when(jwt.getClaim("role_id")).thenReturn(3);
        when(jwt.getClaimAsString("roles")).thenReturn("Gestor");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
            service.validarVeterinario(jwt, "vet@huellitas.mx")
        );
        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
    }

    @Test
    void testGetCitasVeterinarioRetornaTieneReceta() {
        String correo = "vet@huellitas.mx";
        UsuarioModel vet = new UsuarioModel();
        vet.setIdUsuario(7);
        vet.setRol(new RolModel("Veterinario"));
        vet.getRol().setIdRol(4);

        EspecieModel especie = new EspecieModel("Perro");
        RazaModel raza = new RazaModel("Labrador", especie);
        MascotaModel mascota = new MascotaModel();
        mascota.setNombre("Firulais");
        mascota.setRaza(raza);

        UsuarioModel dueno = new UsuarioModel();
        dueno.setNombre("Ana");
        dueno.setPaterno("López");
        dueno.setMaterno("Ruiz");

        CitaModel cita = new CitaModel();
        cita.setIdCita(10);
        cita.setFecha(LocalDate.of(2026, 11, 12));
        cita.setEntradaAgendada(LocalTime.of(10, 30, 0));
        cita.setEstadoCita(2);
        cita.setMascotaModel(mascota);
        cita.setUsuarioMascota(dueno);
        cita.setUsuarioVeterinario(vet);

        when(usuarioRepository.findByCorreoHMAC(HMACUtil.GenerarHuella(correo))).thenReturn(Optional.of(vet));
        when(citaRepository.findByUsuarioVeterinario_IdUsuario(7)).thenReturn(List.of(cita));
        when(consultaMedicaRepository.existsByCita_IdCita(10)).thenReturn(false);

        List<CitaVeterinarioDTO> dtos = service.getCitas(correo);
        assertEquals(1, dtos.size());
        CitaVeterinarioDTO dto = dtos.get(0);
        assertEquals(10, dto.getIdCita());
        assertEquals("2026-11-12", dto.getFecha());
        assertEquals("10:30", dto.getEntradaAgendada());
        assertEquals(2, dto.getEstadoCita());
        assertEquals("Firulais", dto.getMascotaNombre());
        assertEquals("Perro", dto.getEspecie());
        assertEquals("Labrador", dto.getRaza());
        assertEquals("Ana López Ruiz", dto.getDuenoNombre());
        assertFalse(dto.isTieneReceta());
    }

    @Test
    void testCrearRecetaValidacionCitaCancelada() {
        String correo = "vet@huellitas.mx";
        UsuarioModel vet = new UsuarioModel();
        vet.setIdUsuario(7);
        vet.setRol(new RolModel("Veterinario"));
        vet.getRol().setIdRol(4);

        CitaModel cita = new CitaModel();
        cita.setIdCita(10);
        cita.setEstadoCita(3); // cancelada
        cita.setUsuarioVeterinario(vet);

        when(usuarioRepository.findByCorreoHMAC(any())).thenReturn(Optional.of(vet));
        when(citaRepository.findById(10)).thenReturn(Optional.of(cita));

        CrearRecetaDTO dto = new CrearRecetaDTO(10, "Motivo", "Diag", 10.0, "Rec",
            List.of(new TratamientoItemDTO("Med", "Desc", 100.0)));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
            service.crearReceta(correo, dto)
        );
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void testCrearRecetaValidacionCitaYaTieneConsultaConflicto() {
        String correo = "vet@huellitas.mx";
        UsuarioModel vet = new UsuarioModel();
        vet.setIdUsuario(7);
        vet.setRol(new RolModel("Veterinario"));
        vet.getRol().setIdRol(4);

        CitaModel cita = new CitaModel();
        cita.setIdCita(10);
        cita.setEstadoCita(2);
        cita.setUsuarioVeterinario(vet);

        when(usuarioRepository.findByCorreoHMAC(any())).thenReturn(Optional.of(vet));
        when(citaRepository.findById(10)).thenReturn(Optional.of(cita));
        when(consultaMedicaRepository.existsByCita_IdCita(10)).thenReturn(true);

        CrearRecetaDTO dto = new CrearRecetaDTO(10, "Motivo", "Diag", 10.0, "Rec",
            List.of(new TratamientoItemDTO("Med", "Desc", 100.0)));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
            service.crearReceta(correo, dto)
        );
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }

    @Test
    void testCrearRecetaExitoso() {
        String correo = "vet@huellitas.mx";
        UsuarioModel vet = new UsuarioModel();
        vet.setIdUsuario(7);
        vet.setRol(new RolModel("Veterinario"));
        vet.getRol().setIdRol(4);

        CitaModel cita = new CitaModel();
        cita.setIdCita(10);
        cita.setEstadoCita(2);
        cita.setUsuarioVeterinario(vet);

        when(usuarioRepository.findByCorreoHMAC(any())).thenReturn(Optional.of(vet));
        when(citaRepository.findById(10)).thenReturn(Optional.of(cita));
        when(consultaMedicaRepository.existsByCita_IdCita(10)).thenReturn(false);
        when(tratamientoRepository.save(any(TratamientoModel.class))).thenAnswer(i -> i.getArgument(0));

        CrearRecetaDTO dto = new CrearRecetaDTO(10, "Vómito", "Gastritis leve", 18.5, "Dieta blanda",
            List.of(new TratamientoItemDTO("Omeprazol", "10 mg cada 24 h por 5 días", 120.0)));

        service.crearReceta(correo, dto);
        verify(consultaMedicaRepository).save(any(ConsultaMedicaModel.class));
    }

    @Test
    void testGetRecetasVeterinario() {
        String correo = "vet@huellitas.mx";
        UsuarioModel vet = new UsuarioModel();
        vet.setIdUsuario(7);
        vet.setRol(new RolModel("Veterinario"));
        vet.getRol().setIdRol(4);

        MascotaModel mascota = new MascotaModel();
        mascota.setNombre("Firulais");

        UsuarioModel dueno = new UsuarioModel();
        dueno.setNombre("Ana");
        dueno.setPaterno("López");
        dueno.setMaterno("Ruiz");

        CitaModel cita = new CitaModel();
        cita.setIdCita(10);
        cita.setFecha(LocalDate.of(2026, 11, 12));
        cita.setUsuarioMascota(dueno);
        cita.setMascotaModel(mascota);
        cita.setUsuarioVeterinario(vet);

        ConsultaMedicaModel cm = new ConsultaMedicaModel();
        cm.setIdConsulta(5);
        cm.setCita(cita);
        cm.setIdMascota(mascota);
        cm.setMotivo("Vómito");
        cm.setDiagnostico("Gastritis leve");
        cm.setPeso(18.5);
        cm.setRecomendaciones("Dieta blanda 3 días");
        cm.setTratamientos(List.of(new TratamientoModel("Omeprazol", "10 mg cada 24 h por 5 días", 120.0, 0)));

        when(usuarioRepository.findByCorreoHMAC(HMACUtil.GenerarHuella(correo))).thenReturn(Optional.of(vet));
        when(consultaMedicaRepository.findByCita_UsuarioVeterinario_IdUsuarioOrderByCita_FechaDescIdConsultaDesc(7))
            .thenReturn(List.of(cm));

        List<RecetaDTO> recetas = service.getRecetas(correo);
        assertEquals(1, recetas.size());
        RecetaDTO r = recetas.get(0);
        assertEquals(5, r.getIdConsulta());
        assertEquals(10, r.getIdCita());
        assertEquals("2026-11-12", r.getFecha());
        assertEquals("Firulais", r.getMascotaNombre());
        assertEquals("Ana López Ruiz", r.getDuenoNombre());
        assertEquals("Vómito", r.getMotivo());
        assertEquals("Gastritis leve", r.getDiagnostico());
        assertEquals(18.5, r.getPeso());
        assertEquals("Dieta blanda 3 días", r.getRecomendaciones());
        assertEquals(1, r.getTratamientos().size());
        assertEquals("Omeprazol", r.getTratamientos().get(0).getMedicamento());
        assertEquals(120.0, r.getTratamientos().get(0).getCosto());
    }
}
