package com.Proyect_DevOps.mascotas.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Proyect_DevOps.common.controllers.GenericController;
import com.Proyect_DevOps.mascotas.dtos.MascotaDTO;
import com.Proyect_DevOps.mascotas.models.MascotaModel;
import com.Proyect_DevOps.mascotas.services.MascotaService;

/**
 * Controlador REST para la gestión y consulta de mascotas.
 * Proporciona rutas bajo el endpoint {@code /mascota} para obtener listados y totales por usuario.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@RestController
@RequestMapping("/mascota")
public class MascotaController extends GenericController<MascotaModel, Integer, MascotaService> {

    /**
     * Endpoint HTTP GET para listar todas las mascotas asociadas al correo de un usuario.
     *
     * @param correo Correo electrónico del usuario dueño.
     * @return Lista de {@link MascotaDTO} de sus mascotas.
     */
    @GetMapping("/{correo:.+@.+}")
    public List<MascotaDTO> getListMascota(@PathVariable String correo) {
        return service.getAllMascotaByUser(correo);
    }
    
    /**
     * Endpoint HTTP GET para contar cuántas mascotas tiene registradas un usuario.
     *
     * @param correo Correo electrónico del usuario dueño.
     * @return Total de mascotas asociadas al usuario.
     */
    @GetMapping("/usuario/{correo:.+}")
    public long contarMascotasPorUsuario(@PathVariable String correo) {
        return service.getCountMascotasByUser(correo);
    }
}
