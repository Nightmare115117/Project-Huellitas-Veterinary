package com.example.Proyect_DevOps.geolocalizacion.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.geolocalizacion.repositories.CalleRepository;

@Service
public class CalleService {

    @Autowired
    @SuppressWarnings("unused")
    private CalleRepository calleRepository;
}
