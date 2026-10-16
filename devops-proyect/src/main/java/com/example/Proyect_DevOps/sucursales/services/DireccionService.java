package com.example.Proyect_DevOps.sucursales.services;

import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.common.services.GenericServiceImpl;
import com.example.Proyect_DevOps.sucursales.models.DireccionModel;
import com.example.Proyect_DevOps.sucursales.repositories.DireccionRepository;

@Service
public class DireccionService extends GenericServiceImpl<DireccionModel, Integer, DireccionRepository> {

}
