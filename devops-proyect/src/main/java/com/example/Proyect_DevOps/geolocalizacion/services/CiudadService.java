package com.example.Proyect_DevOps.geolocalizacion.services;

import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.common.services.GenericServiceImpl;
import com.example.Proyect_DevOps.geolocalizacion.models.CiudadModel;
import com.example.Proyect_DevOps.geolocalizacion.repositories.CiudadRepository;

@Service
public class CiudadService extends GenericServiceImpl<CiudadModel, Integer, CiudadRepository> {

}
