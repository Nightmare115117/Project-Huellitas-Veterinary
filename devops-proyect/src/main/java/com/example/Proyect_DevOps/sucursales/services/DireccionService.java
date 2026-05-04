package com.example.Proyect_DevOps.sucursales.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.sucursales.repositories.DireccionRepository;

@Service
public class DireccionService {

    @Autowired
    @SuppressWarnings("unused")
    private DireccionRepository direccionRepository;
}
