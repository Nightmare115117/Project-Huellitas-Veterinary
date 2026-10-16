package com.example.Proyect_DevOps.geolocalizacion.services;

import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.common.services.GenericServiceImpl;
import com.example.Proyect_DevOps.geolocalizacion.models.PaisModel;
import com.example.Proyect_DevOps.geolocalizacion.repositories.PaisRepository;

@Service
public class PaisService extends GenericServiceImpl<PaisModel, Integer, PaisRepository> {

}
