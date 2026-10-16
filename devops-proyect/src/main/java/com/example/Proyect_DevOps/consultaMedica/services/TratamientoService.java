package com.example.Proyect_DevOps.consultaMedica.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.common.services.GenericServiceImpl;
import com.example.Proyect_DevOps.consultaMedica.dtos.TratamientoDTO;
import com.example.Proyect_DevOps.consultaMedica.models.ConsultaMedicaModel;
import com.example.Proyect_DevOps.consultaMedica.models.TratamientoModel;
import com.example.Proyect_DevOps.consultaMedica.repositories.TratamientoRepository;

@Service
public class TratamientoService extends GenericServiceImpl<TratamientoModel, Integer, TratamientoRepository> {

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
        for (TratamientoModel tratamiento : repository.findByconsultas_cita_usuarioMascota_correo(correo)) {
            for (ConsultaMedicaModel consulta : tratamiento.getListaConsultas()) {
                listaDTO.add(convertirADTO(tratamiento,
                    consulta.getCita().getMascotaModel().getNombre()));
            }
        }
        return listaDTO;
    }

    public List<TratamientoDTO> mostrarTratamientosPorUsuarioYMascota(String correo, int idMascota) {
        List<TratamientoDTO> listaDTO = new ArrayList<>();
        for (TratamientoModel tratamiento : repository.findByconsultas_cita_usuarioMascota_correoAndConsultas_cita_mascotaModel_idMascota(correo, idMascota)) {
            for (ConsultaMedicaModel consulta : tratamiento.getListaConsultas()) {
                listaDTO.add(convertirADTO(tratamiento,
                    consulta.getCita().getMascotaModel().getNombre()));
            }
        }
        return listaDTO;
    }

    public long contarTratamientosIncompletos(String correo) {
        return repository.countByconsultas_cita_usuarioMascota_correoAndEstatus(correo, 0);
    }
}
