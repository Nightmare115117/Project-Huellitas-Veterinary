package com.example.Proyect_DevOps.consultaMedica.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.consultaMedica.dtos.TratamientoDTO;
import com.example.Proyect_DevOps.consultaMedica.models.ConsultaMedicaModel;
import com.example.Proyect_DevOps.consultaMedica.models.TratamientoModel;
import com.example.Proyect_DevOps.consultaMedica.repositories.TratamientoRepository;

@Service
public class TratamientoService {

    @Autowired
    private TratamientoRepository tratamientoRepository;

    private TratamientoDTO convertirADTO(TratamientoModel tratamiento, String mascotaNombre) {
        return new TratamientoDTO(
            tratamiento.getIdTratamiento(),
            tratamiento.getMedicamento(),
            tratamiento.getDescripcion(),
            tratamiento.getCosto(),
            tratamiento.getStatus(),
            mascotaNombre);
    }

    public List<TratamientoDTO> mostrarTratamientosPorUsuario(String correo){
        List<TratamientoDTO> listaDTO = new ArrayList<>();
        for (TratamientoModel tratamiento : tratamientoRepository.findByconsultas_cita_usuarioMascota_correo(correo)) {
            for (ConsultaMedicaModel consulta : tratamiento.getListaConsultas()) {
                listaDTO.add(convertirADTO(tratamiento,
                    consulta.getCita().getMascotaModel().getNombre()));
            }
        }
        return listaDTO;
    }

    public List<TratamientoDTO> mostrarTratamientosPorUsuarioYMascota(String correo, int idMascota) {
        List<TratamientoDTO> listaDTO = new ArrayList<>();
        for (TratamientoModel tratamiento : tratamientoRepository.findByconsultas_cita_usuarioMascota_correoAndConsultas_cita_mascotaModel_idMascota(correo, idMascota)) {
            for (ConsultaMedicaModel consulta : tratamiento.getListaConsultas()) {
                listaDTO.add(convertirADTO(tratamiento,
                    consulta.getCita().getMascotaModel().getNombre()));
            }
        }
        return listaDTO;
    }

    public long contarTratamientosIncompletos(String correo) {
        return tratamientoRepository.countByconsultas_cita_usuarioMascota_correoAndEstatus(correo, 0);
    }
}
