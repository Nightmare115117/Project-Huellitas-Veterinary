package com.example.Proyect_DevOps.geolocalizacion.services;

import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.common.services.GenericServiceImpl;
import com.example.Proyect_DevOps.geolocalizacion.models.EstadoModel;
import com.example.Proyect_DevOps.geolocalizacion.repositories.EstadoRepository;

@Service
public class EstadoService extends GenericServiceImpl<EstadoModel, Integer, EstadoRepository> {

}
