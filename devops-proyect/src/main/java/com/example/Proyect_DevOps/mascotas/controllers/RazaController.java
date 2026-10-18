package com.example.Proyect_DevOps.mascotas.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.example.Proyect_DevOps.common.controllers.GenericController;
import com.example.Proyect_DevOps.mascotas.models.RazaModel;
import com.example.Proyect_DevOps.mascotas.services.RazaService;

/**
 * Controlador REST para el catálogo de razas de mascotas.
 * Expone rutas bajo el endpoint {@code /raza} e incluye consultas filtradas por especie.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Tag(name = "Razas", description = "Catálogo de razas y consulta por especie")
@RestController
@RequestMapping("/raza")
public class RazaController extends GenericController<RazaModel, Integer, RazaService> {

    /**
     * Endpoint HTTP GET para listar todas las razas vinculadas a una especie específica.
     *
     * @param idEspecie Identificador de la especie.
     * @return Lista de {@link RazaModel} pertenecientes a dicha especie.
     */
    @Operation(summary = "Listar razas por especie", description = "Retorna las razas que pertenecen a la especie indicada.")
    @GetMapping("/especie/{idEspecie:[0-9]+}")
    public List<RazaModel> getRazasByEspecie(@PathVariable int idEspecie) {
        return service.findByEspecieId(idEspecie);
    }
}
