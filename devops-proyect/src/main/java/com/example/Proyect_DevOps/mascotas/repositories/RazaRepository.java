package com.example.Proyect_DevOps.mascotas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Proyect_DevOps.mascotas.models.EspecieModel;
import com.example.Proyect_DevOps.mascotas.models.RazaModel;

/**
 * Repositorio JPA para la entidad {@link RazaModel}.
 * Permite buscar razas pertenecientes a una especie determinada.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Repository
public interface RazaRepository extends JpaRepository<RazaModel, Integer>{

    /**
     * Busca todas las razas vinculadas a una especie específica.
     *
     * @param especie Especie por la cual filtrar.
     * @return Lista de razas pertenecientes a la especie dada.
     */
    List<RazaModel> findByEspecie (EspecieModel especie);

}
