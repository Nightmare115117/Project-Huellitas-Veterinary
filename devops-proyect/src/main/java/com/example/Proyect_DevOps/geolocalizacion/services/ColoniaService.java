package com.example.Proyect_DevOps.geolocalizacion.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.geolocalizacion.repositories.ColoniaRepository;

@Service
public class ColoniaService {

    @Autowired
    @SuppressWarnings("unused")
    private ColoniaRepository coloniaRepository;
}
