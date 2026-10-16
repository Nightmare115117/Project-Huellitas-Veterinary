package com.example.Proyect_DevOps.servicios.services;

import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.common.services.GenericServiceImpl;
import com.example.Proyect_DevOps.servicios.models.ServicioModel;
import com.example.Proyect_DevOps.servicios.repositories.ServicioRepository;

@Service
public class ServicioService extends GenericServiceImpl<ServicioModel, Integer, ServicioRepository> {

}
