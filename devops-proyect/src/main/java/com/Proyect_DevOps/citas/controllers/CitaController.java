package com.Proyect_DevOps.citas.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Proyect_DevOps.citas.dtos.CitaDTO;
import com.Proyect_DevOps.citas.models.CitaModel;
import com.Proyect_DevOps.citas.services.CitaService;
import com.Proyect_DevOps.common.controllers.GenericController;

/**
 * Controlador REST para la gestión de citas veterinarias.
 * Expone endpoints para contar citas activas, listar citas por usuario o por mascota,
 * agendar nuevas citas y cancelar citas existentes.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@RestController
@RequestMapping("/cita")
public class CitaController extends GenericController<CitaModel, Integer, CitaService> {

    /**
     * Endpoint HTTP GET para contar cuántas citas activas tiene programadas un usuario.
     *
     * @param correo Correo electrónico del usuario.
     * @return Cantidad de citas activas agendadas.
     */
    @GetMapping("/usuario/{correo:.+}")
    public long contarCitasPorUsuario(@PathVariable String correo) {
        return service.countByUsuarioCitas(correo);
    }

    /**
     * Endpoint HTTP GET para listar las citas de un usuario en base a su correo.
     *
     * @param correo Correo electrónico del usuario.
     * @return Lista de {@link CitaDTO} pertenecientes al usuario.
     */
    @GetMapping("/{correo:.+@.+}")
    public List<CitaDTO> conseguirListaDeCitas(@PathVariable String correo) {
        return service.getCitasByUser(correo);
    }

    /**
     * Endpoint HTTP GET para listar las citas de una mascota concreta de un usuario.
     *
     * @param correo Correo electrónico del usuario.
     * @param idMascota Identificador de la mascota.
     * @return Lista de {@link CitaDTO} de la mascota.
     */
    @GetMapping("/{correo:.+@.+}/{idMascota:[0-9]+}")
    public List<CitaDTO> conseguirListaDeCitasPorMascota(@PathVariable String correo, @PathVariable int idMascota){
        return service.getCitasByUserAndPet(correo, idMascota);
    }
    
    /**
     * Endpoint HTTP POST para agendar una nueva cita médica.
     *
     * @param citaDTO Objeto con la información de la cita médica a agendar.
     * @return {@link CitaDTO} con la información de la cita persistida.
     */
    @PostMapping("/crear")
    public CitaDTO crearCita(@RequestBody CitaDTO citaDTO){
        return service.createCita(citaDTO);
    }

    /**
     * Endpoint HTTP PUT para cancelar una cita agendada por su identificador.
     *
     * @param idCita Identificador único de la cita médica a cancelar.
     * @return {@link ResponseEntity} con código 200 (OK) y {@code true} si se canceló, o 404 (NOT_FOUND) con el error.
     */
    @PutMapping("/cancelar/{idCita:[0-9]+}") 
    public ResponseEntity<?> cancelarCita(@PathVariable int idCita) {
        try {
            boolean respuesta = service.cancelarCita(idCita);
            return ResponseEntity.ok(respuesta);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
