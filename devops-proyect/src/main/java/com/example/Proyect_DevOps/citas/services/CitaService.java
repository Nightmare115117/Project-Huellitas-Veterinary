package com.example.Proyect_DevOps.citas.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.Proyect_DevOps.citas.dtos.CitaDTO;
import com.example.Proyect_DevOps.citas.models.CitaModel;
import com.example.Proyect_DevOps.citas.repositories.CitaRepository;
import com.example.Proyect_DevOps.mascotas.models.MascotaModel;
import com.example.Proyect_DevOps.mascotas.repositories.MascotaRepository;
import com.example.Proyect_DevOps.users.models.UsuarioModel;
import com.example.Proyect_DevOps.users.repositories.UsuarioRepository;

@Service
public class CitaService {

    private CitaDTO convertirADTO(CitaModel citaM) {
        String nombreVeterinario =
            citaM.getUsuarioVeterinario() != null
                ? citaM.getUsuarioVeterinario().getNombre() + " " + citaM.getUsuarioVeterinario().getPaterno()
                : "Pendiente de asignación";
        return new CitaDTO(
            citaM.getIdCita(),
            nombreVeterinario,
            citaM.getUsuarioMascota().getCorreo(),
            citaM.getMascotaModel().getIdMascota(),
            citaM.getFecha(),
            citaM.getEntradaAgendada(),
            citaM.getHoraSalida(),
            citaM.getEstadoCita());
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
        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByCorreo(citaDTO.correoCliente());
        if (usuarioOpt.isPresent()) {
            UsuarioModel usuario = usuarioOpt.get();
            Optional<MascotaModel> mascotaOpt = mascotaRepository.findById(citaDTO.idMascota());
            if (mascotaOpt.isPresent()) {
                MascotaModel mascota = mascotaOpt.get();
                CitaModel cita = new CitaModel(mascota, usuario, citaDTO.fecha(), citaDTO.entradaAgendada(), citaDTO.estadoCita());
                citaRepository.save(cita);
                return convertirADTO(cita);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mascota no encontrada");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
    }

    public boolean cancelarCita (int idCita) {
        CitaModel cita = citaRepository.findById(idCita).orElseThrow(()-> new RuntimeException("El id de la cita no fue encontrado: " + idCita));
        try {
            cita.setEstadoCita(3);
            citaRepository.save(cita);
            return true;
        } catch (Exception  e) {
            return false;
        }
    }
}