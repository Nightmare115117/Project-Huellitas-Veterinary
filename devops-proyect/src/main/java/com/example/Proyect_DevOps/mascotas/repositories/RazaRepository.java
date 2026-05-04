package com.example.Proyect_DevOps.mascotas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Proyect_DevOps.mascotas.models.EspecieModel;
import com.example.Proyect_DevOps.mascotas.models.RazaModel;

@Repository
public interface RazaRepository extends JpaRepository<RazaModel, Integer>{

    List<RazaModel> findByEspecie (EspecieModel especie);

}
