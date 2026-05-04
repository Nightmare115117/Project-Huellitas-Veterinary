package com.example.Proyect_DevOps.sucursales.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Proyect_DevOps.sucursales.models.GestorModel;

@Repository
public interface GestorRepository extends JpaRepository<GestorModel, Integer>{

}
