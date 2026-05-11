package com.example.Proyect_DevOps.mascotas.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.Proyect_DevOps.mascotas.dtos.MascotaDTO;
import com.example.Proyect_DevOps.mascotas.models.MascotaModel;
import com.example.Proyect_DevOps.mascotas.repositories.MascotaRepository;
import com.example.Proyect_DevOps.users.models.UsuarioModel;
import com.example.Proyect_DevOps.users.repositories.UsuarioRepository;

@Service
public class MascotaService {

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private UsuarioRepository user;

    public MascotaDTO convertirADTO(MascotaModel mascota) {
        return new MascotaDTO(
            mascota.getIdMascota(),
            mascota.getNombre(),
            mascota.getFechaNacimiento(),
            mascota.getStatus(),
            mascota.getRaza());
    }

    public List<MascotaDTO> getAllMascotaByUser(String correo){
        Optional<UsuarioModel> usuarioOpt = user.findByCorreo(correo);
        if (usuarioOpt.isPresent()){
            UsuarioModel usuario = usuarioOpt.get();
            List<MascotaModel> mascotasModels = mascotaRepository.findByUsuario(usuario);
            List<MascotaDTO> mascotasDTOs = new ArrayList <>();
            for (MascotaModel elemento : mascotasModels){
                mascotasDTOs.add(convertirADTO(elemento));
            } 
            return mascotasDTOs;
        } else {
            return List.of();
        }
    }

    public long getCountMascotasByUser (String correo) throws RuntimeException {
        Optional<UsuarioModel> usuarioOpt = user.findByCorreo(correo);
        if (usuarioOpt.isPresent()){
            UsuarioModel usuario = usuarioOpt.get();
            return mascotaRepository.countByUsuario(usuario);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
    }
}