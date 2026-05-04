package com.example.Proyect_DevOps.sucursales.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.sucursales.repositories.SucursalRepository;

@Service
public class SucursalService {

    @Autowired
    @SuppressWarnings("unused")
    private SucursalRepository sucursalRepository;

}
