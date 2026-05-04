package com.example.Proyect_DevOps.mascotas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.mascotas.repositories.RazaRepository;

@Service
public class RazaService {
    
    @Autowired
    @SuppressWarnings("unused")
    private RazaRepository razaRepository;
}
