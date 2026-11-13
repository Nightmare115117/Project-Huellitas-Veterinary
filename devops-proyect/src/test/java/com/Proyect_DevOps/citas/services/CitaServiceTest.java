package com.Proyect_DevOps.citas.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.Proyect_DevOps.citas.dtos.CitaDTO;
import com.Proyect_DevOps.citas.models.CitaModel;
import com.Proyect_DevOps.citas.repositories.CitaRepository;
import com.Proyect_DevOps.mascotas.models.MascotaModel;
import com.Proyect_DevOps.mascotas.repositories.MascotaRepository;
import com.Proyect_DevOps.users.models.UsuarioModel;
import com.Proyect_DevOps.users.repositories.UsuarioRepository;
import com.Proyect_DevOps.utilities.HMACUtil;

class CitaServiceTest {

    private CitaRepository citaRepository;
    private MascotaRepository mascotaRepository;
    private UsuarioRepository usuarioRepository;
    private CitaService service;

    @BeforeEach
    void setUp() throws Exception {
        citaRepository = mock(CitaRepository.class);
        mascotaRepository = mock(MascotaRepository.class);
        usuarioRepository = mock(UsuarioRepository.class);
        service = new CitaService();

        setField(service, "repository", citaRepository);
        setField(service, "mascotaRepository", mascotaRepository);
        setField(service, "usuarioRepository", usuarioRepository);
    }

    @Test
    void rejectsAppointmentWhenPetBelongsToAnotherUser() {
        String clientEmail = "cliente@example.com";
        UsuarioModel client = user(1);
        client.setCorreo(clientEmail);
        MascotaModel pet = new MascotaModel();
        pet.setIdMascota(12);
        pet.setUsuario(user(2));

        when(usuarioRepository.findByCorreoHMAC(HMACUtil.GenerarHuella(clientEmail)))
            .thenReturn(Optional.of(client));
        when(mascotaRepository.findById(12)).thenReturn(Optional.of(pet));

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> service.createCita(citaFor(clientEmail, 12))
        );

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        verify(citaRepository, never()).save(any(CitaModel.class));
    }

    @Test
    void rejectsAppointmentInThePast() {
        CitaDTO pastAppointment = new CitaDTO(
            0,
            null,
            "cliente@example.com",
            12,
            LocalDateTime.now().minusDays(1).toLocalDate(),
            LocalDateTime.now().minusDays(1).toLocalTime(),
            null,
            1
        );

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> service.createCita(pastAppointment)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(usuarioRepository, never()).findByCorreoHMAC(any());
    }

    @Test
    void createsAppointmentWhenPetBelongsToClient() {
        String clientEmail = "cliente@example.com";
        UsuarioModel client = user(1);
        client.setCorreo(clientEmail);
        MascotaModel pet = new MascotaModel();
        pet.setIdMascota(12);
        pet.setUsuario(client);

        when(usuarioRepository.findByCorreoHMAC(HMACUtil.GenerarHuella(clientEmail)))
            .thenReturn(Optional.of(client));
        when(mascotaRepository.findById(12)).thenReturn(Optional.of(pet));
        when(citaRepository.save(any(CitaModel.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        CitaDTO result = service.createCita(citaFor(clientEmail, 12));

        assertEquals(clientEmail, result.correoCliente());
        verify(citaRepository).save(any(CitaModel.class));
    }

    @Test
    void deniesCancellationWhenAppointmentBelongsToAnotherUser() {
        String requesterEmail = "cliente@example.com";
        UsuarioModel requester = user(1);
        CitaModel appointment = new CitaModel(
            new MascotaModel(),
            user(2),
            LocalDate.now().plusDays(1),
            LocalTime.of(10, 0),
            1
        );
        appointment.setIdCita(42);

        when(citaRepository.findById(42)).thenReturn(Optional.of(appointment));
        when(usuarioRepository.findByCorreoHMAC(HMACUtil.GenerarHuella(requesterEmail)))
            .thenReturn(Optional.of(requester));

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> service.cancelarCita(42, requesterEmail, false)
        );

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        verify(citaRepository, never()).save(any(CitaModel.class));
    }

    private UsuarioModel user(int id) {
        UsuarioModel user = new UsuarioModel();
        user.setIdUsuario(id);
        return user;
    }

    private CitaDTO citaFor(String email, int petId) {
        return new CitaDTO(
            0,
            null,
            email,
            petId,
            LocalDate.now().plusDays(2),
            LocalTime.of(10, 0),
            null,
            1
        );
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Class<?> current = target.getClass();
        while (current != null) {
            try {
                var field = current.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(target, value);
                return;
            } catch (NoSuchFieldException exception) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchFieldException(fieldName);
    }
}
