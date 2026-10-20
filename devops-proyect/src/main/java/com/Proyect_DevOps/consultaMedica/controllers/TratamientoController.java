package com.Proyect_DevOps.consultaMedica.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Proyect_DevOps.common.controllers.GenericController;
import com.Proyect_DevOps.consultaMedica.dtos.TratamientoDTO;
import com.Proyect_DevOps.consultaMedica.models.TratamientoModel;
import com.Proyect_DevOps.consultaMedica.services.TratamientoService;

/**
 * Controlador REST para la administración y consulta de tratamientos médicos veterinarios.
 * Permite filtrar prescripciones por usuario, mascota y contar tratamientos incompletos.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@RestController
@RequestMapping("/tratamiento")
public class TratamientoController extends GenericController<TratamientoModel, Integer, TratamientoService> {

    /**
     * Endpoint HTTP GET para obtener todos los tratamientos de las mascotas de un usuario.
     *
     * @param correo Correo electrónico del usuario.
     * @return Lista de {@link TratamientoDTO} asociados.
     */
    @GetMapping("/usuario/{correo:.+}")
    public List<TratamientoDTO> getTratamientosPorUsuario(@PathVariable String correo){
        return service.mostrarTratamientosPorUsuario(correo);
    }

    /**
     * Endpoint HTTP GET para contar cuántos tratamientos pendientes o en curso (incompletos) tiene un usuario.
     *
     * @param correo Correo electrónico del usuario.
     * @return Cantidad de tratamientos incompletos.
     */
    @GetMapping("/contar/{correo:.+}")
    public long contarTratamientosIncompletos(@PathVariable String correo) {
        return service.contarTratamientosIncompletos(correo);
    }

    /**
     * Endpoint HTTP GET para listar los tratamientos específicos de una mascota perteneciente a un usuario.
     *
     * @param correo Correo electrónico del usuario.
     * @param idMascota Identificador de la mascota.
     * @return Lista de {@link TratamientoDTO} de la mascota.
     */
    @GetMapping("/usuario/{correo:.+}/{idMascota:[0-9]+}")
    public List<TratamientoDTO> getTratamientosPorUsuarioYMascota(@PathVariable String correo, @PathVariable int idMascota) {
        return service.mostrarTratamientosPorUsuarioYMascota(correo, idMascota);
    }
}
