package com.example.Proyect_DevOps.sucursales.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.sucursales.repositories.GestorRepository;

@Service
public class GestorService {

    @Autowired
    @SuppressWarnings("unused")
    private GestorRepository gestorRepository;

}
