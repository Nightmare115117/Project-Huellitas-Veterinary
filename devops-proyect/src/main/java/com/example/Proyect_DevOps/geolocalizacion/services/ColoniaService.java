package com.example.Proyect_DevOps.geolocalizacion.services;

import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.common.services.GenericServiceImpl;
import com.example.Proyect_DevOps.geolocalizacion.models.ColoniaModel;
import com.example.Proyect_DevOps.geolocalizacion.repositories.ColoniaRepository;

@Service
public class ColoniaService extends GenericServiceImpl<ColoniaModel, Integer, ColoniaRepository> {

}
