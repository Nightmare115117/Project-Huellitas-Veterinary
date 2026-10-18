package com.example.Proyect_DevOps.consultaMedica.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.common.services.GenericServiceImpl;
import com.example.Proyect_DevOps.consultaMedica.dtos.TratamientoDTO;
import com.example.Proyect_DevOps.consultaMedica.models.ConsultaMedicaModel;
import com.example.Proyect_DevOps.consultaMedica.models.TratamientoModel;
import com.example.Proyect_DevOps.consultaMedica.repositories.TratamientoRepository;

/**
 * Servicio de negocio para la administración de tratamientos veterinarios.
 * Permite consultar tratamientos por usuario, tratamientos específicos por mascota
 * y contabilizar tratamientos en curso o pendientes.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Service
public class TratamientoService extends GenericServiceImpl<TratamientoModel, Integer, TratamientoRepository> {

    /**
     * Convierte una entidad {@link TratamientoModel} junto con el nombre de la mascota a un {@link TratamientoDTO}.
     *
     * @param tratamiento Entidad del tratamiento a convertir.
     * @param mascotaNombre Nombre de la mascota receptora del tratamiento.
     * @return DTO con los detalles consolidados del tratamiento.
     */
    private TratamientoDTO convertirADTO(TratamientoModel tratamiento, String mascotaNombre) {
        return new TratamientoDTO(
            tratamiento.getIdTratamiento(),
            tratamiento.getMedicamento(),
            tratamiento.getDescripcion(),
            tratamiento.getCosto(),
            tratamiento.getStatus(),
            mascotaNombre);
    }

    /**
     * Muestra todos los tratamientos asociados a las mascotas de un usuario.
     *
     * @param correo Correo electrónico del usuario dueño.
     * @return Lista de {@link TratamientoDTO} correspondientes.
     */
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

    /**
     * Muestra los tratamientos asignados a una mascota específica de un usuario.
     *
     * @param correo Correo electrónico del cliente.
     * @param idMascota Identificador de la mascota.
     * @return Lista de {@link TratamientoDTO} de la mascota consultada.
     */
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

    /**
     * Cuenta la cantidad de tratamientos incompletos (estatus 0) para las mascotas de un usuario.
     *
     * @param correo Correo electrónico del cliente.
     * @return Total de tratamientos pendientes o activos.
     */
    public long contarTratamientosIncompletos(String correo) {
        return repository.countByconsultas_cita_usuarioMascota_correoAndEstatus(correo, 0);
    }
}
