package com.example.Proyect_DevOps.sucursales.services;

import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.common.services.GenericServiceImpl;
import com.example.Proyect_DevOps.sucursales.models.GestorModel;
import com.example.Proyect_DevOps.sucursales.repositories.GestorRepository;

@Service
public class GestorService extends GenericServiceImpl<GestorModel, Integer, GestorRepository> {

}
