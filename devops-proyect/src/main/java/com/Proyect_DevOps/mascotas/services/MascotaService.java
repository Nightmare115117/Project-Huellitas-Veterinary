package com.Proyect_DevOps.mascotas.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.Proyect_DevOps.common.services.GenericServiceImpl;
import com.Proyect_DevOps.mascotas.dtos.MascotaDTO;
import com.Proyect_DevOps.mascotas.models.MascotaModel;
import com.Proyect_DevOps.mascotas.repositories.MascotaRepository;
import com.Proyect_DevOps.users.models.UsuarioModel;
import com.Proyect_DevOps.users.repositories.UsuarioRepository;
import com.Proyect_DevOps.utilities.HMACUtil;

/**
 * Servicio de negocio para la administración y consulta de mascotas.
 * Gestiona la transformación a DTO, búsqueda de mascotas por usuario mediante HMAC y conteo total de mascotas.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Service
public class MascotaService extends GenericServiceImpl<MascotaModel, Integer, MascotaRepository> {

    /**
     * Repositorio de usuarios para resolver la identidad del dueño.
     */
    @Autowired
    private UsuarioRepository user;

    /**
     * Transforma una entidad {@link MascotaModel} a su representación desacoplada {@link MascotaDTO}.
     *
     * @param mascota Entidad de la mascota.
     * @return Objeto {@link MascotaDTO}.
     */
    public MascotaDTO convertirADTO(MascotaModel mascota) {
        return new MascotaDTO(
            mascota.getIdMascota(),
            mascota.getNombre(),
            mascota.getFechaNacimiento(),
            mascota.getStatus(),
            mascota.getRaza());
    }

    /**
     * Obtiene la lista de todas las mascotas registradas a nombre de un usuario según su correo.
     *
     * @param correo Correo electrónico del usuario dueño.
     * @return Lista de {@link MascotaDTO} asociadas.
     */
    public List<MascotaDTO> getAllMascotaByUser(String correo){
        Optional<UsuarioModel> usuarioOpt = user.findByCorreoHMAC(HMACUtil.GenerarHuella(correo));
        if (usuarioOpt.isPresent()){
            UsuarioModel usuario = usuarioOpt.get();
            List<MascotaModel> mascotasModels = repository.findByUsuario(usuario);
            List<MascotaDTO> mascotasDTOs = new ArrayList<>();
            for (MascotaModel elemento : mascotasModels){
                mascotasDTOs.add(convertirADTO(elemento));
            } 
            return mascotasDTOs;
        } else {
            return List.of();
        }
    }

    /**
     * Cuenta el total de mascotas asociadas a un usuario según su correo.
     *
     * @param correo Correo electrónico del usuario dueño.
     * @return Cantidad total de mascotas registradas.
     * @throws ResponseStatusException Si el usuario no fue encontrado en el sistema (404).
     */
    public long getCountMascotasByUser (String correo) throws RuntimeException {
        Optional<UsuarioModel> usuarioOpt = user.findByCorreoHMAC(HMACUtil.GenerarHuella(correo));
        if (usuarioOpt.isPresent()){
            UsuarioModel usuario = usuarioOpt.get();
            return repository.countByUsuario(usuario);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
    }
}
