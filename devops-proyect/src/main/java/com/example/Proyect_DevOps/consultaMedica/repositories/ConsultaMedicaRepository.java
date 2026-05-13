package com.example.Proyect_DevOps.consultaMedica.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Proyect_DevOps.consultaMedica.models.ConsultaMedicaModel;

@Repository
public interface ConsultaMedicaRepository extends JpaRepository<ConsultaMedicaModel, Integer>{

}
