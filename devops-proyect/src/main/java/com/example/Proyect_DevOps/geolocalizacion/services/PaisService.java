package com.example.Proyect_DevOps.geolocalizacion.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.geolocalizacion.repositories.PaisRepository;

@Service
public class PaisService {

    @Autowired
    @SuppressWarnings("unused")
    private PaisRepository paisRepository;

}
