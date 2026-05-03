package com.example.Proyect_DevOps.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.Proyect_DevOps.dtos.CitaDTO;
import com.example.Proyect_DevOps.models.CitaModel;
import com.example.Proyect_DevOps.models.MascotaModel;
import com.example.Proyect_DevOps.models.UsuarioModel;
import com.example.Proyect_DevOps.repositories.CitaRepository;
import com.example.Proyect_DevOps.repositories.MascotaRepository;
import com.example.Proyect_DevOps.repositories.UsuarioRepository;

@Service
public class CitaService {

    private CitaDTO convertirADTO (CitaModel citaM){
        CitaDTO cita = new CitaDTO();
        cita.setEntradaAgendada(citaM.getEntradaAgendada());
        cita.setEstadoCita(citaM.getEstadoCita());
        cita.setHoraSalida(citaM.getHoraSalida());
        cita.setIdCita(citaM.getIdCita());
        cita.setFecha(citaM.getFecha());
        // Verificar si el veterinario existe antes de acceder
        if (citaM.getUsuarioVeterinario() != null) {
            cita.setNombreVeterinario(citaM.getUsuarioVeterinario().getNombre() + " " + citaM.getUsuarioVeterinario().getPaterno());
        } else {
            cita.setNombreVeterinario("Pendiente de asignación");
        }
        return cita;
    }

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    public long countByUsuarioCitas (String correo){
        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByCorreo(correo);
        if (usuarioOpt.isPresent()) {
            UsuarioModel usuario = usuarioOpt.get();
            return citaRepository.countByUsuarioMascotaAndEstadoCita(usuario, 1);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
    }

    public List<CitaDTO> getCitasByUser (String correo) {
        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByCorreo(correo);
        if (usuarioOpt.isPresent()) {
            UsuarioModel usuario = usuarioOpt.get();
            List<CitaDTO> listaDTO = new ArrayList<>();
            for (CitaModel elemento : citaRepository.findByUsuarioMascota(usuario)) {
                listaDTO.add(convertirADTO(elemento));
            }
            return listaDTO;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
    }

    public List<CitaDTO> getCitasByUserAndPet (String correo , int idMascota) {
        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByCorreo(correo);
        List<CitaDTO> listaDTO = new ArrayList<>();
        UsuarioModel usuario;
        if (usuarioOpt.isPresent()) {
            usuario = usuarioOpt.get();
            Optional<MascotaModel> mascotaOpt = mascotaRepository.findById(idMascota);
            if (mascotaOpt.isPresent()) {
                MascotaModel mascota = mascotaOpt.get();
                for (CitaModel elemento : citaRepository.findByUsuarioMascotaAndMascotaModel(usuario, mascota)) {
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

    public CitaDTO createCita(CitaDTO citaDTO){
        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByCorreo(citaDTO.getCorreoCliente());
        if (usuarioOpt.isPresent()) {
            UsuarioModel usuario = usuarioOpt.get();
            Optional<MascotaModel> mascotaOpt = mascotaRepository.findById(citaDTO.getIdMascota());
            if (mascotaOpt.isPresent()) {
                MascotaModel mascota = mascotaOpt.get();
                CitaModel cita = new CitaModel(mascota, usuario, citaDTO.getFecha(), citaDTO.getEntradaAgendada(), citaDTO.getEstadoCita());
                citaRepository.save(cita);
                return convertirADTO(cita);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mascota no encontrada");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
    }
}