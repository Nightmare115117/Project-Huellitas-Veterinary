package com.example.Proyect_DevOps.mascotas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Proyect_DevOps.mascotas.models.EspecieModel;

@Repository
public interface EspecieRepository extends JpaRepository<EspecieModel, Integer>{

}
