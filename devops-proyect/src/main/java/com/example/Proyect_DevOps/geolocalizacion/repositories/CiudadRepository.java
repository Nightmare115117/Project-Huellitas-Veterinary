package com.example.Proyect_DevOps.geolocalizacion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Proyect_DevOps.geolocalizacion.models.CiudadModel;

@Repository
public interface CiudadRepository extends JpaRepository<CiudadModel, Integer>{

}
