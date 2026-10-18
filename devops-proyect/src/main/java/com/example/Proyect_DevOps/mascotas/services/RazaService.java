package com.example.Proyect_DevOps.mascotas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.common.services.GenericServiceImpl;
import com.example.Proyect_DevOps.mascotas.models.EspecieModel;
import com.example.Proyect_DevOps.mascotas.models.RazaModel;
import com.example.Proyect_DevOps.mascotas.repositories.EspecieRepository;
import com.example.Proyect_DevOps.mascotas.repositories.RazaRepository;

/**
 * Servicio de negocio para la gestión de razas de mascotas.
 * Permite filtrar el catálogo de razas a partir del identificador de especie.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Service
public class RazaService extends GenericServiceImpl<RazaModel, Integer, RazaRepository> {

    /**
     * Repositorio de especies para validar la existencia antes de consultar razas.
     */
    @Autowired
    private EspecieRepository especieRepository;

    /**
     * Obtiene la lista de razas correspondientes a una especie según su identificador.
     *
     * @param idEspecie Identificador de la especie.
     * @return Lista de {@link RazaModel} pertenecientes a la especie, o lista vacía si la especie no existe.
     */
    public List<RazaModel> findByEspecieId(int idEspecie) {
        return especieRepository.findById(idEspecie)
                .map(repository::findByEspecie)
                .orElse(List.of());
    }
}
